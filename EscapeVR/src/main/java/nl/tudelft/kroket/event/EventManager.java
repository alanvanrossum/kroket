package nl.tudelft.kroket.event;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;

import nl.tudelft.kroket.event.events.ButtonPressEvent;
import nl.tudelft.kroket.event.events.InteractionEvent;
import nl.tudelft.kroket.input.InteractionHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.minigames.ColorSequenceMinigame;

import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Manages events.
 * 
 * @author Team Kroket
 *
 */
public class EventManager extends InteractionHandler implements ActionListener {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** List of events. */
  private CopyOnWriteArrayList<EventObject> eventList = new CopyOnWriteArrayList<EventObject>();

  /** List of all registered listeners. */
  private List<EventListener> listenerList = new ArrayList<EventListener>();
  
  /** Time in milliseconds new input is ignored after an event. */
  private final int INPUT_GRACE_PERIOD = 200;

  long prevInput = 0;

  private Node rootNode;

  /** All registered triggers: the name of the object and the range. */
  private HashMap<String, Float> triggers = new HashMap<String, Float>();

  /**
   * constructor of the event manager.
   * 
   * @param root
   *          - node
   */
  public EventManager(Spatial observer, Node root) {
    super(observer);
    log.info(className, "Initializing...");

    this.rootNode = root;
  }

  /**
   * Register an object to fire an InteractionEvent.
   * 
   * @param objName
   *          the name of the object
   * @param range
   *          the range the player/observer has to be in (related to the object)
   */
  public void registerObjectInteractionTrigger(String objName, float range) {
    triggers.put(objName, range);
  }

  /**
   * Fire/delegate all events we have in our list and forward them to all registered listeners.
   */
  private synchronized void fireEvents() {

    // Copy list to prevent concurrent modifications
    // this could be dealt with more elegantly
    for (EventObject event : eventList) {
      for (EventListener listener : listenerList) {

        listener.handleEvent(event);

      }
    }

    eventList.clear();
  }

  /**
   * Add an event to be fired.
   * 
   * @param type
   *          the type/name of the event
   * @param event
   *          the event object
   */
  public synchronized void addEvent(EventObject event) {
    eventList.add(event);
  }

  /**
   * Add a listener.
   * 
   * @param listener
   *          object to be registered
   */
  public void addListener(EventListener listener) {
    listenerList.add(listener);
  }

  /**
   * Remove a listener.
   * 
   * @param listener
   *          object to be removed
   */
  public void removeListener(EventListener listener) {
    listenerList.remove(listener);
  }

  /**
   * General update method.
   */
  public void update(float tpf) {
	
    // Fire all events we registered
    fireEvents();
  }

  @Override
  public void onAction(String name, boolean isPressed, float tpf) {

    // If the button wasn't pressed, ignore it
    if (!isPressed) {
      return;
    }

    // Get current system time
    long now = System.currentTimeMillis();

    // Compute difference between the current button press
    // and previous button press
    long delta = now - prevInput;

    // If the time between button presses is too short
    // ignore the button press
    if (delta < INPUT_GRACE_PERIOD) {
      return;
    }

    // Store the current action's time so we can compare it later
    prevInput = now;

    // Register a new button press event
    addEvent(new ButtonPressEvent(this, name));

    // Loop over all object triggers
    for (Entry<String, Float> entry : triggers.entrySet()) {

      // Get the object from the rootnode
      Spatial object = rootNode.getChild(entry.getKey());

      // Check whether the object exists
      if (object == null) {
        log.error(
            className,
            String.format("Warning: object '%s' does not exist in current scene (null)",
                entry.getKey()));

      } else {
    	  
        if (InteractionEvent.checkConditions(object, entry.getValue(), name)
            && !ColorSequenceMinigame.isActive()) {
          InteractionEvent interactionEvent = new InteractionEvent(this, entry.getKey());
          addEvent(interactionEvent);
        }

      }
    }

  }

}