package nl.tudelft.kroket.event.events;

import java.util.EventObject;

/**
 * Event to indicate that the game should start.
 * 
 * @author Team Kroket
 *
 */
@SuppressWarnings("serial")
public class GameStartEvent extends EventObject {

  /**
   * Constructor for the GameStartEvent.
   * 
   * @param source the source of the event.
   */
  public GameStartEvent(Object source) {
    super(source);
  }
  

}
