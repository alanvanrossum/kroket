package nl.tudelft.kroket.event;

import java.util.EventObject;

import nl.tudelft.kroket.event.events.InteractionEvent;

/**
 * EventListener interface.
 * 
 * @author Team Kroket
 *
 */
public interface EventListener {
  public void handleEvent(EventObject event);

}