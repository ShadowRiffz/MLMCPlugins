package me.neoblade298.neoquests.objectives;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigParser;

public abstract class Objective implements LineConfigParser<Objective> {
	protected ObjectiveEvent type;
	protected ObjectiveSet set;
	protected int needed;
	protected String endpoint;
	
	public Objective() {}
	
	public Objective(ObjectiveEvent type, LineConfig cfg) {
		this.type = type;
		this.needed = cfg.getInt("needed", 1);
		this.endpoint = cfg.getString("endpoint", null);
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
	public String getEndpoint() {
		return endpoint;
	}
	// Sets up obj count on quest startup, returns true if quest complete
	public boolean initialize(ObjectiveInstance oi) {	return false;	}
	// Done after stage ends, basically just for GetStoredItem
	public void finalize(Player p) { } 
	
	public abstract String getKey();
	public abstract String getDisplay();
}
