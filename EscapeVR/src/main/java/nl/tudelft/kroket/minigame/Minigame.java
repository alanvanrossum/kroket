package nl.tudelft.kroket.minigame;

import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

import java.util.EventObject;

public abstract class Minigame {

  protected HeadUpDisplay hud;
  protected ClientThread clientThread;
  protected ScreenManager screenManager;
  protected SceneManager sceneManager;
  protected MinigameManager minigameManager;

  public void setHud(HeadUpDisplay hud) {
    this.hud = hud;
  }

  public void setClientThread(ClientThread clientThread) {
    this.clientThread = clientThread;
  }

  public void setScreenManager(ScreenManager screenManager) {
    this.screenManager = screenManager;
  }

  public void setSceneManager(SceneManager sceneManager) {
    this.sceneManager = sceneManager;
  }

  public void setMinigameManager(MinigameManager minigameManager) {
    this.minigameManager = minigameManager;
  }

  public abstract void start();

  public abstract void stop();

  public void update(float tpf) {

  }

  public void handleEvent(EventObject event) {

  }

  public abstract String getName();

}
