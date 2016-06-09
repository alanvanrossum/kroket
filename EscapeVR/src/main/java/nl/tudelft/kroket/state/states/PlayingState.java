package nl.tudelft.kroket.state.states;

import java.util.Random;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.escape.Settings;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.event.events.GameLostEvent;
import nl.tudelft.kroket.event.events.TimeoutEvent;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.state.GameState;

public class PlayingState extends GameState {

  /** The unique singleton instance of this class. */
  private static PlayingState instance = new PlayingState();

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private long spookyTime;

  private static Random rand = new Random();


  private long timeLimit;

  private boolean timeoutHit = false;


  public static GameState getInstance() {
    // TODO Auto-generated method stub
    return instance;
  }

  @Override
  public void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager) {

    log.debug(className, "Setting up " + className);

    timeoutHit = false;

    sceneManager.getScene("escape").createScene();
    audioManager.play("letthegamebegin");
    audioManager.play("ambient");

    setSpookyTime(Settings.INTERVAL_SPOOKYTIME_LOWER, Settings.INTERVAL_SPOOKYTIME_UPPER);
  }

  @Override
  public void stop(AudioManager audioManager, SceneManager sceneManager, ScreenManager screenManager) {
    sceneManager.getScene("escape").destroyScene();
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

  public void setTimeLimit(int seconds) {

    // get time NOW (current system time) and add amount of seconds to it
    timeLimit = System.currentTimeMillis() + (seconds * 1000);

    log.info(className, String.format("Game ends in %d seconds...", seconds));
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

    long timeRemaining = Math.max(timeLimit - System.currentTimeMillis(), 0);

    if (timeRemaining == 0 && !timeoutHit) {
      
      hud.setTimerText("");
      System.out.println("Firing timeoutEvent...");
      timeoutHit = true;
      // prevent firing event multiple times
//      em.addEvent(new TimeoutEvent(this));
//      em.addEvent(new GameLostEvent(this));

    }

    // this if statement isn't really necessary, performance-wise
    // only update label once per second
    else if ((timeRemaining % 10) == 0) {

      int secondsRemaining = (int) (timeRemaining / 1000);

      int minutesRemaining = secondsRemaining / 60;
      secondsRemaining -= (minutesRemaining * 60);

      // log.debug(className, minutesRemaining + " minutes remaining");

      hud.setTimerText(String.format("Time remaining: %02d:%02d", minutesRemaining,
          secondsRemaining));
    }

  }

}
