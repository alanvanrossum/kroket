package nl.tudelft.kroket.state;

import nl.tudelft.kroket.audio.AudioManager;
import nl.tudelft.kroket.event.EventManager;
import nl.tudelft.kroket.input.InputHandler;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

public abstract class GameState {

  public abstract void begin(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager);

  public abstract void stop(AudioManager audioManager, SceneManager sceneManager,
      ScreenManager screenManager);

  public abstract void update(AudioManager audioManager, InputHandler inputHandler,
      ScreenManager screenManager, HeadUpDisplay hud, EventManager em, float tpf);

}