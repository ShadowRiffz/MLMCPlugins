package me.neoblade298.neoleaderboard;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.io.IOComponent;

public class PointsManager implements IOComponent {
	private static HashMap<UUID, PlayerPoints> playerPoints;
	private static HashMap<UUID, NationPoints> nationPoints;
	
	public static void handleLeaveNation(Town town) {
		for (Resident res : town.getResidents()) {
			UUID uuid = res.getUUID();
			
			// Player is online
			boolean isOnline = Bukkit.getPlayer(uuid) != null;
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
			Statement stmt = NeoCore.getStatement();
		}
	}
	
	private void saveNation(NationPoints npoints, Statement insert, Statement delete) {
		try {
			HashMap<NationPointType, Double> points = npoints.getAllPoints();
			insert.addBatch("REPLACE INTO neoleaderboard_accounts VALUES ('"
					+ npoints.getUuid() + "','" + npoints.getDisplay() + "',1);");
			
			for (Entry<NationPointType, Double> e : points.entrySet()) {
				insert.addBatch("REPLACE INTO neoleaderboard_points VALUES ('"
									+ npoints.getUuid() + "','" + e.getKey() + "'," + e.getValue() + ");");
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
			saveNation(npoints, insert, delete);
		}
	}

	@Override
	public String getKey() {
		return "PointsManager";
	}

	@Override
	public void loadPlayer(Player arg0, Statement arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preloadPlayer(OfflinePlayer arg0, Statement arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {
		if (!playerPoints.containsKey(p.getUniqueId())) {
			return;
		}
		PlayerPoints ppoints = playerPoints.get(p.getUniqueId());
		try {
			
			for (Entry<PlayerPointType, Double> e : ppoints.getAllPoints().entrySet()) {
				insert.addBatch("REPLACE INTO neoleaderboard_points VALUES ('"
									+ ppoints.getUuid() + "','" + e.getKey() + "'," + e.getValue() + ");");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().warning("[NeoLeaderboard] Failed to save player " + ppoints.getDisplay() + " on cleanup.");
			e.printStackTrace();
		}
	}
}
