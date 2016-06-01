package nl.tudelft.kroket.event;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import nl.tudelft.kroket.event.events.ButtonPressEvent;
import nl.tudelft.kroket.event.events.InteractionEvent;
import nl.tudelft.kroket.input.InteractionHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.minigames.ColorSequenceMinigame;

import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.InputListener;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class EventManager extends InteractionHandler implements ActionListener {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private List<EventObject> eventList = new ArrayList<EventObject>();
  private List<EventListener> listenerList = new ArrayList<EventListener>();

  private final int INPUT_GRACE_PERIOD = 200;

  long prevInput = 0;

  private Node rootNode;

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

  public void registerObjectInteractionTrigger(String objName, float range) {
    triggers.put(objName, range);
  }

  private synchronized void fireEvents() {

    for (EventObject event : eventList) {
      for (EventListener listener : listenerList) {

        ((EventListener) listener).handleEvent(event);

      }
    }

    eventList.clear();
  }

  public synchronized void addEvent(String type, EventObject event) {
    eventList.add(event);

//    System.out.println("Events in list:");
//
//    for (EventObject entry : eventList) {
//      System.out.println(entry);
//    }
  }

  public synchronized void addListener(EventListener listener) {
    listenerList.add(listener);
  }

  public synchronized void removeListener(EventListener listener) {
    listenerList.remove(listener);
  }

  public void update(float tpf) {
    fireEvents();
  }

  @Override
  public void onAction(String name, boolean isPressed, float tpf) {
    if (!isPressed) {
      return;
    }

    System.out.println("onAction " + name);

    long now = System.currentTimeMillis();
    long delta = now - prevInput;

    if (delta < INPUT_GRACE_PERIOD) {
      return;
    }

    prevInput = now;

    ButtonPressEvent bbEvent = new ButtonPressEvent(this, name);
    addEvent("buttonpress", bbEvent);

    for (Entry<String, Float> entry : triggers.entrySet()) {

      Spatial object = rootNode.getChild(entry.getKey());

      if (object == null) {
        log.error(className, String
            .format("Warning: object '%s' does not exist in current scene (null)", entry.getKey()));

      } else {

        if (InteractionEvent.checkConditions(object, entry.getValue(), name) 
            && !ColorSequenceMinigame.isActive()) {
          InteractionEvent interactionEvent = new InteractionEvent(this, entry.getKey());
          addEvent("interaction", interactionEvent);
        }

      }
    }
    fireEvents();

  }
}