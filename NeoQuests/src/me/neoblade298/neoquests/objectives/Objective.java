package me.neoblade298.neoquests.objectives;

import org.bukkit.event.Event;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigParser;

public abstract class Objective implements LineConfigParser<Objective> {
	protected ObjectiveEvent type;
	protected ObjectiveSet set;
	protected int count, needed;
	
	public Objective() {}
	
	public Objective(ObjectiveEvent type, LineConfig cfg) {
		this.type = type;
		this.count = 0;
		this.needed = cfg.getInt("needed", 1);
	}
	
	public ObjectiveEvent getType() {
		return type;
	}
	public ObjectiveSet getSet() {
		return set;
	}
	public void setSet(ObjectiveSet set) {
		this.set = set;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getNeeded() {
		return needed;
	}
	public void incrementCount() {
		this.count++;
		set.check();
	}
	public boolean isComplete() {
		updateCount();
		return count >= needed;
	}
	public void updateCount() {}
	
	public abstract String getKey();
}
