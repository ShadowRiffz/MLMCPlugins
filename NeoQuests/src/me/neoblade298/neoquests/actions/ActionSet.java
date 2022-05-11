package me.neoblade298.neoquests.actions;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class ActionSet {
	private ArrayList<Action> actions = new ArrayList<Action>();
	private int delay;
	
	public ActionSet(int delay) {
		this.delay = delay;
	}
	
	public void run(Player p) {
		for (Action action : actions) {
			action.run(p);
		}
	}
	
	public void addAction(Action action) {
		actions.add(action);
	}
	
	public int getDelay() {
		return delay;
	}
	
	public boolean isEmpty() {
		return actions.isEmpty();
	}
}
