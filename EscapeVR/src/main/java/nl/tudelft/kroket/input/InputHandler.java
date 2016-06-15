package nl.tudelft.kroket.input;

import java.util.ArrayList;
import java.util.List;

import com.jme3.input.DefaultJoystickAxis;
import com.jme3.input.InputManager;
import com.jme3.input.Joystick;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.JoyButtonTrigger;
import com.jme3.input.controls.KeyTrigger;

import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.log.Logger;

/**
 * InputHandler object, used to handle keyboard/gamepad input.
 * 
 * @author Team Kroket
 *
 */
public class InputHandler {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private InputManager inputManager;
  private List<InteractionHandler> listeners = new ArrayList<InteractionHandler>();

  /**
   * Constructor for InputHandler.
   * 
   * @param inputManager
   *          the InputManager
   * @param eventManager
   *          the EvenManager
   */
  public InputHandler(InputManager inputManager, EventManager eventManager) {

    log.info(className, "Initializing...");

    this.inputManager = inputManager;

    initKeyControls();
    initJoysticks();
  }

  /**
   * Registers a listerener.
   * 
   * @param listener
   *          an InteractionHandler object
   */
  public void registerListener(InteractionHandler listener) {

    if (listener == null) {
      return;
    }
    if (listeners.contains(listener)) {
      return;
    }
    listeners.add(listener);
  }

  /**
   * Removes a listener.
   * 
   * @param listener
   *          the listener to be removed
   */
  public void removeListener(InteractionHandler listener) {
    if (listeners.contains(listener)) {
      listeners.remove(listener);
    }
  }

  /**
   * Registers the names of the input mappings to the listener.
   * 
   * @param listener
   *          the InterActionHandler
   * @param mappingNames
   *          the name of the mappig as string
   */
  public void registerMappings(InteractionHandler listener, String... mappingNames) {

    registerListener(listener);

    for (String mapping : mappingNames) {
      inputManager.addListener((InputListener) listener, mapping);
    }
  }

  private void initKeyControls() {

    if (inputManager == null) {
      log.error(className, "inputManager == null");
    }
    log.info(className, "Registering mappings...");

    inputManager.addMapping("forward", new KeyTrigger(KeyInput.KEY_W));
    inputManager.addMapping("back", new KeyTrigger(KeyInput.KEY_S));
    inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_A));
    inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_D));

    // register numpad keys, for when we don't have a gamepad
    inputManager.addMapping("Button A", new KeyTrigger(KeyInput.KEY_NUMPAD1));
    inputManager.addMapping("Button B", new KeyTrigger(KeyInput.KEY_NUMPAD2));
    inputManager.addMapping("Button X", new KeyTrigger(KeyInput.KEY_NUMPAD3));
    inputManager.addMapping("Button Y", new KeyTrigger(KeyInput.KEY_NUMPAD4));

    inputManager.addMapping("Button A", new KeyTrigger(KeyInput.KEY_SPACE));

    inputManager.addMapping("lookRight", new KeyTrigger(KeyInput.KEY_RIGHT));
    inputManager.addMapping("lookLeft", new KeyTrigger(KeyInput.KEY_LEFT));
    inputManager.addMapping("lookUp", new KeyTrigger(KeyInput.KEY_DOWN));
    inputManager.addMapping("lookDown", new KeyTrigger(KeyInput.KEY_UP));

    inputManager.addMapping("tiltLeft", new KeyTrigger(KeyInput.KEY_H));
    inputManager.addMapping("tiltRight", new KeyTrigger(KeyInput.KEY_K));
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

      if (joy.getXAxis() instanceof DefaultJoystickAxis) {
        ((DefaultJoystickAxis) joy.getXAxis()).setDeadZone(0.40f);
        ((DefaultJoystickAxis) joy.getYAxis()).setDeadZone(0.40f);
        ((DefaultJoystickAxis) joy.getPovXAxis()).setDeadZone(0.60f);
        ((DefaultJoystickAxis) joy.getPovYAxis()).setDeadZone(0.60f);
        
        
      }

      joy.rumble(1f);

      // assign axes left stick
      joy.getAxes().get(0).assignAxis("right", "left");
      joy.getAxes().get(1).assignAxis("forward", "back");
      // assign axes right stick
      joy.getAxes().get(2).assignAxis("lookRight", "lookLeft");
      joy.getAxes().get(3).assignAxis("lookUp", "lookDown");

      inputManager.addMapping("Button A", new JoyButtonTrigger(0, 0));
      inputManager.addMapping("Button B", new JoyButtonTrigger(0, 1));
      inputManager.addMapping("Button X", new JoyButtonTrigger(0, 2));
      inputManager.addMapping("Button Y", new JoyButtonTrigger(0, 3));

      // inputManager.addListener(actionListener, "Button A", "Button B", "Button X", "Button Y");

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

    for (InteractionHandler actionListener : listeners) {
      actionListener.update(tpf);
    }

  }

}
