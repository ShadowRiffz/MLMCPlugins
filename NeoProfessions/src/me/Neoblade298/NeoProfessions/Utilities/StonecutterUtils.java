package me.Neoblade298.NeoProfessions.Utilities;

import org.bukkit.inventory.ItemStack;

public class StonecutterUtils {
	
	public boolean isWeaponAttribute(String attr) {
		return (attr.equalsIgnoreCase("Strength") ||
				attr.equalsIgnoreCase("Dexterity") ||
				attr.equalsIgnoreCase("Intelligence") ||
				attr.equalsIgnoreCase("Spirit") ||
				attr.equalsIgnoreCase("Perception"));
	}

	public boolean isArmorAttribute(String attr) {
		return (attr.equalsIgnoreCase("Strength") ||
				attr.equalsIgnoreCase("Dexterity") ||
				attr.equalsIgnoreCase("Intelligence") ||
				attr.equalsIgnoreCase("Spirit") ||
				attr.equalsIgnoreCase("Perception") ||
				attr.equalsIgnoreCase("Endurance") ||
				attr.equalsIgnoreCase("Vitality"));
	}
	
	// r
	public boolean isEssence(ItemStack item) {
		return (item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).contains("Essence"));
	}
	
	public boolean isOre(ItemStack item) {
		return (item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).contains("Ore"));
	}
	
	public String getOreType(ItemStack item) {
		String ore = item.getItemMeta().getLore().get(0).split(" ")[2];
		switch (ore) {
		case "Ruby":
			return "strength";
		case "Amethyst":
			return "dexterity";
		case "Sapphire":
			return "intelligence";
		case "Emerald":
			return "spirit";
		case "Topaz":
			return "perception";
		case "Adamantium":
			return "endurance";
		case "Garnet":
			return "vitality";
		}
		return null;
	}
}
