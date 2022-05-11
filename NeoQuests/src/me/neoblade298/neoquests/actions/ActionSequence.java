package me.neoblade298.neoquests.actions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neoquests.NeoQuests;

public class ActionSequence {
	private ArrayList<ActionSet> sets = new ArrayList<ActionSet>();
	int count = 0;
	ActionSet curr = new ActionSet(0); // Delay 0 for first set always
	
	public ActionSequence(List<String> list) {
		// Parse list of actions
	}

	private void addAction(Action action) {
		curr.addAction(action);
	}
	
	private void addAction(Action action, int seconds) {
		if (!curr.isEmpty()) {
			sets.add(curr);
		}
		curr = new ActionSet(seconds);
		curr.addAction(action);
	}
	
	public void run(Player p) {
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
				task.runTaskLater(NeoQuests.inst(), set.getDelay() * 20);
			}
		}
	}
}
