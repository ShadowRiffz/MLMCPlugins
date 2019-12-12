package me.neoblade298.neobossinstances;

import org.bukkit.Location;

public class Boss {
	private Location coords = null;
	private String cmd = null;
	private int cooldown = 0;
	private String displayName = null;

	public Boss (Location coords, String cmd, int cooldown, String displayName){
		this.coords = coords;
		this.cmd = cmd;
		this.cooldown = cooldown;
		this.displayName = displayName;
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
