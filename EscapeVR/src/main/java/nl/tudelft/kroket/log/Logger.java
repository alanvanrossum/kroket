package nl.tudelft.kroket.log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	/**
	 * Logger object constructor.
	 */
	private Logger() {
	}

	private LogLevel level = LogLevel.ALL;

	public enum LogLevel {
		NONE, INFO, ERROR, DEBUG, ALL
	}

	private String msgFormat = "%s %s: %s";

	public void setLevel(LogLevel level) {
		this.level = level;
	}

	public LogLevel getLevel() {
		return level;
	}

	private static Logger instance = new Logger();

	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * Singleton instance.
	 * @return the logger object
	 */
	public static Logger getInstance() {
		return instance;
	}
	
	/**
	 * Print an info message.
	 * @param tag the tag
	 * @param message the message
	 */
	public void info(String tag, String message) {

		print(LogLevel.INFO, tag, message);
	}

	/**
	 * Print a debug message.
	 * @param tag the tag
	 * @param message the message
	 */
	public void debug(String tag, String message) {

		print(LogLevel.DEBUG, tag, message);
	}
	
	/**
	 * Print an error message.
	 * @param tag the tag
	 * @param message the message
	 */
	public void error(String tag, String message) {

		print(LogLevel.ERROR, tag, message);
	}

	void print(LogLevel level, String tag, String message) {

		if (getLevel() == LogLevel.NONE) {
			return;
		}
		if (level.ordinal() <= getLevel().ordinal()) {
			String output = "[" + timeFormat.format(new Date()) + "]: "
					+ String.format(msgFormat, level, tag, message);

			if (level == LogLevel.ERROR) {
				System.err.println(output);
			} else {
				System.out.println(output);
			}
		}
	}

}
