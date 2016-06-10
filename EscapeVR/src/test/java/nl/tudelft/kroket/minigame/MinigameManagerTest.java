package nl.tudelft.kroket.minigame;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import nl.tudelft.kroket.minigame.minigames.TapMinigame;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

/**
 * Class for testing the MinigameManager class.
 * @author Team kroket
 *
 */
public class MinigameManagerTest {
  
  @Mock private HeadUpDisplay hud;
  @Mock private ClientThread clientThread;
  @Mock private ScreenManager screenManager;
  @Mock private SceneManager sceneManager;
  
  private MinigameManager manager;

  /**
   * Sets up the objects before the test.
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    hud = Mockito.mock(HeadUpDisplay.class);
    clientThread = Mockito.mock(ClientThread.class);
    screenManager = Mockito.mock(ScreenManager.class);
    sceneManager = Mockito.mock(SceneManager.class);
    
    manager = new MinigameManager(hud, clientThread, screenManager, sceneManager);
  }

  /**
   * Test for minigamemanager constructor.
   */
  @Test
  public void testMinigameManager() {
    assertNotNull(manager);
  }

  /**
   * Test for launchgame method.
   */
  @Test
  public void testLaunchGame() {
    Minigame minigame = Mockito.mock(TapMinigame.class);
    manager.launchGame(minigame);
    Mockito.verify(minigame).setHud(Mockito.any());
    Mockito.verify(minigame).start();
  }
  
  /**
   * Test for launchgame method with null minigame.
   */
  @Test
  public void testLaunchGameNull() {
    Minigame minigame = Mockito.mock(TapMinigame.class);
    manager.launchGame(minigame);
    manager.launchGame(null);
    assertEquals(manager.getCurrent(), minigame);
  }

  /**
   * Test for endgame method.
   */
  @Test
  public void testEndGame() {
    Minigame minigame = Mockito.mock(TapMinigame.class);
    manager.launchGame(minigame);
    manager.endGame();
    Mockito.verify(minigame).stop();
    assertEquals(manager.getCurrent(), null);
  }

  /**
   * Test for the update method.
   */
  @Test
  public void testUpdate() {
    Minigame minigame = Mockito.mock(TapMinigame.class);
    manager.launchGame(minigame);
    manager.update(1f);
    Mockito.verify(minigame).update(1f);
  }

  /**
   * Test for the getter of currentgame.
   */
  @Test
  public void testGetCurrent() {
    Minigame minigame = Mockito.mock(TapMinigame.class);
    manager.launchGame(minigame);
    assertEquals(manager.getCurrent(), minigame);
  }

  /**
   * Test for gameactive method.
   */
  @Test
  public void testGameActive() {
    assertNull(manager.getCurrent());
    assertFalse(manager.gameActive());
    Minigame minigame = Mockito.mock(TapMinigame.class);
    manager.launchGame(minigame);
    assertTrue(manager.gameActive());
  }

  /**
   * Test for isactive method.
   */
  @Test
  public void testIsActive() {
    assertFalse(manager.isActive(""));  
    Minigame minigame = TapMinigame.getInstance();
    Minigame gameSpy = Mockito.spy(minigame);
    Mockito.doNothing().when(gameSpy).start();
    manager.launchGame(gameSpy);
    assertTrue(manager.isActive("B"));
  }

}
