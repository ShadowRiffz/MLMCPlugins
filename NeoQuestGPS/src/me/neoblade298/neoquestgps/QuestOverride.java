package me.neoblade298.neoquestgps;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class QuestOverride {
	private Location loc;
	private int stage;
	
	public QuestOverride(String input) {
		String[] params = input.split(" ");
		this.stage = Integer.parseInt(params[0]);
		double x = Double.parseDouble(params[1]);
		double y = Double.parseDouble(params[2]);
		double z = Double.parseDouble(params[3]);
		this.loc = new Location(Bukkit.getWorld("Argyll"), x, y, z);
	}
	
	public Location getLoc() {
		return loc;
	}
	public void setLoc(Location loc) {
		this.loc = loc;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
}
