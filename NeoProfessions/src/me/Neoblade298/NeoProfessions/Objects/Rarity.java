package me.Neoblade298.NeoProfessions.Objects;

import org.bukkit.Material;

public enum Rarity {
	LEGENDARY("§4", Material.RED_DYE, "§4Legendary", 4),
	EPIC("§6", Material.ORANGE_DYE, "§6Epic", 3),
	RARE("§9", Material.LIGHT_BLUE_DYE, "§9Rare", 2),
	UNCOMMON("§a", Material.LIME_DYE, "§aUncommon", 1),
	COMMON("§7", Material.LIGHT_GRAY_DYE, "§7Common", 0);
	
	private final String colorcode;
	private final Material mat;
	private final String display;
	private final int priority;
	private Rarity(final String colorcode, final Material mat, final String display, final int priority) {
		this.colorcode = colorcode;
		this.mat = mat;
		this.display = display;
		this.priority = priority;
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
	
	public int getPriority() {
		return priority;
	}
}
