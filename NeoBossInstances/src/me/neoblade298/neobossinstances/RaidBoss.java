package me.neoblade298.neobossinstances;

import org.bukkit.Location;

public class RaidBoss {

	private Location coords = null;
	private String cmd = null;
	private String name = null;

	public RaidBoss (Location coords, String cmd, String name){
		this.coords = coords;
		this.cmd = cmd;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
