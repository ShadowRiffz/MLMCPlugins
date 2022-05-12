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
	ActionSet curr = new ActionSet();
	int runtime;
	
	public ActionSequence(List<String> list) {
		int delay = 0;
		for (String line : list) {
			LineConfig cfg = new LineConfig(line);

			Action action = Action.getNew(cfg);
			
			if (!(action instanceof EmptyAction)) { // DelayAction is empty
				addAction(action, runtime);
				delay = 0;
			}
			
			if (action instanceof DelayableAction) { // Delay is always after action
				runtime += ((DelayableAction) action).getDelay();
			}
		}
		
		if (!curr.isEmpty()) {
			curr.setPostDelay(delay);
			sets.add(curr);
		}
	}
	
	private void addAction(Action action, int delay) {
		if (delay > 0) {
			curr.setPostDelay(delay);
			sets.add(curr);
			curr = new ActionSet();
		}
		curr.addAction(action);
	}
	
	public int run(Player p) {
		return run(p, 0);
	}
	
	public int run(Player p, int delay) {
		int tick = delay;
		for (ActionSet set : sets) {
			if (set.isEmpty()) {
				BukkitRunnable task = new BukkitRunnable() {
					public void run() {
						set.run(p);
					}
				};
				task.runTaskLater(NeoQuests.inst(), tick * 20);
			}
			tick += set.getPostDelay();
		}
		return tick;
	}
	
	public int changeStage() {
		return nextStage;
	}
	
	public boolean isEmpty() {
		return sets.isEmpty();
	}
	
	public int getRuntime() {
		return runtime;
	}
}
