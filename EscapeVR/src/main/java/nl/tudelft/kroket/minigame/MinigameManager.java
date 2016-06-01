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
  
  private Minigame currentGame = null;
  
  public HeadUpDisplay hud;
  public ClientThread clientThread;
  public ScreenManager screenManager;
  public SceneManager sceneManager;

  public MinigameManager(HeadUpDisplay hud, ClientThread clientThread, 
      ScreenManager screenManager, SceneManager sceneManager) {
    log.info(className, "Initializing...");
    
    this.hud = hud;
    this.clientThread = clientThread;
    this.screenManager = screenManager;
    this.sceneManager = sceneManager;
    
  }

  @Override
  public void handleEvent(EventObject event) {
    if (currentGame == null)
      return;
    
    currentGame.handleEvent(event);
    
    
  }
  
  public void launchGame(Minigame minigame) {
    currentGame = minigame;
    
    minigame.setClientThread(clientThread);
    minigame.setHud(hud);
    minigame.setScreenManager(screenManager);
    minigame.setSceneManager(sceneManager);
    
    currentGame.start(); 
  }
  
  public void update(float tpf) {
    
    if (currentGame == null)
      return;
          
    currentGame.update(tpf);
  }
  
  public Minigame getCurrent() {
    return currentGame;
  }
  
}
