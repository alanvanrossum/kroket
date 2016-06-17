package nl.tudelft.kroket.timer;

import nl.tudelft.kroket.escape.Settings;
import nl.tudelft.kroket.log.Logger;
import nl.tudelft.kroket.net.ClientThread;
import nl.tudelft.kroket.net.protocol.Protocol;
import nl.tudelft.kroket.screen.HeadUpDisplay;

/**
 * Class that handles the timer for the VR player.
 * 
 * @author Team Kroket
 * 
 */
public class Timer {

  /** Singleton logger instance. */
  static final Logger log = Logger.getInstance();

  /** Current class, used as tag for logger. */
  private String className = this.getClass().getSimpleName();

  private long timeLimit;
  private boolean active;

  private ClientThread clientThread;
  private HeadUpDisplay hud;

  /**
   * Constructor for the timer object.
   * 
   * @param clientThread
   *          the client thread for sending messages to the server.
   * @param hud
   *          the head up display.
   */
  public Timer(ClientThread clientThread, HeadUpDisplay hud) {
    active = false;
    this.clientThread = clientThread;
    this.hud = hud;
  }

  /**
   * Starts the timer.
   */
  public void startTimer() {
    log.info(className, "Starting timer...");
    timeLimit = Settings.TIMELIMIT * 1000 + System.currentTimeMillis();
    active = true;
  }

  /**
   * Stops the timer.
   */
  public void stopTimer() {
    log.info(className, "Stopping timer...");
    clientThread.sendMessage(String.format("%s", Protocol.COMMAND_GAMEOVER));
    active = false;
  }

  /**
   * Checks whether the timer is active.
   * 
   * @return true iff the timer is active.
   */
  public boolean isActive() {
    return active;
  }

  /**
   * Updates the timer.
   */
  public void update() {
    if (isActive()) {

      long timeRemaining = Math.max(timeLimit - System.currentTimeMillis(), 0);

      if (timeRemaining <= 0) {
        stopTimer();
        return;
      }

      int secondsRemaining = (int) (timeRemaining / 1000);

      int minutesRemaining = secondsRemaining / 60;
      secondsRemaining -= (minutesRemaining * 60);

      hud.setTimerText(String.format("Time remaining: %02d:%02d", minutesRemaining,
          secondsRemaining));
    }
  }

  /**
   * Extend the time left and send this to the virtual client.
   * 
   * @param time
   *          the time to be added in seconds.
   */
  public void bonusTime() {
    log.info(className, String.format("Timer increased by %d seconds.", Settings.BONUSTIME));
    timeLimit += Settings.BONUSTIME * 1000;
  }

  /**
   * Gets the timelimit.
   * @return the timelimit
   */
  public long getTimeLimit() {
    return timeLimit;
  }
 
  /**
   * Set timer activity.
   * 
   * @param active set the timer's activity
   */
  public void setActive(boolean active) {
    this.active = active;
  }

}
