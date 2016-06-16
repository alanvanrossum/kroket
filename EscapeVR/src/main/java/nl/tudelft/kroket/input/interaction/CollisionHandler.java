package nl.tudelft.kroket.input.interaction;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

import nl.tudelft.kroket.escape.Settings;
import nl.tudelft.kroket.input.InteractionHandler;

/**
 * Handles collisions.
 * 
 * @author Team Kroket
 *
 */
public class CollisionHandler extends InteractionHandler {

  private boolean restrictObserver;

  private Vector3f boundaries;

  float collisionThreshold = 0.9f;
  float collisionOffset = 8.0f;

  /**
   * Constructs a CollisionHandler.
   * 
   * @param observer
   *          the observer reference
   * @param boundaries
   *          the boundaries to restrict the observer to
   */
  public CollisionHandler(Spatial observer, Vector3f boundaries) {
    super(observer);

    this.boundaries = boundaries;

    if (Settings.WALLCOLLISION) {
      enableRestriction();
    }
  }

  /**
   * Handles collisions.
   * 
   * @param collisionOffset how hard you are pushed back when colliding 
   * @param collisionThreshold how close you can get to an object
   * @param tpf time per frame
   */
  private void handleCollision(float collisionOffset, float collisionThreshold, float tpf) {

    // deltaCorrected is the distance we should use to push the
    // observer back with whenever it tries to move into the boundaries
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

  /**
   * Enables restrictions.
   */
  public void enableRestriction() {
    setRestriction(true);
  }

  /**
   * Disable restrictions.
   */
  public void disableRestriction() {
    setRestriction(false);
  }

  /**
   * Enable or disable restrictions.
   * 
   * @param restrict true iff restrictions should be enabled.
   */
  public void setRestriction(boolean restrict) {
    this.restrictObserver = restrict;
  }

  /**
   * Update method which handles collisions.
   */
  @Override
  public void update(float tpf) {

    if (restrictObserver) {
      handleCollision(collisionOffset, collisionThreshold, tpf);
    }
  }

}
