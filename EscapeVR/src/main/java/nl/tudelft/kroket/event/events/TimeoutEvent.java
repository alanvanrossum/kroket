package nl.tudelft.kroket.event.events;

import java.util.EventObject;

/**
 * TimeoutEvent, fired when the game timer runs out.
 * 
 * @author Team Kroket
 *
 */
@SuppressWarnings("serial")
public class TimeoutEvent extends EventObject {

  /**
   * Constructor for the TimeoutEvent.
   * 
   * @param source the source of the event.
   */
  public TimeoutEvent(Object source) {
    super(source);
  }
  
}
