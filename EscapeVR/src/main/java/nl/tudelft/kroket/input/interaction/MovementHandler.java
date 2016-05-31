package nl.tudelft.kroket.input.interaction;

import nl.tudelft.kroket.input.InteractionHandler;
import jmevr.app.VRApplication;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class MovementHandler extends InteractionHandler implements ActionListener {

  private final float movementSpeed = 90f;
  
  private Vector3f walkDirection = new Vector3f();

  private CharacterControl player;

  public MovementHandler(Spatial observer, CharacterControl player) {
    super(observer);

    this.player = player;
  }

  private boolean moveForward, moveBackwards;

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

  public void update(float tpf) {

    observer.setLocalTranslation(player.getPhysicsLocation());
    
    walkDirection.set(0, 0, 0);
    
    if (moveForward) {
        walkDirection.addLocal(VRApplication.getFinalObserverRotation().getRotationColumn(2)
            .mult(tpf * movementSpeed));

    } else if (moveBackwards) {
      walkDirection.addLocal(VRApplication.getFinalObserverRotation().getRotationColumn(2)
          .mult(-tpf * movementSpeed));
    }
    player.setWalkDirection(walkDirection);
    observer.setLocalTranslation(player.getPhysicsLocation());
    
   
  }

}
