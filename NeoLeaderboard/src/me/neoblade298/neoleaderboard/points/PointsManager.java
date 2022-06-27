package me.neoblade298.neoleaderboard.points;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neoleaderboard.NeoLeaderboard;

public class PointsManager implements IOComponent {
	private static HashMap<UUID, PlayerEntry> playerPoints = new HashMap<UUID, PlayerEntry>();
	private static HashMap<UUID, NationEntry> nationEntries = new HashMap<UUID, NationEntry>();
	private static HashMap<UUID, Long> lastSaved = new HashMap<UUID, Long>();
	private static final double MAX_PLAYER_CONTRIBUTION = 1000;
	
	public static void initialize() {
		// Ground rules:
		// PlayerPoints only exists once a player gets their first points, but it is created even if the player gets them while offline
		// PlayerPoints does not persist on logout
		// Nationent exists on startup, listens to nation create and delete
		// Nationent only increments numContributors if PlayerPoints was 0
		
		new BukkitRunnable() {
			public void run() {
				try {
					// Initialize all nations
					Statement stmt = NeoCore.getStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM leaderboard_nations");
					while (rs.next()) {
						UUID uuid = UUID.fromString(rs.getString(1));
						nationEntries.put(uuid, new NationEntry(uuid, rs.getInt(3)));
					}
					
					// If any nations exist that weren't loaded in, load them
					for (Nation n : TownyUniverse.getInstance().getNations()) {
						nationEntries.putIfAbsent(n.getUUID(), new NationEntry(n.getUUID()));
					}
					
					// Initialize all towns
					rs = stmt.executeQuery("SELECT * FROM leaderboard_towns");
					while (rs.next()) {
						UUID uuid = UUID.fromString(rs.getString(1));
						UUID nation = UUID.fromString(rs.getString(2));
						nationEntries.get(nation).initializeTown(uuid, rs.getInt(3));
					}
					
					// Set items for nation entries
					for (NationEntry n : nationEntries.values()) {
						rs = stmt.executeQuery("SELECT * FROM leaderboard_nationpoints WHERE uuid = '" + "';");
						while (rs.next()) {
							n.setNationPoints(rs.getDouble(3), NationPointType.valueOf(rs.getString(2)));
						}

						rs = stmt.executeQuery("SELECT * FROM leaderboard_nationplayerpoints WHERE uuid = '" + "';");
						while (rs.next()) {
							n.setPlayerPoints(rs.getDouble(3), PlayerPointType.valueOf(rs.getString(2)));
						}

						rs = stmt.executeQuery("SELECT * FROM leaderboard_townpoints WHERE nation_uuid = '" + "';");
						while (rs.next()) {
							UUID uuid = UUID.fromString(rs.getString(1));
							n.initializeTownPoints(rs.getDouble(5), PlayerPointType.valueOf(rs.getString(4)), uuid);
						}
					}
				}
				catch (Exception e) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoLeaderboard] Failed to initialize nationwide points");
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}
	
	public static void handleLeaveTown(Nation n, Town town, Resident r) {
		if (!playerPoints.containsKey(r.getUUID())) return;
		
		try {
			Statement delete = NeoCore.getStatement();
			clearPlayer(r.getUUID(), delete);
			playerPoints.remove(r.getUUID()).clearPoints(delete);
			delete.executeBatch();
		}
		catch (Exception e) {
			Bukkit.getLogger().warning("[NeoLeaderboard] Failed to handle player " + r.getName() + " leaving nation");
			e.printStackTrace();
		}
	}
	
	public static void handleLeaveNation(Nation n, Town town) {
		new BukkitRunnable() {
			public void run() {
				try {	
					// Remove points from nation, this should also remove towns
					nationEntries.get(n.getUUID()).removeTown(town);
						
					// Clear players themselves	
					Statement delete = NeoCore.getStatement();	
					for (Resident res : town.getResidents()) {	
						clearPlayer(res.getUUID(), delete);
					}
					delete.executeBatch();
				}
				catch (Exception e) {
					Bukkit.getLogger().warning("[NeoLeaderboard] Failed to handle town " + town.getName() + " leaving nation");
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}
	
	public static void clearPlayer(UUID uuid, Statement delete) throws SQLException {
		// Player is online
		if (playerPoints.containsKey(uuid)) {
			playerPoints.remove(uuid).clearPoints(delete); // This adds batch
		}
		// Player is offline
		else {
			delete.addBatch("DELETE FROM leaderboard_playerpoints WHERE uuid = '" + uuid + "';");
			delete.addBatch("DELETE FROM leaderboard_contributed WHERE uuid = '" + uuid + "';");
		}
	}
	
	public static void takePlayerPoints(UUID uuid, double amount, PlayerPointType type) {
		addPlayerPoints(uuid, -amount, type);
	}
	
	public static void addPlayerPoints(UUID uuid, double amount, PlayerPointType type) {
		addPlayerPoints(uuid, amount, type, Bukkit.getPlayer(uuid) != null);
	}
	
	public static void addPlayerPoints(UUID uuid, double amount, PlayerPointType type, boolean online) {
		new BukkitRunnable() {
			public void run() {
				TownyAPI api = TownyAPI.getInstance();
				Resident r = api.getResident(uuid);
				Nation n = api.getResidentNationOrNull(r);
				Town t = api.getResidentTownOrNull(r);
				double contributable  = 0;
				if (n == null) return;

				NationEntry nent = nationEntries.get(n.getUUID());
				PlayerEntry ppoints = playerPoints.get(uuid);
				
				if (online) {
					if (ppoints == null) {
						ppoints = new PlayerEntry(uuid);
						nent.incrementContributors();
						playerPoints.put(uuid, ppoints);
					}
					contributable = ppoints.addPoints(amount, type);
					nent.addPlayerPoints(contributable, type, t, uuid);
					
				}
				else {
					try {
						Statement stmt = NeoCore.getStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM leaderboard_playerpoints WHERE uuid = '" + uuid + "';");
						
						// If this was a player's first points
						if (!rs.next()) {
							nent.incrementContributors();
						}
						else {	
							// Simply load in the player and save them after	
							ppoints = loadPlayerPoints(uuid, stmt);	
							contributable = ppoints.addPoints(amount, type);	
							savePlayerData(uuid, stmt);
						}
						nent.addPlayerPoints(amount, type, t, uuid);
					}
					catch (Exception e) {
						Bukkit.getLogger().warning("[NeoLeaderboard] Failed to give points to offline player " + uuid + " for type " + type + ", amount " + amount);
					}
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}
	
	public static void addNationPoints(UUID uuid, double amount, NationPointType type) {
		nationEntries.get(uuid).addNationPoints(amount, type);
	}
	
	private void saveNation(Nation n, Statement insert) {
		new BukkitRunnable() {
			public void run() {
				// Don't save same nation more than once every 10 seconds
				NationEntry nent = nationEntries.getOrDefault(n.getUUID(), new NationEntry(n.getUUID()));
				if (lastSaved.getOrDefault(nent.getUuid(), 0L) + 10000L > System.currentTimeMillis()) {
					return;
				}
				lastSaved.put(nent.getUuid(), System.currentTimeMillis());
				
				try {
					HashMap<NationPointType, Double> points = nent.getAllNationPoints();
					HashMap<PlayerPointType, Double> ppoints = nent.getAllPlayerPoints();
					HashMap<UUID, TownEntry> tpoints = nent.getAllTownPoints();

					insert.addBatch("REPLACE INTO leaderboard_nations VALUES ('"
										+ nent.getUuid() + "','" + n.getName() + "'," + nent.getContributors() + ");");
					for (Entry<NationPointType, Double> e : points.entrySet()) {
						insert.addBatch("REPLACE INTO leaderboard_nationpoints VALUES ('"
											+ nent.getUuid() + "','" + e.getKey() + "'," + e.getValue() + ");");
					}
					for (Entry<PlayerPointType, Double> e : ppoints.entrySet()) {
						insert.addBatch("REPLACE INTO leaderboard_playerpoints VALUES ('"
											+ nent.getUuid() + "','" + e.getKey() + "'," + e.getValue() + ");");
					}
					for (UUID uuid : tpoints.keySet()) {
						String name = TownyUniverse.getInstance().getTown(uuid).getName();
						for (Entry<PlayerPointType, Double> e : tpoints.get(uuid).getPlayerPoints().entrySet()) {
							insert.addBatch("REPLACE INTO leaderboard_townpoints VALUES ('"
									+ uuid + "','" + name + "','" + e.getKey() + "'," + e.getValue() + ");");
						}
					}
				}
				catch (Exception e) {
					Bukkit.getLogger().warning("[NeoLeaderboard] Failed to save nation " + n.getName() + " on cleanup.");
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}

	@Override
	public void cleanup(Statement insert, Statement delete) {
		TownyUniverse tu = TownyUniverse.getInstance();
		for (NationEntry nent : nationEntries.values()) {
			saveNation(tu.getNation(nent.getUuid()), insert);
		}
	}

	@Override
	public String getKey() {
		return "PointsManager";
	}

	@Override
	public void loadPlayer(Player arg0, Statement arg1) {}

	@Override
	public void preloadPlayer(OfflinePlayer p, Statement stmt) {
		try {
			playerPoints.put(p.getUniqueId(), loadPlayerPoints(p.getUniqueId(), stmt));
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoLeaderboard] Failed to load points for player " + p.getName());
			e.printStackTrace();
		}
	}
	
	public static PlayerEntry loadPlayerPoints(UUID uuid, Statement stmt) throws SQLException {
		PlayerEntry ppoints = new PlayerEntry(uuid);
		ResultSet rs = stmt.executeQuery("SELECT * FROM leaderboard_players WHERE uuid = '" + uuid + "';");
		while (rs.next()) {
			UUID town = UUID.fromString(rs.getString(2));
			ppoints.setTown(town);
		}
		
		if (ppoints.getTown() == null) {
			return ppoints; // No sql entry exists, return without loading more
		}
		
		rs = stmt.executeQuery("SELECT * FROM leaderboard_playerpoints WHERE uuid = '" + uuid + "';");
		while (rs.next()) {
			ppoints.setPoints(rs.getDouble(2), PlayerPointType.valueOf(rs.getString(3)));
		}
		rs = stmt.executeQuery("SELECT * FROM leaderboard_contributed WHERE uuid = '" + uuid + "';");
		while (rs.next()) {
			ppoints.setContributedPoints(rs.getDouble(2), PlayerPointType.valueOf(rs.getString(3)));
		}
		ppoints.calculateContributed();
		return ppoints;
	}

	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {
		if (!playerPoints.containsKey(p.getUniqueId())) {
			return;
		}
		try {
			savePlayerData(p.getUniqueId(), insert);
			
			// Also save the nation so we don't get issues if server crash
			TownyAPI api = TownyAPI.getInstance();
			Resident r = api.getResident(p);
			Nation n = api.getResidentNationOrNull(r);
			if (n != null) {
				saveNation(n, insert); // Limited to once every 10 seconds per nation
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().warning("[NeoLeaderboard] Failed to save player " + p.getName() + " on cleanup.");
			e.printStackTrace();
		}
		finally {
			playerPoints.remove(p.getUniqueId());
		}
	}
	
	private static void savePlayerData(UUID uuid, Statement insert) throws SQLException {
		PlayerEntry ppoints = playerPoints.get(uuid);
		for (Entry<PlayerPointType, Double> e : ppoints.getTotalPoints().entrySet()) {
			insert.addBatch("REPLACE INTO leaderboard_playerpoints VALUES ('"
								+ ppoints.getUuid() + "','" + e.getKey() + "'," + e.getValue() + ");");
		}
		for (Entry<PlayerPointType, Double> e : ppoints.getContributedPoints().entrySet()) {
			insert.addBatch("REPLACE INTO leaderboard_contributed VALUES ('"
								+ ppoints.getUuid() + "','" + e.getKey() + "'," + e.getValue() + ");");
		}
	}
	
	public static Collection<NationEntry> getNationEntries() {
		return nationEntries.values();
	}
	
	public static NationEntry getNationEntry(UUID uuid) {
		return nationEntries.get(uuid);
	}
	
	public static PlayerEntry getPlayerEntry(UUID uuid) {
		return playerPoints.get(uuid);
	}
	
	public static double getMaxContribution() {
		return MAX_PLAYER_CONTRIBUTION;
	}
	
	public static double calculateEffectivePoints(NationEntry ne, double total) {
		return total / ne.getContributors();
	}
}
