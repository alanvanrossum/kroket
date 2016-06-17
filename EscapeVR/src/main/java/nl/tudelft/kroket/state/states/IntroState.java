package nl.tudelft.kroket.state.states;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.Screen;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.screen.screens.IntroScreen;
import nl.tudelft.kroket.state.GameState;

/**
 * State that indicates that the game is showing the introduction.
 * 
 * @author Team Kroket
 *
 */
public class IntroState extends GameState {

  /** The unique singleton instance of this class. */
  private static IntroState instance = new IntroState();

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /**
   * Private constructor for the IntroState
   */
  private IntroState() {}

  /**
   * Begin this state.
   */
  @Override
  public void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager) {

    log.debug(className, "Setting up " + className);

    if (screenManager == null) {
      log.error(className, "screenManager == null!");
    } else {
      audioManager.stopAudio();
      audioManager.play("intro");
      screenManager.showScreen("intro");
    }
  }

  /**
   * Stop this state.
   */
  @Override
  public void stop(AudioManager audioManager, SceneManager sceneManager, ScreenManager screenManager) {}

  /**
   * Get the instance of this state.
   * 
   * @return the instance
   */
  public static GameState getInstance() {
    return instance;
  }

  /**
   * Updates the intro state.
   */
  @Override
  public void update(AudioManager audioManager, InputHandler inputHandler,
                     ScreenManager screenManager, HeadUpDisplay hud, EventManager em, float tpf) {

    float time = audioManager.getPlaybackTime("intro");
    Screen currentScreen = screenManager.getCurrent();

    if (currentScreen instanceof IntroScreen) {
      IntroScreen intro = (IntroScreen) currentScreen;
        if(time!=0.0) {
            int current = convertTimeToSlide((int) (time));
            intro.setCurrent(current);
        }
    }
  }
  
  /**
   * This method takes the current time of the audio playing and decides which introOverlay to show.
   * It takes into account the amount of text for each slide, giving slides with more text more time on screen.
   * 
   * @param time current time of the playing audio
   * @return the slide number
   */
   private int convertTimeToSlide(int time) {
     int[] timePerSlide = new int[]{4, 6, 5, 6, 7, 1, 2, 4};
     int sum = 0;
     for (int i = 0; i < timePerSlide.length; i++) {
       sum += timePerSlide[i];
       if (time < sum){
         return i;
       }
     }
     return timePerSlide.length - 1;
   }

}
