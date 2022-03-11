package me.Neoblade298.NeoProfessions.Objects;

import org.bukkit.Material;

public enum Rarity {
	LEGENDARY("§4", Material.RED_DYE, "§4Legendary"),
	EPIC("§6", Material.ORANGE_DYE, "§6Epic"),
	RARE("§9", Material.LIGHT_BLUE_DYE, "§9Rare"),
	UNCOMMON("§a", Material.LIME_DYE, "§aUncommon"),
	COMMON("§7", Material.LIGHT_GRAY_DYE, "§7Common");
	
	private final String colorcode;
	private final Material mat;
	private final String display;
	private Rarity(final String colorcode, final Material mat, final String display) {
		this.colorcode = colorcode;
		this.mat = mat;
		this.display = display;
	}
	
	public String getCode() {
		return colorcode;
	}
	
	public Material getMaterial() {
		return mat;
	}
	
	public String getDisplay() {
		return display;
	}
}
