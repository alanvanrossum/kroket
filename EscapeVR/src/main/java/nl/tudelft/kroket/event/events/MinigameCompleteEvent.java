package nl.tudelft.kroket.event.events;

import java.util.EventObject;

public class MinigameCompleteEvent extends EventObject {

  /**
   * 
   */
  private static final long serialVersionUID = 4932454541418846945L;

  public MinigameCompleteEvent(Object source) {
    super(source);
  }

}
