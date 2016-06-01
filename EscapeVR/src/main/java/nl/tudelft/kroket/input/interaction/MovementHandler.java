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

public class MovementHandler extends InteractionHandler implements ActionListener {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private final float movementSpeed = 8f;

  private Node rootNode;

  public MovementHandler(Spatial observer, Node rootNode) {
    super(observer);

    this.rootNode = rootNode;
    this.objectList = new ArrayList<String>();

  }

  private boolean restrictObserver = true;

  float collisionThreshold = 3.2f;
  float collisionOffset = 8.0f;

  // using a string here so that we only need to use the name
  // so that this works even when objects aren't present in the hierarchy
  private List<String> objectList;

  private boolean moveForward, moveBackwards;

  private boolean flying = true;

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (name.equals("forward")) {
      if (keyPressed) {
        moveForward = true;
      } else {
        moveForward = false;
      }
    } else if (name.equals("back")) {
      if (keyPressed) {
        moveBackwards = true;
      } else {
        moveBackwards = false;
      }
    }
  }

  public void addObject(String objectName) {
    log.debug(className, "Adding collision object: " + objectName);
    objectList.add(objectName);
  }

  public void removeObject(Spatial objectName) {
    log.debug(className, "Removing collision object: " + objectName);
    objectList.remove(objectName);
  }

  private boolean allowMovement(Vector3f newPos) {

    if (!restrictObserver)
      return true;

    // boolean intersects = false;

    for (String objectName : objectList) {

      Spatial object = rootNode.getChild(objectName);

      if (object == null)
        continue;

      if (intersectsWith(object, newPos))
        return false;
    }
    return true;
  }

  public void update(float tpf) {

    // float deltaCorrected = collisionOffset * tpf;
    if (moveForward) {

      Vector3f newPosition = VRApplication.getFinalObserverRotation().getRotationColumn(2)
          .mult(tpf * movementSpeed);

      Vector3f oldPosition = newPosition
          .subtract(VRApplication.getFinalObserverRotation().getRotationColumn(2))
          .mult(tpf * movementSpeed);

      if (allowMovement(newPosition)) {
        observer.move(newPosition);
      } else if (allowMovement(oldPosition))
        observer.move(oldPosition);
    }
    if (moveBackwards) {
      Vector3f newPosition = VRApplication.getFinalObserverRotation().getRotationColumn(2)
          .mult(-tpf * movementSpeed);

      Vector3f oldPosition = newPosition
          .subtract(VRApplication.getFinalObserverRotation().getRotationColumn(2))
          .mult(-tpf * movementSpeed);

      if (allowMovement(newPosition)) {
        observer.move(newPosition);
      } else if (allowMovement(oldPosition))
        observer.move(oldPosition);
    }

  }

  private boolean intersectsWith(Spatial object, Vector3f newPos) {

    boolean intersects = (object.getWorldBound()
        .intersects(observer.clone(false).move(newPos).getWorldBound()));

    // if (intersects) {
    // log.debug(className, "Observer intersects with object: " + object.getName());
    // }

    return intersects;

  }

}
