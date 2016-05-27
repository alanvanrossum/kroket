package nl.tudelft.kroket.event;

import java.util.EventObject;

/**
 * EventListener interface.
 * 
 * @author Team Kroket
 *
 */
public interface EventListener {
  public void handleEvent(EventObject event);

}