package nl.tudelft.kroket.input;

import com.jme3.scene.Spatial;

/**
 * Handles interactions with (objects in the) room.
 * 
 * @author Team Kroket
 *
 */
public abstract class InteractionHandler {

  protected Spatial observer;

  /**
   * Constructor for the interaction handler.
   * 
   * @param observer the observer of the handler.
   */
  public InteractionHandler(Spatial observer) {
    setObserver(observer);
  }

  /**
   * Getter for the observer.
   * 
   * @return the observer.
   */
  public Spatial getObserver() {
    return observer;
  }

  /**
   * Setter for the observer.
   * 
   * @param observer the observer to be set.
   */
  public void setObserver(Spatial observer) {
    this.observer = observer;
  }

  /**
   * Abstract method for updating.
   * 
   * @param tpf time per frame
   */
  public abstract void update(float tpf);

}
