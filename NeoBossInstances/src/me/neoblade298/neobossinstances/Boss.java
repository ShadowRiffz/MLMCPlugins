package me.neoblade298.neobossinstances;


import org.bukkit.Location;

public class Boss {
	private Location coords = null;
	private String cmd = null;
	private int cooldown = 0;
	private String displayName = null;
	private boolean isRaid = false;
	private int timeLimit = 0;

	public Boss (Location coords, String cmd, int cooldown, String displayName){
		this.coords = coords;
		this.cmd = cmd;
		this.cooldown = cooldown;
		this.displayName = displayName;
	}

	public Boss (Location coords, String cmd, int cooldown, String displayName, boolean isRaid, int timeLimit){
		this.coords = coords;
		this.cmd = cmd;
		this.cooldown = cooldown;
		this.displayName = displayName;
		this.isRaid = isRaid;
		this.timeLimit = timeLimit;
	}
	
	public boolean isRaid() {
		return isRaid;
	}

	public void setRaid(boolean isRaid) {
		this.isRaid = isRaid;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Location getCoords() {
		return coords;
	}

	public void setCoords(Location coords) {
		this.coords = coords;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
}
