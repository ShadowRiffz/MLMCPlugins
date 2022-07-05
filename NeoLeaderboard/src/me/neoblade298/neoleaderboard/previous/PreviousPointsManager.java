package me.neoblade298.neoleaderboard.previous;

import java.util.HashMap;
import java.util.TreeSet;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neoleaderboard.points.NationEntry;
import me.neoblade298.neoleaderboard.points.PlayerPointType;
import me.neoblade298.neoleaderboard.points.PointType;
import me.neoblade298.neoleaderboard.points.TownEntry;

public class PreviousPointsManager {
	private static TreeSet<NationEntry> topNations;
	private static TreeSet<TownEntry> topTowns;
	private static TreeSet<TownEntry> topPlayers;
	private static HashMap<PointType, TreeSet<NationEntry>> topNationCategories;
	private static HashMap<PlayerPointType, TreeSet<TownEntry>> topTownCategories;
	private static HashMap<PlayerPointType, TreeSet<PlayerEntry>> topPlayerCategories
	
	static {
		new BukkitRunnable() {
			public void run() {
				// Grab the top 10 for everything
				Statement stmt = NeoCore.getStatement();
				Comparator comp = new Comparator<NationEntry>
			}
		}
	}
}
