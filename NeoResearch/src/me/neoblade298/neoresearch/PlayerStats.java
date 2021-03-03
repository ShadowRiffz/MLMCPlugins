package me.neoblade298.neoresearch;

import java.util.HashMap;
import java.util.TreeSet;

public class PlayerStats {
	private TreeSet<String> completedResearchItems;
	private HashMap<String, Integer> researchPoints;
	private HashMap<String, Integer> mobKills;
	private int exp;
	private int level;
	
	public PlayerStats(int level, int exp, TreeSet<String> completedResearchItems, HashMap<String, Integer> researchPoints, HashMap<String, Integer> mobKills) {
		this.level = level;
		this.exp = exp;
		this.completedResearchItems = completedResearchItems;
		this.researchPoints = researchPoints;
		this.mobKills = mobKills;
	}
	
	public TreeSet<String> getCompletedResearchItems() {
		return completedResearchItems;
	}
	public void setCompletedResearchItems(TreeSet<String> completedResearchItems) {
		this.completedResearchItems = completedResearchItems;
	}
	public HashMap<String, Integer> getResearchPoints() {
		return researchPoints;
	}
	public void setResearchPoints(HashMap<String, Integer> researchPoints) {
		this.researchPoints = researchPoints;
	}
	public HashMap<String, Integer> getMobKills() {
		return mobKills;
	}
	public void setMobKills(HashMap<String, Integer> mobKills) {
		this.mobKills = mobKills;
	}
	public int getExp() {
		return exp;
	}
	public void setExp(int exp) {
		this.exp = exp;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
}
