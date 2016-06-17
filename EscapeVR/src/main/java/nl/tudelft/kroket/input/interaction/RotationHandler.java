package nl.tudelft.kroket.input.interaction;

import nl.tudelft.kroket.input.InteractionHandler;
import nl.tudelft.kroket.log.Logger;

import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Spatial;

/**
 * Handles rotation of the player.
 * 
 * @author Team Kroket
 *
 */
public class RotationHandler extends InteractionHandler implements ActionListener {
  
  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  float rotationSpeed = 0.85f;

  private boolean lookUp;
  private boolean lookDown;
  private boolean lookLeft;
  private boolean lookRight;

  private boolean tiltRight;
  private boolean tiltLeft;

  /**
   * Constructor for the rotation handler.
   * 
   * @param observer the observer of the handler
   */
  public RotationHandler(Spatial observer) {
    super(observer);
  }

  /**
   * Handles looking up, down, left and right.
   */
  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (name.equals("lookLeft")) {
      lookLeft = keyPressed;
    } else if (name.equals("lookRight")) {
      lookRight = keyPressed;
    }

    if (name.equals("lookUp")) {
      lookUp = keyPressed;
    } else if (name.equals("lookDown")) {
      lookDown = keyPressed;
    }

    if (name.equals("tiltLeft")) {
      tiltLeft = keyPressed;
    } else if (name.equals("tiltRight")) {
      tiltRight = keyPressed;
    }
    
    update(tpf);

  }

  /**
   * Update triggers when the right stick sends input.
   */
  public void update(float tpf) {

    float deltaMovement = rotationSpeed * tpf;
    handleRotation(deltaMovement);
  }

  /**
   * Decides which axis and direction should be rotated on.
   * 
   * @param deltaMovement
   *          delata movement
   */
  private void handleRotation(float deltaMovement) {
    if (lookDown) {
      rotateX(deltaMovement);
    }
    if (lookUp) {
      rotateX(-deltaMovement);
    }

    if (lookLeft) {
      rotateY(deltaMovement);
    }
    if (lookRight) {
      rotateY(-deltaMovement);
    }

    if (tiltRight) {
      rotateZ(deltaMovement);
    }
    if (tiltLeft) {
      rotateZ(-deltaMovement);
    }
  }

  /**
   * Rotate around the x-axis.
   * 
   * @param delta the angle in radians
   */
  private void rotateX(float delta) {
    observer.rotate(delta, 0, 0);
  }

  /**
   * Rotate around the y-axis.
   * 
   * @param delta the angle in radians
   */
  private void rotateY(float delta) {
    observer.rotate(0, delta, 0);
  }

  /**
   * Rotate around the z-axis.
   * 
   * @param delta the angle in radians
   */
  private void rotateZ(float delta) {
    observer.rotate(0, 0, delta);
  }

  /**
   * Lookup boolean.
   * @return lookUp
   */
  public boolean isLookUp() {
    return lookUp;
  }

  /**
   * LookDown boolean.
   * @return lookDown
   */
  public boolean isLookDown() {
    return lookDown;
  }

  /**
   * LookLeft boolean.
   * @return lookLeft
   */
  public boolean isLookLeft() {
    return lookLeft;
  }

  /**
   * LookRight boolean.
   * @return lookRight
   */
  public boolean isLookRight() {
    return lookRight;
  }

  /**
   * TiltRight boolean.
   * @return tiltRight
   */
  public boolean isTiltRight() {
    return tiltRight;
  }

  /**
   * TiltLeft boolean.
   * @return tiltLeft
   */
  public boolean isTiltLeft() {
    return tiltLeft;
  }
  
  

}
