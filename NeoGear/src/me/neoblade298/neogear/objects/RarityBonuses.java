package me.neoblade298.neogear.objects;

import java.util.ArrayList;

import org.bukkit.Material;

public class RarityBonuses {
	public Attributes attributes;
	public int duraBonus;
	public ArrayList<String> prefixes;
	public Material material;
	
	public RarityBonuses(Attributes attributes, int duraBonus, ArrayList<String> prefixes, String material) {
		this.attributes = attributes;
		this.duraBonus = duraBonus;
		this.prefixes = prefixes;
		this.material = Material.getMaterial(material);
	}
	
	public RarityBonuses() {
		this.attributes = new Attributes();
		this.duraBonus = 0;
		this.prefixes = null;
		this.material = null;
	}
}
