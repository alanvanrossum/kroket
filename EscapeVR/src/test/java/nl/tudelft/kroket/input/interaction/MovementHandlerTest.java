package nl.tudelft.kroket.input.interaction;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

public class MovementHandlerTest {

  private Spatial observer;
  private Node rootNode;
  
  private MovementHandler movementHandler;
  private MovementHandler handlerSpy;
  
  
  /**
   * Sets up the mocks.
   * @throws Exception exception
   */
  @Before
  public void setUp() throws Exception {
    observer = Mockito.mock(Spatial.class);
    rootNode = Mockito.mock(Node.class);
    movementHandler = new MovementHandler(observer, rootNode);
    handlerSpy = Mockito.spy(movementHandler);
  }


  @Test
  public void testUpdateMoveForwardFlying() {
    handlerSpy.setForceFlying(true);
    handlerSpy.setMoveForward(true);
    Mockito.doNothing().when(handlerSpy).move(Mockito.anyFloat(), Mockito.anyInt());
    handlerSpy.update(1f);
    Mockito.verify(handlerSpy).move(1f, 2);
  }
  
  @Test
  public void testUpdateMovebackwardNotFlying() {
    handlerSpy.setForceFlying(false);
    handlerSpy.setMoveBackwards(true);
    Mockito.doNothing().when(handlerSpy).move(Mockito.anyFloat(), Mockito.anyInt());
    handlerSpy.update(1f);
    Mockito.verify(handlerSpy).move(-1f, 2);
  }
  
  @Test
  public void testUpdateMoveLeft() {
    handlerSpy.setMoveLeft(true);
    Mockito.doNothing().when(handlerSpy).move(Mockito.anyFloat(), Mockito.anyInt());
    handlerSpy.update(1f);
    Mockito.verify(handlerSpy).move(1f, 0);
  }
  
  @Test
  public void testUpdateMoveRight() {
    handlerSpy.setMoveRight(true);
    Mockito.doNothing().when(handlerSpy).move(Mockito.anyFloat(), Mockito.anyInt());
    handlerSpy.update(1f);
    Mockito.verify(handlerSpy).move(-1f, 0);
  }

  @Test
  public void testIsForceFlying() {
    assertFalse(movementHandler.isForceFlying());
    movementHandler.setForceFlying(true);
    assertTrue(movementHandler.isForceFlying());
  }

  @Test
  public void testSetForceFlying() {
    assertFalse(movementHandler.isForceFlying());
    movementHandler.setForceFlying(true);
    assertTrue(movementHandler.isForceFlying());
  }

  @Test
  public void testMovementHandler() {
    assertNotNull(movementHandler);
  }

  @Test
  public void testSetGetMovementSpeed() {
    assertTrue(movementHandler.getMovementSpeed() == 8f);
    movementHandler.setMovementSpeed(1f);
    assertTrue(movementHandler.getMovementSpeed() == 1f);
  }


  @Test
  public void testIsSetLockHorizontal() {
    assertTrue(movementHandler.isLockHorizontal());
    movementHandler.setLockHorizontal(false);
    assertFalse(movementHandler.isLockHorizontal());
  }

  @Test
  public void testOnActionForward() {
    assertFalse(movementHandler.isMoveForward());
    movementHandler.onAction("forward", true, 1f);
    assertTrue(movementHandler.isMoveForward());
  }
  
  @Test
  public void testOnActionBack() {
    assertFalse(movementHandler.isMoveBackwards());
    movementHandler.onAction("back", true, 1f);
    assertTrue(movementHandler.isMoveBackwards());
  }
  
  @Test
  public void testOnActionLeft() {
    assertFalse(movementHandler.isMoveLeft());
    movementHandler.onAction("left", true, 1f);
    assertTrue(movementHandler.isMoveLeft());
  }
  
  @Test
  public void testOnActionRight() {
    assertFalse(movementHandler.isMoveRight());
    movementHandler.onAction("right", true, 1f);
    assertTrue(movementHandler.isMoveRight());
  }

  @Test
  public void testAddObject() {
    assertFalse(movementHandler.getObjectList().contains("test"));
    movementHandler.addObject("test");
    assertTrue(movementHandler.getObjectList().contains("test"));
  }

  @Test
  public void testRemoveObject() {
    movementHandler.addObject("test");
    assertTrue(movementHandler.getObjectList().contains("test"));
    movementHandler.removeObject("test");
    assertFalse(movementHandler.getObjectList().contains("test"));
  }

}
