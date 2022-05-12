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
	
	public ActionSequence(List<String> list) {
		int delay = 0;
		for (String line : list) {
			LineConfig cfg = new LineConfig(line);
			
			Action action = Action.getNew(cfg);
			if (action instanceof Action) { // DelayAction
				delay += 0;
			}
			else {
				addAction(action, delay);
			}
		}
	}

	private void addAction(Action action) {
		if (action instanceof DialogueAction) {
			addAction(action, ((DialogueAction) action).getDelay());
		}
		curr.addAction(action);
	}
	
	private void addAction(Action action, int seconds) {
		curr.addAction(action);
		sets.add(curr);
		curr = new ActionSet(seconds);
	}
	
	public int run(Player p) {
		run(p, 0);
		return getRuntime();
	}
	
	public int run(Player p, int delay) {
		for (ActionSet set : sets) {
			BukkitRunnable task = new BukkitRunnable() {
				public void run() {
					set.run(p);
				}
			};
			if (set.getDelay() == 0) {
				task.runTask(NeoQuests.inst());
			}
			else {
				task.runTaskLater(NeoQuests.inst(), (set.getDelay() * 20) + (delay * 20));
			}
		}
		return getRuntime();
	}
	
	private int getRuntime() {
		return sets.get(sets.size() - 1).getDelay();
	}
	
	public int changeStage() {
		return nextStage;
	}
	
	public boolean isEmpty() {
		return sets.isEmpty();
	}
}
