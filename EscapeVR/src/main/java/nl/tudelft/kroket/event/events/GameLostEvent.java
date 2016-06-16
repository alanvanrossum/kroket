package nl.tudelft.kroket.event.events;

import java.util.EventObject;

/**
 * Event to indicate that the game is lost.
 * 
 * @author Team Kroket
 *
 */
@SuppressWarnings("serial")
public class GameLostEvent extends EventObject {

  /**
   * Constructor for the GameLostEvent.
   * 
   * @param source the source of the event.
   */
  public GameLostEvent(Object source) {
    super(source);
  }
  

}
