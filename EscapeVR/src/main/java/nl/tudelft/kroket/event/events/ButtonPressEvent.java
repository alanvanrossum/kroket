package nl.tudelft.kroket.event.events;

import java.util.EventObject;

import nl.tudelft.kroket.log.Logger;

public class ButtonPressEvent extends EventObject {

  
  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();
  
  /**
   * 
   */
  private static final long serialVersionUID = 1899976237424414092L;
  String name;

  public ButtonPressEvent(Object source, String name) {
    super(source);
    this.name = name;
  }

  /**
   * Get the name of the object this event is attached to.
   * 
   * @return the name of the object
   */
  public String getName() {
    return name;
  }
}
