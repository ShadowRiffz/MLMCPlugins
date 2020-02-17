package me.neoblade298.neocollections;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

public class Attributes {
	private int strength, dexterity, intelligence, spirit, perception, endurance, vitality;

	public Attributes(int strength, int dexterity, int intelligence, int spirit, int perception, int endurance,
			int vitality) {
		this.strength = strength;
		this.dexterity = dexterity;
		this.intelligence = intelligence;
		this.spirit = spirit;
		this.perception = perception;
		this.endurance = endurance;
		this.vitality = vitality;
	}

	public void applyAttributes(Player p) {
		PlayerData data = SkillAPI.getPlayerData(p);
		data.addBonusAttributes("Strength", strength);
		data.addBonusAttributes("Dexterity", dexterity);
		data.addBonusAttributes("Intelligence", intelligence);
		data.addBonusAttributes("Spirit", spirit);
		data.addBonusAttributes("Perception", perception);
		data.addBonusAttributes("Endurance", endurance);
		data.addBonusAttributes("Vitality", vitality);
	}

	public void removeAttributes(Player p) {
		PlayerData data = SkillAPI.getPlayerData(p);
		data.addBonusAttributes("Strength", -strength);
		data.addBonusAttributes("Dexterity", -dexterity);
		data.addBonusAttributes("Intelligence", -intelligence);
		data.addBonusAttributes("Spirit", -spirit);
		data.addBonusAttributes("Perception", -perception);
		data.addBonusAttributes("Endurance", -endurance);
		data.addBonusAttributes("Vitality", -vitality);
	}

	public int getStrength() {
		return strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public int getSpirit() {
		return spirit;
	}

	public int getPerception() {
		return perception;
	}

	public int getEndurance() {
		return endurance;
	}

	public int getVitality() {
		return vitality;
	}

	public String toString() {
		return strength + " " + dexterity + " " + intelligence + " " + spirit + " " + perception + " " + endurance + " "
				+ vitality;
	}

}
