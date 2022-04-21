package me.neoblade298.neoquestgps;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class QuestOverride {
	private ArrayList<Location> loc;
	private int stage;
	
	public QuestOverride(String stage, ArrayList<String> locations) {
		this.stage = Integer.parseInt(stage);
		
		this.loc = new ArrayList<Location>();
		
		for (String location : locations) {
			String[] params = location.split(" ");
			double x = Double.parseDouble(params[0]);
			double y = Double.parseDouble(params[1]);
			double z = Double.parseDouble(params[2]);
			this.loc.add(new Location(Bukkit.getWorld("Argyll"), x, y, z));
		}
	}
	
	public ArrayList<Location> getLocations() {
		return loc;
	}
	public void setLocations(ArrayList<Location> loc) {
		this.loc = loc;
	}
	public int getStage() {
		return stage;
	}
	public void setStage(int stage) {
		this.stage = stage;
	}
}
