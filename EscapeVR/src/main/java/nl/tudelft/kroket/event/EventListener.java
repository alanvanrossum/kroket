package nl.tudelft.kroket.event;

import java.util.EventObject;

import nl.tudelft.kroket.event.events.InteractionEvent;

/**
 * EventListener interface.
 * 
 * @author Team Kroket
 *
 */
public interface EventListener extends Event {

  @Override
  public default void handleEvent(EventObject e) {

    if (e instanceof InteractionEvent) {
      System.out.println("event received: " + e.toString());
    }
  }

}