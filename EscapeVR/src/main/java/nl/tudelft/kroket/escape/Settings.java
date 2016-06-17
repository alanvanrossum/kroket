package nl.tudelft.kroket.escape;

import com.jme3.math.Vector3f;

/**
 * Global settings for EscapeVR client.
 * 
 * @author Team Kroket
 *
 */
public class Settings {

  /** Gameplay time in seconds. */
  public static final int TIMELIMIT = 15 * 60;
  
  /** The amount of seconds added to the timer when bonustime is received. */
  public static final int BONUSTIME = 20;

  /** Debug flag. */
  public static final boolean DEBUG = false;

  /** Flag for colliding with the walls. */
  public static final boolean WALLCOLLISION = true;

  /** Portnumber of the gamehost. */
  public static final int PORTNUM = 1234;

  /** Default spawn position for the player/observer. */
  public final static Vector3f spawnPosition = new Vector3f(0, 2, 0);
  
  /** Default spawn position for the the player/observer after winning the game. */
  public final static Vector3f winningPosition = new Vector3f(0, 2, 15f);

  /** Enable the spookyscreen overlay. */
  public static final boolean SPOOKY_ENABLED = true;

  /** The number of miliseconds to show the spookyscreen. */
  public static final int SPOOKY_DISPLAY_TIME = 400;

  /**
   * Spookytime is an event that occurs between a random time in a certain interval. This interval
   * can be defined here.
   */
  public static final int INTERVAL_SPOOKYTIME_LOWER = 20;
  public static final int INTERVAL_SPOOKYTIME_UPPER = 120;

  /** The button that causes interaction events. */
  public static final String INTERACTION_BUTTON = "Button A";

}
