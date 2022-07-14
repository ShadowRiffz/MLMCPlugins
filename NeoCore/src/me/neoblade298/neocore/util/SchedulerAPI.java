package me.neoblade298.neocore.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.NeoCore;

public class SchedulerAPI {
	private static final int startupTime = getDateKey(Calendar.getInstance());
	private static ArrayList<HashMap<Integer, ArrayList<OffsetRunnable>>> schedule = new ArrayList<HashMap<Integer, ArrayList<OffsetRunnable>>>(3);
	private static HashMap<ScheduleInterval, ArrayList<Runnable>> repeaters = new HashMap<ScheduleInterval, ArrayList<Runnable>>();
	
	private static final int MINUTES_PER_SEGMENT = 15;
	
	public static void initialize() {
		for (int i = 0; i < 2; i++) {
			schedule.add(new HashMap<Integer, ArrayList<OffsetRunnable>>());
		}
		
		for (ScheduleInterval interval : ScheduleInterval.values()) {
			repeaters.put(interval, new ArrayList<Runnable>());
		}

		scheduleTimekeeper();
	}
	
	// Every 15 minutes
	private static void scheduleTimekeeper() {
		long ticks = TimeUtil.getTicksToNextSegment(MINUTES_PER_SEGMENT);
		new BukkitRunnable() {
			public void run() {
				runScheduledItems();
				scheduleTimekeeper();
			}
		}.runTaskLater(NeoCore.inst(), ticks);
	}
	
	private static void runScheduledItems() {
		Calendar inst = Calendar.getInstance();
		int diff = getDateKey(inst) - startupTime;
		int hour = inst.get(Calendar.HOUR_OF_DAY);
		int minute = inst.get(Calendar.MINUTE);
		minute -= minute % 15;
		int timeKey = (hour * 100) + minute;
		
		// Scheduled items first
		HashMap<Integer, ArrayList<OffsetRunnable>> day = schedule.get(diff);
		if (day != null) {
			if (day.containsKey(timeKey)) {
				for (OffsetRunnable runnable : day.get(timeKey)) {
					if (runnable.offset == 0) {
						runnable.runnable.run();
					}
					else {
						new BukkitRunnable() {
							public void run() {
								runnable.runnable.run();
							}
						}.runTaskLater(NeoCore.inst(), runnable.offset * 20);
					}
				}
			}
		}

		// Repeating tasks
		for (Runnable runnable : repeaters.get(ScheduleInterval.FIFTEEN_MINUTES)) {
			runnable.run();
		}

		if (minute % 30 == 0) {
			for (Runnable runnable : repeaters.get(ScheduleInterval.HALF_HOUR)) {
				runnable.run();
			}
		}
		
		if (minute == 0) {
			for (Runnable runnable : repeaters.get(ScheduleInterval.HOUR)) {
				runnable.run();
			}
		}
	}
	
	private static boolean schedule(int date, int hour, int minute, int second, Runnable runnable) {
		int diff = date - startupTime;
		runnable.run();
		if (diff >= 0 && diff <= 2) {
			int scheduledMinute = minute - (minute % 15); // Round to previous 15
			int offset = ((minute % 15) * 60) + second; // Offset is number of seconds after the scheduled 15 minute interval
			int time = (hour * 100) + (scheduledMinute);
	
			// If the schedule time is within 15 minutes from now, just make it a regular bukkitrunnable
			if (diff == 0 && time == getLastTimeKey()) {
				Calendar now = Calendar.getInstance();
				Calendar scheduledTime = Calendar.getInstance();
				scheduledTime.set(Calendar.MINUTE, minute);
				scheduledTime.set(Calendar.SECOND, second);
				long timeToSchedule = scheduledTime.getTimeInMillis() - now.getTimeInMillis();
				new BukkitRunnable() {
					public void run() {
						runnable.run();
					}
				}.runTaskLater(NeoCore.inst(), timeToSchedule / 50);
			}
			// If schedule time is > 15 minutes from now, put it in the schedule
			else {
				HashMap<Integer, ArrayList<OffsetRunnable>> day = schedule.get(diff);
				
				ArrayList<OffsetRunnable> runnables = day.getOrDefault(time, new ArrayList<OffsetRunnable>());
				runnables.add(new OffsetRunnable(runnable, offset));
				day.putIfAbsent(time, runnables);
				return true;
			}
		}
		return false;
	}
	
	public static boolean schedule(int year, int month, int day, int hour, int minute, int second, Runnable runnable) {
		int date = (year * 10000) + (month * 100) + day;
		return schedule(date, hour, minute, second, runnable);
	}
	
	public static boolean schedule(int hour, int minute, int second, Runnable runnable) {
		Calendar c = Calendar.getInstance();
		return schedule(getDateKey(c), hour, minute, second, runnable);
	}
	
	public static boolean schedule(int hour, int minute, Runnable runnable) {
		return schedule(hour, minute, 0, runnable);
	}
	
	public static void scheduleRepeating(ScheduleInterval interval, Runnable runnable) {
		repeaters.get(interval).add(runnable);
	}
	
	public static boolean schedule(long time, Runnable runnable) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		return schedule(getDateKey(c), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), runnable);
	}
	
	private static int getDateKey(Calendar c) {
		return (c.get(Calendar.YEAR) * 10000) + ((c.get(Calendar.MONTH) + 1) * 100) + c.get(Calendar.DAY_OF_MONTH);
	}
	
	private static class OffsetRunnable {
		private Runnable runnable;
		private int offset;
		public OffsetRunnable(Runnable runnable, int offset) {
			this.runnable = runnable;
			this.offset = offset;
		}
	}
	
	private static int getLastTimeKey() {
		Calendar c = Calendar.getInstance();
		int minute = c.get(Calendar.MINUTE);
		minute -= minute % MINUTES_PER_SEGMENT;
		return (c.get(Calendar.HOUR) * 100) + minute;
	}
}