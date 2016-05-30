package nl.tudelft.kroket.input.interaction;

import nl.tudelft.kroket.input.InteractionHandler;
import jmevr.app.VRApplication;

import com.jme3.bullet.control.CharacterControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class RotationHandler extends InteractionHandler implements ActionListener {

  float rotationSpeed = 0.85f;

  private boolean rotateUp;
  private boolean rotateDown;
  private boolean rotateLeft;
  private boolean rotateRight;

  private boolean tiltRight;
  private boolean tiltLeft;
  
  private CharacterControl player;

  public RotationHandler(Spatial observer,  CharacterControl player) {
    super(observer);
    
    this.player = player;
  }

  @Override
  public void onAction(String name, boolean keyPressed, float tpf) {

    if (name.equals("left")) {
      rotateLeft = keyPressed;
    } else if (name.equals("right")) {
      rotateRight = keyPressed;
    }

    if (name.equals("lookup")) {
      rotateUp = keyPressed;
    } else if (name.equals("lookdown")) {
      rotateDown = keyPressed;
    }

    if (name.equals("tiltleft")) {
      tiltLeft = keyPressed;
    } else if (name.equals("tiltright")) {
      tiltRight = keyPressed;
    }

    update(tpf);

  }

  public void update(float tpf) {

    float deltaMovement = rotationSpeed * tpf;
    handleRotation(deltaMovement);
    
    
    player.setPhysicsLocation(observer.getLocalTranslation());
    //player.setPhysicsLocation(VRApplication.getFinalObserverPosition());
    
    System.out.println("Player is at " + player.getPhysicsLocation().toString());
    System.out.println("Observer is at " + observer.getLocalTranslation().toString());
  //  observer.move(player.getPhysicsLocation());
    
  }

  private void handleRotation(float deltaMovement) {
    if (rotateDown) {
      rotateX(deltaMovement);
    }
    if (rotateUp) {
      rotateX(-deltaMovement);
    }

    if (rotateLeft) {
      rotateY(deltaMovement);
    }
    if (rotateRight) {
      rotateY(-deltaMovement);
    }

    if (tiltRight) {
      rotateZ(deltaMovement);
    }
    if (tiltLeft) {
      rotateZ(-deltaMovement);
    }
  }

  private void rotateX(float delta) {
    observer.rotate(delta, 0, 0);
  }

  private void rotateY(float delta) {
    observer.rotate(0, delta, 0);
    
    //player.jump();
    
    player.setPhysicsLocation(new Vector3f(0f, 
        player.getPhysicsLocation().getY() + delta, 0f
    ));
  }

  private void rotateZ(float delta) {
    observer.rotate(0, 0, delta);
    
    player.setPhysicsLocation(new Vector3f(0f, 0f,
        player.getPhysicsLocation().getZ() + delta
    ));
    
  }

}
