package nl.tudelft.kroket.event.events;

import java.util.EventObject;

/**
 * Event that indicates that a minigame is complete.
 * 
 * @author Team Kroket
 *
 */
@SuppressWarnings("serial")
public class MinigameCompleteEvent extends EventObject {

  /**
   * Constructor for the MinigameCompleteEvent.
   * 
   * @param source the source of the event.
   */
  public MinigameCompleteEvent(Object source) {
    super(source);
  }

}
