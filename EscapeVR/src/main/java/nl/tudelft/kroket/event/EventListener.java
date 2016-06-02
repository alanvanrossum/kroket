package nl.tudelft.kroket.event;

import java.util.EventObject;

/**
 * EventListener interface.
 * 
 * @author Team Kroket
 *
 */
public interface EventListener {
  /**
   * Handle the event.
   * 
   * @param event
   *          the event
   */
  public void handleEvent(EventObject event);

}