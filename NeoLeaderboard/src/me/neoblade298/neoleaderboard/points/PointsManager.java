package me.neoblade298.neoleaderboard.points;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeSet;
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
import me.neoblade298.neocore.bungee.BungeeAPI;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neocore.io.IOType;
import me.neoblade298.neoleaderboard.NeoLeaderboard;

public class PointsManager implements IOComponent {
	private static HashMap<UUID, PlayerEntry> playerEntries = new HashMap<UUID, PlayerEntry>();
	private static HashMap<UUID, NationEntry> nationEntries = new HashMap<UUID, NationEntry>();
	private static HashMap<UUID, Long> lastSaved = new HashMap<UUID, Long>();
	private static final double MAX_PLAYER_CONTRIBUTION = 1000;
	private static final DecimalFormat df = new DecimalFormat("##.00");
	
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
						nationEntries.get(nation).initializeTown(uuid, rs.getInt(4));
					}
					
					// Set items for nation entries
					for (NationEntry n : nationEntries.values()) {
						rs = stmt.executeQuery("SELECT * FROM leaderboard_nationpoints WHERE uuid = '" + "';");
						while (rs.next()) {
							String type = rs.getString(2);
							NationPointType ntype = NationPointType.valueOf(type);
							if (ntype == null) {
								n.setPlayerPoints(rs.getDouble(3), PlayerPointType.valueOf(type));
							}
							else {
								n.setNationPoints(rs.getDouble(3), ntype);
							}
						}

						rs = stmt.executeQuery("SELECT * FROM leaderboard_townpoints WHERE nation_uuid = '" + "';");
						while (rs.next()) {
							UUID uuid = UUID.fromString(rs.getString(1));
							n.initializeTownPoints(rs.getDouble(5), PlayerPointType.valueOf(rs.getString(4)), uuid);
						}
					}
				}
				catch (Exception e) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoLeaderboard] Failed to initialize nations");
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}

	// To delete PlayerEntry, must clear town and pointsmanager playerentries
	// To delete NationEntry, just remove from pointsmanager
	// To delete TownEntry, must clear NationEntry and PlayerEntries
	public static void deleteNationEntry(UUID nuuid) {
		NationEntry ne = nationEntries.get(nuuid);
		for (TownEntry te : ne.getTopTowns()) {
			deleteTownEntry(te, false);
		}
		
		new BukkitRunnable() {
			public void run() {
				try {
					Statement stmt = NeoCore.getStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM neoleaderboard_players WHERE nation = '" + nuuid + "';");

					while (rs.next()) {
						UUID uuid = UUID.fromString(rs.getString(1));
						stmt.addBatch("DELETE FROM neoleaderboard_playerpoints WHERE uuid = '" + uuid + "';");
						stmt.addBatch("DELETE FROM neoleaderboard_contributed WHERE uuid = '" + uuid + "';");
						stmt.addBatch("DELETE FROM neoleaderboard_player WHERE uuid = '" + uuid + "';");
					}
					stmt.executeBatch();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}
	
	public static void deleteTownEntry(UUID nation, UUID town, boolean deleteFromSql) {
		NationEntry ne = nationEntries.get(nation);
		deleteTownEntry(ne.getTownEntry(town), deleteFromSql);
	}
	
	public static void deleteTownEntry(TownEntry te, boolean deleteFromSql) {
		for (PlayerEntry pe : te.getTopPlayers()) {
			deletePlayerEntry(pe.getUuid(), false);
		}
		
		if (deleteFromSql) {
			new BukkitRunnable() {
				public void run() {
					try {
						Statement stmt = NeoCore.getStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM neoleaderboard_players WHERE town = '" + te.getTown().getUUID() + "';");

						while (rs.next()) {
							UUID uuid = UUID.fromString(rs.getString(1));
							stmt.addBatch("DELETE FROM neoleaderboard_playerpoints WHERE uuid = '" + uuid + "';");
							stmt.addBatch("DELETE FROM neoleaderboard_contributed WHERE uuid = '" + uuid + "';");
							stmt.addBatch("DELETE FROM neoleaderboard_player WHERE uuid = '" + uuid + "';");
						}
						stmt.executeBatch();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.runTaskAsynchronously(NeoLeaderboard.inst());
		}
	}
	
	public static void deletePlayerEntry(UUID player, boolean deleteFromSql) {
		PlayerEntry pe = playerEntries.get(player);
		if (pe != null) {
			pe.clear(); // Clears from any town entries
			playerEntries.remove(pe.getUuid());
		}

		if (deleteFromSql) {
			new BukkitRunnable() {
				public void run() {
					try {
						Statement stmt = NeoCore.getStatement();
						stmt.addBatch("DELETE FROM neoleaderboard_playerpoints WHERE uuid = '" + player + "';");
						stmt.addBatch("DELETE FROM neoleaderboard_contributed WHERE uuid = '" + player + "';");
						stmt.addBatch("DELETE FROM neoleaderboard_player WHERE uuid = '" + player + "';");
						stmt.executeBatch();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.runTaskAsynchronously(NeoLeaderboard.inst());
		}
	}
	
	public static void initializeTownInNation(UUID nation, Town t) {
		NationEntry ne = nationEntries.get(nation);
		ne.initializeTown(t.getUUID());
	}
	
	public static void takePlayerPoints(UUID uuid, double amount, PlayerPointType type) {
		addPlayerPoints(uuid, -amount, type);
	}
	
	public static void addPlayerPoints(UUID uuid, double amount, PlayerPointType type) {
		addPlayerPoints(uuid, amount, type, Bukkit.getPlayer(uuid) != null);
	}
	
	public static void addPlayerPoints(UUID uuid, double amount, PlayerPointType type, boolean online) {
		BukkitRunnable runnable = new BukkitRunnable() {
			public void run() {
				TownyAPI api = TownyAPI.getInstance();
				Resident r = api.getResident(uuid);
				Nation n = api.getResidentNationOrNull(r);
				Town t = api.getResidentTownOrNull(r);
				double contributable  = 0;
				if (n == null) return;

				NationEntry nent = nationEntries.get(n.getUUID());
				PlayerEntry pentry = playerEntries.get(uuid);
				
				if (online) {
					if (pentry == null) {
						pentry = new PlayerEntry(uuid);
						nent.incrementContributors();
						nent.initializeTown(t.getUUID()); // If town is already initialized, this does nothing
						playerEntries.put(uuid, pentry);
					}
					
					contributable = pentry.addPoints(amount, type);
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
						
						// Simply load in the player and save them after	
						pentry = loadPlayerEntry(uuid, stmt);	
						contributable = pentry.addPoints(amount, type);	
						savePlayerData(pentry, stmt);
						nent.addPlayerPoints(amount, type, t, uuid);
						stmt.executeBatch();
					}
					catch (Exception e) {
						Bukkit.getLogger().warning("[NeoLeaderboard] Failed to give points to offline player " + uuid + " for type " + type + ", amount " + amount);
						e.printStackTrace();
					}
				}
			}
		};
		
		if (NeoCore.isPerformingIO(uuid, IOType.PRELOAD)) {
			NeoCore.addPostIORunnable(runnable, IOType.PRELOAD, uuid, true);
		}
		else {
			runnable.runTaskAsynchronously(NeoLeaderboard.inst());
		}
	}
	
	public static void addNationPoints(UUID uuid, double amount, NationPointType type) {
		nationEntries.get(uuid).addNationPoints(amount, type);
	}
	
	private static void saveNation(Nation n, Statement insert) {
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
						insert.addBatch("REPLACE INTO leaderboard_nationpoints VALUES ('"
											+ nent.getUuid() + "','" + e.getKey() + "'," + e.getValue() + ");");
					}
					for (UUID uuid : tpoints.keySet()) {
						String name = TownyUniverse.getInstance().getTown(uuid).getName();
						insert.addBatch("REPLACE INTO leaderboard_towns VALUES ('"
								+ uuid + "','" + nent.getUuid() + "','" + n.getName() + "'," + nent.getContributors() + ");");
						for (Entry<PlayerPointType, Double> e : tpoints.get(uuid).getPlayerPoints().entrySet()) {
							insert.addBatch("REPLACE INTO leaderboard_townpoints VALUES ('"
									+ uuid + "','" + nent.getUuid() + "','" + name + "','" + e.getKey() + "'," + e.getValue() + ");");
						}
					}
					insert.executeBatch();
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
		try {
			insert.executeBatch();
		} catch (SQLException e) {
			Bukkit.getLogger().warning("[NeoLeaderboard] Failed to cleanup nations");
			e.printStackTrace();
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
			PlayerEntry pe = loadPlayerEntry(p.getUniqueId(), stmt);
			if (pe != null) {
				playerEntries.put(p.getUniqueId(), pe);
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoLeaderboard] Failed to load points for player " + p.getName());
			e.printStackTrace();
		}
	}
	
	public static PlayerEntry loadPlayerEntry(UUID uuid, Statement stmt) throws SQLException {
		PlayerEntry ppoints;
		ResultSet rs = stmt.executeQuery("SELECT * FROM leaderboard_players WHERE uuid = '" + uuid + "';");
		// Return null if no points exist, since object doesn't exist until points do
		if (!rs.next()) {
			return null;
		}
		
		ppoints = new PlayerEntry(uuid);
		UUID town = UUID.fromString(rs.getString(3));
		UUID nation = UUID.fromString(rs.getString(4));
		
		if (ppoints.getTown() == null || ppoints.getNation() == null) {
			return null; // town/nation was deleted, return without loading more
		}
		
		// If town/nation was changed, points are reset to 0
		if (!ppoints.getTown().getUUID().equals(town) || !ppoints.getNation().getUUID().equals(nation)) {
			return null;
		}
		
		rs = stmt.executeQuery("SELECT * FROM leaderboard_playerpoints WHERE uuid = '" + uuid + "';");
		while (rs.next()) {
			ppoints.setPoints(rs.getDouble(3), PlayerPointType.valueOf(rs.getString(2)));
		}
		rs = stmt.executeQuery("SELECT * FROM leaderboard_contributed WHERE uuid = '" + uuid + "';");
		while (rs.next()) {
			ppoints.setContributedPoints(rs.getDouble(3), PlayerPointType.valueOf(rs.getString(2)));
		}
		ppoints.calculateContributed();
		return ppoints;
	}
	
	public static void clearPlayerEntry(UUID uuid) {
		playerEntries.remove(uuid);
	}

	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {
		try {
			if (!playerEntries.containsKey(p.getUniqueId())) {
				UUID uuid = p.getUniqueId();
				delete.addBatch("DELETE FROM leaderboard_players WHERE uuid = '" + uuid + "';");
				delete.addBatch("DELETE FROM leaderboard_playerpoints WHERE uuid = '" + uuid + "';");
				delete.addBatch("DELETE FROM leaderboard_contributed WHERE uuid = '" + uuid + "';");
				delete.executeBatch();
				return;
			}
			// Only save if nation exists
			TownyAPI api = TownyAPI.getInstance();
			Resident r = api.getResident(p);
			Nation n = api.getResidentNationOrNull(r);
			if (n == null) return;
			
			savePlayerData(p.getUniqueId(), insert);
			saveNation(n, insert); // Limited to once every 10 seconds per nation
			insert.executeBatch();
		}
		catch (Exception e) {
			Bukkit.getLogger().warning("[NeoLeaderboard] Failed to save player " + p.getName() + " on cleanup.");
			e.printStackTrace();
		}
		finally {
			// Remove references so they don't show up in things like town top players
			if (playerEntries.containsKey(p.getUniqueId())) {
				playerEntries.get(p.getUniqueId()).clear();
			}
			
			playerEntries.remove(p.getUniqueId());
		}
	}
	
	private static void savePlayerData(UUID uuid, Statement insert) throws SQLException {
		PlayerEntry pentry = playerEntries.get(uuid);
		savePlayerData(pentry, insert);
	}
	
	private static void savePlayerData(PlayerEntry pentry, Statement insert) throws SQLException {
		insert.addBatch("REPLACE INTO leaderboard_players VALUES ('"
				+ pentry.getUuid() + "','" + pentry.getDisplay() + "','"
				+ pentry.getTown().getUUID() + "','" + pentry.getNation().getUUID() + "');");
		
		for (Entry<PlayerPointType, Double> e : pentry.getTotalPoints().entrySet()) {
			insert.addBatch("REPLACE INTO leaderboard_playerpoints VALUES ('"
								+ pentry.getUuid() + "','" + e.getKey() + "'," + e.getValue() + ");");
		}
		
		for (Entry<PlayerPointType, Double> e : pentry.getContributedPoints().entrySet()) {
			insert.addBatch("REPLACE INTO leaderboard_contributed VALUES ('"
								+ pentry.getUuid() + "','" + e.getKey() + "'," + e.getValue() + ");");
		}
	}
	
	public static Collection<NationEntry> getNationEntries() {
		return nationEntries.values();
	}
	
	public static NationEntry getNationEntry(UUID uuid) {
		return nationEntries.get(uuid);
	}
	
	public static PlayerEntry getPlayerEntry(UUID uuid) {
		return playerEntries.get(uuid);
	}
	
	public static double getMaxContribution() {
		return MAX_PLAYER_CONTRIBUTION;
	}
	
	public static double calculateEffectivePoints(NationEntry ne, double total) {
		return total / ne.getContributors();
	}
	
	public static String formatPoints(double points) {
		return df.format(Math.round(points * 1000) / 1000D);
	}
	
	public static void initializeNation(Nation n) {
		nationEntries.put(n.getUUID(), new NationEntry(n.getUUID()));
	}
	
	public static void saveAll() {
		Statement stmt = NeoCore.getStatement();
		TownyUniverse tu = TownyUniverse.getInstance();
		for (NationEntry nent : nationEntries.values()) {
			saveNation(tu.getNation(nent.getUuid()), stmt);
		}
		for (PlayerEntry pe : playerEntries.values()) {
			try {
				savePlayerData(pe, stmt);
			}
			catch (SQLException e) {
				Bukkit.getLogger().warning("[NeoLeaderboard] Failed to save player " + pe.getDisplay());
				e.printStackTrace();
			}
		}
	}
	
	public static void reset() {
		Comparator<NationEntry> comp = new Comparator<NationEntry>() {
			@Override
			public int compare(NationEntry n1, NationEntry n2) {
				if (n1.getTotalPoints() > n2.getTotalPoints()) {
					return 1;
				}
				else if (n1.getTotalPoints() > n2.getTotalPoints()) {
					return -1;
				}
				else {
					return n2.getNation().getName().compareTo(n1.getNation().getName());
				}
			}
		};
		
		new BukkitRunnable() {
			public void run() {
				TreeSet<NationEntry> sorted = new TreeSet<NationEntry>(comp);
				NationEntry winner = sorted.last();
				Nation n = winner.getNation();
				BungeeAPI.broadcast("&4[&c&lMLMC&4] &7This month's winner for top nation is: &6&l" + winner.getNation().getName() + "&7!");
				saveAll();
				
				Statement stmt = NeoCore.getStatement();
				String prefix = "INSERT INTO leaderboard_previous_nations (SELECT * FROM ";
				String suffix = " ORDER BY points DESC LIMIT 10);";
				
				try {
					stmt.addBatch("INSERT INTO leaderboard_winners VALUES ('" + n.getUUID() + "','" + n.getName() +
							"'," + System.currentTimeMillis() + "," + winner.getEffectivePoints() + "," + winner.getContributors());
					
					// Totals
					stmt.addBatch(prefix + "SELECT uuid, leaderboard_nations.name, 'TOTAL' AS category, SUM(points) AS points FROM leaderboard_nationpoints "
							+ "JOIN leaderboard_nations ON leaderboard_nationpoints.uuid = leaderboard_nations.uuid GROUP BY uuid" + suffix);
					stmt.addBatch(prefix + "SELECT uuid, leaderboard_towns.name, 'TOTAL' AS category, SUM(points) AS points FROM leaderboard_townpoints "
							+ "JOIN leaderboard_towns ON leaderboard_townpoints.uuid = leaderboard_towns.uuid GROUP BY uuid" + suffix);
					stmt.addBatch(prefix + "SELECT uuid, 'TOTAL' AS category, SUM(points) AS points FROM Dev.leaderboard_playerpoints GROUP BY uuid" + suffix);
					
					for (NationPointType type : NationPointType.values()) {
						stmt.addBatch(prefix + "leaderboard_nationpoints WHERE category = '" + type + "'" + suffix);
					}
					for (PlayerPointType type : PlayerPointType.values()) {
						stmt.addBatch(prefix + "leaderboard_nationpoints WHERE category = '" + type + "'" + suffix);
						stmt.addBatch(prefix + "leaderboard_townpoints WHERE category = '" + type + "'" + suffix);
						stmt.addBatch(prefix + "leaderboard_playerpoints WHERE category = '" + type + "'" + suffix);
					}
					stmt.executeBatch();

					playerEntries.clear();
					nationEntries.clear();
				} catch (SQLException e) {
					Bukkit.getLogger().warning("[NeoLeaderboard] Failed to reset leaderboard");
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}
}
