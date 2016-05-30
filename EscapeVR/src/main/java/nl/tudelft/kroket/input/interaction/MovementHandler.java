package nl.tudelft.kroket.input.interaction;

import nl.tudelft.kroket.input.InteractionHandler;
import jmevr.app.VRApplication;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class MovementHandler extends InteractionHandler implements ActionListener {

  private final float movementSpeed = 90f;

  private CharacterControl player;

  public MovementHandler(Spatial observer, CharacterControl player) {
    super(observer);

    this.player = player;
  }

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
    update(tpf);
  }

  public void update(float tpf) {

   /// System.out.println("MovementHandler.update()");

    if (flying) {
      if (moveForward) {
       //observer.move(VRApplication.getFinalObserverRotation().getRotationColumn(2)
         //   .mult(tpf * movementSpeed));
       //player.setPhysicsLocation(observer.getLocalTranslation());
    //    player.setPhysicsLocation(VRApplication.getFinalObserverRotation().getRotationColumn(2)
     //       .mult(tpf * movementSpeed));
       player.setWalkDirection(VRApplication.getFinalObserverRotation().getRotationColumn(2)
              .mult(tpf * movementSpeed));
     //  player.setPhysicsLocation(observer.getWorldTranslation());
       
     
      }
      if (moveBackwards) {
        player.setWalkDirection(VRApplication.getFinalObserverRotation().getRotationColumn(2)
            .mult(-tpf * movementSpeed));
        //observer.move(VRApplication.getFinalObserverRotation().getRotationColumn(2)
         //   .mult(-tpf * movementSpeed));
       // player.setPhysicsLocation(observer.getLocalTranslation());
       // player.setWalkDirection(VRApplication.getFinalObserverRotation().getRotationColumn(2)); 
      }
    } else {
      if (moveForward) {
        Vector3f direction = VRApplication.getFinalObserverRotation().getRotationColumn(2);
        direction.setY(0);
        observer.move(direction.mult(tpf * movementSpeed));
        player.setPhysicsLocation(observer.getLocalTranslation());
      }
      if (moveBackwards) {
        Vector3f direction = VRApplication.getFinalObserverRotation().getRotationColumn(2);
        direction.setY(0);
        observer.move(direction.mult(-tpf * movementSpeed));
        //player.setPhysicsLocation(observer.getLocalTranslation());
        player.setPhysicsLocation(observer.getLocalTranslation());

      }
    }
    
    if (!moveForward && !moveBackwards)
      player.setWalkDirection(new Vector3f(0, 0, 0));

    // System.out.println("updating...");

    // player.setViewDirection(observer.getLocalTranslation());
    //player.setPhysicsLocation(observer.getLocalTranslation());
    //observer.setLocalTranslation(player.getPhysicsLocation());
    
    
  }

}
