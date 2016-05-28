package nl.tudelft.kroket.state.states;

import java.util.Random;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.ScreenManager;
import nl.tudelft.kroket.state.GameState;

public class ModelTestState implements GameState {

  /** The unique singleton instance of this class. */
  private static ModelTestState instance = new ModelTestState();

  /** The singleton reference to the Logger instance. */
  private static Logger logger = Logger.getInstance();

  
  public static GameState getInstance() {
    // TODO Auto-generated method stub
    return instance;
  }

  @Override
  public void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager) {
    sceneManager.getScene("modeltest").createScene();
    audioManager.play("testing");
  }

  @Override
  public void stop(AudioManager audioManager, SceneManager sceneManager, ScreenManager screenManager) {
    sceneManager.getScene("modeltest").destroyScene();
    audioManager.stopAudio();
  }

  @Override
  public void update(InputHandler inputHandler, ScreenManager screenManager, float tpf) {
    inputHandler.handleInput(tpf);
 
  }
}
