package nl.tudelft.kroket.event.events;

import java.util.EventObject;

import nl.tudelft.kroket.escape.Settings;
import jmevr.app.VRApplication;

import com.jme3.scene.Spatial;

/**
 * Event that indicates that an interaction with an object has taken place.
 * 
 * @author Team Kroket
 *
 */
@SuppressWarnings("serial")
public class InteractionEvent extends EventObject {

  /** Name of the object. */
  String name;

  /**
   * Constructor for the InteractionEvent.
   * 
   * @param source the source of the event.
   * @param name the name of the interacted objet.
   */
  public InteractionEvent(Object source, String name) {
    super(source);
    this.name = name;
  }

  /**
   * Check the condition for firing this event.
   * 
   * @param object
   *          the object the user interacted with
   * @param range
   *          positional condition (distance threshold)
   * @param button
   *          the button that was used
   * @return true if event should be fired
   */
  public static boolean checkConditions(Spatial object, float range, String button) {

    float distance = VRApplication.getFinalObserverPosition()
        .distance(object.getWorldBound().getCenter());

    return (distance <= range && button.equals(Settings.INTERACTION_BUTTON));
  }

  /**
   * Get the name of the object this event is attached to.
   * 
   * @return the name of the object
   */
  public String getName() {
    return name;
  }
  
}
