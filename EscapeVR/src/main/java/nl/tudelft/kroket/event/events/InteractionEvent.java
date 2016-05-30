package nl.tudelft.kroket.event.events;

import java.util.EventObject;

import jmevr.app.VRApplication;

import com.jme3.scene.Spatial;

@SuppressWarnings("serial")
public class InteractionEvent extends EventObject {

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
   * @param threshold
   *          positional condition (distance threshold)
   * @param button
   *          the button that was used
   * @return true if event should be fired
   */
  public static boolean checkConditions(Spatial object, float threshold, String button) {

    float distance = VRApplication.getFinalObserverPosition().distance(
        object.getWorldBound().getCenter());
    
    System.out.println("distance = " + distance);

    return (distance <= threshold && button.equals("Button A"));
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
