package nl.tudelft.kroket.escape;

import java.util.ArrayList;
import java.util.List;

import com.jme3.audio.AudioData.DataType;
import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioSource;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.input.InputManager;
import com.jme3.input.JoyInput;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.RawInputListener;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.JoyAxisTrigger;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;
import com.jme3.input.JoystickAxis;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.Spatial.CullHint;
import com.jme3.scene.shape.Box;
import com.jme3.system.AppSettings;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture.MagFilter;
import com.jme3.texture.Texture.MinFilter;
import com.jme3.ui.Picture;
import com.jme3.util.SkyFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import jmevr.app.VRApplication;
import jmevr.input.OpenVR;
import jmevr.input.VRBounds;
import jmevr.input.VRInput;
import jmevr.input.VRInput.VRINPUT_TYPE;
import jmevr.post.CartoonSSAO;
import jmevr.util.VRGuiManager;
import jmevr.util.VRGuiManager.POSITIONING_MODE;
import jopenvr.VRControllerAxis_t;
import nl.tudelft.kroket.net.Client;

public class Main extends VRApplication {

	public static void main(String[] args) {
            
		Main mainApplication = new Main();
//            try {
//                Client client = new Client();
//            } catch (IOException ex) {
//                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
//            }

		// create AppSettings object to enable joysticks/gamepads
		// and set the title
		AppSettings settings = new AppSettings(true);

		// enable joysticks/gamepads
		settings.setUseJoysticks(true);

		// set application/window title
		settings.setTitle("EscapeVR");

		mainApplication.setSettings(settings);

		mainApplication.preconfigureVRApp(
				PRECONFIG_PARAMETER.USE_CUSTOM_DISTORTION, false);
		mainApplication.preconfigureVRApp(
				PRECONFIG_PARAMETER.ENABLE_MIRROR_WINDOW, false);
		mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.FORCE_VR_MODE,
				false);
		mainApplication.preconfigureVRApp(
				PRECONFIG_PARAMETER.SET_GUI_CURVED_SURFACE, true);
		mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.FLIP_EYES, false);
		mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.SET_GUI_OVERDRAW,
				true);
		mainApplication.preconfigureVRApp(
				PRECONFIG_PARAMETER.INSTANCE_VR_RENDERING, false);
		mainApplication.preconfigureVRApp(PRECONFIG_PARAMETER.NO_GUI, false);
		mainApplication.preconfigureFrustrumNearFar(0.1f, 512f);
		mainApplication.start();
	}

	// protected class JoystickEventListener implements RawInputListener {
	//
	// public void onJoyAxisEvent(JoyAxisEvent evt) {
	// System.out.println("onJoyAxisEvent");
	// }
	//
	// public void onJoyButtonEvent(JoyButtonEvent evt) {
	// System.out.println("onJoyButtonEvent");
	// }
	//
	// public void beginInput() {
	// }
	//
	// public void endInput() {
	// }
	//
	// public void onMouseMotionEvent(MouseMotionEvent evt) {
	// }
	//
	// public void onMouseButtonEvent(MouseButtonEvent evt) {
	// }
	//
	// public void onKeyEvent(KeyInputEvent evt) {
	// }
	//
	// public void onTouchEvent(TouchEvent evt) {
	// }
	//
	// }

	BitmapFont guiFont;

	boolean introPlayed = false;

	Picture ready;

	List<Picture> overlayList = new ArrayList<Picture>();

	private void initFont() {
		guiFont = getAssetManager().loadFont("Interface/Fonts/fyf.ttf");
		// hudText = new BitmapText(myFont, false);

	}

	private void displayText() {
		BitmapText hudText = new BitmapText(guiFont, false);
		hudText.setSize(guiFont.getCharSet().getRenderedSize()); // font size
		hudText.setColor(ColorRGBA.Blue); // font color
		hudText.setText("You can write any string here"); // the text
		hudText.setLocalTranslation(300, hudText.getLineHeight(), 0); // position
		guiNode.attachChild(hudText);
	}

	private AudioNode audio_ambient;
	private AudioNode audio_gameBegin;
	private AudioNode audio_welcome;

	private void initAudio() {

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
		initTestScene();

		// print out what device we have
		if (VRApplication.getVRHardware() != null) {
			System.out.println("Attached device: "
					+ VRApplication.getVRHardware().getName());
		}
	}

	private void playMusic() {
		audio_ambient.play();
	}

	private void playIntro() {

	}

	private void initTestScene() {
		observer = new Node("observer");

		Spatial sky = SkyFactory.createSky(getAssetManager(),
				"Textures/Sky/Bright/spheremap.png",
				SkyFactory.EnvMapType.EquirectMap);
		rootNode.attachChild(sky);

		Geometry box = new Geometry("", new Box(5, 5, 5));
		mat = new Material(getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
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

		box.setMaterial(mat);

		Geometry box2 = box.clone();
		box2.move(15, 0, 0);
		box2.setMaterial(mat);
		Geometry box3 = box.clone();
		box3.move(-15, 0, 0);
		box3.setMaterial(mat);

		boxes.attachChild(box);
		boxes.attachChild(box2);
		boxes.attachChild(box3);
		rootNode.attachChild(boxes);

		observer.setLocalTranslation(new Vector3f(0.0f, 0.0f, 0.0f));

		VRApplication.setObserver(observer);
		rootNode.attachChild(observer);

		// addAllBoxes();

		initInputs();

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

		System.out.println("Starting audio playback...");
		audio_welcome.play();

		// displayText();
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
		//
		// inputManager.addMapping("forward", new JoyAxisTrigger(0,
		// JoyInput.AXIS_POV_X, true));
		// inputManager.addMapping("back", new JoyAxisTrigger(0,
		// JoyInput.AXIS_POV_X, false));
		// inputManager.addMapping("left", new JoyAxisTrigger(0,
		// JoyInput.AXIS_POV_Y, true));
		// inputManager.addMapping("right", new JoyAxisTrigger(0,
		// JoyInput.AXIS_POV_Y, false));

		// inputManager.addMapping("left", new JoyAxisTrigger(0,
		// JoyInput.AXIS_POV_X, true));
		// inputManager.addMapping("right", new JoyAxisTrigger(0,
		// JoyInput.AXIS_POV_X, false));
		// inputManager.addMapping("back", new JoyAxisTrigger(0,
		// JoyInput.AXIS_POV_Y, true));
		// inputManager.addMapping("forward", new JoyAxisTrigger(0,
		// JoyInput.AXIS_POV_Y, false));

		// inputManager.addMapping("Axis LS Up", new JoyAxisTrigger(0, 0,
		// true));
		// inputManager
		// .addMapping("Axis LS Down", new JoyAxisTrigger(0, 0, false));
		// inputManager.addMapping("Axis LS Left", new JoyAxisTrigger(0, 1,
		// true));
		// inputManager.addMapping("Axis LS Right",
		// new JoyAxisTrigger(0, 1, false));
		// inputManager.addMapping("Axis RS Up", new JoyAxisTrigger(0, 2,
		// true));
		// inputManager
		// .addMapping("Axis RS Down", new JoyAxisTrigger(0, 2, false));
		// inputManager.addMapping("Axis RS Left", new JoyAxisTrigger(0, 3,
		// true));
		// inputManager.addMapping("Axis RS Right",
		// new JoyAxisTrigger(0, 3, false));
		// inputManager.addMapping("Button A", new JoyButtonTrigger(0, 0));
		// inputManager.addMapping("Button B", new JoyButtonTrigger(0, 1));
		// inputManager.addMapping("Button X", new JoyButtonTrigger(0, 2));
		// inputManager.addMapping("Button Y", new JoyButtonTrigger(0, 3));
		// inputManager.addMapping("Button LB", new JoyButtonTrigger(0, 4));
		// inputManager.addMapping("Button RB", new JoyButtonTrigger(0, 5));
		// inputManager.addMapping("Button BACK", new JoyButtonTrigger(0, 6));
		// inputManager.addMapping("Button START", new JoyButtonTrigger(0, 7));
		// inputManager.addMapping("Button LT", new JoyButtonTrigger(0, 8));
		// inputManager.addMapping("Button RT", new JoyButtonTrigger(0, 9));
		// inputManager.addMapping("Button RT", new JoyAxisTrigger(0, 4, true));
		// inputManager.addMapping("Button LT", new JoyAxisTrigger(0, 4,
		// false));

		// inputManager.addMapping("Button A", new KeyTrigger(KeyInput.KEY_A));

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

	@Override
	public void simpleUpdate(float tpf) {

		if (!introPlayed
				&& audio_welcome.getStatus() != AudioSource.Status.Playing) {
			introPlayed = true;
			audio_gameBegin.play();
			playMusic();
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

		} else
			clearOverlays();

		// FPS test
		tpfAdder += tpf;
		tpfCount++;
		if (tpfCount == 60) {
			System.out.println("FPS: "
					+ Float.toString(1f / (tpfAdder / tpfCount)));
			tpfCount = 0;
			tpfAdder = 0f;
		}

		prod += tpf;
		distance = 100f * FastMath.sin(prod);
		boxes.setLocalTranslation(0, 0, 200f + distance);

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

		handleWandInput(0, leftHand);
		handleWandInput(1, rightHand);
		if (placeRate > 0f)
			placeRate -= tpf;
	}

	private ActionListener actionListener = new ActionListener() {

		public void onAction(String name, boolean isPressed, float tpf) {

		}
	};

	private void handleWandInput(int index, Geometry geo) {
		Quaternion q = VRInput.getFinalObserverRotation(index);
		Vector3f v = VRInput.getFinalObserverPosition(index);
		if (q != null && v != null) {
			geo.setCullHint(CullHint.Dynamic); // make sure we see it
			geo.setLocalTranslation(v);
			geo.setLocalRotation(q);
			// place boxes when holding down trigger
			if (VRInput.getAxis(index, VRINPUT_TYPE.ViveTriggerAxis).x >= 1f
					&& placeRate <= 0f) {
				placeRate = 0.5f;
				// addBox(v, q, 0.1f);
				VRInput.triggerHapticPulse(index, 0.1f);
			}

			// print out all of the known information about the controllers here
			for (int i = 0; i < VRInput.getRawControllerState(index).rAxis.length; i++) {
				VRControllerAxis_t cs = VRInput.getRawControllerState(index).rAxis[i];
				System.out
						.println("Controller#" + Integer.toString(index)
								+ ", Axis#" + Integer.toString(i) + " X: "
								+ Float.toString(cs.x) + ", Y: "
								+ Float.toString(cs.y));
			}
			System.out
					.println("Button press: "
							+ Long.toString(VRInput
									.getRawControllerState(index).ulButtonPressed)
							+ ", touch: "
							+ Long.toString(VRInput
									.getRawControllerState(index).ulButtonTouched));

		} else {
			geo.setCullHint(CullHint.Always); // hide it
		}
	}

	// private void addAllBoxes() {
	// // float distance = 8;
	// // for (int x = 0; x < 35; x++) {
	// // float cos = FastMath.cos(x * FastMath.PI / 16f) * distance;
	// // float sin = FastMath.sin(x * FastMath.PI / 16f) * distance;
	// // Vector3f loc = new Vector3f(cos, 0, sin);
	// // addBox(loc, null, 1f);
	// // loc = new Vector3f(0, cos, sin);
	// // addBox(loc, null, 1f);
	// // }
	//
	// }

	// private static final Box smallBox = new Box(0.3f, 0.3f, .3f);
	//
	// private void addBox(Vector3f location, Quaternion rot, float scale) {
	// Geometry leftQuad = new Geometry("Box", smallBox);
	// if (rot != null) {
	// leftQuad.setLocalRotation(rot);
	// } else {
	// leftQuad.rotate(0.5f, 0f, 0f);
	// }
	// leftQuad.setLocalScale(scale);
	// leftQuad.setMaterial(mat);
	// leftQuad.setLocalTranslation(location);
	// rootNode.attachChild(leftQuad);
	// }
}