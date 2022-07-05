package me.neoblade298.neoleaderboard.points;

import java.util.HashMap;
import java.util.TreeSet;

public class PreviousPointsManager {
	private static TreeSet<NationEntry> topNations;
	private static TreeSet<TownEntry> topTowns;
	private static TreeSet<TownEntry> topPlayers;
	private static HashMap<PointType, TreeSet<NationEntry>> topNationCategories;
	private static HashMap<PlayerPointType, TreeSet<TownEntry>> topTownCategories;
	private static HashMap<PlayerPointType, TreeSet<PlayerEntry>> topPlayerCategories;
}
