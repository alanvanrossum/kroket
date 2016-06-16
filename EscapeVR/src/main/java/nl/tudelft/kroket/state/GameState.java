package nl.tudelft.kroket.state;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

/**
 * Abstract class for the state the game is in.
 * 
 * @author Team Kroket
 *
 */
public abstract class GameState {

  /**
   * Begin the state.
   * @param audioManager the audio manager instance
   * @param sceneManager the scene manager instance
   * @param screenManager the screen manager instance
   */
  public abstract void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager);

  /**
   * Stop the state.
   * @param audioManager the audio manager instance
   * @param sceneManager the scene manager instance
   * @param screenManager the screen manager instance
   */
  public abstract void stop(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager);

  /**
   * Update the state.
   * 
   * @param audioManager the audio manager instance
   * @param inputHandler the input handler instance
   * @param screenManager the screen manager instance
   * @param hud the head up display
   * @param em the event manager instance
   * @param tpf the time per frame
   */
  public abstract void update(AudioManager audioManager, InputHandler inputHandler,
      ScreenManager screenManager, HeadUpDisplay hud, EventManager em, float tpf);

}