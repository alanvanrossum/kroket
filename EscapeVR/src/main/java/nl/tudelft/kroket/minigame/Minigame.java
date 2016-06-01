package nl.tudelft.kroket.minigame;

import java.util.EventObject;

import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.scene.SceneManager;
import nl.tudelft.kroket.screen.HeadUpDisplay;
import nl.tudelft.kroket.screen.ScreenManager;

public abstract class Minigame {

  public HeadUpDisplay hud;
  public ClientThread clientThread;
  public ScreenManager screenManager;
  public SceneManager sceneManager;
  
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

  public abstract void start();
  
  public abstract void stop();
  
  public abstract void update(float tpf);
  
  public abstract void handleEvent(EventObject event);
  
}
