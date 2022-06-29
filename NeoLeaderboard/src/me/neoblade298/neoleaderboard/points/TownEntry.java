package me.neoblade298.neoleaderboard.points;

import java.util.HashMap;
import java.util.TreeSet;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

public class TownEntry implements Comparable<TownEntry> {
	private HashMap<PlayerPointType, Double> playerPoints = new HashMap<PlayerPointType, Double>();
	private HashMap<UUID, PlayerEntry> players = new HashMap<UUID, PlayerEntry>();
	private TreeSet<PlayerEntry> topPlayers;
	private double total;
	private int contributors;
	private Town town;
	private Nation nation;
	private NationEntry ne;
	
	public TownEntry(UUID uuid, UUID nation, int contributors) {
		this.contributors = contributors;
		this.town = TownyAPI.getInstance().getTown(uuid);
		this.nation = TownyAPI.getInstance().getNation(nation);
		this.ne = PointsManager.getNationEntry(nation);
		topPlayers = new TreeSet<PlayerEntry>();
	}
	
	public void addPlayerPoints(double amount, PlayerPointType type, UUID uuid) {
		// Re-sort is done from PointsManager calling removeFromSort
		
		playerPoints.put(type, playerPoints.getOrDefault(type, 0D) + amount);
		total += amount;
		
		PlayerEntry pe = PointsManager.getPlayerEntry(uuid);
		// Only add if the player is online
		if (pe != null) {
			players.putIfAbsent(uuid, pe);
			topPlayers.add(pe);
		}
	}
	
	// Must be called anytime a player gets points in PointsManager to remove the entry from the set and re-sort it later
	public void removeFromSort(PlayerEntry pe) {
		topPlayers.remove(pe);
	}
	
	public void takePlayerPoints(double amount, PlayerPointType type, UUID uuid) {
		addPlayerPoints(-amount, type, uuid);
	}
	
	public HashMap<PlayerPointType, Double> getPlayerPoints() {
		return playerPoints;
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
	
	public TreeSet<PlayerEntry> getTopPlayers() {
		return topPlayers;
	}
	
	public HashMap<UUID, PlayerEntry> getAllPlayerPoints() {
		return players;
	}
	
	public Nation getNation() {
		return nation;
	}
	
	public NationEntry getNationEntry() {
		return ne;
	}


	@Override
	public int compareTo(TownEntry o) {
		if (this.total > o.total) {
			return 1;
		}
		else if (this.total < o.total) {
			return -1;
		}
		else {
			return o.town.getName().compareTo(this.town.getName());
		}
	}
	
	public void clearPlayer(PlayerEntry pe) {
		topPlayers.remove(pe);
		players.remove(pe.getUuid());
	}
	
	public void clearAllPlayers() {
		for (PlayerEntry pe : topPlayers) {
			
		}
		players.clear();
		topPlayers.clear();
	}
}
