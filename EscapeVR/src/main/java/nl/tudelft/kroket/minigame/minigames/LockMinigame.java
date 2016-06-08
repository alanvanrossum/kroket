package nl.tudelft.kroket.minigame.minigames;

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;

import java.util.EventObject;

/**
 * Class that handles the correct behaviour for the lock game.
 * @author Team Kroket
 */
public class LockMinigame extends Minigame {

  
  private static final String GAME_NAME = "F";
  
  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();
  
  /** Singleton instance. */
  private static Minigame instance = new LockMinigame();
  
  /** Private constructor. */
  private LockMinigame() {}

  /** Get the singleton instance. */
  public static Minigame getInstance() {
    return instance;
  }
  
  /**
   * Start the minigame.
   */
  @Override
  public void start() {
    log.info(className, "Minigame F started with code 13-37-21");
    hud.setCenterText("13-37-21", 30);
  }

  /**
   * Stop the minigame.
   */
  @Override
  public void stop() {
    log.info(className, "Minigame F completed.");
    hud.setCenterText("Minigame F complete!", 10);
  }

  @Override
  public void update(float tpf) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void handleEvent(EventObject event) {
    // TODO Auto-generated method stub
  }

  public String getName() {
    return GAME_NAME;
  }
    
}
