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
			end = start.truncatedTo(ChronoUnit.HOURS).plusMinutes((segmentsPassed + 1) * segmentLength).truncatedTo(ChronoUnit.MINUTES);
		}

		// Get Duration
		long between = (Duration.between(start, end).toMillis() / 50) + 1; // Add 1 because truncate will round it down 1 tick
		return between <= 1 ? segmentLength * 60 * 20 : between; // If between is 0, get the next segment
	}

	public static long getTicksPastPreviousSegment(int segmentLength) {
		LocalDateTime end = LocalDateTime.now();
		int segmentsPassed = end.getMinute() / segmentLength;
		LocalDateTime start;
		if (segmentsPassed == 0) {
			start = end.truncatedTo(ChronoUnit.HOURS); // Previous segment was top of the hour
		}
		else {
			start = end.truncatedTo(ChronoUnit.HOURS).plusMinutes((segmentsPassed - 1) * segmentLength).truncatedTo(ChronoUnit.MINUTES);
		}

		// Get Duration
		long between = (Duration.between(start, end).toMillis() / 50) + 1;
		return between <= 1 ? segmentLength * 60 * 20 : between; // If between is 0, get the previous segment
	}
}
