package nl.tudelft.kroket.scene;


import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;

import com.jme3.asset.AssetManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Node;

import nl.tudelft.kroket.scene.scenes.EscapeScene;

/**
 * Class for testing the EscapeScene class.
 * @author Kroket
 *
 */
public class EscapeSceneTest {

  private AssetManager am;

  private Node rn;

  private ViewPort vp;

  /**
   * Sets up for the tests.
   * 
   * @throws Exception
   *           the exception
   */
  @Before
  public void setUp() throws Exception {
    am = mock(AssetManager.class);
    rn = mock(Node.class);
    vp = mock(ViewPort.class);
  }
  
  /**
   * Test for the escapescene constructor.
   */
  @Test
  public void escapeSceneTest() {
    EscapeScene es = new EscapeScene("test", am, rn, vp);
    assertNotNull(es);
  }
  
//  /**
//   * Test for the createscene method.
//   */
//  @Test
//  public void createSceneTest() {
//    EscapeScene es = new EscapeScene("test", am, rn, vp);
//    es.createScene();
//    verify(es).createScene();  
//  }

}
