package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class BlacksmithItems {

	
	// All items go here
	public static ItemStack getDurabilityItem(int level, String itemtype) {
		ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
		ItemMeta meta = item.getItemMeta();
		item.addEnchantment(Enchantment.DURABILITY, 1);
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
		
		return item;
	}
	
	public static ItemStack getDurabilityReq(int level, String itemtype) {
		ItemStack item = new ItemStack(Material.QUARTZ);
		ItemMeta meta = item.getItemMeta();
		switch (level) {
		case 1:	meta.setDisplayName("§4[Lv " + level + "] §cDull Essence");
						break;
		case 2:	meta.setDisplayName("§4[Lv " + level + "] §cMinor Essence");
						break;
		case 3:	meta.setDisplayName("§4[Lv " + level + "] §cPotent Essence");
						break;
		case 4:	meta.setDisplayName("§4[Lv " + level + "] §cSublime Essence");
						break;
		case 5:	meta.setDisplayName("§4[Lv " + level + "] §cPure Essence");
						break;
		}
		item.addEnchantment(Enchantment.DURABILITY, 1);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		lore.add("§7Level " + level);
		lore.add("§7Item used for profession crafting");
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		return item;
	}
}
