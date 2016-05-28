package nl.tudelft.kroket.input.interaction;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.kroket.input.InteractionHandler;

public class CollisionHandler extends InteractionHandler {

  private boolean restrictObserver;

  private Vector3f boundaries;

  float collisionThreshold = 0.9f;
  float collisionOffset = 8.0f;

  public CollisionHandler(Spatial observer, Vector3f boundaries) {
    super(observer);

    this.boundaries = boundaries;
    
    enableRestriction();
  }

  private void handleCollision(float collisionOffset, float collisionThreshold, float tpf) {

    float deltaCorrected = collisionOffset * tpf;

    // collide X
    if (observer.getLocalTranslation().getX() > boundaries.getX() - collisionThreshold) {
      observer.move(-deltaCorrected, 0, 0);
    }
    if (observer.getLocalTranslation().getX() < -boundaries.getX() + collisionThreshold) {
      observer.move(deltaCorrected, 0, 0);
    }
    // collideZ
    if (observer.getLocalTranslation().getZ() > boundaries.getZ() - collisionThreshold) {
      observer.move(0, 0, -deltaCorrected);
    }
    if (observer.getLocalTranslation().getZ() < -boundaries.getZ() + collisionThreshold) {
      observer.move(0, 0, deltaCorrected);
    }
    // roof
    if (observer.getLocalTranslation().getY() > boundaries.getY()) {
      observer.move(0, -deltaCorrected, 0);
    }
    // floor
    if (observer.getLocalTranslation().getY() < 0) {
      observer.move(0, deltaCorrected, 0);
    }
  }

  public void enableRestriction() {
    setRestriction(true);
  }

  public void disableRestriction() {
    setRestriction(false);
  }

  public void setRestriction(boolean restrict) {
    this.restrictObserver = restrict;
  }

  @Override
  public void update(float tpf) {

    if (restrictObserver) {
      handleCollision(collisionOffset, collisionThreshold, tpf);
    }
  }

}
