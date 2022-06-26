package me.neoblade298.neoleaderboard.points;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

public class TownEntry implements Comparable<TownEntry> {
	private HashMap<PlayerPointType, Double> playerPoints = new HashMap<PlayerPointType, Double>();
	private HashMap<UUID, PlayerEntry> players = new HashMap<UUID, PlayerEntry>();
	private TreeSet<UUID> topPlayers;
	private double total;
	private int contributors;
	private Town town;
	private Nation nation;
	
	private final Comparator<UUID> playerComparer;
	
	public TownEntry(UUID uuid, UUID nation, int contributors) {
		this.contributors = contributors;
		this.town = TownyAPI.getInstance().getTown(uuid);
		this.nation = TownyAPI.getInstance().getNation(nation);
		
		playerComparer = new Comparator<UUID>() {
			public int compare(UUID o1, UUID o2) {
				return (int) (players.get(o1).getContributed() - players.get(o2).getContributed());
			}
		};
		
		topPlayers = new TreeSet<UUID>(playerComparer);
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
	
	public TreeSet<UUID> getTopPlayers() {
		return topPlayers;
	}
	
	public HashMap<UUID, PlayerEntry> getAllPlayerPoints() {
		return players;
	}
	
	public Nation getNation() {
		return nation;
	}
}
