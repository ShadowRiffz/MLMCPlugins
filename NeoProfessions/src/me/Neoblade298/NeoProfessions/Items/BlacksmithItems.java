package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Util;
import net.md_5.bungee.api.ChatColor;

public class BlacksmithItems {

	// Durability
	public static ItemStack getDurabilityItem(int level, String itemtype) {
		ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4[Lv " + level + "] §cDurability Gem");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		
		switch(level) {
			case 1:	lore.add("§7Compatibility: Common - §9Rare");
						 	break;
			case 2: lore.add("§7Compatibility: Common - §6Epic");
							break;
			case 3: lore.add("§7Compatibility: Common - §bAngelic");
							break;
			case 4: lore.add("§7Compatibility: Common - §2Mythic");
							break;
			case 5: lore.add("§7Compatibility: Common - §4§lLegendary");
							break;
		}
		lore.add("§7Effect: Increases max durability");
		
		if(itemtype.equals("armor")) {
			int offset = 5 + (25 * (level-1));
			lore.add("§7Potency: §e" + ((int)(Math.random() * 5) * 5 + offset));
		}
		if(itemtype.equals("weapon")) {
			int offset = 5 + (50 * (level-1));
			lore.add("§7Potency: §e" + ((int)(Math.random() * 10) * 5 + offset));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		
		return item;
	}
	
	// Repair
	public static ItemStack getRepairItem(int level) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4[Lv " + level + "] §cRepair Kit");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();

		lore.add("§6Right click to use");
		lore.add("§cOnly works on quest items");
		switch(level) {
			case 1:	lore.add("§7Compatibility: Common - §9Rare");
						 	break;
			case 2: lore.add("§7Compatibility: Common - §6Epic");
							break;
			case 3: lore.add("§7Compatibility: Common - §bAngelic");
							break;
			case 4: lore.add("§7Compatibility: Common - §2Mythic");
							break;
			case 5: lore.add("§7Compatibility: Common - §4§lLegendary");
							break;
		}
		lore.add("§7Effect: Restores durability of an item");
		lore.add("§7Potency: §e" + (20 + (level*5)) + "%");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		
		return item;
	}
	
	public static boolean isRepairItem(ItemStack item) {
		if(!item.hasItemMeta()) {
			return false;
		}
		ItemMeta meta = item.getItemMeta();
		for(String line : meta.getLore()) {
			if(line.contains("Restores durability of an item")) {
				return true;
			}
		}
		return false;
	}
	
	public static int getItemLevel(ItemStack item) {
		return item.getEnchantments().get(Enchantment.DURABILITY);
	}
	
	public static int getItemPotency(ItemStack item) {
		for(String line : item.getItemMeta().getLore()) {
			if(line.contains("Potency")) {
				line = ChatColor.stripColor(line);
				return Integer.parseInt(line.substring(line.indexOf(" ") + 1, line.indexOf("%")));
			}
		}
		return -1;
	}
	
	public static boolean canRepair(ItemStack item) {
		// Check if the item is a quest item
		System.out.println(item.getType().getMaxDurability());
		if(Util.getItemLevel(item) != -1) {
			return true;
		}
		if(item.getType().getMaxDurability() > 0) {
			return true;
		}
		return false;
	}
}
