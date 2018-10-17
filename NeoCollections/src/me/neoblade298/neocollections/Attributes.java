package me.neoblade298.neocollections;

public class Attributes {
	private int strength, dexterity, intelligence, spirit, perception, endurance, vitality;
	
	public Attributes (int strength, int dexterity, int intelligence, int spirit, int perception, int endurance, int vitality) {
		this.strength = strength;
		this.dexterity = dexterity;
		this.intelligence = intelligence;
		this.spirit = spirit;
		this.perception = perception;
		this.endurance = endurance;
		this.vitality = vitality;
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
	
}
