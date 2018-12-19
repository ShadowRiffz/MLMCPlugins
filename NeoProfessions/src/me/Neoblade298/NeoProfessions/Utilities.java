package me.Neoblade298.NeoProfessions;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utilities {
	public static void sendMessage(Player p, String m) {
		p.sendMessage(m.replaceAll("&", "§"));
	}
	
	public static void createItem(Player p, String profession, String item, String itemtype, int level) {
		int slot = p.getInventory().firstEmpty();
		if(slot == -1) {
			Utilities.sendMessage(p, "&cYour inventory is full!");
		}
		else {
			if (p.hasPermission("neoprofessions." + profession + "." + item + "." + level)) {
				// Give max durability shard from mythicmobs
				// mythicmobs items give -s item + level
			}
			else {
				Utilities.sendMessage(p, "&cInsufficient permissions!");
			}
		}
	}
	
	public static void createItem(Player p, String profession, String item, int level) {
		int slot = p.getInventory().firstEmpty();
		if(slot == -1) {
			Utilities.sendMessage(p, "&cYour inventory is full!");
		}
		else {
			if (p.hasPermission("neoprofessions." + profession + "." + item + "." + level)) {
				// Give max durability shard from mythicmobs
				// mythicmobs items give -s item + level
			}
			else {
				Utilities.sendMessage(p, "&cInsufficient permissions!");
			}
		}
	}
	
	public static void createItem(Player p, String profession, String item, String type) {
		int slot = p.getInventory().firstEmpty();
		if(slot == -1) {
			Utilities.sendMessage(p, "&cYour inventory is full!");
		}
		else {
			if (p.hasPermission("neoprofessions." + profession + "." + item + "." + type)) {
				// Give charm
				// mythicmobs items give -s item + type
			}
			else {
				Utilities.sendMessage(p, "&cInsufficient permissions!");
			}
		}
	}
	
	// All items go here
	private ItemStack getMaxDurabilityItem(int level, String itemtype) {
		ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
		item.addEnchantment(Enchantment.DURABILITY, 1);
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
		
		return item;
	}
}
