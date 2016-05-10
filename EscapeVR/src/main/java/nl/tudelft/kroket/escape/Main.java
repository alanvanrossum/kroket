package nl.tudelft.kroket.escape;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.tudelft.kroket.net.Client;
import jmevr.app.VRApplication;
import jmevr.input.OpenVR;
import jmevr.input.VRBounds;
import jmevr.post.CartoonSSAO;
import jmevr.util.VRGuiManager;
import jmevr.util.VRGuiManager.POSITIONING_MODE;

import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;

public class Main extends VRApplication {

	public enum GameState {
		LOBBY, INTRO, STARTED
	}

	private GameState currentState = GameState.LOBBY;

	BitmapFont guiFont;

	boolean introComplete = false;

	Picture ready;

	List<Picture> overlayList = new ArrayList<Picture>();

	private AudioNode audio_ambient;
	private AudioNode audio_gameBegin;
	private AudioNode audio_welcome;
	private AudioNode audio_waiting;

	boolean waitingSet = false;

	Client client = null;

	BitmapText hudText;

	private void initAudio() {

		audio_waiting = new AudioNode(getAssetManager(),
				"Sound/Soundtrack/waiting.wav", DataType.Buffer);
		audio_waiting.setPositional(false);
		audio_waiting.setLooping(true);
		audio_waiting.setVolume(1);
		rootNode.attachChild(audio_waiting);

		audio_ambient = new AudioNode(getAssetManager(),
				"Sound/Soundtrack/ambient.wav", DataType.Buffer);
		audio_ambient.setPositional(false);
		audio_ambient.setLooping(true);
		audio_ambient.setVolume(1);
		rootNode.attachChild(audio_ambient);

		audio_welcome = new AudioNode(getAssetManager(),
				"Sound/Voice/intro2.wav", DataType.Buffer);
		audio_welcome.setPositional(false);
		audio_welcome.setLooping(false);
		audio_welcome.setVolume(5);
		rootNode.attachChild(audio_welcome);

		audio_gameBegin = new AudioNode(getAssetManager(),
				"Sound/Voice/letthegamebegin3.wav", DataType.Buffer);
		audio_gameBegin.setPositional(false);
		audio_gameBegin.setLooping(false);
		audio_gameBegin.setVolume(5);
		rootNode.attachChild(audio_gameBegin);

	}

	private void overlayImage(String path) {
		Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
		if (ready != null)
			ready.removeFromParent();

		ready = new Picture("testpic");
		ready.setImage(getAssetManager(), path, true);
		ready.setWidth(guiCanvasSize.x);
		ready.setHeight(guiCanvasSize.y);
		// ready.setPosition(guiCanvasSize.x * 0.5f - 192f * 0.5f,
		// guiCanvasSize.y);
		guiNode.attachChild(ready);

		overlayList.add(ready);
	}

	// general objects for scene management
	Node boxes = new Node("boxes");
	Spatial observer;
	boolean moveForward, moveBackwards, rotateLeft, rotateRight;
	Material mat, floorMat;
	Geometry leftHand, rightHand;

	@Override
	public void simpleInitApp() {
		initAudio();
		initScene();

		// print out what device we have
		if (VRApplication.getVRHardware() != null) {
			System.out.println("Attached device: "
					+ VRApplication.getVRHardware().getName());
		}

		// displayText("Game not started.\r\nWaiting for players...");

		try {
			System.out.println("Setting up client...");
			client = new Client();

			System.out.println("Registering player...");

			client.sendMessage("REGISTER[Harvey]");
			client.sendMessage("TYPE[VIRTUAL]");

			final DataInputStream stream = client.getStream();

			Thread thread = new Thread() {
				@Override
				public void run() {

					String line;
					try {
						while ((line = stream.readLine()) != null) {
							receiveLoop(line);
						}
						System.out.println("end of loop");
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				};
			};
			thread.start();
			System.out.println("Thread started");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void playMusic() {
		audio_ambient.play();
	}

	// public void displayText(String text) {
	//
	// Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
	//
	// Picture pic = new Picture("HUD Picture");
	// pic.setImage(getAssetManager(), "Textures/overlay/waiting.png", true);
	//
	// /** Write text on the screen (HUD) */
	// //guiNode.detachAllChildren();
	//
	//
	// guiNode.attachChild(pic);
	//
	// }
	/** Write text on the screen (HUD) */

	private void initScene() {
		observer = new Node("observer");

		BitmapFont guiFont = getAssetManager().loadFont(
				"Interface/Fonts/Default.fnt");

		hudText = new BitmapText(guiFont, false);
		hudText.setSize(guiFont.getCharSet().getRenderedSize());
		hudText.setText("hudText placeholder");
		hudText.setLocalTranslation(0, hudText.getLineHeight(), 0);
		guiNode.attachChild(hudText);

		Spatial sky = SkyFactory.createSky(getAssetManager(),
				"Textures/Sky/Bright/spheremap.png",
				SkyFactory.EnvMapType.EquirectMap);
		rootNode.attachChild(sky);

		Geometry box = new Geometry("", new Box(5, 5, 5));

		mat = new Material(getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		Texture noise = getAssetManager().loadTexture("Textures/noise.png");
		noise.setMagFilter(MagFilter.Nearest);
		noise.setMinFilter(MinFilter.Trilinear);
		noise.setAnisotropicFilter(16);
		mat.setTexture("ColorMap", noise);

		floorMat = new Material(getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");

		Texture floorTexture = getAssetManager().loadTexture(
				"Textures/floor2.png");
		floorTexture.setMagFilter(MagFilter.Nearest);
		floorTexture.setMinFilter(MinFilter.Trilinear);
		floorTexture.setAnisotropicFilter(16);
		floorMat.setTexture("ColorMap", floorTexture);

		// create 4 walls

		Geometry wall1 = new Geometry("wall", new Box(.5f, 6f, 6f));
		wall1.setMaterial(mat);
		wall1.move(-12, 5, 0);
		rootNode.attachChild(wall1);

		Geometry wall2 = new Geometry("wall", new Box(.5f, 6f, 6f));
		wall2.setMaterial(mat);
		wall2.move(12, 5, 0);
		rootNode.attachChild(wall2);

		Geometry wall3 = new Geometry("wall", new Box(12f, 6f, .5f));
		wall3.setMaterial(mat);
		wall3.move(0, 5, 6);
		rootNode.attachChild(wall3);

		Geometry wall4 = new Geometry("wall", new Box(12f, 6f, .5f));
		wall4.setMaterial(mat);
		wall4.move(0, 5, -6);
		rootNode.attachChild(wall4);

		// ----

		// make the floor according to the size of our play area
		Geometry floor = new Geometry("floor", new Box(6f, 1f, 3f));
		Vector2f playArea = VRBounds.getPlaySize();
		if (playArea == null) {
			// no play area, use default size & height
			floor.setLocalScale(2f, 0.5f, 2f);
			floor.move(0f, -1.5f, 0f);
		} else {
			// cube model is actually 2x as big, cut it down to proper playArea
			// size with * 0.5
			floor.setLocalScale(playArea.x * 0.5f, 0.5f, playArea.y * 0.5f);
			floor.move(0f, -0.5f, 0f);
		}

		floor.setMaterial(floorMat);
		rootNode.attachChild(floor);

		// hand wands
		leftHand = (Geometry) getAssetManager().loadModel(
				"Models/vive_controller.j3o");
		rightHand = leftHand.clone();
		Material handMat = new Material(getAssetManager(),
				"Common/MatDefs/Misc/Unshaded.j3md");
		handMat.setTexture("ColorMap",
				getAssetManager().loadTexture("Textures/vive_controller.png"));
		leftHand.setMaterial(handMat);
		rightHand.setMaterial(handMat);
		rootNode.attachChild(rightHand);
		rootNode.attachChild(leftHand);

		// gui element
		// Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
		// Picture test = new Picture("testpic");
		// test.setImage(getAssetManager(), "Textures/crosshair.png", true);
		// test.setWidth(192f);
		// test.setHeight(128f);
		// test.setPosition(guiCanvasSize.x * 0.5f - 192f * 0.5f,
		// guiCanvasSize.y * 0.5f - 128f * 0.5f);
		// guiNode.attachChild(test);

		// test any positioning mode here (defaults to AUTO_CAM_ALL)
		VRGuiManager
				.setPositioningMode(POSITIONING_MODE.AUTO_CAM_ALL_SKIP_PITCH);
		VRGuiManager.setGuiScale(0.4f);
		VRGuiManager.setPositioningElasticity(10f);

		// box.setMaterial(mat);
		//
		// Geometry box2 = box.clone();
		// box2.move(15, 0, 0);
		// box2.setMaterial(mat);
		// Geometry box3 = box.clone();
		// box3.move(-15, 0, 0);
		// box3.setMaterial(mat);
		//
		// boxes.attachChild(box);
		// boxes.attachChild(box2);
		// boxes.attachChild(box3);
		// rootNode.attachChild(boxes);

		observer.setLocalTranslation(new Vector3f(0.0f, 0.0f, 0.0f));

		VRApplication.setObserver(observer);
		rootNode.attachChild(observer);

		// addAllBoxes();

		// initFont();

		// use magic VR mouse cusor (same usage as non-VR mouse cursor)
		getInputManager().setCursorVisible(true);

		// filter test (can be added here like this)
		// but we are going to save them for the F key during runtime
		/*
		 * CartoonSSAO cartfilt = new CartoonSSAO(); FilterPostProcessor fpp =
		 * new FilterPostProcessor(assetManager); fpp.addFilter(cartfilt);
		 * viewPort.addProcessor(fpp);
		 */

		// System.out.println("Starting audio playback...");
		// audio_welcome.play();

	}

	private void clearOverlays() {

		for (Picture pic : overlayList) {
			pic.removeFromParent();
		}

	}

	private void initJoysticks() {
		InputManager inputManager = getInputManager();

		// inputManager.addRawInputListener(new JoystickEventListener());

		Joystick[] joysticks = inputManager.getJoysticks();

		if (joysticks == null || joysticks.length == 0) {
			System.out.println("No joysticks found");
		} else {

			System.out.println("joysticks.length = " + joysticks.length);

			Joystick joy = joysticks[0];
			// JoystickAxis axis = joy.getAxes().get(0);

			// assign axes
			joy.getAxes().get(0).assignAxis("right", "left");
			joy.getAxes().get(1).assignAxis("forward", "back");

			// joy.getButtons().get(1).assignButton("X");
			// joy.getButtons().get(2).assignButton("Y");
			// joy.getButtons().get(3).assignButton("X");
			// joy.getButtons().get(4).assignButton("Y");
		}

	}

	private void initInputs() {
		InputManager inputManager = getInputManager();

		initJoysticks();

		inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("incShift", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("decShift", new KeyTrigger(KeyInput.KEY_E));
		inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("back", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("filter", new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping("dumpImages", new KeyTrigger(KeyInput.KEY_I));

		ActionListener acl = new ActionListener() {

			public void onAction(String name, boolean keyPressed, float tpf) {

				if (!keyPressed) {
					System.out.println("You have pressed : " + name);
				}

				if (name.equals("incShift") && keyPressed) {
					VRGuiManager.adjustGuiDistance(-0.1f);
				} else if (name.equals("decShift") && keyPressed) {
					VRGuiManager.adjustGuiDistance(0.1f);
				} else if (name.equals("filter") && keyPressed) {
					// adding filters in realtime
					CartoonSSAO cartfilt = new CartoonSSAO();
					FilterPostProcessor fpp = new FilterPostProcessor(
							getAssetManager());
					fpp.addFilter(cartfilt);
					getViewPort().addProcessor(fpp);
					// filters added to main viewport during runtime,
					// move them into VR processing
					// (won't do anything if not in VR mode)
					VRApplication.moveScreenProcessingToVR();
				}
				if (name.equals("toggle")) {
					VRGuiManager.positionGui();
				}
				if (name.equals("forward")) {
					if (keyPressed) {
						moveForward = true;
					} else {
						moveForward = false;
					}
				} else if (name.equals("back")) {
					if (keyPressed) {
						moveBackwards = true;
					} else {
						moveBackwards = false;
					}
				} else if (name.equals("dumpImages")) {
					OpenVR.getCompositor().CompositorDumpImages.apply();
				} else if (name.equals("left")) {
					if (keyPressed) {
						rotateLeft = true;
					} else {
						rotateLeft = false;
					}
				} else if (name.equals("right")) {
					if (keyPressed) {
						rotateRight = true;
					} else {
						rotateRight = false;
					}
				}

			}
		};
		inputManager.addListener(acl, "forward");
		inputManager.addListener(acl, "back");
		inputManager.addListener(acl, "left");
		inputManager.addListener(acl, "right");
		inputManager.addListener(acl, "toggle");
		inputManager.addListener(acl, "incShift");
		inputManager.addListener(acl, "decShift");
		inputManager.addListener(acl, "filter");
		inputManager.addListener(acl, "dumpImages");

	}

	private float distance = 100f;
	private float prod = 0f;
	private float placeRate = 0f;

	// FPS test
	private float tpfAdder = 0f;
	private int tpfCount = 0;

	private void displayIntro() {
		if (!introComplete
				&& audio_welcome.getStatus() != AudioSource.Status.Playing) {
			introComplete = true;
			initInputs();
			audio_gameBegin.play();
			playMusic();
			currentState = GameState.STARTED;
		} else if (audio_welcome.getStatus() == AudioSource.Status.Playing) {

			if (audio_welcome.getPlaybackTime() < 3) {
				clearOverlays();
				overlayImage("Textures/overlay/teamkroket.png");
			} else if (audio_welcome.getPlaybackTime() < 5) {
				clearOverlays();
				overlayImage("Textures/overlay/presents.png");
			} else if (audio_welcome.getPlaybackTime() < 12) {
				clearOverlays();
				overlayImage("Textures/overlay/escaparade.png");
			} else if (audio_welcome.getPlaybackTime() < 18) {
				clearOverlays();
				overlayImage("Textures/overlay/locked.png");
			} else if (audio_welcome.getPlaybackTime() < 26) {
				clearOverlays();
				overlayImage("Textures/overlay/toxicgas.png");
			} else if (audio_welcome.getPlaybackTime() < 32) {
				clearOverlays();
				overlayImage("Textures/overlay/onlygoal.png");
			} else if (audio_welcome.getPlaybackTime() < 35) {
				clearOverlays();
				overlayImage("Textures/overlay/getout.png");
			} else if (audio_welcome.getPlaybackTime() < 38) {
				clearOverlays();
				overlayImage("Textures/overlay/onlyway.png");
			} else if (audio_welcome.getPlaybackTime() < 41) {
				clearOverlays();
				overlayImage("Textures/overlay/byworkingtogether.png");
			} else if (audio_welcome.getPlaybackTime() < 45) {
				clearOverlays();
				overlayImage("Textures/overlay/makeitoutalive.png");
			} else if (audio_welcome.getPlaybackTime() < 48) {
				clearOverlays();
				overlayImage("Textures/overlay/getready.png");
			} else if (audio_welcome.getPlaybackTime() < 51) {
				clearOverlays();
				overlayImage("Textures/overlay/toescape.png");
			} else
				clearOverlays();

		} else {
			clearOverlays();
		}
	}

	private void showLobby() {
		clearOverlays();
		overlayImage("Textures/overlay/waiting.png");
		guiNode.attachChild(hudText);
	}

	boolean introSet = false;

	@Override
	public void simpleUpdate(float tpf) {

		if (currentState == GameState.LOBBY) {

			if (!waitingSet) {
				audio_waiting.play();
				waitingSet = true;
				// displayWaiting();

				guiNode.detachAllChildren();
				showLobby();
				hudText.setText("Waiting for other players to connect...");
				guiNode.attachChild(hudText);
			}

		} else if (currentState == GameState.INTRO) {

			if (!introSet) {
				introSet = true;
				audio_waiting.stop();
				// audio_welcome.play();
			}
			displayIntro();
		} else if (currentState == GameState.STARTED) {

			// FPS test
			tpfAdder += tpf;
			tpfCount++;
			if (tpfCount == 60) {
				// System.out.println("FPS: "
				// + Float.toString(1f / (tpfAdder / tpfCount)));
				tpfCount = 0;
				tpfAdder = 0f;
			}

			prod += tpf;
			distance = 100f * FastMath.sin(prod);
			// boxes.setLocalTranslation(0, 0, 200f + distance);

			if (moveForward) {
				observer.move(VRApplication.getFinalObserverRotation()
						.getRotationColumn(2).mult(tpf * 8f));
			}
			if (moveBackwards) {
				observer.move(VRApplication.getFinalObserverRotation()
						.getRotationColumn(2).mult(-tpf * 8f));
			}
			if (rotateLeft) {
				observer.rotate(0, 0.75f * tpf, 0);
			}
			if (rotateRight) {
				observer.rotate(0, -0.75f * tpf, 0);
			}

			if (placeRate > 0f)
				placeRate -= tpf;

		}
	}

	public void remoteInput(String line) {

		if (line.equals("START")) {
			// audio_waiting.stop();
			guiNode.detachAllChildren();
			audio_waiting.stop();
			audio_welcome.play();
			currentState = GameState.INTRO;
		} else

			hudText.setText(line);
	}

	public void receiveLoop(String message) {
		System.out.println("Remote input: " + message);

		remoteInput(message);
	}

}