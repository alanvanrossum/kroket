package nl.tudelft.kroket.minigame.minigames;

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;

/**
 * Class that handles the correct behaviour for the lock game.
 * 
 * @author Team Kroket
 */
public class LockMinigame extends Minigame {

  /** Name for the lock minigame. */
  private static final String GAME_NAME = "D";
  
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
    log.info(className, "Minigame D started.");
    hud.setCenterText("What could the code for the lock be?", 30);
    sceneManager.extendEscapeScene("D");
  }

  /**
   * Stop the minigame.
   */
  @Override
  public void stop() {
    log.info(className, "Minigame D completed.");
    hud.setCenterText("You opened the lock!", 10);
  }

  /**
   * Get the name of the minigame.
   */
  public String getName() {
    return GAME_NAME;
  }

    
}
