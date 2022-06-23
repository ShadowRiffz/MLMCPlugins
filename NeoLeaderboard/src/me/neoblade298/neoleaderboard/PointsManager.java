package me.neoblade298.neoleaderboard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class PointsManager implements IOComponent {
	private static HashMap<UUID, PlayerPoints> playerPoints = new HashMap<UUID, PlayerPoints>();
	private static HashMap<UUID, NationPoints> nationPoints = new HashMap<UUID, NationPoints>();
	private static HashMap<UUID, Long> lastSaved = new HashMap<UUID, Long>();
	
	public PointsManager() {
		
		new BukkitRunnable() {
			public void run() {
				// First load nationwide points
				try {
					Statement stmt = NeoCore.getStatement();
					ResultSet rs = stmt.executeQuery("SELECT * FROM neoleaderboard_points WHERE isNation = 1 AND isNationPoints = 1;");
					while (rs.next()) {
						UUID uuid = UUID.fromString(rs.getString(1));
						NationPoints np = nationPoints.getOrDefault(uuid, new NationPoints(uuid));
						np.addNationPoints(rs.getDouble(3), NationPointType.valueOf(rs.getString(4)));
						nationPoints.putIfAbsent(uuid, np);
					}
					
					rs = stmt.executeQuery("SELECT * FROM neoleaderboard_points WHERE isNation = 1 AND isNationPoints = 0;");
					while (rs.next()) {
						UUID uuid = UUID.fromString(rs.getString(1));
						NationPoints np = nationPoints.getOrDefault(uuid, new NationPoints(uuid));
						np.addPlayerPoints(rs.getDouble(3), PlayerPointType.valueOf(rs.getString(4)));
						nationPoints.putIfAbsent(uuid, np);
					}
				}
				catch (Exception e) {
					Bukkit.getLogger().log(Level.WARNING, "[NeoLeaderboard] Failed to initialize nationwide points");
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}
	
	public static void handleLeaveNation(Town town) {
		for (Resident res : town.getResidents()) {
			UUID uuid = res.getUUID();
			
			// Player is online
			boolean isOnline = Bukkit.getPlayer(uuid) != null;
			if (playerPoints.containsKey(uuid)) {
				playerPoints.remove(uuid);
			}
		}
	}
	
	public static void addPoints(UUID uuid, double amount, PlayerPointType type) {
		addPoints(uuid, amount, type, true);
	}
	
	public static void addPoints(UUID uuid, double amount, PlayerPointType type, boolean online) {
		if (online) {
			PlayerPoints points = playerPoints.getOrDefault(uuid, new PlayerPoints(uuid));
			points.addPoints(amount, type);
			playerPoints.putIfAbsent(uuid, points);
		}
		else {
			if (!playerPoints.containsKey(uuid)) {
				Bukkit.getLogger().warning("[NeoLeaderboard] Failed to give points to uuid " + uuid + " for type " + type + ", amount " + amount);
				return;
			}
			playerPoints.get(uuid).addPoints(amount, type);

			TownyAPI api = TownyAPI.getInstance();
			Resident r = api.getResident(uuid);
			Nation n = api.getResidentNationOrNull(r);
			if (n != null) {
				NationPoints npoints = nationPoints.getOrDefault(n.getUUID(), new NationPoints(n.getUUID()));
				npoints.addPlayerPoints(amount, type);
				nationPoints.putIfAbsent(n.getUUID(), npoints);
			}
		}
	}
	
	private void saveNation(Nation n, Statement insert) {
		saveNation(nationPoints.getOrDefault(n.getUUID(), new NationPoints(n.getUUID())), insert);
	}
	
	private void saveNation(NationPoints npoints, Statement insert) {
		// Don't save same nation more than once every 10 seconds
		if (lastSaved.getOrDefault(npoints.getUuid(), 0L) + 10000L > System.currentTimeMillis()) {
			return;
		}
		lastSaved.put(npoints.getUuid(), System.currentTimeMillis());
		
		try {
			HashMap<NationPointType, Double> points = npoints.getAllNationPoints();
			HashMap<PlayerPointType, Double> ppoints = npoints.getAllPlayerPoints();
			for (Entry<NationPointType, Double> e : points.entrySet()) {
				insert.addBatch("REPLACE INTO neoleaderboard_points VALUES ('"
									+ npoints.getUuid() + "',1,1,'" + e.getKey() + "'," + e.getValue() + ");");
			}

			for (Entry<PlayerPointType, Double> e : ppoints.entrySet()) {
				insert.addBatch("REPLACE INTO neoleaderboard_points VALUES ('"
									+ npoints.getUuid() + "',1,0,'" + e.getKey() + "'," + e.getValue() + ");");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().warning("[NeoLeaderboard] Failed to save nation " + npoints.getDisplay() + " on cleanup.");
			e.printStackTrace();
		}
	}

	@Override
	public void cleanup(Statement insert, Statement delete) {
		for (NationPoints npoints : nationPoints.values()) {
			saveNation(npoints, insert);
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
		if (playerPoints.containsKey(p.getUniqueId())) {
			return;
		}
		
		try {
			PlayerPoints ppoints = new PlayerPoints(p.getUniqueId());
			ResultSet rs = stmt.executeQuery("SELECT * FROM neoleaderboard_points WHERE uuid = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				ppoints.addPoints(rs.getDouble(3), PlayerPointType.valueOf(rs.getString(4)));
			}
			
			if (!ppoints.isEmpty()) {
				playerPoints.put(p.getUniqueId(), ppoints);
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoLeaderboard] Failed to load points for player " + p.getName());
			e.printStackTrace();
		}
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
	}
	
	private void savePlayerData(UUID uuid, Statement insert) throws SQLException {
		PlayerPoints ppoints = playerPoints.get(uuid);
		for (Entry<PlayerPointType, Double> e : ppoints.getAllPoints().entrySet()) {
			insert.addBatch("REPLACE INTO neoleaderboard_points VALUES ('"
								+ ppoints.getUuid() + "',0,'" + e.getKey() + "'," + e.getValue() + ");");
		}
	}
}
