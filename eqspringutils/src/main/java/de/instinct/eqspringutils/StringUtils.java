package de.instinct.eqspringutils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class StringUtils {
	
	public static String formatDate(long millis) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy - HH:mm:ss").withZone(ZoneId.systemDefault());
		return formatter.format(Instant.ofEpochMilli(millis));
	}
	
	public static String limitWithAppendix(String message, int limit) {
		return message.length() > limit ? message.substring(0, limit) + "... (cut string length: " + (message.length() - limit) + ")" : message;
	}

}
