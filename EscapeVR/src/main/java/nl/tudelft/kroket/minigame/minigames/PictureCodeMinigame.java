package nl.tudelft.kroket.minigame.minigames;

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;

import java.util.EventObject;

public class PictureCodeMinigame extends Minigame {

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();
  
  /** Singleton instance. */
  private static Minigame instance = new PictureCodeMinigame();
  
  /** Private constructor. */
  private PictureCodeMinigame() {}

  /** Get the singleton instance. */
  public static Minigame getInstance() {
    return instance;
  }
  
  /**
   * Start the minigame.
   */
  @Override
  public void start() {
    // TODO Auto-generated method stub
  }

  /**
   * Stop the minigame.
   */
  @Override
  public void stop() {
    // TODO Auto-generated method stub
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
