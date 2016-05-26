package nl.tudelft.kroket.state;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.ScreenManager;

public interface GameState {

  public void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager);

  public void stop(AudioManager audioManager, SceneManager sceneManager, ScreenManager screenManager);

  public void update(InputHandler inputHandler, ScreenManager screenManager, float tpf);

}