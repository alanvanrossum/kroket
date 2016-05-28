package nl.tudelft.kroket.input.actions;

import jmevr.app.VRApplication;

import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;

public class MovementListener implements ActionListener {

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
    
    handleMovement(tpf);
    

  }

  private void handleMovement(float tpf) {

    if (flying) {
      if (moveForward) {
        observer.move(VRApplication.getFinalObserverRotation().getRotationColumn(2).mult(tpf * 8f));
      }
      if (moveBackwards) {
        observer
            .move(VRApplication.getFinalObserverRotation().getRotationColumn(2).mult(-tpf * 8f));
      }
    } else {
      if (moveForward) {
        Vector3f direction = VRApplication.getFinalObserverRotation().getRotationColumn(2);
        direction.setY(0);
        observer.move(direction.mult(tpf * 8f));
      }
      if (moveBackwards) {
        Vector3f direction = VRApplication.getFinalObserverRotation().getRotationColumn(2);
        direction.setY(0);
        observer.move(direction.mult(-tpf * 8f));
      }
    }
  }

}
