package nl.tudelft.kroket.minigame.minigames;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import nl.tudelft.kroket.event.events.ButtonPressEvent;
import nl.tudelft.kroket.minigame.Minigame;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.Screen;
import nl.tudelft.kroket.screen.ScreenManager;


/**
 * Test class for ColorSequenceMinigame class.
 * @author Team Kroket
 *
 */
public class ColorSequenceMinigameTest {
  

  private ColorSequenceMinigame miniGame;
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
    miniGame = (ColorSequenceMinigame) ColorSequenceMinigame.getInstance();
    hud = Mockito.mock(HeadUpDisplay.class);
    clientThread = Mockito.mock(ClientThread.class);
    screenManager = Mockito.mock(ScreenManager.class);
    sceneManager = Mockito.mock(SceneManager.class);
    miniGame.setClientThread(clientThread);
    miniGame.setHud(hud);
    miniGame.setSceneManager(sceneManager);
    miniGame.setScreenManager(screenManager);
  }
  
  /**
   * Clears the sequencelist after each test.
   */
  @After
  public void tearDown() {
    List<String> list = new ArrayList<>();
    miniGame.setSequence(list);
    
  }

  /**
   * Test for start method.
   */
  @Test
  public void testStart() {
    Screen screen = Mockito.mock(Screen.class);
    Mockito.when(screenManager.getScreen(Mockito.anyString())).thenReturn(screen);
    Mockito.doNothing().when(screen).show();
    Mockito.doNothing().when(hud).setCenterText(Mockito.anyString(), Mockito.anyInt());
    assertFalse(ColorSequenceMinigame.isActive());
    miniGame.start();
    assertTrue(ColorSequenceMinigame.isActive());
  }

  /**
   * Test for stop method.
   */
  @Test
  public void testStop() {
    Screen screen = Mockito.mock(Screen.class);
    Mockito.when(screenManager.getScreen(Mockito.anyString())).thenReturn(screen);
    Mockito.doNothing().when(screen).hide();
    Mockito.doNothing().when(hud).setCenterText(
        Mockito.anyString(), Mockito.anyInt());
    miniGame.stop();
    assertFalse(ColorSequenceMinigame.isActive());
  }

  /**
   * Test for update method.
   */
  @Test
  public void testUpdate() {
    Minigame gameSpy = Mockito.spy(miniGame);
    Mockito.when(((ColorSequenceMinigame) gameSpy).checkSequence()).thenReturn(true);
    gameSpy.update(1f); 
    ((ColorSequenceMinigame) Mockito.verify(gameSpy)).finish();
  }
  
  /**
   * Test for update when the sequence is wrong.
   */
  @Test
  public void testUpdateFalse() {
    Minigame gameSpy = Mockito.spy(miniGame);
    Mockito.when(((ColorSequenceMinigame) gameSpy).checkSequence()).thenReturn(false);
    gameSpy.update(1f);
    ((ColorSequenceMinigame) Mockito.verify(gameSpy, Mockito.never())).finish();
  }
  
  @Test
  public void testHandleEvent() {
    ButtonPressEvent bpe = new ButtonPressEvent(Mockito.mock(Object.class), "test");
    List<String> list = new ArrayList<>();
    list.add("test");
    miniGame.setSequence(list);
    miniGame.handleEvent(bpe);
    assertTrue(ColorSequenceMinigame.getButtonList().contains("test"));
  }


  /**
   * Test getName method.
   */
  @Test
  public void testGetName() {
    assertEquals(miniGame.getName(), "C");
  }

  /**
   * Test getinstance method.
   */
  @Test
  public void testGetInstance() {
    assertNotNull(ColorSequenceMinigame.getInstance());
    assertTrue(ColorSequenceMinigame.getInstance() instanceof ColorSequenceMinigame);
  }

  /**
   * Test for isactive method.
   */
  @Test
  public void testIsActive() {
    assertFalse(ColorSequenceMinigame.isActive());
  }

  /**
   * Test finish method.
   */
  @Test
  public void testFinish() {
    miniGame.finish();
    Mockito.doNothing().when(clientThread).sendMessage(Mockito.anyString());
    Mockito.verify(clientThread).sendMessage(Mockito.anyString());
  }

  /**
   * Test setsequence method.
   */
  @Test
  public void testSetSequence() {
    assertTrue(ColorSequenceMinigame.getSequenceList().isEmpty());
    List<String> list = new ArrayList<>();
    list.add("test");
    miniGame.setSequence(list);
    assertEquals(ColorSequenceMinigame.getSequenceList(), list);
  }

  /**
   * Test parsecolors method.
   */
  @Test
  public void testParseColors() {
    miniGame.setSequence(new ArrayList<>());
    List<String> list = new ArrayList<>();
    list.add("Button B");
    list.add("Button A");
    list.add("Button X");
    list.add("Button Y");
    List<String> colors = new ArrayList<>();
    colors.add("RED");
    colors.add("GREEN");
    colors.add("BLUE");
    colors.add("YELLOW");
    miniGame.parseColors(colors);
    assertEquals(ColorSequenceMinigame.getSequenceList(), list);
  }

}
