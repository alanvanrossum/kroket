package nl.tudelft.kroket.EscapeServer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

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

	public static Logger getInstance() {
		return instance;
	}

	public void info(String tag, String message) {

		print(LogLevel.INFO, tag, message);
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
