package nl.tudelft.kroket.state.states;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.state.GameState;

public class LobbyState implements GameState {

  /** The unique singleton instance of this class. */
  private static LobbyState instance = new LobbyState();

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private LobbyState() {

  }

  @Override
  public void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager) {
    
    log.debug(className, "Setting up LobbyState");
    
    audioManager.stopAudio();
    screenManager.getScreen("lobby").show();
    audioManager.play("lobby");
  }

  @Override
  public void stop(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager) {
    audioManager.stop("lobby");
    screenManager.getScreen("lobby").hide();
  }

  public static GameState getInstance() {
    return instance;
  }

  @Override
  public void update(AudioManager audioManager, InputHandler inputHandler,
      ScreenManager screenManager, float tpf) {
    inputHandler.handleInput(tpf);
  }

}
