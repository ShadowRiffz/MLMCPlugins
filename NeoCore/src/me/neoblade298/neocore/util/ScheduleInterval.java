package me.neoblade298.neocore.util;

public enum ScheduleInterval {
	HOUR(60),
	HALF_HOUR(30),
	FIFTEEN_MINUTES(15);
	private final int divisor;
	private ScheduleInterval(final int divisor) {
		this.divisor = divisor;
	}
	
	public int getDivisor() {
		return divisor;
	}
}
