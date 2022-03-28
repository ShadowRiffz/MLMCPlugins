package me.Neoblade298.NeoProfessions.PlayerProfessions;

import java.util.HashMap;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;

public class Profession {
	ProfessionType type;
	int exp, level;
	static HashMap<Integer, Integer> nextLv= new HashMap<Integer, Integer>();
	
	static {
		for (int lv = 1; lv <= 60; lv++) {
			nextLv.put(lv, (15 * lv^2) + (100 * lv));
		}
	}
	
	public Profession(ProfessionType prof) {
		this.type = prof;
		this.exp = 0;
		this.level = 1;
	}

	public ProfessionType getType() {
		return type;
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
		double newExp = exp + this.exp;
		int percent = (int) ((newExp / (double) nextLv.get(this.level)) * 100);
		percent = Math.min(100, percent);
		
		// If next level exists, check that the player can reach it, else just add
		p.sendMessage("�a+" + exp + " �7(�f" + percent + "%�7) �6" + type.getDisplay() + " �7exp");
		boolean levelup = false;
		while (nextLv.containsKey(this.level) && newExp >= nextLv.get(this.level)) {
			newExp -= nextLv.get(this.level);
			this.level++;
			levelup = true;
		}
		if (levelup) {
			p.sendMessage(Professions.lvlupMsg.replaceAll("%level%", "" + this.level).replaceAll("%type%", "" + this.type.getDisplay())
					.replaceAll("%profname%", type.getDisplay()));
			p.playSound(p.getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, SoundCategory.BLOCKS, 1, 1);
		}
		this.exp = (int) newExp;
	}
}
