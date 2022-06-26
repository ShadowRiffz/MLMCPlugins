package me.neoblade298.neoleaderboard.points;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeSet;
import java.util.UUID;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

public class NationEntry implements Comparable<NationEntry> {
	private UUID uuid;
	private Nation nation;
	private HashMap<NationPointType, Double> nationPoints = new HashMap<NationPointType, Double>();
	private HashMap<PlayerPointType, Double> playerPoints = new HashMap<PlayerPointType, Double>();
	private HashMap<UUID, TownEntry> townPoints = new HashMap<UUID, TownEntry>();
	private TreeSet<UUID> topTowns;
	private double totalNationPoints, totalPlayerPoints;
	private int numContributors;
	
	private final Comparator<UUID> townComparer;
	
	public NationEntry(UUID uuid) {
		this(uuid, 0);
	}
	
	public NationEntry(UUID uuid, int numContributors) {
		this.uuid = uuid;
		this.numContributors = numContributors;
		
		this.nation = TownyAPI.getInstance().getNation(uuid);

		townComparer = new Comparator<UUID>() {
			public int compare(UUID o1, UUID o2) {
				return (int) (townPoints.get(o1).getTotalPoints() - townPoints.get(o2).getTotalPoints());
			}
		};
		
		topTowns = new TreeSet<UUID>(townComparer);
	}
	
	public void incrementContributors() {
		numContributors++;
	}
	
	public void setNationPoints(double amount, NationPointType type) {
		nationPoints.put(type, amount);
		totalNationPoints = amount;
	}
	
	public void setPlayerPoints(double amount, PlayerPointType type) {
		playerPoints.put(type, amount);
		totalPlayerPoints = amount;
	}
	
	public void initializeTown(UUID uuid) {
		initializeTown(uuid, 0);
	}
	
	public void initializeTown(UUID uuid, int contributors) {
		townPoints.put(uuid, new TownEntry(uuid, this.getNation().getUUID(), contributors));
	}
	
	public void addTownPoints(double amount, PlayerPointType type, UUID uuid) {
		TownEntry te = townPoints.getOrDefault(uuid, new TownEntry(uuid, this.getNation().getUUID(), 0));
		te.addPlayerPoints(amount, type, uuid);
		townPoints.putIfAbsent(uuid, te);
		
		// Changes to town points means town uuid must be re-sorted
		if (topTowns.contains(uuid)) {
			topTowns.remove(uuid);
		}
		topTowns.add(uuid);
	}
	
	public void addNationPoints(double amount, NationPointType type) {
		double after = nationPoints.getOrDefault(type, 0D) + amount;
		nationPoints.putIfAbsent(type, after);
		totalNationPoints += amount;
	}
	
	public void takeNationPoints(double amount, NationPointType type) {
		addNationPoints(-amount, type);
	}
	
	public void addPlayerPoints(double amount, PlayerPointType type, Town town, UUID player) {
		double after = playerPoints.getOrDefault(type, 0D) + amount;
		playerPoints.putIfAbsent(type, after);
		totalPlayerPoints += amount;
		addTownPoints(amount, type, town, player);
		System.out.println("4");
	}
	
	public void takePlayerPoints(double amount, PlayerPointType type, Town town, UUID player) {
		addPlayerPoints(-amount, type, town, player);
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
	
	public HashMap<UUID, TownEntry> getAllTownPoints() {
		return townPoints;
	}
	
	public void removePlayer(PlayerEntry ppoints, Town town, UUID player) {
		for (Entry<PlayerPointType, Double> e : ppoints.getContributedPoints().entrySet()) {
			takePlayerPoints(e.getValue(), e.getKey(), town, player);
		}
	}
	
	public void removeTown(Town town) {
		// If town hasn't contributed any points
		if (!townPoints.containsKey(town.getUUID())) {
			return;
		}
		
		// Remove all town points from nation entry
		for (Entry<PlayerPointType, Double> e : townPoints.get(town.getUUID()).getPlayerPoints().entrySet()) {
			playerPoints.put(e.getKey(), playerPoints.get(e.getKey()) - e.getValue());
		}
		townPoints.remove(town.getUUID());
	}
	
	public Nation getNation() {
		return nation;
	}
	
	public void takeTownPoints(double amount, PlayerPointType type, Town town, UUID player) {
		addTownPoints(-amount, type, town, player);
	}
	
	public void addTownPoints(double amount, PlayerPointType type, Town town, UUID player) {
		TownEntry te = townPoints.getOrDefault(town, new TownEntry(town.getUUID(), this.getNation().getUUID(), 0));
		te.addPlayerPoints(amount, type, player);
		townPoints.putIfAbsent(town.getUUID(), te);
	}
	
	public double getEffectivePoints() {
		return totalNationPoints + PointsManager.calculateEffectivePoints(this, totalPlayerPoints);
	}

	@Override
	public int compareTo(NationEntry o) {
		return (int) (this.getEffectivePoints() - o.getEffectivePoints());
	}
	
	public TreeSet<UUID> getTopTownOrder() {
		return topTowns;
	}
}	
