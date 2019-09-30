package me.neoblade298.neobossinstances;

import org.bukkit.Location;

public class Boss {
	private Location coords = null;
	private String cmd = null;
	private int cooldown = 0;

	public Boss (Location coords, String cmd, int cooldown){
		this.coords = coords;
		this.cmd = cmd;
		this.cooldown = cooldown;
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
}
