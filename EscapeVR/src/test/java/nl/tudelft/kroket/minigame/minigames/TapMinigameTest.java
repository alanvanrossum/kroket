package nl.tudelft.kroket.minigame.minigames;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import nl.tudelft.kroket.event.events.ButtonPressEvent;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.Screen;
import nl.tudelft.kroket.screen.ScreenManager;

public class TapMinigameTest {
  
  private TapMinigame miniGame;
  @Mock private HeadUpDisplay hud;
  @Mock private ClientThread clientThread;
  @Mock private ScreenManager screenManager;
  @Mock private SceneManager sceneManager;
  List<String> list = new ArrayList<>();
  List<String> colors = new ArrayList<>();
  
  /**
   * Sets up objects for test method.
   * @throws Exception exception.
   */
  @Before
  public void setUp() throws Exception {
    miniGame = (TapMinigame)TapMinigame.getInstance();
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
    Screen screen = Mockito.mock(Screen.class);
    Mockito.when(screenManager.getScreen(Mockito.anyString())).thenReturn(screen);
    Mockito.doNothing().when(screen).show();
    Mockito.doNothing().when(hud).setCenterText(Mockito.anyString(), Mockito.anyInt());
    miniGame.start();
    Mockito.verify(hud).setCenterText( "Hack the computer by enter the colorsequences\n"
        + "you will receive from your fellow CIA agents.\n"
        + "Use the colored buttons on your controller!", 20);
  }

  @Test
  public void testStop() {
    Screen screen = Mockito.mock(Screen.class);
    Mockito.when(screenManager.getScreen(Mockito.anyString())).thenReturn(screen);
    Mockito.doNothing().when(screen).hide();
    Mockito.doNothing().when(sceneManager).extendEscapeScene(Mockito.anyString());
    Mockito.doNothing().when(hud).setCenterText(
        Mockito.anyString(), Mockito.anyInt());
    miniGame.stop();
    Mockito.verify(hud).setCenterText(
        "Great job!\nWait... I think I saw something appear on that wall!", 10);
  }

  @Test
  public void testHandleEvent() {
    ButtonPressEvent bpe = new ButtonPressEvent(Mockito.mock(Object.class), "test");
    miniGame.handleEvent(bpe);
    assertTrue(TapMinigame.getButtonList().contains("test"));
  }

  @Test
  public void testGetName() {
    assertEquals("B", miniGame.getName());
  }

  @Test
  public void testGetInstance() {
    assertNotNull(TapMinigame.getInstance());
    assertTrue(TapMinigame.getInstance() instanceof TapMinigame);
  }

  @Test
  public void testParseButtons() {
    addButtons();
    addColors();
    addColors();
    addColors();
    addColors();
    miniGame.parseButtons(colors);
    assertEquals(TapMinigame.getFirstSequence(), list);
    assertEquals(TapMinigame.getSecondSequence(), list);
    assertEquals(TapMinigame.getThirdSequence(), list);
    assertEquals(TapMinigame.getFourthSequence(), list);
  }

  /**
   * Add button strings to list.
   */
  public void addButtons() {
    list.add("Button B");
    list.add("Button A");
    list.add("Button X");
    list.add("Button Y");
  }
  
  /**
   * Add color strings to list.
   */
  public void addColors() {
    colors.add("RED");
    colors.add("GREEN");
    colors.add("BLUE");
    colors.add("YELLOW");
  }
  
}
