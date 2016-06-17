package nl.tudelft.kroket.state;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

/**
 * StateManager object used to manage gamestates.
 * 
 * @author Team Kroket
 *
 */
public class StateManager {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** Current gamestate. */
  private GameState currentState;

  private AudioManager audioManager;
  private InputHandler inputHandler;
  private SceneManager sceneManager;
  private ScreenManager screenManager;

  private HeadUpDisplay headupDisplay;

  private EventManager eventManager;

  /**
   * StateManager constructor.
   * 
   * @param audioManager
   *          handle
   * @param inputHandler
   *          handle
   * @param sceneManager
   *          handle
   * @param screenManager
   *          handle
   * @param initialState
   *          the inital state
   */
  public StateManager(AudioManager audioManager, InputHandler inputHandler,
      SceneManager sceneManager, ScreenManager screenManager, HeadUpDisplay hud,
      EventManager eventManager, GameState initialState) {
    this.audioManager = audioManager;
    this.inputHandler = inputHandler;
    this.sceneManager = sceneManager;
    this.screenManager = screenManager;
    this.headupDisplay = hud;
    this.eventManager = eventManager;

    this.currentState = initialState;

    currentState.begin(audioManager, sceneManager, screenManager);
  }

  /**
   * General update method.
   * 
   * @param tpf
   *          time per frame
   */
  public void update(float tpf) {

    screenManager.update(tpf);

    currentState.update(audioManager, inputHandler, screenManager, headupDisplay, eventManager, tpf);
  }

  /**
   * Set the current game state (not thread-safe).
   * 
   * @param state
   *          the new state
   */
  public void setGameState(GameState state) {

    // Do not switch state if already in given state
    if (currentState == state) {
      log.error(className, "currentState == state");
      return;
    }

    // Switch the state
    switchState(currentState, state);
  }

  /**
   * Stop a GameState.
   * 
   * @param state
   *          the state to be stopped
   */
  private void stopState(GameState state) {
    log.debug(className, String.format("Stopping state %s", state.getClass().getSimpleName()));

    state.stop(audioManager, sceneManager, screenManager);
  }

  /**
   * Start a GameState.
   * 
   * @param state
   *          the state to be started
   */
  private void startState(GameState state) {
    log.debug(className, String.format("Starting state %s", state.getClass().getSimpleName()));

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

    // Do not switch state if already in given state
    if (oldState == newState) {
      return;
    }

    log.debug(className, String.format("Switching state from %s to %s", oldState.getClass()
        .getSimpleName(), newState.getClass().getSimpleName()));

    // First, stop the previous/old state
    stopState(oldState);

    // Start the new state
    startState(newState);

    // Set the current state to the new state
    currentState = newState;
  }

  /**
   * Get the currently active gamestate.
   * 
   * @return the active gamestate
   */
  public GameState getCurrentState() {
    return currentState;
  }
  
  

}
