package me.neoblade298.neoleaderboard;

import java.util.HashMap;
import java.util.UUID;

import com.palmergames.bukkit.towny.Towny;
import com.palmergames.bukkit.towny.TownyAPI;

public class NationPoints {
	private UUID uuid;
	private String display;
	private HashMap<NationPointType, Double> points;
	
	public NationPoints(UUID uuid) {
		this.uuid = uuid;
		this.display = TownyAPI.getInstance().getNation(uuid).getName();
	}
	
	public void addPoints(double amount, NationPointType type) {
		points.put(type, points.getOrDefault(type, 0D));
	}
	
	public void takePoints(double amount, NationPointType type) {
		double after = points.getOrDefault(type, 0D) - amount;
		points.put(type, Math.max(0, after));
	}
	
	public void clearPoints() {
		points.clear();
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public String getDisplay() {
		return display;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public HashMap<NationPointType, Double> getAllPoints() {
		return points;
	}
}	
