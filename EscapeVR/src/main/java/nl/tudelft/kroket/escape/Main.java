package nl.tudelft.kroket.escape;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import nl.tudelft.kroket.net.Client;
import jmevr.app.VRApplication;
import jmevr.input.OpenVR;
import jmevr.post.CartoonSSAO;
import jmevr.util.VRGuiManager;
import jmevr.util.VRGuiManager.POSITIONING_MODE;

import com.jme3.asset.AssetManager;
import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.font.Rectangle;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
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

	/** Hostname of the gamehost. */
	private static final String HOSTNAME = "localhost";

	/** Portnumber of the gamehost. */
	private static int PORTNUM = 1234;

	protected boolean controlsInitialized = false;
	protected boolean joysticksInitialized = false;

	/** Enum for all GameStates. */
	public enum GameState {
		NONE, LOBBY, INTRO, PLAYING
	}

	/** Observer object. */
	Spatial observer;

	/** Observer controls. */
	boolean moveForward, moveBackwards, rotateLeft, rotateRight;

	/** Materials. */
	Material mat, floorMat;

	/** Current gamestate. */
	private GameState currentState = GameState.NONE;

	/** Font used in overlays. */
	BitmapFont guiFont;

	Picture ready;

	List<Picture> overlayList = new ArrayList<Picture>();

	HashMap<String, AudioNode> audioList = new HashMap<String, AudioNode>();

	private AudioNode audio_ambient;
	private AudioNode audio_gameBegin;
	private AudioNode audio_welcome;
	private AudioNode audio_waiting;

	/** TCP client object. */
	Client client = null;

	/** The text displayed in the HUD. */
	BitmapText hudText;

	/** Accept user input. */
	boolean acceptInput = false;

	private boolean forceUpdateState = true;

	/** State to force game to. */
	private GameState insertState = GameState.LOBBY; // start in lobby

	/**
	 * Register all audio assets.
	 */
	private void initAudio() {

		audio_waiting = new AudioNode(getAssetManager(),
				"Sound/Soundtrack/waiting.wav", DataType.Buffer);
		audio_waiting.setPositional(false);
		audio_waiting.setLooping(true);
		audio_waiting.setVolume(1);
		rootNode.attachChild(audio_waiting);

		audioList.put("waiting", audio_waiting);

		audio_ambient = new AudioNode(getAssetManager(),
				"Sound/Soundtrack/ambient.wav", DataType.Buffer);
		audio_ambient.setPositional(false);
		audio_ambient.setLooping(true);
		audio_ambient.setVolume(1);
		rootNode.attachChild(audio_ambient);

		audioList.put("ambient", audio_ambient);

		audio_welcome = new AudioNode(getAssetManager(),
				"Sound/Voice/intro2.wav", DataType.Buffer);
		audio_welcome.setPositional(false);
		audio_welcome.setLooping(false);
		audio_welcome.setVolume(5);
		rootNode.attachChild(audio_welcome);

		audioList.put("welcome", audio_welcome);

		audio_gameBegin = new AudioNode(getAssetManager(),
				"Sound/Voice/letthegamebegin3.wav", DataType.Buffer);
		audio_gameBegin.setPositional(false);
		audio_gameBegin.setLooping(false);
		audio_gameBegin.setVolume(5);
		rootNode.attachChild(audio_gameBegin);

		audioList.put("gameBegin", audio_gameBegin);

	}

	/**
	 * Stop all audio tracks from playing.
	 */
	public void stopAudio() {
		for (AudioNode node : audioList.values()) {
			if (node.getStatus() == AudioSource.Status.Playing)
				node.stop();
		}
	}

	/**
	 * Display an overlay image (may be translucent).
	 * 
	 * @param path
	 *            the path to the file
	 */
	private void overlayImage(String path) {
		Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
		if (ready != null)
			ready.removeFromParent();

		ready = new Picture("overlay");
		ready.setImage(getAssetManager(), path, true);
		ready.setWidth(guiCanvasSize.x);
		ready.setHeight(guiCanvasSize.y);
		ready.move(0, 0, -1); // move back image (so text can appear in front of
								// image)

		guiNode.attachChild(ready);

		overlayList.add(ready);
	}

	/**
	 * Initialize the application.
	 */
	@Override
	public void simpleInitApp() {

		initControls();

		initAudio();

		initScene();

		if (VRApplication.getVRHardware() != null) {
			System.out.println("Attached device: "
					+ VRApplication.getVRHardware().getName());
		}

		System.out.println("Setting up client...");
		Client client = new Client(HOSTNAME, PORTNUM);

		final DataInputStream stream = client.getStream();

		Thread thread = new Thread() {
			@Override
			public void run() {

				String line;
				try {
					while ((line = stream.readLine()) != null) {
						receiveLoop(line);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			};
		};
		thread.start();

		System.out.println("Registering player...");

		try {
			client.sendMessage("REGISTER[Harvey]");
			client.sendMessage("TYPE[VIRTUAL]");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create a label (text).
	 * 
	 * @param assetManager
	 *            assetmanager instance
	 * @param fontpath
	 *            path to the font asset
	 * @param x
	 *            the x-coordinate to position the label to
	 * @param y
	 *            the y-coordinate to position the label to
	 * @param width
	 *            the width of the label
	 * @param height
	 *            the height of the label
	 * @return the bitmap object
	 */
	protected BitmapText createLabel(AssetManager assetManager,
			String fontpath, float x, float y, float width, float height) {
		BitmapFont fnt = assetManager.loadFont(fontpath);
		BitmapText txt = new BitmapText(fnt, false);
		txt.setBox(new Rectangle(0, 0, width, height));
		txt.setLocalTranslation(x, y, 0);
		return txt;
	}

	/**
	 * Initialize the scene.
	 */
	private void initScene() {
		Vector2f guiCanvasSize = VRGuiManager.getCanvasSize();
		observer = new Node("observer");

		guiFont = getAssetManager().loadFont("Interface/Fonts/Default.fnt");

		hudText = createLabel(getAssetManager(), "Interface/Fonts/Default.fnt",
				guiCanvasSize.getX() * 0.5f - 145,
				(guiCanvasSize.getY() * 0.5f) - 70, guiCanvasSize.getX(),
				guiCanvasSize.getY());
		hudText.setSize(24);

		hudText.setText("Loading...");
		guiNode.attachChild(hudText);

		Spatial sky = SkyFactory.createSky(getAssetManager(),
				"Textures/Sky/Bright/spheremap.png",
				SkyFactory.EnvMapType.EquirectMap);
		rootNode.attachChild(sky);

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

		floor.setLocalScale(2f, 0.5f, 2f);
		floor.move(0f, -1.5f, 0f);

		floor.setMaterial(floorMat);
		rootNode.attachChild(floor);

		Geometry ceiling = new Geometry("ceiling", new Box(6f, 1f, 3f));

		ceiling.setLocalScale(2f, 0.5f, 2f);
		ceiling.move(0f, 12f, 0f);

		ceiling.setMaterial(floorMat);
		rootNode.attachChild(ceiling);

		// test any positioning mode here (defaults to AUTO_CAM_ALL)
		VRGuiManager
				.setPositioningMode(POSITIONING_MODE.AUTO_CAM_ALL_SKIP_PITCH);
		VRGuiManager.setGuiScale(0.4f);
		VRGuiManager.setPositioningElasticity(10f);

		observer.setLocalTranslation(new Vector3f(0.0f, 0.0f, 0.0f));

		VRApplication.setObserver(observer);
		rootNode.attachChild(observer);

		// use magic VR mouse cusor (same usage as non-VR mouse cursor)
		getInputManager().setCursorVisible(true);

	}

	/**
	 * Clear all overlay images from parent.
	 */
	private void clearOverlays() {

		for (Picture pic : overlayList) {
			pic.removeFromParent();
		}

		overlayList.clear();
	}

	protected void initControls() {

		if (controlsInitialized)
			return;

		controlsInitialized = true;

		InputManager inputManager = getInputManager();

		inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("incShift", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("decShift", new KeyTrigger(KeyInput.KEY_E));
		inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("back", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("filter", new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping("dumpImages", new KeyTrigger(KeyInput.KEY_I));

		inputManager.addListener(actionListener, "forward");
		inputManager.addListener(actionListener, "back");
		inputManager.addListener(actionListener, "left");
		inputManager.addListener(actionListener, "right");
		inputManager.addListener(actionListener, "toggle");
		inputManager.addListener(actionListener, "incShift");
		inputManager.addListener(actionListener, "decShift");
		inputManager.addListener(actionListener, "filter");
		inputManager.addListener(actionListener, "dumpImages");

		initJoysticks();
	}

	private void initJoysticks() {

		if (joysticksInitialized)
			return;

		joysticksInitialized = true;

		InputManager inputManager = getInputManager();

		// inputManager.addRawInputListener(new JoystickEventListener());

		Joystick[] joysticks = inputManager.getJoysticks();

		if (joysticks == null || joysticks.length == 0) {
			System.out.println("No joysticks found");
		} else {

			// System.out.println("joysticks.length = " + joysticks.length);

			Joystick joy = joysticks[0];

			// // assign axes
			joy.getAxes().get(0).assignAxis("right", "left");
			joy.getAxes().get(1).assignAxis("forward", "back");

			inputManager.addMapping("Button A", new JoyButtonTrigger(0, 0));
			inputManager.addMapping("Button B", new JoyButtonTrigger(0, 1));
			inputManager.addMapping("Button X", new JoyButtonTrigger(0, 2));
			inputManager.addMapping("Button Y", new JoyButtonTrigger(0, 3));

			inputManager.addListener(actionListener, "Button A", "Button B",
					"Button X", "Button Y");
		}

	}

	private ActionListener actionListener = new ActionListener() {

		public void onAction(String name, boolean keyPressed, float tpf) {

			// ignore input if acceptInput is set to false
			if (!acceptInput)
				return;

			if (keyPressed) {
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

	/**
	 * Display intro overlay images.
	 * 
	 * @param f
	 *            time into intro
	 */
	private void displayIntro(float f) {

		if (f < 3) {
			clearOverlays();
			overlayImage("Textures/overlay/teamkroket.png");
		} else if (f < 5) {
			clearOverlays();
			overlayImage("Textures/overlay/presents.png");
		} else if (f < 12) {
			clearOverlays();
			overlayImage("Textures/overlay/escaparade.png");
		} else if (f < 18) {
			clearOverlays();
			overlayImage("Textures/overlay/locked.png");
		} else if (f < 26) {
			clearOverlays();
			overlayImage("Textures/overlay/toxicgas.png");
		} else if (f < 32) {
			clearOverlays();
			overlayImage("Textures/overlay/onlygoal.png");
		} else if (f < 35) {
			clearOverlays();
			overlayImage("Textures/overlay/getout.png");
		} else if (f < 38) {
			clearOverlays();
			overlayImage("Textures/overlay/onlyway.png");
		} else if (f < 41) {
			clearOverlays();
			overlayImage("Textures/overlay/byworkingtogether.png");
		} else if (f < 45) {
			clearOverlays();
			overlayImage("Textures/overlay/makeitoutalive.png");
		} else if (f < 48) {
			clearOverlays();
			overlayImage("Textures/overlay/getready.png");
		} else if (f < 51) {
			clearOverlays();
			overlayImage("Textures/overlay/toescape.png");
		} else {
			clearOverlays();
		}
	}

	/**
	 * Show lobby overlay.
	 */
	private void showLobby() {
		clearOverlays();
		// viewPort.setBackgroundColor(ColorRGBA.DarkGray);
		overlayImage("Textures/overlay/waiting.png");
		guiNode.attachChild(hudText);
	}

	/**
	 * Main method to update the scene.
	 */
	@Override
	public void simpleUpdate(float tpf) {

		if (forceUpdateState) {
			setGameState(insertState);
			forceUpdateState = false;
		}

		switch (currentState) {

		case LOBBY:
			guiNode.attachChild(hudText);
			break;
		case INTRO:
			if (audio_welcome.getStatus() == AudioSource.Status.Playing) {
				displayIntro(audio_welcome.getPlaybackTime());
			} else {
				setGameState(GameState.PLAYING);
			}
			break;

		case PLAYING:
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
			break;
		case NONE:
			break;
		default:
			break;
		}
	}

	/**
	 * Set the current game state (not thread-safe).
	 * 
	 * @param state
	 *            the new state
	 */
	public void setGameState(GameState state) {
		
		// do not switch state if already in given state
		if (currentState == state)
			return;

		switchState(currentState, state);
	}

	/**
	 * Switch game states.
	 * 
	 * @param oldState
	 *            the old state
	 * @param newState
	 *            the new state
	 */
	private void switchState(GameState oldState, GameState newState) {
		
		// do not switch state if already in given state
		if (oldState == newState)
			return;

		currentState = newState;

		System.out.println("Switching states from " + oldState.toString()
				+ " to " + newState.toString());

		switch (oldState) {
		case LOBBY:

			stopAudio();
			break;
		case INTRO:
			stopAudio();
			break;
		case PLAYING:
			// only accept input in the playing state
			acceptInput = false;
			stopAudio();
			break;
		case NONE:
		default:
			break;
		}

		switch (newState) {
		case LOBBY:
			if (audio_waiting.getStatus() != AudioSource.Status.Playing)
				audio_waiting.play();
			showLobby();
			break;
		case INTRO:
			if (audio_welcome.getStatus() != AudioSource.Status.Playing)
				audio_welcome.play();
			break;
		case PLAYING:
			// accept input from the user
			acceptInput = true;

			audio_gameBegin.play();
			if (audio_ambient.getStatus() != AudioSource.Status.Playing)
				audio_ambient.play();

			break;
		case NONE:
		default:
			break;
		}
	}

	/**
	 * Process remote input.
	 * 
	 * @param line
	 *            incoming from remote source
	 */
	public void remoteInput(String line) {

		if (line.equals("START")) {
			guiNode.detachAllChildren();

			// do not call setGameState or switchState here as those run in
			// a different thread, use updateStates and insertState instead
			forceUpdateState = true;

			// to skip the intro, set insertState to PLAYING
			// insertState = GameState.PLAYING

			insertState = GameState.INTRO;

			hudText.setText("");
		} else {

			hudText.setText(line);
			guiNode.attachChild(hudText);
		}
	}

	/**
	 * Main callback method for handling remote input from socket.
	 * 
	 * @param message
	 *            the input received from the socket
	 */
	public void receiveLoop(String message) {
		System.out.println("Remote input: " + message);

		remoteInput(message);
	}

}