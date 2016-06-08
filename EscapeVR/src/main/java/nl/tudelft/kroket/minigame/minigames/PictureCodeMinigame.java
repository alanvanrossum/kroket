package nl.tudelft.kroket.minigame.minigames;

import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.minigame.Minigame;

import java.util.EventObject;

/**
 * This class contains the logic of the first minigame (A) of the VR player.
 * 
 * @author Team Kroket
 */
public class PictureCodeMinigame extends Minigame {

  private static final String GAME_NAME = "A";
  
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
    log.info(className, "Minigame A started.");
    hud.setCenterText("Try to decipher the code by studying the painting!", 10);
  }

  /**
   * Stop the minigame.
   */
  @Override
  public void stop() {
    log.info(className, "Minigame A completed.");
    hud.setCenterText("Great! You cracked the code to open the safe!", 10);
    sceneManager.extendEscapeScene("A");
  }

  @Override
  public void update(float tpf) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void handleEvent(EventObject event) {
    // TODO Auto-generated method stub
  }

  /**
   * Returns the name of the game.
   */
  public String getName() {
    return GAME_NAME;
  }
  
}
