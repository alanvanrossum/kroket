package nl.tudelft.kroket.minigame.minigames;

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;


/**
 * This class contains the logic of waiting minigame Gyroscope of the VR player.
 * 
 * @author Team Kroket
 *
 */

public class GyroscopeMinigame extends Minigame {
  
  /** The name of the game. */
  private static final String GAME_NAME = "WAITING";

  /** Current class, used as tag for logger. */
  private final String className = this.getClass().getSimpleName();

  /** Singleton logger instance. */
  private Logger log = Logger.getInstance();
  
  /** Singleton instance. */
  private static Minigame instance = new GyroscopeMinigame();
  
  /** Private constructor. */
  private GyroscopeMinigame() {}

  /** Get the singleton instance. */
  public static Minigame getInstance() {
    return instance;
  }
  
  /**
   * Start the minigame.
   */
  @Override
  public void start() {
    log.info(className, "Minigame WAITING started.");
    hud.setCenterText("Minigame WAITING started!", 10);
  }

  /**
   * Stop the minigame.
   */
  @Override
  public void stop() {
    log.info(className, "Minigame WAITING completed.");
    hud.setCenterText("Minigame WAITING complete!", 10);
  }

  /**
   * Getter for the name of the game.
   */
  public String getName() {
    return GAME_NAME;
  }

}
