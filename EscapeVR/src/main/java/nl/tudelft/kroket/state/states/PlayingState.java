package nl.tudelft.kroket.state.states;

import java.util.Random;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.escape.Settings;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.state.GameState;

/**
 * State that indicates that the actual game is being played.
 * 
 * @author Team Kroket
 *
 */
public class PlayingState extends GameState {

  /** The unique singleton instance of this class. */
  private static PlayingState instance = new PlayingState();

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private long spookyTime;

  private static Random rand = new Random();

  /**
   * Get the instance of this state.
   * 
   * @return the instance
   */
  public static GameState getInstance() {
    return instance;
  }

  /**
   * Begin this state.
   */
  @Override
  public void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager) {

    log.debug(className, "Setting up " + className);

    sceneManager.getScene("escape").createScene();
    audioManager.play("letthegamebegin");
    audioManager.play("ambient");

    setSpookyTime(Settings.INTERVAL_SPOOKYTIME_LOWER, Settings.INTERVAL_SPOOKYTIME_UPPER);
  }

  /**
   * Stop this state.
   */
  @Override
  public void stop(AudioManager audioManager, SceneManager sceneManager, ScreenManager screenManager) {
    //sceneManager.getScene("escape").destroyScene();
    audioManager.stopAudio();
  }

  /**
   * Set the spooky time overlay to display between two intervals. The actual time will be randomly
   * selected.
   * 
   * @param lowerInterval
   *          the lower amount of seconds
   * @param upperInterval
   *          the upper amount of seconds
   */
  private void setSpookyTime(int lowerInterval, int upperInterval) {

    int seconds = randInt(lowerInterval, upperInterval);

    log.info(className,
        String.format("Setting spookytime overlay to display in %d seconds...", seconds));

    spookyTime = System.currentTimeMillis() + seconds * 1000;
  }

  /**
   * Get a random integer value between two values.
   * 
   * @param min
   *          the minimum value
   * @param max
   *          the maximum value
   * @return int the random integer
   */
  public static int randInt(int min, int max) {

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
  }

  /**
   * Updates this state. 
   * Checks if the spooky screen should be showed.
   */
  @Override
  public void update(AudioManager audioManager, InputHandler inputHandler,
      ScreenManager screenManager, HeadUpDisplay hud, EventManager em, float tpf) {
    inputHandler.handleInput(tpf);

    if (Settings.SPOOKY_ENABLED) {

      if (spookyTime <= System.currentTimeMillis()) {
        screenManager.showScreen("spooky");
        setSpookyTime(Settings.INTERVAL_SPOOKYTIME_LOWER, Settings.INTERVAL_SPOOKYTIME_UPPER);
      }
    }
  }

}
