package nl.tudelft.kroket.minigame;

import java.util.EventObject;

import nl.tudelft.kroket.event.EventListener;
import nl.tudelft.kroket.log.Logger;

public class MinigameManager implements EventListener {
  
  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();
  
  private Minigame currentGame = null;

  public MinigameManager() {
    log.info(className, "Initializing...");
  }

  @Override
  public void handleEvent(EventObject event) {
    if (currentGame == null)
      return;
    
    currentGame.handleEvent(event);
  }
  
  public void launchGame(Minigame minigame) {
    currentGame = minigame;
    
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
