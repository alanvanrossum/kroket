package nl.tudelft.kroket.state;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.ScreenManager;

public class StateManager {

  private AudioManager audioManager;
  private InputHandler inputHandler;
  private SceneManager sceneManager;
  private ScreenManager screenManager;

  // public StateManager(AudioManager audioManager, InputHandler inputHandler, SceneManager
  // sceneManager, ScreenManager screenManager) {
  // this.audioManager = audioManager;
  // this.inputHandler = inputHandler;
  // this.sceneManager = sceneManager;
  // this.screenManager = screenManager;
  // }

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

    currentState.update(inputHandler, tpf);
  }

  private GameState currentState;

  /**
   * Set the current game state (not thread-safe).
   * 
   * @param state
   *          the new state
   */
  public void setGameState(GameState state) {

    // do not switch state if already in given state
    if (currentState == state) {
      return;
    }

    switchState(currentState, state);

  }

  private void stopState(GameState state) {
    state.stop(audioManager, sceneManager, screenManager);
  }

  private void startState(GameState state) {
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

    stopState(oldState);
    startState(newState);
  }

}
