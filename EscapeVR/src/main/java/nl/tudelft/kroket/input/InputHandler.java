package nl.tudelft.kroket.input;

import jmevr.app.VRApplication;
import jmevr.input.OpenVR;
import jmevr.util.VRGuiManager;

import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.scene.Spatial;

import nl.tudelft.kroket.log.Logger;

public final class InputHandler {

	private final String className = this.getClass().getSimpleName();
	private Logger log = Logger.getInstance();

	/** Observer controls. */
	private boolean moveForward, moveBackwards, rotateLeft, rotateRight, rotateUp, rotateDown;

	private InputManager inputManager;
	private Spatial observer;
	private boolean acceptInput = false;

	public InputHandler(InputManager inputManager, Spatial observer,
			boolean acceptInput) {

		log.info(className, "Initializing...");

		this.inputManager = inputManager;
		this.observer = observer;
		this.acceptInput = acceptInput;

		setAcceptInput(acceptInput);

		initControls();
		initJoysticks();
	}

	public void setAcceptInput(boolean acceptInput) {
		this.acceptInput = acceptInput;
	}

	public void initControls() {

		if (inputManager == null)
			log.error(className, "inputManager == null");

		log.debug(className, "Registering keyboard controls...");

		inputManager.addMapping("toggle", new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addMapping("incShift", new KeyTrigger(KeyInput.KEY_Q));
		inputManager.addMapping("decShift", new KeyTrigger(KeyInput.KEY_E));
		inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_W));
		inputManager.addMapping("back", new KeyTrigger(KeyInput.KEY_S));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));
		inputManager.addMapping("filter", new KeyTrigger(KeyInput.KEY_F));
		inputManager.addMapping("dumpImages", new KeyTrigger(KeyInput.KEY_I));
                inputManager.addMapping("goup", new KeyTrigger(KeyInput.KEY_U));
                inputManager.addMapping("godown", new KeyTrigger(KeyInput.KEY_J));

		inputManager.addListener(actionListener, "forward");
                inputManager.addListener(actionListener, "goup");
                inputManager.addListener(actionListener, "godown");
		inputManager.addListener(actionListener, "back");
		inputManager.addListener(actionListener, "left");
		inputManager.addListener(actionListener, "right");
		inputManager.addListener(actionListener, "toggle");
		inputManager.addListener(actionListener, "incShift");
		inputManager.addListener(actionListener, "decShift");
		inputManager.addListener(actionListener, "filter");
		inputManager.addListener(actionListener, "dumpImages");

		log.debug(className, "Keyboard controls registered.");
	}

	private void initJoysticks() {

		log.debug(className, "Registering joystick/gamepad controls...");

		Joystick[] joysticks = inputManager.getJoysticks();

		if (joysticks == null || joysticks.length == 0) {
			log.debug(className, "No joystick/gamepad found.");
		} else {

			// System.out.println("joysticks.length = " + joysticks.length);

			Joystick joy = joysticks[0];

			// assign axes
			joy.getAxes().get(0).assignAxis("right", "left");
			joy.getAxes().get(1).assignAxis("forward", "back");

			inputManager.addMapping("Button A", new JoyButtonTrigger(0, 0));
			inputManager.addMapping("Button B", new JoyButtonTrigger(0, 1));
			inputManager.addMapping("Button X", new JoyButtonTrigger(0, 2));
			inputManager.addMapping("Button Y", new JoyButtonTrigger(0, 3));

			inputManager.addListener(actionListener, "Button A", "Button B",
					"Button X", "Button Y");

			log.debug(className, "Joystick/gamepad controls registered.");
		}
	}

	public void handleInput(float tpf) {
    
		if (moveForward) {
			observer.move(VRApplication.getFinalObserverRotation()
					.getRotationColumn(2).mult(tpf * 8f));
		}
		if (moveBackwards) {
			observer.move(VRApplication.getFinalObserverRotation()
					.getRotationColumn(2).mult(-tpf * 8f));
		}
                if(rotateUp){
                    observer.rotate(-0.75f * tpf, 0, 0);                
                }
                if(rotateDown){
                    observer.rotate(0.75f * tpf, 0, 0 );                
                }
		if (rotateLeft) {
			observer.rotate(0, 0.75f * tpf, 0);
		}
		if (rotateRight) {
			observer.rotate(0, -0.75f * tpf, 0);
		}
	}

	private ActionListener actionListener = new ActionListener() {

		public void onAction(String name, boolean keyPressed, float tpf) {

			// ignore input if acceptInput is set to false
			if (!acceptInput)
				return;

			if (keyPressed) {
				 log.debug(className, "You have pressed : " + name);

//				System.out.println(String.format(
//						"onAction(name = %s, keyPressed = true, tpf = %s)",
//						name, tpf));
			} 
//			else {
//				System.out.println(String.format(
//						"onAction(name = %s, keyPressed = false tpf = %s)",
//						name, tpf));
//			}
			
			if (name.equals("incShift") && keyPressed) {
				VRGuiManager.adjustGuiDistance(-0.1f);
			} else if (name.equals("decShift") && keyPressed) {
				VRGuiManager.adjustGuiDistance(0.1f);
			}


			// else if (name.equals("filter") && keyPressed) {
			// // adding filters in realtime
			// CartoonSSAO cartfilt = new CartoonSSAO();
			// FilterPostProcessor fpp = new FilterPostProcessor(
			// getAssetManager());
			// fpp.addFilter(cartfilt);
			// getViewPort().addProcessor(fpp);
			// // filters added to main viewport during runtime,
			// // move them into VR processing
			// // (won't do anything if not in VR mode)
			// VRApplication.moveScreenProcessingToVR();
			// }
			//
			if (name.equals("toggle")) {
				VRGuiManager.positionGui();
			}
			if (name.equals("forward")) {
				if (keyPressed) {
					moveForward = true;
				} else {
					moveForward = false;
				}
                        } else if (name.equals("goup")) {
				if (keyPressed) {
					rotateUp = true;
				} else {
					rotateUp = false;
				}
                        } else if (name.equals("godown")) {
				if (keyPressed) {
					rotateDown = true;
				} else {
					rotateDown = false;
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

}
