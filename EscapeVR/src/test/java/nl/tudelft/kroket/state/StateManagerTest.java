package nl.tudelft.kroket.state;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.state.states.IntroState;
import nl.tudelft.kroket.state.states.LobbyState;

public class StateManagerTest {
  
  private AudioManager audioManager;
  private InputHandler inputHandler;
  private SceneManager sceneManager;
  private ScreenManager screenManager;
  private HeadUpDisplay headupDisplay;
  private EventManager eventManager;
  private GameState initialState;
 
  private StateManager stateManager;

  /**
   * Sets up the mocks.
   * @throws Exception exception
   */
  @Before
  public void setUp() throws Exception {
    audioManager = Mockito.mock(AudioManager.class);
    inputHandler = Mockito.mock(InputHandler.class);
    sceneManager = Mockito.mock(SceneManager.class);
    screenManager = Mockito.mock(ScreenManager.class);
    headupDisplay = Mockito.mock(HeadUpDisplay.class);
    eventManager = Mockito.mock(EventManager.class);
    initialState = Mockito.mock(LobbyState.class);
    
    stateManager = new StateManager(audioManager, inputHandler, sceneManager, 
        screenManager, headupDisplay, eventManager, initialState);
  }

  @Test
  public void testStateManager() {
    assertNotNull(stateManager);
  }

  @Test
  public void testUpdate() {
    Mockito.doNothing().when(screenManager).update(1f);
    Mockito.doNothing().when(initialState).update(audioManager, inputHandler, screenManager, 
        headupDisplay, eventManager, 1f);
    
    stateManager.update(1f);
    Mockito.verify(screenManager).update(1f);
    Mockito.verify(initialState).update(audioManager, inputHandler, screenManager, 
        headupDisplay, eventManager, 1f);
  }

  @Test
  public void testSetGameState() {
    GameState test = Mockito.mock(IntroState.class);
    stateManager.setGameState(test);
    assertTrue(stateManager.getCurrentState() instanceof IntroState);
  }

  @Test
  public void testGetCurrentState() {
    assertTrue(stateManager.getCurrentState() instanceof LobbyState);
  }

}
