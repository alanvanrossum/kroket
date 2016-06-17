package nl.tudelft.kroket.input.interaction;

import nl.tudelft.kroket.input.InteractionHandler;
import nl.tudelft.kroket.log.Logger;
import jmevr.app.VRApplication;

import java.util.ArrayList;
import java.util.List;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 * Handles movement of the player.
 * 
 * @author Team Kroket
 *
 */
public class MovementHandler extends InteractionHandler implements ActionListener {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private float movementSpeed = 8f;

  private boolean forceFlying = false;

  private Node rootNode;

  private boolean restrictObserver = true;

  float collisionThreshold = 3.2f;
  float collisionOffset = 8.0f;

  // Using a string here so that we only need to use the name
  // so that this works even when objects aren't present in the hierarchy
  private List<String> objectList;

  private boolean moveForward, moveBackwards;
  private boolean moveLeft, moveRight;

  private boolean lockHorizontal = true;
  
  /**
   * Getter for the forceFlying boolean.
   * 
   * @return true iff flying is enabled.
   */
  public boolean isForceFlying() {
    return forceFlying;
  }

  /**
   * Setter for the forceFlying boolean.
   * 
   * @param forceFlying true iff forceFlying should be set to true.
   */
  public void setForceFlying(boolean forceFlying) {
    this.forceFlying = forceFlying;
  }

  /**
   * Constuctor for movementHandler.
   * 
   * @param observer
   *          the observer spatial
   * @param rootNode
   *          the rootnode
   */
  public MovementHandler(Spatial observer, Node rootNode) {
    super(observer);

    this.rootNode = rootNode;
    this.objectList = new ArrayList<String>();

  }

  /**
   * Setter for the movement speed.
   * 
   * @param speed the speed to be set.
   */
  public void setMovementSpeed(float speed) {
    this.movementSpeed = speed;
  }

  /**
   * Getter for the movement speed.
   * 
   * @return the movement speed
   */
  public float getMovementSpeed() {
    return this.movementSpeed;
  }

  /**
   * Getter for the lockHorizontal boolean.
   * 
   * @return true iff the horizontal axis are locked.
   */
  public boolean isLockHorizontal() {
    return lockHorizontal;
  }

  /**
   * Setter for locking the horizontal axis.
   * 
   * @param lockHorizontal the boolean to be set.
   */
  public void setLockHorizontal(boolean lockHorizontal) {
    this.lockHorizontal = lockHorizontal;
  }

  /**
   * Handles movement to front, back, left and right.
   */
  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (name.equals("forward")) {
      moveForward = keyPressed;
    } else if (name.equals("back")) {
      moveBackwards = keyPressed;
    }

    if (name.equals("left")) {
      moveLeft = keyPressed;
    } else if (name.equals("right")) {
      moveRight = keyPressed;
    }

  }

  /**
   * Add an object we want to use.
   * 
   * @param objectName
   *          object name
   */
  public void addObject(String objectName) {

    if (objectList.contains(objectName)) {
      return;
    }
    log.debug(className, "Adding collision object: " + objectName);
    objectList.add(objectName);
  }

  /**
   * Removes an object.
   * 
   * @param objectName
   *          object name
   */
  public void removeObject(String objectName) {
    if (!objectList.contains(objectName)) {
      return;
    }
    log.debug(className, "Removing collision object: " + objectName);
    objectList.remove(objectName);
  }

  /**
   * Checks if movement is allowed.
   * 
   * @param newPos
   *          a vector3f
   * @return a boolean
   */
  private boolean allowMovement(Vector3f newPos) {

    if (!restrictObserver) {
      return true;
    }

    for (String objectName : objectList) {

      Spatial object = rootNode.getChild(objectName);

      if (object == null) {
        continue;
      }

      if (intersectsWith(object, newPos)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Makes the move.
   * 
   * @param tpf
   *          a float
   * @param rotationColumn
   *          the rotation column
   */
  public void move(float tpf, int rotationColumn) {

    Vector3f newPosition = VRApplication.getFinalObserverRotation()
        .getRotationColumn(rotationColumn).mult(tpf * movementSpeed);

    Vector3f oldPosition = newPosition.subtract(
        VRApplication.getFinalObserverRotation().getRotationColumn(rotationColumn)).mult(
        tpf * movementSpeed);

    if (isLockHorizontal()) {

      newPosition.setY(0);
      oldPosition.setY(0);
    }

    if (allowMovement(newPosition.mult(movementSpeed))) {
      observer.move(newPosition);
    } else if (allowMovement(oldPosition)) {
      observer.move(oldPosition);
    }
  }

  /**
   * Update method.
   */
  public void update(float tpf) {

    if (moveForward || isForceFlying()) {
      move(tpf, 2);
    }

    if (moveBackwards && !isForceFlying()) {
      move(-tpf, 2);
    }

    if (moveLeft) {
      move(tpf, 0);

    }

    if (moveRight) {
      move(-tpf, 0);
    }
  }

  /**
   * Checks if player intersects with an object.
   * 
   * @param object
   *          the object
   * @param newPos
   *          the position
   * @return true if there is an intersection
   */
  private boolean intersectsWith(Spatial object, Vector3f newPos) {

    boolean intersects = (object.getWorldBound().intersects(observer.clone(false).move(newPos)
        .getWorldBound()));

    if (intersects) {
      log.debug(className, "Observer intersects with object: " + object.getName());
    }

    return intersects;

  }

  public boolean isMoveForward() {
    return moveForward;
  }

  public void setMoveForward(boolean moveForward) {
    this.moveForward = moveForward;
  }

  public boolean isMoveBackwards() {
    return moveBackwards;
  }

  public void setMoveBackwards(boolean moveBackwards) {
    this.moveBackwards = moveBackwards;
  }

  public boolean isMoveLeft() {
    return moveLeft;
  }

  public void setMoveLeft(boolean moveLeft) {
    this.moveLeft = moveLeft;
  }

  public boolean isMoveRight() {
    return moveRight;
  }

  public void setMoveRight(boolean moveRight) {
    this.moveRight = moveRight;
  }

  public List<String> getObjectList() {
    return objectList;
  }
  
  

}
