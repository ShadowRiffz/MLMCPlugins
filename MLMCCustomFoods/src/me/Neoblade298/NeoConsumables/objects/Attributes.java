package me.Neoblade298.NeoConsumables.objects;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

public class Attributes {
	private int strength, dexterity, intelligence, spirit, endurance;

	public Attributes() {
		this.strength = 0;
		this.dexterity = 0;
		this.intelligence = 0;
		this.spirit = 0;
		this.endurance = 0;
	}
	
	public Attributes clone() {
		Attributes newAttr = new Attributes();
		newAttr.addAttribute(this);
		return newAttr;
	}

	public void applyAttributes(Player p) {
		PlayerData data = SkillAPI.getPlayerData(p);
		data.addBonusAttributes("Strength", strength);
		data.addBonusAttributes("Dexterity", dexterity);
		data.addBonusAttributes("Intelligence", intelligence);
		data.addBonusAttributes("Spirit", spirit);
		data.addBonusAttributes("Endurance", endurance);
	}

	public void removeAttributes(Player p) {
		PlayerData data = SkillAPI.getPlayerData(p);
		data.addBonusAttributes("Strength", -strength);
		data.addBonusAttributes("Dexterity", -dexterity);
		data.addBonusAttributes("Intelligence", -intelligence);
		data.addBonusAttributes("Spirit", -spirit);
		data.addBonusAttributes("Endurance", -endurance);
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

	public int getEndurance() {
		return endurance;
	}
	
	public void setAttribute(String attr, int num) {
		switch (attr) {
			case "str": strength = num; break;
			case "dex": dexterity = num; break;
			case "int": intelligence = num; break;
			case "spr": spirit = num; break;
			case "end": endurance = num; break;
		}
	}
	
	public void addAttribute(String attr, int num) {
		switch (attr) {
			case "str": strength += num; break;
			case "dex": dexterity += num; break;
			case "int": intelligence += num; break;
			case "spr": spirit += num; break;
			case "end": endurance += num; break;
		}
	}
	
	public void addAttribute(Attributes added) {
		strength += added.getStrength();
		dexterity += added.getDexterity();
		intelligence += added.getIntelligence();
		spirit += added.getSpirit();
		endurance += added.getEndurance();
	}
	
	public void resetAttributes() {
		strength = 0;
		dexterity = 0;
		intelligence = 0;
		spirit = 0;
		endurance = 0;
	}
	
	public void multiply(double multiplier) {
		strength *= multiplier;
		dexterity *= multiplier;
		intelligence *= multiplier;
		spirit *= multiplier;
		endurance *= multiplier;
	}

	public String toString() {
		return strength + " " + dexterity + " " + intelligence + " " + spirit + " " + endurance;
	}
	
	public boolean isEmpty() {
		return strength == 0 && dexterity == 0 && intelligence == 0 && spirit == 0 &&
				endurance == 0;
	}

}
