package nl.tudelft.kroket.screen;

import static org.junit.Assert.*;

import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Node;

import nl.tudelft.kroket.screen.screens.LobbyScreen;

public class ScreenManagerTest {

  private AssetManager assetManager;
  private Node guiNode;
  private ScreenManager screenManager;

  /**
   * Sets mocks up before tests.
   * @throws Exception exception
   */
  @Before
  public void setUp() throws Exception {
    assetManager = Mockito.mock(AssetManager.class);
    guiNode = Mockito.mock(Node.class);
    screenManager = new ScreenManager(assetManager, guiNode, 1f, 1f);
  }
  
  @Test
  public void testScreenManager() {
    assertNotNull(screenManager);
  }

  @Test
  public void testGetScreenNotPresent() {
    assertNull(screenManager.getScreen("test"));
  }

  @Test
  public void testGetScreen() {
    Screen test = Mockito.mock(LobbyScreen.class);
    screenManager.screens.put("test", test);
    assertEquals(screenManager.getScreen("test"), test);
  }

  @Test
  public void testShowScreen() {
    Screen test = Mockito.mock(LobbyScreen.class);
    screenManager.screens.put("test", test);
    Mockito.doNothing().when(test).show();
    screenManager.showScreen("test");
    Mockito.verify(test).show();
  } 

  @Test
  public void testShowScreenNull() {
    Screen test = Mockito.mock(LobbyScreen.class);
    screenManager.screens.put("test", test);    
    Mockito.doNothing().when(test).show();
    screenManager.showScreen("test");
    screenManager.screens.put("test2", null);
    screenManager.showScreen("test2");
    assertNotNull(screenManager.getCurrent());
  }


  @Test
  public void testHideScreen() {
    Screen test = Mockito.mock(LobbyScreen.class);
    screenManager.screens.put("test", test);    
    Mockito.doNothing().when(test).hide();
    Mockito.doNothing().when(test).show();
    screenManager.showScreen("test");
    screenManager.hideScreen("test");
    assertNull(screenManager.getCurrent());
    Mockito.verify(test).hide();
  }

  @Test
  public void testHideAll() {
    Screen test = Mockito.mock(LobbyScreen.class);
    screenManager.screens.put("test", test);    
    Mockito.doNothing().when(test).hide();
    screenManager.screens.put("test2", test);
    screenManager.hideAll();
    Mockito.verify(test, Mockito.times(2)).hide();
  }

  @Test
  public void testGetCurrent() {
    assertNull(screenManager.getCurrent());
  }

}
