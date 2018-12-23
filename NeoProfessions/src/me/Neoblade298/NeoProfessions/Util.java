package me.Neoblade298.NeoProfessions;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Util {
	public static void sendMessage(Player p, String m) {
		String message = "&4[&c&lMLMC&4] " + m;
		p.sendMessage(message.replaceAll("&", "§"));
	}

	public static ItemStack setAmount(ItemStack item, int amount) {
		item.setAmount(amount);
		return item;
	}
	
	public static int getItemLevel(ItemStack item) {
		ArrayList<String> lore  = (ArrayList<String>) item.getItemMeta().getLore();
		String tier = null;
		
		// Find the tier string
		for(String line : lore) {
			if(line.contains("Tier:")) {
				tier = line;
				break;
			}
		}
		
		if (tier != null) {
			if(tier.contains("Common") ||
					tier.contains("Uncommon") ||
					tier.contains("Rare")) {
				return 1;
			}
			else if(tier.contains("Unique") ||
					tier.contains("Epic")) {
				return 2;
			}
			else if(tier.contains("Angelic")) {
				return 3;
			}
			else if(tier.contains("Mythic")) {
				return 4;
			}
			else if(tier.contains("Legendary")) {
				return 5;
			}
		}
		return -1;
	}
	
	public static boolean isWeapon(ItemStack item) {
		Material mat = item.getType();
		return
				mat.equals(Material.WOOD_SWORD) ||
				mat.equals(Material.STONE_SWORD) ||
				mat.equals(Material.GOLD_SWORD) ||
				mat.equals(Material.IRON_SWORD) ||
				mat.equals(Material.DIAMOND_SWORD) ||
				mat.equals(Material.WOOD_AXE) ||
				mat.equals(Material.STONE_AXE) ||
				mat.equals(Material.GOLD_AXE) ||
				mat.equals(Material.IRON_AXE) ||
				mat.equals(Material.DIAMOND_AXE) ||
				mat.equals(Material.WOOD_HOE) ||
				mat.equals(Material.STONE_HOE) ||
				mat.equals(Material.GOLD_HOE) ||
				mat.equals(Material.IRON_HOE) ||
				mat.equals(Material.DIAMOND_HOE) ||
				mat.equals(Material.SHIELD) ||
				mat.equals(Material.BOW);
	}

	
	public static boolean isArmor(ItemStack item) {
		Material mat = item.getType();
		return
				mat.equals(Material.LEATHER_HELMET) ||
				mat.equals(Material.GOLD_HELMET) ||
				mat.equals(Material.IRON_HELMET) ||
				mat.equals(Material.DIAMOND_HELMET) ||
				mat.equals(Material.LEATHER_CHESTPLATE) ||
				mat.equals(Material.GOLD_CHESTPLATE) ||
				mat.equals(Material.IRON_CHESTPLATE) ||
				mat.equals(Material.DIAMOND_CHESTPLATE) ||
				mat.equals(Material.LEATHER_LEGGINGS) ||
				mat.equals(Material.GOLD_LEGGINGS) ||
				mat.equals(Material.IRON_LEGGINGS) ||
				mat.equals(Material.DIAMOND_LEGGINGS) ||
				mat.equals(Material.LEATHER_BOOTS) ||
				mat.equals(Material.GOLD_BOOTS) ||
				mat.equals(Material.IRON_BOOTS) ||
				mat.equals(Material.DIAMOND_BOOTS);
	}
}
