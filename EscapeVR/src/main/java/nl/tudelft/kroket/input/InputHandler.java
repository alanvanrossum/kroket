package nl.tudelft.kroket.input;

import jmevr.app.VRApplication;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.log.Logger;

/**
 * InputHandler object, used to handle keyboard/gamepad input.
 * 
 * @author Team Kroket
 *
 */
public final class InputHandler {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** Observer controls. */
  private boolean moveForward, moveBackwards, rotateLeft, rotateRight, rotateUp, rotateDown,
      tiltLeft, tiltRight;

  private InputManager inputManager;
  private boolean flying;
  private Spatial observer;
  private boolean acceptInput = false;
  private EventManager eventManager;

  public InputHandler(InputManager inputManager, Spatial observer, EventManager eventManager,
      boolean acceptInput) {

    log.info(className, "Initializing...");
    flying = true;
    this.inputManager = inputManager;
    this.observer = observer;
    this.acceptInput = acceptInput;
    setEventManager(eventManager);

    setAcceptInput(acceptInput);

    initControls();
    initJoysticks();

    if (eventManager == null) {
      log.debug(className, "Event manager is null");
    } else {
      inputManager.addListener(eventManager.getActionListener(), "Button A", "Button B",
          "Button X", "Button Y");
    }
  }

  public void setEventManager(EventManager em) {
    this.eventManager = em;

  }

  public void setAcceptInput(boolean acceptInput) {
    this.acceptInput = acceptInput;
  }

  /**
   * Initialize keyboard controls.
   */
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
    // inputManager.addMapping("dumpImages", new KeyTrigger(KeyInput.KEY_I));

    // register numpad keys, for when we don't have a keypad
    inputManager.addMapping("Button A", new KeyTrigger(KeyInput.KEY_NUMPAD1));
    inputManager.addMapping("Button B", new KeyTrigger(KeyInput.KEY_NUMPAD2));
    inputManager.addMapping("Button X", new KeyTrigger(KeyInput.KEY_NUMPAD3));
    inputManager.addMapping("Button Y", new KeyTrigger(KeyInput.KEY_NUMPAD4));

    inputManager.addMapping("Button A", new KeyTrigger(KeyInput.KEY_SPACE));

    inputManager.addMapping("goup", new KeyTrigger(KeyInput.KEY_J));
    inputManager.addMapping("godown", new KeyTrigger(KeyInput.KEY_U));
    inputManager.addMapping("tiltleft", new KeyTrigger(KeyInput.KEY_H));
    inputManager.addMapping("tiltright", new KeyTrigger(KeyInput.KEY_K));

    inputManager.addListener(actionListener, "forward");
    inputManager.addListener(actionListener, "back");
    inputManager.addListener(actionListener, "left");
    inputManager.addListener(actionListener, "right");

    inputManager.addListener(actionListener, "goup");
    inputManager.addListener(actionListener, "godown");
    inputManager.addListener(actionListener, "tiltleft");
    inputManager.addListener(actionListener, "tiltright");

    log.debug(className, "Keyboard controls registered.");

  }

  /**
   * Initialize joysticks/gamepads.
   */
  private void initJoysticks() {

    log.debug(className, "Registering joystick/gamepad controls...");

    Joystick[] joysticks = inputManager.getJoysticks();

    if (joysticks == null || joysticks.length == 0) {
      log.debug(className, "No joystick/gamepad found.");
    } else {

      Joystick joy = joysticks[0];

      // assign axes
      joy.getAxes().get(0).assignAxis("right", "left");
      joy.getAxes().get(1).assignAxis("forward", "back");

      inputManager.addMapping("Button A", new JoyButtonTrigger(0, 0));
      inputManager.addMapping("Button B", new JoyButtonTrigger(0, 1));
      inputManager.addMapping("Button X", new JoyButtonTrigger(0, 2));
      inputManager.addMapping("Button Y", new JoyButtonTrigger(0, 3));

      inputManager.addListener(actionListener, "Button A", "Button B", "Button X", "Button Y");

      log.debug(className, "Joystick/gamepad controls registered.");
    }
  }

  /**
   * Handle user input. This should be called upon refresh.
   * 
   * @param tpf
   *          time per frame
   */
  public void handleInput(float tpf) {

    handleMovement(tpf);

    float collisionThreshold = 0.9f;
    float collisionOffset = 8.0f;
    //handleCollision(collisionOffset, collisionThreshold, tpf);

    float speedMovement = 0.75f;
    float deltaMovement = speedMovement * tpf;
    handleRotation(deltaMovement);
  }

  private void handleMovement(float tpf) {

    if (flying) {
      if (moveForward) {
        observer.move(VRApplication.getFinalObserverRotation().getRotationColumn(2).mult(tpf * 8f));
      }
      if (moveBackwards) {
        observer
            .move(VRApplication.getFinalObserverRotation().getRotationColumn(2).mult(-tpf * 8f));
      }
    } else {
      if (moveForward) {
        Vector3f direction = VRApplication.getFinalObserverRotation().getRotationColumn(2);
        direction.setY(0);
        observer.move(direction.mult(tpf * 8f));
      }
      if (moveBackwards) {
        Vector3f direction = VRApplication.getFinalObserverRotation().getRotationColumn(2);
        direction.setY(0);
        observer.move(direction.mult(-tpf * 8f));
      }
    }
  }

  private void handleCollision(float collisionOffset, float collisionThreshold, float tpf) {

    // currently using fixed boundaries
    float roomDepth = 12f;
    float roomWidth = 9f;
    float roomHeight = 6f;

    float deltaCorrected = collisionOffset * tpf;

    // collide X
    if (observer.getLocalTranslation().x > roomWidth - collisionThreshold) {
      observer.move(-deltaCorrected, 0, 0);
    }
    if (observer.getLocalTranslation().x < -roomWidth + collisionThreshold) {
      observer.move(deltaCorrected, 0, 0);
    }
    // collideZ
    if (observer.getLocalTranslation().z > roomDepth - collisionThreshold) {
      observer.move(0, 0, -deltaCorrected);
    }
    if (observer.getLocalTranslation().z < -roomDepth + collisionThreshold) {
      observer.move(0, 0, deltaCorrected);
    }
    // roof
    if (observer.getLocalTranslation().y > roomHeight) {
      observer.move(0, -deltaCorrected, 0);
    }
    // floor
    if (observer.getLocalTranslation().y < 0) {
      observer.move(0, deltaCorrected, 0);
    }
  }

  private void handleRotation(float deltaMovement) {
    if (rotateDown) {
      rotateX(deltaMovement);
    }
    if (rotateUp) {
      rotateX(-deltaMovement);
    }

    if (rotateLeft) {
      rotateY(deltaMovement);
    }
    if (rotateRight) {
      rotateY(-deltaMovement);
    }

    if (tiltRight) {
      rotateZ(deltaMovement);
    }
    if (tiltLeft) {
      rotateZ(-deltaMovement);
    }
  }

  private void rotateX(float delta) {
    observer.rotate(delta, 0, 0);
  }

  private void rotateY(float delta) {
    observer.rotate(0, delta, 0);
  }

  private void rotateZ(float delta) {
    observer.rotate(0, 0, delta);
  }

  /**
   * Default ActionListener object, used to process user input.
   */

  private ActionListener actionListener = new ActionListener() {

    public void onAction(String name, boolean keyPressed, float tpf) {

      // ignore input if acceptInput is set to false
      if (!acceptInput) {
        return;
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
      } else if (name.equals("goup")) {
        if (keyPressed) {
          rotateUp = true;
        } else {
          rotateUp = false;
        }
      } else if (name.equals("tiltleft")) {
        if (keyPressed) {
          tiltLeft = true;
        } else {
          tiltLeft = false;
        }
      } else if (name.equals("tiltright")) {
        if (keyPressed) {
          tiltRight = true;
        } else {
          tiltRight = false;
        }
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
