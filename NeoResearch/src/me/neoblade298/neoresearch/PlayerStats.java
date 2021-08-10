package me.neoblade298.neoresearch;

import java.util.HashMap;
import java.util.TreeSet;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class PlayerStats {
	private Research main;
	private TreeSet<String> completedResearchItems;
	private HashMap<String, Integer> researchPoints;
	private HashMap<String, Integer> mobKills;
	private int exp;
	private int level;
	
	public PlayerStats(Research main, int level, int exp, TreeSet<String> completedResearchItems, HashMap<String, Integer> researchPoints, HashMap<String, Integer> mobKills) {
		this.main = main;
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
	
	public void addExp(Player p, int exp) {
		int prevLvl = this.level;
		int remainingExp = exp + this.exp;
		
		// If next level exists, check that the player can reach it, else just add
		while (main.toNextLvl.containsKey(this.level) && remainingExp >= main.toNextLvl.get(this.level)) {
			remainingExp -= main.toNextLvl.get(this.level);
			this.level++;
			p.sendMessage(main.levelup.replaceAll("%level%", "" + this.level).replaceAll("%previous%", "" + prevLvl));
			p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1, 1);
		}
		this.exp = remainingExp;
	}
	
	public void takeExp(Player p, int exp) {
		int expToTake = exp;
		while (expToTake > 0 || this.level > 5) {
			if (expToTake > this.exp) {
				expToTake -= this.exp;
				this.level--;
				this.exp = main.toNextLvl.get(this.level);
			}
			else {
				this.exp -= expToTake;
				expToTake = 0;
				break;
			}
		}
	}
}
