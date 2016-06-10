package nl.tudelft.kroket.minigame.minigames;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import nl.tudelft.kroket.minigame.Minigame;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

public class PictureCodeMinigameTest {

  private Minigame miniGame;
  @Mock private HeadUpDisplay hud;
  @Mock private ClientThread clientThread;
  @Mock private ScreenManager screenManager;
  @Mock private SceneManager sceneManager;
  
  /**
   * Sets up objects for test method.
   * @throws Exception exception.
   */
  @Before
  public void setUp() throws Exception {
    miniGame = PictureCodeMinigame.getInstance();
    hud = Mockito.mock(HeadUpDisplay.class);
    clientThread = Mockito.mock(ClientThread.class);
    screenManager = Mockito.mock(ScreenManager.class);
    sceneManager = Mockito.mock(SceneManager.class);
    miniGame.setClientThread(clientThread);
    miniGame.setHud(hud);
    miniGame.setSceneManager(sceneManager);
    miniGame.setScreenManager(screenManager);
  }

  @Test
  public void testStart() {
    Mockito.doNothing().when(hud).setCenterText(
        "Try to decipher the code by studying the painting!", 10);
    miniGame.start();
    Mockito.verify(hud).setCenterText("Try to decipher the code by studying the painting!", 10);
  }

  @Test
  public void testStop() {
    Mockito.doNothing().when(hud).setCenterText(
        "Great! You cracked the code to open the safe!", 10);
    miniGame.stop();
    Mockito.verify(hud).setCenterText("Great! You cracked the code to open the safe!", 10);
  }

  @Test
  public void testGetName() {
    assertEquals("A", miniGame.getName());
  }

  @Test
  public void testGetInstance() {
    assertNotNull(PictureCodeMinigame.getInstance());
    assertTrue(PictureCodeMinigame.getInstance() instanceof PictureCodeMinigame);
  }

}
