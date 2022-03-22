package me.Neoblade298.NeoProfessions.PlayerProfessions;

import java.util.HashMap;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;

public class Profession {
	String name;
	String display;
	int exp, level;
	static HashMap<Integer, Integer> nextLv= new HashMap<Integer, Integer>();
	
	static {
		for (int lv = 1; lv <= 60; lv++) {
			nextLv.put(lv, (15 * lv^2) * (100 * lv));
		}
	}
	
	public Profession(String name) {
		this.name = name;
		this.display = StringUtils.capitalize(name);
		this.exp = 0;
		this.level = 1;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public static HashMap<Integer, Integer> getNextLv() {
		return nextLv;
	}

	public static void setNextLv(HashMap<Integer, Integer> nextLv) {
		Profession.nextLv = nextLv;
	}
	
	public void addExp(Player p, int exp) {
		int prevLvl = this.level;
		int newExp = exp + this.exp;
		int displayExp = newExp > nextLv.get(this.level) ? nextLv.get(this.level) : newExp;
		
		// If next level exists, check that the player can reach it, else just add
		p.sendMessage("§a+" + exp + " §7(§f" + displayExp + " / " + nextLv.get(this.level) + "§7) §6" + display + " §7exp");
		boolean levelup = false;
		while (nextLv.containsKey(this.level) && newExp >= nextLv.get(this.level)) {
			newExp -= nextLv.get(this.level);
			this.level++;
			levelup = true;
		}
		if (levelup) {
			p.sendMessage(Professions.lvlupMsg.replaceAll("%level%", "" + this.level).replaceAll("%previous%", "" + prevLvl)
					.replaceAll("%profname%", this.name));
			p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1, 1);
		}
		this.exp = newExp;
	}
}
