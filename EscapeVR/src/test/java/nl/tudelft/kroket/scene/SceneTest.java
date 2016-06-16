package nl.tudelft.kroket.scene;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

import nl.tudelft.kroket.scene.scenes.EscapeScene;

public class SceneTest {


  private AssetManager assetManager;

  private Node rootNode;

  private ViewPort viewPort;
  
  /**
   * Sets up mocks.
   * @throws Exception exception
   */
  @Before
  public void setUp() throws Exception {
    assetManager = Mockito.mock(AssetManager.class);
    rootNode = Mockito.mock(Node.class);
    viewPort = Mockito.mock(ViewPort.class);
  }

  @Test
  public void testScene() {
    Scene sc = new EscapeScene("test", assetManager, rootNode, viewPort);
    assertNotNull(sc);
  }

  @Test
  public void testGetAssetManager() {
    Scene sc = new EscapeScene("test", assetManager, rootNode, viewPort);
    assertEquals(assetManager, sc.getAssetManager());
  }

  @Test
  public void testGetRootNode() {
    Scene sc = new EscapeScene("test", assetManager, rootNode, viewPort);
    assertEquals(rootNode, sc.getRootNode());
  }

  @Test
  public void testGetObject() {
    Scene sc = new EscapeScene("test", assetManager, rootNode, viewPort);
    Mockito.when(rootNode.attachChild(Mockito.any())).thenReturn(1);
    Spatial object = Mockito.mock(Spatial.class);
    sc.addObject("testObject", object);
    assertEquals(object, sc.getObject("testObject"));
  }

  @Test
  public void testAddObject() {
    Scene sc = new EscapeScene("test", assetManager, rootNode, viewPort);
    Mockito.when(rootNode.attachChild(Mockito.any())).thenReturn(1);
    Spatial object = Mockito.mock(Spatial.class);
    sc.addObject("testObject", object);
    assertTrue(sc.objects.containsKey("testObject"));
  }

  @Test
  public void testDestroyScene() {
    Scene sc = new EscapeScene("test", assetManager, rootNode, viewPort);
    Mockito.when(rootNode.detachChild(Mockito.any())).thenReturn(1);
    Spatial object = Mockito.mock(Spatial.class);
    sc.addObject("testObject", object);
    sc.destroyScene();
    Mockito.verify(rootNode).detachChild(object);
  }

}
