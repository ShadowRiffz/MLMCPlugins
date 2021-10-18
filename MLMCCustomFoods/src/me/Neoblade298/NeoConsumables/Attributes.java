package me.Neoblade298.NeoConsumables;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

public class Attributes {
	private int strength, dexterity, intelligence, spirit, perception, endurance, vitality;

	public Attributes() {
		this.strength = 0;
		this.dexterity = 0;
		this.intelligence = 0;
		this.spirit = 0;
		this.perception = 0;
		this.endurance = 0;
		this.vitality = 0;
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
	
	public void setAttribute(String attr, int num) {
		switch (attr) {
			case "str": strength = num; break;
			case "dex": dexterity = num; break;
			case "int": intelligence = num; break;
			case "spr": spirit = num; break;
			case "prc": perception = num; break;
			case "vit": vitality = num; break;
			case "end": endurance = num; break;
		}
	}
	
	public void addAttribute(String attr, int num) {
		switch (attr) {
			case "str": strength += num; break;
			case "dex": dexterity += num; break;
			case "int": intelligence += num; break;
			case "spr": spirit += num; break;
			case "prc": perception += num; break;
			case "vit": vitality += num; break;
			case "end": endurance += num; break;
		}
	}
	
	public void addAttribute(Attributes added) {
		strength += added.getStrength();
		dexterity += added.getDexterity();
		intelligence += added.getIntelligence();
		spirit += added.getSpirit();
		perception += added.getPerception();
		vitality += added.getVitality();
		endurance += added.getEndurance();
	}
	
	public void resetAttributes() {
		strength = 0;
		dexterity = 0;
		intelligence = 0;
		spirit = 0;
		perception = 0;
		vitality = 0;
		endurance = 0;
	}

	public String toString() {
		return strength + " " + dexterity + " " + intelligence + " " + spirit + " " + perception + " " + endurance + " "
				+ vitality;
	}
	
	public boolean isEmpty() {
		return strength == 0 && dexterity == 0 && intelligence == 0 && spirit == 0 &&
				perception == 0 && vitality == 0 && endurance == 0;
	}

}
