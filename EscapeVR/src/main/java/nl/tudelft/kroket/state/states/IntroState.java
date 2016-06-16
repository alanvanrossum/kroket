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

public class IntroState extends GameState {

  /** The unique singleton instance of this class. */
  private static IntroState instance = new IntroState();

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  private IntroState() {

  }

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

  @Override
  public void stop(AudioManager audioManager, SceneManager sceneManager, ScreenManager screenManager) {
  }

  public static GameState getInstance() {
    return instance;
  }

    @Override
    public void update(AudioManager audioManager, InputHandler inputHandler,
                       ScreenManager screenManager, HeadUpDisplay hud, EventManager em, float tpf) {

        float time = audioManager.getPlaybackTime("intro");
        Screen currentScreen = screenManager.getCurrent();

        if (currentScreen instanceof IntroScreen) {
            IntroScreen intro = (IntroScreen) currentScreen;
            //index 0 = KroketLogo
            //index 1-6 = StoryLine
            //index 7 = controls

            int current = convertTimeToSlide((int) (time));
            intro.setCurrent(current);
        }
    }
    /*
    This method takes the current time of the audio playing and decides which introOverlay to show.
    It takes into account the amount of text for each slide, giving slides with more text more time on screen.
     */
    private int convertTimeToSlide(int time) {
        int[] timePerSlide = new int[]{4,6,5,6,7,1,2,4};
        int sum=0;
        for (int i = 0; i < timePerSlide.length; i++) {
            sum+=timePerSlide[i];
            if(time<sum){
                return i;
            }
        }
        return 7;
        //The code above does exactly the same as the commented code below.
        //Below is a more readable version.
        //Above is a smarter shorter version.
//        if(time<4){
//            return 0;
//        } else if(time<10) {
//            return 1;
//        } else if(time<15){
//            return 2;
//        } else if(time<21){
//            return 3;
//        } else if(time<28){
//            return 4;
//        } else if(time<29){
//            return 5;
//        } else if(time<31){
//            return 6;
//        } else {
//            return 7;
//        }
    }

}
