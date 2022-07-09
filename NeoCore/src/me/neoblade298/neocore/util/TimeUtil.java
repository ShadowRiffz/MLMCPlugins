package me.neoblade298.neocore.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class TimeUtil {
	public static long getTicksToHour(int hour) {
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.withHour(hour);
		return Duration.between(start, end).toMillis() / 50;
	}

	public static long getTicksToNextSegment(int segmentLength) {
		LocalDateTime start = LocalDateTime.now();
		int totalSegments = (60 / segmentLength); // Segment Length must cleanly divide 60
		int segmentsPassed = start.getMinute() / segmentLength;
		LocalDateTime end;
		if (segmentsPassed + 1 == totalSegments) {
			end = start.plusHours(1).truncatedTo(ChronoUnit.HOURS); // Next segment starts on the hour
		}
		else {
			end = start.truncatedTo(ChronoUnit.HOURS).plusMinutes((segmentsPassed + 1) * segmentLength);
		}

		// Get Duration
		return Duration.between(start, end).toMillis() / 50;
	}
}
