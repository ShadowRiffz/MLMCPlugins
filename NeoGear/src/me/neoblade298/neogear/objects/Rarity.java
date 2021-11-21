package me.neoblade298.neogear.objects;

import org.bukkit.Material;

public class Rarity {
	public String colorCode;
	public String displayName;
	public double priceModifier;
	public Material mat;
	
	public Rarity(String colorCode, String displayName, double priceModifier, String mat) {
		this.colorCode = colorCode.replaceAll("&", "§");
		this.displayName = displayName.replaceAll("&", "§");
		this.priceModifier = priceModifier;
		this.mat = Material.getMaterial(mat);
	}
}
