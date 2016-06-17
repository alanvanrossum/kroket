package nl.tudelft.kroket.input.interaction;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.jme3.scene.Spatial;

public class RotationHandlerTest {

  private Spatial observer;
  
  private RotationHandler rotationHandler;
  
  RotationHandler handlerSpy;
  
  /**
   * Sets up the mocks.
   * @throws Exception exception
   */
  @Before
  public void setUp() throws Exception {
    observer = PowerMockito.mock(Spatial.class);
    rotationHandler = new RotationHandler(observer);
    handlerSpy = Mockito.spy(rotationHandler);
  }

  @Test
  public void testUpdate() throws Exception {
    handlerSpy.update(1f);
//    PowerMockito.verifyPrivate(handlerSpy).invoke("handleRotation", Mockito.anyFloat());
  }

  @Test
  public void testRotationHandler() {
    assertNotNull(rotationHandler);
  }

  @Test
  public void testOnActionLookLeft() {
    assertFalse(rotationHandler.isLookLeft());
    rotationHandler.onAction("lookLeft", true, 1f);
    assertTrue(rotationHandler.isLookLeft());
  }
  
  @Test
  public void testOnActionLookRight() {
    assertFalse(rotationHandler.isLookRight());
    rotationHandler.onAction("lookRight", true, 1f);
    assertTrue(rotationHandler.isLookRight());
  }

  @Test
  public void testOnActionLookUp() {
    assertFalse(rotationHandler.isLookUp());
    rotationHandler.onAction("lookUp", true, 1f);
    assertTrue(rotationHandler.isLookUp());
  }
  
  @Test
  public void testOnActionLookDown() {
    assertFalse(rotationHandler.isLookDown());
    rotationHandler.onAction("lookDown", true, 1f);
    assertTrue(rotationHandler.isLookDown());
  }
  
  @Test
  public void testOnActionTiltLeft() {
    assertFalse(rotationHandler.isTiltLeft());
    rotationHandler.onAction("tiltLeft", true, 1f);
    assertTrue(rotationHandler.isTiltLeft());
  }
  
  @Test
  public void testOnActionTiltRight() {
    assertFalse(rotationHandler.isTiltRight());
    rotationHandler.onAction("tiltRight", true, 1f);
    assertTrue(rotationHandler.isTiltRight());
  }

}
