package nl.tudelft.kroket.minigame;

import java.util.EventObject;

import nl.tudelft.kroket.event.EventListener;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

public class MinigameManager implements EventListener {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();

  /** The currently active minigame. */
  private Minigame currentGame = null;

  public HeadUpDisplay hud;
  public ClientThread clientThread;
  public ScreenManager screenManager;
  public SceneManager sceneManager;

  public MinigameManager(HeadUpDisplay hud, ClientThread clientThread, ScreenManager screenManager,
      SceneManager sceneManager) {
    log.info(className, "Initializing...");

    this.hud = hud;
    this.clientThread = clientThread;
    this.screenManager = screenManager;
    this.sceneManager = sceneManager;
  }

  /**
   * Handle an incoming event.
   */
  @Override
  public void handleEvent(EventObject event) {

    // if no minigame is active, don't do anything
    if (currentGame == null) {
      return;
    }

    // forward the event to the current minigame
    currentGame.handleEvent(event);
  }

  /**
   * Launch a minigame.
   * 
   * @param minigame
   *          the minigame to be launched
   */
  public void launchGame(Minigame minigame) {

    if (minigame == null) {
      return;
    }

    currentGame = minigame;

    log.info(className, "Launching minigame " + currentGame.getClass().getSimpleName());

    currentGame.setClientThread(clientThread);
    currentGame.setHud(hud);
    currentGame.setScreenManager(screenManager);
    currentGame.setSceneManager(sceneManager);
    currentGame.setMinigameManager(this);

    // start the minigame
    currentGame.start();
  }

  /**
   * End the current minigame.
   */
  public void endGame() {

    if (currentGame == null) {
      return;
    }

    // stop the current minigame
    currentGame.stop();

    currentGame = null;
  }

  /**
   * General update method.
   * 
   * @param tpf
   *          time per frame
   */
  public void update(float tpf) {

    // if currentGame is not set don't do anything
    if (currentGame == null) {
      return;
    }

    // forward update event to current minigame
    currentGame.update(tpf);
  }

  /**
   * Get the currently active minigame.
   * 
   * @return the minigame
   */
  public Minigame getCurrent() {
    return currentGame;
  }

  public boolean gameActive() {
    return getCurrent() != null;
  }

}
