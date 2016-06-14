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
      audioManager.play("welcome");
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

        float time = audioManager.getPlaybackTime("welcome");
        Screen currentScreen = screenManager.getCurrent();

        if (currentScreen instanceof IntroScreen) {
            IntroScreen intro = (IntroScreen) currentScreen;

            int current = (int) (time / 5);
//            if (time > 25) {
//                current = 5;
//            }
//            if (time > 27) {
//                current = 6;
//            }
            intro.setCurrent(current);
        }
    }

}
