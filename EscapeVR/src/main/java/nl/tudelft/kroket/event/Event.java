package nl.tudelft.kroket.event;

import java.util.EventObject;

/**
 * Event interface.
 * 
 * @author Team Kroket
 *
 */
public interface Event {
  public void handleEvent(EventObject e);
}