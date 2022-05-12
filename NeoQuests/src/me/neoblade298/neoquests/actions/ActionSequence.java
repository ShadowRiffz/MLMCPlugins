package me.neoblade298.neoquests.actions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.util.LineConfig;

public class ActionSequence {
	private ArrayList<ActionSet> sets = new ArrayList<ActionSet>();
	int nextStage = -1;
	ActionSet curr = new ActionSet(0); // Delay 0 for first set always
	int runtime = 0;
	
	public ActionSequence(List<String> list) {
		int delay = 0, prevDelay = 0;
		for (String line : list) {
			LineConfig cfg = new LineConfig(line);
			
			Action action = Action.getNew(cfg);
			if (action instanceof DelayableAction) {
				delay += ((DelayableAction) action).getDelay();
			}
			
			if (!(action instanceof EmptyAction)) { // DelayAction is empty
				addAction(action, delay, prevDelay);
			}
		}
		
		if (!curr.isEmpty()) {
			sets.add(curr);
		}
		runtime = delay;
	}
	
	private void addAction(Action action, int delay, int prevDelay) {
		if (delay != prevDelay) {
			if (!curr.isEmpty()) {
				sets.add(curr);
			}
			curr = new ActionSet(delay);
		}
		curr.addAction(action);
	}
	
	public int run(Player p) {
		run(p, 0);
		return runtime;
	}
	
	public int run(Player p, int delay) {
		for (ActionSet set : sets) {
			BukkitRunnable task = new BukkitRunnable() {
				public void run() {
					set.run(p);
				}
			};
			task.runTaskLater(NeoQuests.inst(), (set.getDelay() + delay) * 20);
		}
		return runtime;
	}
	
	private int getRuntime() {
		return runtime;
	}
	
	public int changeStage() {
		return nextStage;
	}
	
	public boolean isEmpty() {
		return sets.isEmpty();
	}
}
