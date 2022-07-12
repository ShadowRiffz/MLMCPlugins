package me.neoblade298.neocore.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.NeoCore;

public class SchedulerAPI {
	private static final int startupTime;
	private static ArrayList<HashMap<Integer, ArrayList<OffsetRunnable>>> schedule = new ArrayList<HashMap<Integer, ArrayList<OffsetRunnable>>>(3);
	private static HashMap<ScheduleInterval, ArrayList<BukkitRunnable>> repeaters = new HashMap<ScheduleInterval, ArrayList<BukkitRunnable>>();
	
	static {
		Calendar inst = Calendar.getInstance();
		startupTime = (inst.get(Calendar.YEAR) * 10000) + (inst.get(Calendar.MONTH) * 100) + inst.get(Calendar.DAY_OF_MONTH);
		
		for (int i = 0; i < 2; i++) {
			schedule.add(new HashMap<Integer, ArrayList<OffsetRunnable>>());
		}
		
		for (ScheduleInterval interval : ScheduleInterval.values()) {
			repeaters.put(interval, new ArrayList<BukkitRunnable>());
		}

		scheduleTimekeeper();
	}
	
	// Every 15 minutes
	private static void scheduleTimekeeper() {
		new BukkitRunnable() {
			public void run() {
				runScheduledItems();
				scheduleTimekeeper();
			}
		}.runTaskLater(NeoCore.inst(), TimeUtil.getTicksToNextSegment(15));
	}
	
	private static void runScheduledItems() {
		Calendar inst = Calendar.getInstance();
		int date = (inst.get(Calendar.YEAR) * 10000) + (inst.get(Calendar.MONTH) * 100) + inst.get(Calendar.DAY_OF_MONTH);
		int diff = date - startupTime;
		int hour = inst.get(Calendar.HOUR_OF_DAY);
		int minute = inst.get(Calendar.MINUTE);
		int roundedMinute = minute - (minute % 15);
		int time = (hour * 100) + roundedMinute;
		
		// Scheduled items first
		HashMap<Integer, ArrayList<OffsetRunnable>> day = schedule.get(diff);
		if (day != null) {
			if (day.containsKey(time)) {
				for (OffsetRunnable runnable : day.get(time)) {
					if (runnable.offset == 0) {
						runnable.runnable.runTask(NeoCore.inst());
					}
					else {
						runnable.runnable.runTaskLater(NeoCore.inst(), runnable.offset * 20);
					}
				}
			}
		}

		// Repeating tasks
		for (BukkitRunnable runnable : repeaters.get(ScheduleInterval.FIFTEEN_MINUTES)) {
			runnable.runTask(NeoCore.inst());
		}

		if (roundedMinute % 30 == 0) {
			for (BukkitRunnable runnable : repeaters.get(ScheduleInterval.HALF_HOUR)) {
				runnable.runTask(NeoCore.inst());
			}
		}
		
		if (roundedMinute == 0) {
			for (BukkitRunnable runnable : repeaters.get(ScheduleInterval.HOUR)) {
				runnable.runTask(NeoCore.inst());
			}
		}
	}
	
	private static void schedule(int date, int hour, int minute, int second, BukkitRunnable runnable) {
		int diff = date - startupTime;
		if (diff >= 0 && diff <= 2) {
			HashMap<Integer, ArrayList<OffsetRunnable>> day = schedule.get(diff);
			int scheduledMinute = minute - (minute % 15); // Round to previous 15
			int offset = ((minute % 15) * 60) + second; // Offset is number of seconds after the scheduled 15 minute interval
			int time = (hour * 100) + (scheduledMinute);
			
			ArrayList<OffsetRunnable> runnables = day.getOrDefault(time, new ArrayList<OffsetRunnable>());
			runnables.add(new OffsetRunnable(runnable, offset));
			day.putIfAbsent(time, runnables);
		}
	}
	
	public static void schedule(int year, int month, int day, int hour, int minute, int second,BukkitRunnable runnable) {
		int date = (year * 10000) + (month * 100) + day;
		schedule(date, hour, minute, second, runnable);
	}
	
	public static void schedule(int hour, int minute, int second, BukkitRunnable runnable) {
		Calendar inst = Calendar.getInstance();
		schedule(inst.get(Calendar.YEAR), inst.get(Calendar.MONTH), inst.get(Calendar.DAY_OF_MONTH), hour, minute, second, runnable);
	}
	
	public static void schedule(int hour, int minute, BukkitRunnable runnable) {
		Calendar inst = Calendar.getInstance();
		schedule(inst.get(Calendar.YEAR), inst.get(Calendar.MONTH), inst.get(Calendar.DAY_OF_MONTH), hour, minute, 0, runnable);
	}
	
	public static void scheduleRepeating(ScheduleInterval interval, BukkitRunnable runnable) {
		repeaters.get(interval).add(runnable);
	}
	
	private static class OffsetRunnable {
		private BukkitRunnable runnable;
		private int offset;
		public OffsetRunnable(BukkitRunnable runnable, int offset) {
			this.runnable = runnable;
			this.offset = offset;
		}
	}
}
