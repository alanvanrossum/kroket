package nl.tudelft.kroket.input.interaction;

import nl.tudelft.kroket.input.InteractionHandler;
import com.jme3.input.controls.ActionListener;
import com.jme3.scene.Spatial;

public class RotationHandler extends InteractionHandler implements ActionListener {

  float rotationSpeed = 0.85f;

  private boolean rotateUp;
  private boolean rotateDown;
  private boolean rotateLeft;
  private boolean rotateRight;

  private boolean tiltRight;
  private boolean tiltLeft;

  public RotationHandler(Spatial observer) {
    super(observer);
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

    // TODO: only call externally?
    update(tpf);

  }

  public void update(float tpf) {

    float deltaMovement = rotationSpeed * tpf;
    handleRotation(deltaMovement);
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
  }

  private void rotateZ(float delta) {
    observer.rotate(0, 0, delta);
  }

}
