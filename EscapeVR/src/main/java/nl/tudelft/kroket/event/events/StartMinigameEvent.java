package nl.tudelft.kroket.event.events;

import java.util.EventObject;

/**
 * Event that indicates that a minigame should start.
 * 
 * @author Team Kroket
 *
 */
@SuppressWarnings("serial")
public class StartMinigameEvent extends EventObject {
  
  /**
   * Constructor for the StartMinigameEvent.
   * 
   * @param source the source of the event.
   */
  public StartMinigameEvent(Object source) {
    super(source);
  }

}
