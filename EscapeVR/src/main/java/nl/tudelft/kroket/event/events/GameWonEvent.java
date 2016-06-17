package nl.tudelft.kroket.event.events;

import java.util.EventObject;

/**
 * Event to indicate tha the game is won.
 * 
 * @author Team Kroket
 *
 */
@SuppressWarnings("serial")
public class GameWonEvent extends EventObject {

  /**
   * Constructor for the GameWonEvent.
   * 
   * @param source the source of the event
   */
  public GameWonEvent(Object source) {
    super(source);
  }
  

}
