package nl.tudelft.kroket.scene.scenes;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.asset.AssetManager;
import com.jme3.material.MaterialDef;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;

public class EscapeSceneTest {

  private AssetManager assetManager;

  private Node rootNode;

  private ViewPort viewPort;
  
  @Before
  public void setUp() throws Exception {
    assetManager = Mockito.mock(AssetManager.class);
    rootNode = Mockito.mock(Node.class);
    viewPort = Mockito.mock(ViewPort.class);
  }

  @Test
  public void testGetBoundaries() {
    EscapeScene es = new EscapeScene("test", assetManager, rootNode, viewPort);
    Vector3f expected = new Vector3f(9f, 6f, 12f);
    assertEquals(expected, es.getBoundaries());
  }

  @Test
  public void testEscapeScene() {
    EscapeScene es = new EscapeScene("test", assetManager, rootNode, viewPort);
    assertNotNull(es);
  }

  @Test
  public void testAddButtons() {
    EscapeScene es = new EscapeScene("test", assetManager, rootNode, viewPort);
    Spatial sp = new Geometry();
    Mockito.when(assetManager.loadModel(Mockito.anyString())).thenReturn(sp);
    Mockito.when(rootNode.attachChild(Mockito.any())).thenReturn(0);
    es.addButtons();
    Mockito.verify(assetManager).loadModel("Models/buttons/fourbuttons2.j3o");
    Mockito.verify(rootNode).attachChild(Mockito.any());
  }

  @Test
  public void testAddOpenSafe() {
    Spatial sp = new Geometry();
    Mockito.when(assetManager.loadModel(Mockito.anyString())).thenReturn(sp);
    Mockito.when(rootNode.attachChild(Mockito.any())).thenReturn(0);
    Mockito.when(rootNode.getChild(Mockito.any())).thenReturn(sp);
    EscapeScene es = new EscapeScene("test", assetManager, rootNode, viewPort);
    es.addOpenSafe();
    Mockito.verify(assetManager).loadModel("Models/safeopen/safeopen.j3o");
    Mockito.verify(rootNode).getChild(Mockito.any());
    Mockito.verify(rootNode).attachChild(Mockito.any());
  }

  @Test
  public void testRemove() {
    EscapeScene es = new EscapeScene("test", assetManager, rootNode, viewPort);
    Spatial sp = new Geometry();
    Mockito.when(rootNode.getChild(Mockito.any())).thenReturn(sp);
    es.remove("test");
    Mockito.verify(rootNode).getChild("test");
  }

}
