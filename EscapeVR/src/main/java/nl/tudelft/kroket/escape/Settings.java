package nl.tudelft.kroket.escape;

import com.jme3.math.Vector3f;

/**
 * Global settings for EscapeVR client.
 * 
 * @author Team Kroket
 *
 */
public class Settings {

  /** Gameplay time in seconds. Value can be overridden by host. */
  public final static int TIMELIMIT = 10 * 60;

  /** Debug flag. */
  public final static boolean DEBUG = false;

  /** Portnumber of the gamehost. */
  public static int PORTNUM = 1234;

  /** Default spawn position for the player/observer. */
  public final static Vector3f spawnPosition = new Vector3f(0, 0, 0);

  /** Enable the spookyscreen overlay. */
  public final static boolean SPOOKY_ENABLED = true;

  /** The number of miliseconds to show the spookyscreen. */
  public final static int SPOOKY_DISPLAY_TIME = 400;

  /**
   * Spookytime is an event that occurs between a random time in a certain interval. This interval
   * can be defined here.
   */
  public final static int INTERVAL_SPOOKYTIME_LOWER = 20;
  public final static int INTERVAL_SPOOKYTIME_UPPER = 120;

  public static final String INTERACTION_BUTTON = "Button A";

}
