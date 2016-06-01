package nl.tudelft.kroket.input.interaction;

import nl.tudelft.kroket.input.InteractionHandler;
import jmevr.app.VRApplication;

import java.util.ArrayList;
import java.util.List;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class MovementHandler extends InteractionHandler implements ActionListener {

  private final float movementSpeed = 8f;

  public MovementHandler(Spatial observer) {
    super(observer);

    this.objectList = new ArrayList<Spatial>();

  }

  private boolean restrictObserver;

  float collisionThreshold = 3.2f;
  float collisionOffset = 8.0f;

  private List<Spatial> objectList;

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

  public void addObject(Spatial object) {
    if (object == null)
      return;

    System.out.println("Adding collision object: " + object.getName());
    objectList.add(object);
  }

  public void removeObject(Spatial object) {
    if (object == null)
      return;

    objectList.remove(object);
  }

  private boolean allowMovement(Vector3f newPos) {

    // boolean intersects = false;

    for (Spatial object : objectList) {
      if (object == null)
        continue;

      if (intersectsWith(object, newPos))
        return false;
    }
    return true;
  }

  public void update(float tpf) {

    float deltaCorrected = collisionOffset * tpf;

    if (flying) {
      if (moveForward) {

        Vector3f newPos = VRApplication.getFinalObserverRotation().getRotationColumn(2)
            .mult(tpf * movementSpeed);

        Vector3f direction = newPos
            .subtract(VRApplication.getFinalObserverRotation().getRotationColumn(2)).mult(tpf * movementSpeed);

        if (allowMovement(newPos)) {
          observer.move(newPos);
        } 
        else if (allowMovement(direction))
          observer.move(direction);
      }
      if (moveBackwards) {
        Vector3f newPos = VRApplication.getFinalObserverRotation().getRotationColumn(2)
            .mult(-tpf * movementSpeed);

        Vector3f direction = newPos
            .subtract(VRApplication.getFinalObserverRotation().getRotationColumn(2)).mult(-tpf * movementSpeed);

        if (allowMovement(newPos)) {
          observer.move(newPos);
        } 
        else if (allowMovement(direction))
          observer.move(direction);
      }
    } 
    
//    else {
//      if (moveForward) {
//        Vector3f direction = VRApplication.getFinalObserverRotation().getRotationColumn(2);
//        direction.setY(0);
//        observer.move(direction.mult(tpf * movementSpeed));
//      }
//      if (moveBackwards) {
//        Vector3f direction = VRApplication.getFinalObserverRotation().getRotationColumn(2);
//        direction.setY(0);
//        observer.move(direction.mult(-tpf * movementSpeed));
//      }
//    }
  }

  private boolean intersectsWith(Spatial object, Vector3f newPos) {

    boolean intersects = (object.getWorldBound()
        .intersects(observer.clone(false).move(newPos).getWorldBound()));

    if (intersects) {
      System.out.println("Intersects with object: " + object.getName());
    }

    return intersects;

  }

}
