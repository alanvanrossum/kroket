package nl.tudelft.kroket.event.events;

import java.util.EventObject;

import jmevr.app.VRApplication;

import com.jme3.scene.Spatial;

@SuppressWarnings("serial")
public class InteractionEvent extends EventObject {

  private static final String INTERACTION_BUTTON = "Button A";

  String name;

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

    return (distance <= range && button.equals(INTERACTION_BUTTON));
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
