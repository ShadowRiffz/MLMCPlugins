package me.neoblade298.neoleaderboard.points;

import java.util.HashMap;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Town;

public class TownEntry implements Comparable<TownEntry> {
	private HashMap<PlayerPointType, Double> playerPoints = new HashMap<PlayerPointType, Double>();
	private double total;
	private int contributors;
	private Town town;
	
	public TownEntry(UUID uuid, int contributors) {
		this.contributors = contributors;
		this.town = TownyAPI.getInstance().getTown(uuid);
	}
	
	public void addPlayerPoints(double amount, PlayerPointType type) {
		playerPoints.put(type, playerPoints.getOrDefault(type, 0D) + amount);
		total += amount;
	}
	
	public HashMap<PlayerPointType, Double> getPlayerPoints() {
		return playerPoints;
	}

	@Override
	public int compareTo(TownEntry o) {
		return (int) (this.total - o.total);
	}
	
	public int getContributors() {
		return contributors;
	}
	
	public Town getTown() {
		return town;
	}
	
	public double getTotalPoints() {
		return total;
	}
}
