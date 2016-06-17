package nl.tudelft.kroket.input.interaction;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import com.jme3.math.Vector3f;
import com.jme3.scene.Spatial;

public class CollisionHandlerTest {

  private Spatial observer;
  private Vector3f boundaries;
  
  private CollisionHandler collisionHandler;
  
  
  /**
   * Sets up the mocks.
   * @throws Exception exception
   */
  @Before
  public void setUp() throws Exception {
    observer = Mockito.mock(Spatial.class);
    boundaries = new Vector3f(0, 0, 0);
    collisionHandler = new CollisionHandler(observer, boundaries);
  }

  @Test
  public void testCollisionHandler() {
    assertNotNull(collisionHandler);
  }

  @Test
  public void testEnableRestriction() {
    collisionHandler.enableRestriction();
    assertTrue(collisionHandler.isRestrictObserver());
  }

  @Test
  public void testDisableRestriction() {
    collisionHandler.enableRestriction();
    assertTrue(collisionHandler.isRestrictObserver());
    collisionHandler.disableRestriction();
    assertFalse(collisionHandler.isRestrictObserver());
  }

  @Test
  public void testSetRestriction() {
    collisionHandler.enableRestriction();
    assertTrue(collisionHandler.isRestrictObserver());
    collisionHandler.setRestriction(false);
    assertFalse(collisionHandler.isRestrictObserver());
  }

}
