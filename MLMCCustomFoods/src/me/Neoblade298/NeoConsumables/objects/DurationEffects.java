package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

public class DurationEffects {
	private long endTime;
	private FoodConsumable cons;
	private ArrayList<BukkitRunnable> activeRunnables;
	
	public DurationEffects(FoodConsumable cons, long endTime) {
		this.cons = cons;
		this.endTime = endTime;
	}
	
	public long getEndTime() {
		return endTime;
	}
	
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	public FoodConsumable getCons() {
		return cons;
	}
	
	public ArrayList<BukkitRunnable> getActiveRunnables() {
		return activeRunnables;
	}
	
	public void endEffects() {
		for (BukkitRunnable runnable : activeRunnables) {
			runnable.cancel();
		}
	}
}
