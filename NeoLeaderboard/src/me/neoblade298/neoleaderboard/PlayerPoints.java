package me.neoblade298.neoleaderboard;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerPoints {
	private UUID uuid;
	private String display;
	private HashMap<PlayerPointType, Double> points = new HashMap<PlayerPointType, Double>();
	
	public PlayerPoints(UUID uuid) {
		this.uuid = uuid;
		this.display = Bukkit.getOfflinePlayer(uuid).getName();
	}
	
	public void addPoints(double amount, PlayerPointType type) {
		points.put(type, points.getOrDefault(type, 0D));
	}
	
	public void takePoints(double amount, PlayerPointType type) {
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
		return this.display;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public HashMap<PlayerPointType, Double> getAllPoints() {
		return points;
	}
}	
