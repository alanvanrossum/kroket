package nl.tudelft.kroket.event;

import java.util.EventObject;

public interface Event {
    public void handleEvent(EventObject e);
}