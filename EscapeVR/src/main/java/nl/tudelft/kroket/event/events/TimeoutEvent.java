package nl.tudelft.kroket.event.events;

import java.util.EventObject;

/**
 * TimeoutEvent, fired when the game timer runs out.
 * 
 * @author Team Kroket
 *
 */
public class TimeoutEvent extends EventObject {

  /**
   * 
   */
  private static final long serialVersionUID = 1896744754464507950L;

  public TimeoutEvent(Object source) {
    super(source);
  }
  
  

}
