package nl.tudelft.kroket.minigame;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import nl.tudelft.kroket.minigame.minigames.TapMinigame;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

/**
 * Class for testing Minigame class non abstract methods.
 * @author Team kroket
 *
 */
public class MinigameTest {

  private Minigame miniGame;
  
  /**
   * Sets up objects for test method.
   * @throws Exception exception.
   */
  @Before
  public void setUp() throws Exception {
    miniGame = TapMinigame.getInstance();
  }

  /**
   * Test for getter and setter hud.
   */
  @Test
  public void testGetSetHud() {
    HeadUpDisplay hud = Mockito.mock(HeadUpDisplay.class);
    miniGame.setHud(hud);
    assertEquals(hud, miniGame.getHud());
  }

  /**
   * Test for getter and setter clientThread.
   */
  @Test
  public void testSetClientThread() {
    ClientThread ct = Mockito.mock(ClientThread.class);
    miniGame.setClientThread(ct);
    assertEquals(miniGame.getClientThread(), ct);
  }

  /**
   * Test getter and setter screenmanager.
   */
  @Test
  public void testSetScreenManager() {
    ScreenManager manager = Mockito.mock(ScreenManager.class);
    miniGame.setScreenManager(manager);
    assertEquals(miniGame.getScreenManager(), manager);
  }

  /**
   * Test getter and setter scenemanager.
   */
  @Test
  public void testSetSceneManager() {
    SceneManager manager = Mockito.mock(SceneManager.class);
    miniGame.setSceneManager(manager);
    assertEquals(miniGame.getSceneManager(), manager);
  }

  /**
   * Test getter and setter minigamemanager.
   */
  @Test
  public void testSetMinigameManager() {
    MinigameManager manager = Mockito.mock(MinigameManager.class);
    miniGame.setMinigameManager(manager);
    assertEquals(miniGame.getMinigameManager(), manager);
  }

}
