package me.neoblade298.neogear.objects;

import java.util.ArrayList;

public class RarityBonuses {
	public Attributes attributes;
	public int duraBonus;
	public ArrayList<String> prefixes;
	
	public RarityBonuses(Attributes attributes, int duraBonus, ArrayList<String> prefixes) {
		this.attributes = attributes;
		this.duraBonus = duraBonus;
		this.prefixes = prefixes;
	}
	
	public RarityBonuses() {
		this.attributes = new Attributes();
		this.duraBonus = 0;
	}
}
