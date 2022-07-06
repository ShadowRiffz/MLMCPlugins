package me.neoblade298.neoleaderboard.previous;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neoleaderboard.NeoLeaderboard;
import me.neoblade298.neoleaderboard.points.NationPointType;
import me.neoblade298.neoleaderboard.points.PlayerPointType;
import me.neoblade298.neoleaderboard.points.PointType;

public class PreviousPointsManager {
	private static TreeSet<PreviousEntry> topNations = new TreeSet<PreviousEntry>();
	private static TreeSet<PreviousEntry> topTowns = new TreeSet<PreviousEntry>();
	private static TreeSet<PreviousEntry> topPlayers = new TreeSet<PreviousEntry>();
	private static HashMap<PointType, TreeSet<PreviousEntry>> topNationCategories = new HashMap<PointType, TreeSet<PreviousEntry>>();
	private static HashMap<PlayerPointType, TreeSet<PreviousEntry>> topTownCategories = new HashMap<PlayerPointType, TreeSet<PreviousEntry>>();
	private static HashMap<PlayerPointType, TreeSet<PreviousEntry>> topPlayerCategories = new HashMap<PlayerPointType, TreeSet<PreviousEntry>>();
	
	public static void reload() {
		topNations.clear();
		topTowns.clear();
		topPlayers.clear();
		topNationCategories.clear();
		topTownCategories.clear();
		topPlayerCategories.clear();
		new BukkitRunnable() {
			public void run() {
				// Grab the top 10 for everything
				Statement stmt = NeoCore.getStatement();
				try {
					ResultSet rs = stmt.executeQuery("SELECT * FROM leaderboard_previous_nations WHERE category = 'TOTAL';");
					while (rs.next()) {
						UUID uuid = UUID.fromString(rs.getString(1));
						topNations.add(new PreviousEntry(uuid, rs.getString(2), PointType.getPointType(rs.getString(3)), rs.getDouble(4)));
					}

					rs = stmt.executeQuery("SELECT * FROM leaderboard_previous_towns WHERE category = 'TOTAL';");
					while (rs.next()) {
						UUID uuid = UUID.fromString(rs.getString(1));
						topTowns.add(new PreviousEntry(uuid, rs.getString(2), PointType.getPointType(rs.getString(3)), rs.getDouble(4)));
					}

					rs = stmt.executeQuery("SELECT * FROM leaderboard_previous_players WHERE category = 'TOTAL';");
					while (rs.next()) {
						UUID uuid = UUID.fromString(rs.getString(1));
						topPlayers.add(new PreviousEntry(uuid, PointType.getPointType(rs.getString(2)), rs.getDouble(3)));
					}
					
					for (NationPointType type : NationPointType.values()) {
						topNationCategories.put(type, new TreeSet<PreviousEntry>());
						rs = stmt.executeQuery("SELECT * FROM leaderboard_previous_nations WHERE category = '" + type + "';");
						while (rs.next()) {
							UUID uuid = UUID.fromString(rs.getString(1));
							topNationCategories.get(type).add(new PreviousEntry(uuid, rs.getString(2), PointType.getPointType(rs.getString(3)), rs.getDouble(4)));
						}
					}
					
					for (PlayerPointType type : PlayerPointType.values()) {
						topNationCategories.put(type, new TreeSet<PreviousEntry>());
						topTownCategories.put(type, new TreeSet<PreviousEntry>());
						topPlayerCategories.put(type, new TreeSet<PreviousEntry>());
						
						rs = stmt.executeQuery("SELECT * FROM leaderboard_previous_nations WHERE category = '" + type + "';");
						while (rs.next()) {
							UUID uuid = UUID.fromString(rs.getString(1));
							topNationCategories.get(type).add(new PreviousEntry(uuid, rs.getString(2), PointType.getPointType(rs.getString(3)), rs.getDouble(4)));
						}
						
						rs = stmt.executeQuery("SELECT * FROM leaderboard_previous_towns WHERE category = '" + type + "';");
						while (rs.next()) {
							UUID uuid = UUID.fromString(rs.getString(1));
							topNationCategories.get(type).add(new PreviousEntry(uuid, rs.getString(2), PointType.getPointType(rs.getString(3)), rs.getDouble(4)));
						}

						rs = stmt.executeQuery("SELECT * FROM leaderboard_previous_players WHERE category = '" + type + "';");
						while (rs.next()) {
							UUID uuid = UUID.fromString(rs.getString(1));
							topNationCategories.get(type).add(new PreviousEntry(uuid, PointType.getPointType(rs.getString(2)), rs.getDouble(3)));
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}

	public static TreeSet<PreviousEntry> getTopNations() {
		return topNations;
	}

	public static TreeSet<PreviousEntry> getTopTowns() {
		return topTowns;
	}

	public static TreeSet<PreviousEntry> getTopPlayers() {
		return topPlayers;
	}

	public static HashMap<PointType, TreeSet<PreviousEntry>> getTopNationCategories() {
		return topNationCategories;
	}

	public static HashMap<PlayerPointType, TreeSet<PreviousEntry>> getTopTownCategories() {
		return topTownCategories;
	}

	public static HashMap<PlayerPointType, TreeSet<PreviousEntry>> getTopPlayerCategories() {
		return topPlayerCategories;
	}
}
