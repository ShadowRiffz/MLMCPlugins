package me.neoblade298.neoquests.objectives;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigParser;

public abstract class Objective implements LineConfigParser<Objective> {
	protected ObjectiveEvent type;
	protected ObjectiveSet set;
	protected int needed;
	
	public Objective() {}
	
	public Objective(ObjectiveEvent type, LineConfig cfg) {
		this.type = type;
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
	public int getNeeded() {
		return needed;
	}
	public void initialize(ObjectiveInstance oi) {	} // For things that must be checked on quest startup, like get stored item
	
	public abstract String getKey();
	public abstract String getDisplay();
}
