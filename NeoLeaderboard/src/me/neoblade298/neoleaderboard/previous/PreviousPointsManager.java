package me.neoblade298.neoleaderboard.previous;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Comparator;
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
	private static TreeSet<PreviousEntry> topNations;
	private static TreeSet<PreviousEntry> topTowns;
	private static TreeSet<PreviousEntry> topPlayers;
	private static HashMap<PointType, TreeSet<PreviousEntry>> topNationCategories;
	private static HashMap<PlayerPointType, TreeSet<PreviousEntry>> topTownCategories;
	private static HashMap<PlayerPointType, TreeSet<PreviousEntry>> topPlayerCategories;
	
	static {
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

					for (NationPointType type : NationPointType.values()) {
						
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoLeaderboard.inst());
	}
}
