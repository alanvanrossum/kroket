package nl.tudelft.kroket.log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Logger class.
 * 
 * @author Team Kroket
 *
 */
public class Logger {

  /**
   * Logger object constructor.
   */
  private Logger() {
  }

  /** The current LogLevel. Default is ALL. */
  private LogLevel level = LogLevel.ALL;

  /** The different log levels available. */
  public enum LogLevel {
    NONE, INFO, ERROR, DEBUG, ALL
  }

  /** The message format. */
  private String msgFormat = "%s %s: %s";

  /** Singleton instance. */
  private static Logger instance = new Logger();

  private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

  /**
   * Sets the log level.
   * 
   * @param level the log level to be set.
   */
  public void setLevel(LogLevel level) {
    this.level = level;
  }

  /**
   * Get the log level.
   * 
   * @return the current log level.
   */
  public LogLevel getLevel() {
    return level;
  }

  /**
   * Singleton instance.
   * 
   * @return the logger object
   */
  public static Logger getInstance() {
    return instance;
  }

  /**
   * Print an info message.
   * 
   * @param tag
   *          the tag
   * @param message
   *          the message
   */
  public void info(String tag, String message) {
    print(LogLevel.INFO, tag, message);
  }

  /**
   * Print a debug message.
   * 
   * @param tag
   *          the tag
   * @param message
   *          the message
   */
  public void debug(String tag, String message) {
    print(LogLevel.DEBUG, tag, message);
  }

  /**
   * Print an error message.
   * 
   * @param tag
   *          the tag
   * @param message
   *          the message
   */
  public void error(String tag, String message) {
    print(LogLevel.ERROR, tag, message);
  }

  /**
   * Format the output string.
   * 
   * @param level
   *          the level of the message
   * @param tag
   *          of the message
   * @param message
   *          the message
   * @return formatted output string
   */
  private String formatOutput(LogLevel level, String tag, String message) {
    String output = "[" + timeFormat.format(new Date()) + "]: "
        + String.format(msgFormat, level, tag, message);
    return output;
  }

  /**
   * Print the message to standard output.
   * 
   * @param level
   *          the level of the message
   * @param tag
   *          the tag of the message
   * @param message
   *          the actual message
   */
  void print(LogLevel level, String tag, String message) {

    if (getLevel() == LogLevel.NONE) {
      return;
    }

    // Only print messages if our settings allow us to
    if (level.ordinal() <= getLevel().ordinal()) {
      String output = formatOutput(level, tag, message);
      if (level == LogLevel.ERROR) {
        System.err.println(output);
      } else {
        System.out.println(output);
      }
    }
  }

}
