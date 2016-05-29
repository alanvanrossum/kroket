package nl.tudelft.kroket.input;

import com.jme3.scene.Spatial;

public abstract class InteractionHandler {

  protected Spatial observer;

  public InteractionHandler(Spatial observer) {
    setObserver(observer);
  }

  public Spatial getObserver() {
    return observer;
  }

  public void setObserver(Spatial observer) {
    this.observer = observer;
  }

  public abstract void update(float tpf);

}
