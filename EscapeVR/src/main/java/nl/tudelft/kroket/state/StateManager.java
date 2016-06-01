package nl.tudelft.kroket.state;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.ScreenManager;

public class StateManager {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();
  
  private GameState currentState;

  private AudioManager audioManager;
  private InputHandler inputHandler;
  private SceneManager sceneManager;
  private ScreenManager screenManager;

  public StateManager(AudioManager audioManager, InputHandler inputHandler,
      SceneManager sceneManager, ScreenManager screenManager, GameState initialState) {
    this.audioManager = audioManager;
    this.inputHandler = inputHandler;
    this.sceneManager = sceneManager;
    this.screenManager = screenManager;

    this.currentState = initialState;

    currentState.begin(audioManager, sceneManager, screenManager);
  }

  public void update(float tpf) {
    
    screenManager.update(tpf);

    currentState.update(audioManager, inputHandler, screenManager, tpf);
  }

  

  /**
   * Set the current game state (not thread-safe).
   * 
   * @param state
   *          the new state
   */
  public void setGameState(GameState state) {

    // do not switch state if already in given state
    if (currentState == state) {
      System.err.println("currentState == state");
      return;
    }

    switchState(currentState, state);

  }

  private void stopState(GameState state) {
    
    log.debug(className, String.format("Stopping state %s",
        state.getClass().getSimpleName()));
    
    state.stop(audioManager, sceneManager, screenManager);
  }

  private void startState(GameState state) {
    
    log.debug(className, String.format("Starting state %s",
        state.getClass().getSimpleName()));
    
    state.begin(audioManager, sceneManager, screenManager);
  }

  /**
   * Switch game states.
   * 
   * @param oldState
   *          the old state
   * @param newState
   *          the new state
   */
  private void switchState(GameState oldState, GameState newState) {

    // do not switch state if already in given state
    if (oldState == newState) {
      return;
    }

    log.debug(className, String.format("Switching state from %s to %s",
        oldState.getClass().getSimpleName(), newState.getClass().getSimpleName()));

    stopState(oldState);
    startState(newState);

    currentState = newState;
  }
  
  public GameState getCurrentState() { 
    return currentState;
  }

}
