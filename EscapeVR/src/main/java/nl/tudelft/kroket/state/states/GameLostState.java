package nl.tudelft.kroket.state.states;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.state.GameState;

public class GameLostState extends GameState {

  /** The unique singleton instance of this class. */
  private static GameLostState instance = new GameLostState();

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private GameLostState() {

  }

  @Override
  public void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager) {
    
    log.debug(className, "Setting up " + className);
    
    audioManager.stopAudio();
    screenManager.getScreen("gameover").show();
    audioManager.play("gamelost");
  }

  @Override
  public void stop(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager) {
    audioManager.stop("gamelost");
    screenManager.getScreen("gameover").hide();
  }

  public static GameState getInstance() {
    return instance;
  }

  @Override
  public void update(AudioManager audioManager, InputHandler inputHandler,
      ScreenManager screenManager, HeadUpDisplay hud, EventManager em, float tpf) {
   // inputHandler.handleInput(tpf);
  }

}
