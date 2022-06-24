package me.neoblade298.neoleaderboard;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlayerPoints {
	private UUID uuid;
	private String display;
	private HashMap<PlayerPointType, Double> points = new HashMap<PlayerPointType, Double>();
	private HashMap<PlayerPointType, Double> contributedPoints = new HashMap<PlayerPointType, Double>();
	private double contributed;
	private static double LIMIT = 250;
	
	public PlayerPoints(UUID uuid) {
		this.uuid = uuid;
		this.display = Bukkit.getOfflinePlayer(uuid).getName();
	}
	
	public void setPoints(double amount, PlayerPointType type) {
		points.put(type, amount);
	}
	
	public void setContributedPoints(double amount, PlayerPointType type) {
		contributedPoints.put(type, amount);
	}
	
	public double calculateContributed() {
		double sum = 0;
		for (double d : contributedPoints.values()) {
			sum += d;
		}
		contributed = sum;
		return contributed;
	}
	
	public void addPoints(double amount, PlayerPointType type) {
		points.put(type, points.getOrDefault(type, 0D));
	}
	
	public void takePoints(double amount, PlayerPointType type) {
		double after = points.getOrDefault(type, 0D) - amount;
		points.put(type, Math.max(0, after));
	}
	
	public void clearPoints(Statement delete) throws SQLException {
		contributedPoints.clear();
		contributed = 0;
		points.clear();
		delete.addBatch("DELETE FROM neoleaderboard_points WHERE uuid = '" + uuid + "';");
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
	
	public double getContributed() {
		return contributed;
	}
	
	public HashMap<PlayerPointType, Double> getContributedPoints() {
		return contributedPoints;
	}
	
	public HashMap<PlayerPointType, Double> getTotalPoints() {
		return points;
	}
	
	public boolean isEmpty() {
		return points.isEmpty();
	}
	
	public static double getLimit() {
		return LIMIT;
	}
}	
