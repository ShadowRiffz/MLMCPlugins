package me.neoblade298.neoleaderboard;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

public class NationEntry {
	private UUID uuid;
	private Nation nation;
	private HashMap<NationPointType, Double> nationPoints = new HashMap<NationPointType, Double>();
	private HashMap<PlayerPointType, Double> playerPoints = new HashMap<PlayerPointType, Double>();
	private HashMap<Town, HashMap<PlayerPointType, Double>> townPoints = new HashMap<Town, HashMap<PlayerPointType, Double>>();
	private int numContributors;
	
	public NationEntry(UUID uuid) {
		this(uuid, 0);
	}
	
	public NationEntry(UUID uuid, int numContributors) {
		this.uuid = uuid;
		this.numContributors = numContributors;
		
		this.nation = TownyAPI.getInstance().getNation(uuid);
	}
	
	public void incrementContributors() {
		numContributors++;
	}
	
	public void setNationPoints(double amount, NationPointType type) {
		nationPoints.put(type, amount);
	}
	
	public void setPlayerPoints(double amount, PlayerPointType type) {
		playerPoints.put(type, amount);
	}
	
	public void setTownPoints(double amount, PlayerPointType type, Town town) {
		HashMap<PlayerPointType, Double> tpoints = townPoints.getOrDefault(town, new HashMap<PlayerPointType, Double>());
		tpoints.put(type, amount);
		townPoints.putIfAbsent(town, tpoints);
	}
	
	public void addNationPoints(double amount, NationPointType type) {
		double after = nationPoints.getOrDefault(type, 0D) + amount;
		nationPoints.putIfAbsent(type, after);
	}
	
	public void takeNationPoints(double amount, NationPointType type) {
		addNationPoints(-amount, type);
	}
	
	public void addPlayerPoints(double amount, PlayerPointType type, Town town) {
		double after = playerPoints.getOrDefault(type, 0D) + amount;
		playerPoints.putIfAbsent(type, after);
		addTownPoints(amount, type, town);
	}
	
	public void takePlayerPoints(double amount, PlayerPointType type, Town town) {
		addPlayerPoints(-amount, type, town);
	}
	
	public void clearPoints() {
		nationPoints.clear();
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public int getContributors() {
		return numContributors;
	}
	
	public HashMap<NationPointType, Double> getAllNationPoints() {
		return nationPoints;
	}
	
	public HashMap<PlayerPointType, Double> getAllPlayerPoints() {
		return playerPoints;
	}
	
	public HashMap<Town, HashMap<PlayerPointType, Double>> getAllTownPoints() {
		return townPoints;
	}
	
	public void removePlayer(PlayerPoints ppoints, Town town) {
		for (Entry<PlayerPointType, Double> e : ppoints.getContributedPoints().entrySet()) {
			takeTownPoints(e.getValue(), e.getKey(), town);
		}
	}
	
	public void removeTown(Town town) {
		// If town hasn't contributed any points
		if (!townPoints.containsKey(town)) {
			return;
		}
		
		// Remove all town points from nation entry
		for (Entry<PlayerPointType, Double> e : townPoints.get(town).entrySet()) {
			playerPoints.put(e.getKey(), playerPoints.get(e.getKey()) - e.getValue());
		}
		townPoints.remove(town);
	}
	
	public Nation getNation() {
		return nation;
	}
	
	private void takeTownPoints(double amount, PlayerPointType type, Town town) {
		addTownPoints(-amount, type, town);
	}
	
	private void addTownPoints(double amount, PlayerPointType type, Town town) {
		HashMap<PlayerPointType, Double> tpoints = townPoints.getOrDefault(town, new HashMap<PlayerPointType, Double>());
		tpoints.put(type, tpoints.getOrDefault(type, 0D) + amount);
		townPoints.putIfAbsent(town, tpoints);
	}
}	
