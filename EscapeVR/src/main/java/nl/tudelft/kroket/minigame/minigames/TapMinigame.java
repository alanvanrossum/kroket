package nl.tudelft.kroket.minigame.minigames;

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;

import java.util.EventObject;

public class TapMinigame extends Minigame {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();
  
  /** Singleton instance. */
  private static Minigame instance = new TapMinigame();
  
  /** Private constructor. */
  private TapMinigame() {}

  /** Get the singleton instance. */
  public static Minigame getInstance() {
    return instance;
  }
  
  /**
   * Start the minigame.
   */
  @Override
  public void start() {
    log.info(className, "Minigame B started.");
    hud.setCenterText("Minigame B started!", 10);
  }

  /**
   * Stop the minigame.
   */
  @Override
  public void stop() {
    log.info(className, "Minigame B completed.");
    hud.setCenterText("Minigame B complete!", 10);
    sceneManager.extendEscapeScene("C");
  }

  @Override
  public void update(float tpf) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void handleEvent(EventObject event) {
    // TODO Auto-generated method stub
  }

}
