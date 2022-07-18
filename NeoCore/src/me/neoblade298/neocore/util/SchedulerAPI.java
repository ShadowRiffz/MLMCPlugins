package me.neoblade298.neocore.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.NeoCore;

public class SchedulerAPI {
	private static final int startupTime = getDateKey(Calendar.getInstance());
	private static ArrayList<HashMap<Integer, ArrayList<CoreRunnable>>> schedule = new ArrayList<HashMap<Integer, ArrayList<CoreRunnable>>>(3);
	private static HashMap<ScheduleInterval, ArrayList<CoreRunnable>> repeaters = new HashMap<ScheduleInterval, ArrayList<CoreRunnable>>();
	
	private static final int MINUTES_PER_SEGMENT = 15;
	
	public static void initialize() {
		for (int i = 0; i < 2; i++) {
			schedule.add(new HashMap<Integer, ArrayList<CoreRunnable>>());
		}
		
		for (ScheduleInterval interval : ScheduleInterval.values()) {
			repeaters.put(interval, new ArrayList<CoreRunnable>());
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
		HashMap<Integer, ArrayList<CoreRunnable>> day = schedule.get(diff);
		if (day != null) {
			if (day.containsKey(timeKey)) {
				for (CoreRunnable runnable : day.get(timeKey)) {
					if (runnable.offset == 0) {
						if (runnable.isCancelled) {
							runnable.runnable.run();
						}
					}
					else {
						new BukkitRunnable() {
							public void run() {
								if (runnable.isCancelled) {
									runnable.runnable.run();
								}
							}
						}.runTaskLater(NeoCore.inst(), runnable.offset * 20);
					}
				}
				day.remove(timeKey);
			}
		}

		// Repeating tasks
		Iterator<CoreRunnable> iter = repeaters.get(ScheduleInterval.FIFTEEN_MINUTES).iterator();
		while (iter.hasNext()) {
			CoreRunnable cr = iter.next();
			if (cr.isCancelled) {
				iter.remove();
			}
			else {
				cr.runnable.run();
			}
		}

		if (minute % 30 == 0) {
			iter = repeaters.get(ScheduleInterval.HALF_HOUR).iterator();
			while (iter.hasNext()) {
				CoreRunnable cr = iter.next();
				if (cr.isCancelled) {
					iter.remove();
				}
				else {
					cr.runnable.run();
				}
			}
		}
		
		if (minute == 0) {
			iter = repeaters.get(ScheduleInterval.HOUR).iterator();
			while (iter.hasNext()) {
				CoreRunnable cr = iter.next();
				if (cr.isCancelled) {
					iter.remove();
				}
				else {
					cr.runnable.run();
				}
			}
		}
	}
	
	private static CoreRunnable schedule(int date, int hour, int minute, int second, Runnable runnable) {
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
				HashMap<Integer, ArrayList<CoreRunnable>> day = schedule.get(diff);
				
				ArrayList<CoreRunnable> runnables = day.getOrDefault(time, new ArrayList<CoreRunnable>());
				CoreRunnable cr = new CoreRunnable(runnable, offset);
				runnables.add(cr);
				day.putIfAbsent(time, runnables);
				return cr;
			}
		}
		return null;
	}
	
	public static CoreRunnable schedule(int year, int month, int day, int hour, int minute, int second, Runnable runnable) {
		int date = (year * 10000) + (month * 100) + day;
		return schedule(date, hour, minute, second, runnable);
	}
	
	public static CoreRunnable schedule(int hour, int minute, int second, Runnable runnable) {
		Calendar c = Calendar.getInstance();
		return schedule(getDateKey(c), hour, minute, second, runnable);
	}
	
	public static CoreRunnable schedule(int hour, int minute, Runnable runnable) {
		return schedule(hour, minute, 0, runnable);
	}
	
	public static CoreRunnable scheduleRepeating(ScheduleInterval interval, Runnable runnable) {
		CoreRunnable cr = new CoreRunnable(runnable, 0);
		repeaters.get(interval).add(cr);
		return cr;
	}
	
	public static CoreRunnable schedule(long time, Runnable runnable) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		return schedule(getDateKey(c), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), runnable);
	}
	
	private static int getDateKey(Calendar c) {
		return (c.get(Calendar.YEAR) * 10000) + ((c.get(Calendar.MONTH) + 1) * 100) + c.get(Calendar.DAY_OF_MONTH);
	}
	
	public static class CoreRunnable {
		private Runnable runnable;
		private boolean isCancelled;
		private int offset;
		public CoreRunnable(Runnable runnable, int offset) {
			this.runnable = runnable;
			this.offset = offset;
			this.isCancelled = false;
		}
		
		public void setCancelled(boolean cancelled) {
			this.isCancelled = cancelled;
		}
	}
	
	private static int getLastTimeKey() {
		Calendar c = Calendar.getInstance();
		int minute = c.get(Calendar.MINUTE);
		minute -= minute % MINUTES_PER_SEGMENT;
		return (c.get(Calendar.HOUR) * 100) + minute;
	}
}
