package me.neoblade298.neoleaderboard.points;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;

public class PlayerEntry {
	private UUID uuid;
	private String display;
	private HashMap<PlayerPointType, Double> points = new HashMap<PlayerPointType, Double>();
	private HashMap<PlayerPointType, Double> contributedPoints = new HashMap<PlayerPointType, Double>();
	private double contributed;
	private static double LIMIT = 250;
	
	public PlayerEntry(UUID uuid) {
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

	// Return the amount that can be contributed
	public double addPoints(double amount, PlayerPointType type) {
		points.put(type, points.getOrDefault(type, 0D) + amount);

		double before = contributedPoints.getOrDefault(type, 0.0D);
		double after = before + amount;
		double contributable = 0;
		
		if (amount >= 0) {
			if (contributed >= PointsManager.getMaxContribution()) {
				return 0;
			}
			else if (before < LIMIT && after > LIMIT) {
				contributable = LIMIT - before;
				contributedPoints.put(type, LIMIT);
				contributed += contributable;
				return contributable; // Will be positive
			}
			else if (before < LIMIT && after < LIMIT) {
				contributedPoints.put(type, after);
				contributed += amount;
				return amount;
			}
			// before > LIMIT && after > LIMIT we don't do anything
			// before > LIMIT && after < LIMIT not possible
		}
		else {
			if (before > LIMIT && after < LIMIT) {
				contributable = after - LIMIT;
				contributedPoints.put(type, after);
				contributed += contributable;
				return contributable; // Will be negative
			}
			else if (before < LIMIT && after < LIMIT) {
				contributedPoints.put(type, after);
				contributed += amount;
				return amount;
			}
			// before > LIMIT && after > LIMIT we don't do anything
			// before < LIMIT && after > LIMIT not possible
		}
		
		return 0;
	}
	
	public double takePoints(double amount, PlayerPointType type) {
		return addPoints(-amount, type);
	}
	
	public void clearPoints(Statement delete) throws SQLException {
		contributedPoints.clear();
		contributed = 0;
		points.clear();
		delete.addBatch("DELETE FROM leaderboard_playerpoints WHERE uuid = '" + uuid + "';");
		delete.addBatch("DELETE FROM leaderboard_contributed WHERE uuid = '" + uuid + "';");
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
