package me.neoblade298.neoquests.objectives;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.bar.BarAPI;
import me.neoblade298.neocore.bar.CoreBar;
import me.neoblade298.neoquests.listeners.ObjectiveListener;

public class ObjectiveInstance {
	private Player p;
	private Objective obj;
	private ObjectiveSetInstance set;
	private int count;

	public ObjectiveInstance(Player p, Objective obj, ObjectiveSetInstance set) {
		this.p = p;
		this.obj = obj;
		this.set = set;
	}

	public Objective getObjective() {
		return obj;
	}

	public Player getPlayer() {
		return p;
	}

	public int getCount() {
		return count;
	}

	public boolean setCount(int count) {
		this.count = Math.min(obj.getNeeded(), count);
		updateBar();
		if (isComplete()) {
			set.checkCompletion();
			return true;
		}
		return false;
	}

	public boolean addCount(int count) {
		this.count += count;
		this.count = Math.min(obj.getNeeded(), this.count);
		updateBar();
		if (isComplete()) {
			set.checkCompletion();
			return true;
		}
		return false;
	}

	public boolean incrementCount() {
		if (this.count < obj.getNeeded()) {
			this.count++;
			updateBar();
		}
		if (isComplete()) {
			set.checkCompletion();
			return true;
		}
		return false;
	}
	
	private void updateBar() {
		CoreBar cb = BarAPI.getBar(p);
		if (!cb.isEnabled()) return;
		if (isComplete()) {
			// Only increment progress bar if we're already following it
			if (cb.getTopic().equals("" + obj.hashCode())) {
				cb.setProgress(1);
			}
		}
		else {
			if (!cb.getTopic().equals("" + obj.hashCode()) && !obj.getDisplay().startsWith("Hide")) {
				cb.setTitle("§7(§c/q§7) " + obj.getDisplay());
			}
			cb.setProgress((double) this.count / (double) obj.getNeeded());
			cb.setTopic("" + obj.hashCode());
		}
	}

	public boolean isComplete() {
		return count >= obj.getNeeded();
	}
	
	public void finalize(Player p) {
		obj.finalize(p);
	}
	
	public void stopListening() {
		ObjectiveListener.stopListening(this);
	}
	
	public void startListening() {
		ObjectiveListener.startListening(this);
	}
}
