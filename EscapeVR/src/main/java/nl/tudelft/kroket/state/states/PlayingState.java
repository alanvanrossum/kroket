package nl.tudelft.kroket.state.states;

import java.util.Random;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.state.GameState;

public class PlayingState implements GameState {
  
  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** The unique singleton instance of this class. */
  private static PlayingState instance = new PlayingState();

  /** The singleton reference to the Logger instance. */
  private static Logger log = Logger.getInstance();

  private long spookyTime;

  private static Random rand = new Random();

  private final int INTERVAL_SPOOKYTIME_LOWER = 20;
  private final int INTERVAL_SPOOKYTIME_UPPER = 120;

  public static GameState getInstance() {
    // TODO Auto-generated method stub
    return instance;
  }

  @Override
  public void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager) {
    sceneManager.getScene("escape").createScene();
    audioManager.play("letthegamebegin");
    audioManager.play("ambient");

    setSpookyTime(INTERVAL_SPOOKYTIME_LOWER, INTERVAL_SPOOKYTIME_UPPER);
  }

  @Override
  public void stop(AudioManager audioManager, SceneManager sceneManager, ScreenManager screenManager) {
    sceneManager.getScene("escape").destroyScene();
    audioManager.stopAudio();
  }

  @Override
  public void update(AudioManager audioManager, ScreenManager screenManager, float tpf) {
    // inputHandler.handleInput(tpf);

    if (spookyTime <= System.currentTimeMillis()) {
      screenManager.showScreen("spooky");
      setSpookyTime(INTERVAL_SPOOKYTIME_LOWER, INTERVAL_SPOOKYTIME_UPPER);
    }
  }

  private void setSpookyTime(int lowerInterval, int upperInterval) {

    int seconds = randInt(lowerInterval, upperInterval);

    log.info(className, "Spooky overlay will be shown in " + seconds + " seconds");

    spookyTime = System.currentTimeMillis() + seconds * 1000;

  }

  public static int randInt(int min, int max) {

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int randomNum = rand.nextInt((max - min) + 1) + min;

    return randomNum;
  }

}
