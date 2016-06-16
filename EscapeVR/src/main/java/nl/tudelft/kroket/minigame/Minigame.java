package nl.tudelft.kroket.minigame;

import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

import java.util.EventObject;

/**
 * Abstract class for a minigame.
 * 
 * @author Team Kroket
 *
 */
public abstract class Minigame {

  protected HeadUpDisplay hud;
  protected ClientThread clientThread;
  protected ScreenManager screenManager;
  protected SceneManager sceneManager;
  protected MinigameManager minigameManager;

  /**
   * Setter for the head up display.
   * 
   * @param hud the head up display to be set.
   */
  public void setHud(HeadUpDisplay hud) {
    this.hud = hud;
  }

  /**
   * Setter for the client thread.
   * 
   * @param clientThread the client thread to be set.
   */
  public void setClientThread(ClientThread clientThread) {
    this.clientThread = clientThread;
  }

  /**
   * Setter for the screen manager.
   * 
   * @param screenManager the screen manager to be set.
   */
  public void setScreenManager(ScreenManager screenManager) {
    this.screenManager = screenManager;
  }

  /**
   * Setter for the scene manager.
   * 
   * @param sceneManager the scene manager to be set.
   */
  public void setSceneManager(SceneManager sceneManager) {
    this.sceneManager = sceneManager;
  }

  /**
   * Setter for the minigame manager.
   * 
   * @param minigameManager the minigame manager to be set.
   */
  public void setMinigameManager(MinigameManager minigameManager) {
    this.minigameManager = minigameManager;
  }

  /**
   * Abstract method for starting a minigame.
   */
  public abstract void start();

  /**
   * Abstract method for stopping a minigame.
   */
  public abstract void stop();

  /**
   * Update method for minigames.
   * 
   * @param tpf time per frame
   */
  public void update(float tpf) {}

  /**
   * Handle an incoming event.
   * 
   * @param event the event to be handled
   */
  public void handleEvent(EventObject event) {}

  /**
   * Abstract method for getting the name of the minigame.
   * 
   * @return the name of the minigame
   */
  public abstract String getName();

  /**
   * Getter for the head up display.
   * 
   * @return the head up display
   */
  public HeadUpDisplay getHud() {
    return hud;
  }

  /**
   * Getter for the client thread.
   * 
   * @return the client thread
   */
  public ClientThread getClientThread() {
    return clientThread;
  }

  /**
   * Getter for the screen manager.
   * 
   * @return the screen manager
   */
  public ScreenManager getScreenManager() {
    return screenManager;
  }

  /**
   * Getter for the scene manager.
   * 
   * @return the scene manager
   */
  public SceneManager getSceneManager() {
    return sceneManager;
  }

  /**
   * Getter for the minigame manager.
   * 
   * @return the minigame manager
   */
  public MinigameManager getMinigameManager() {
    return minigameManager;
  }

}
