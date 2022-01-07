package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

public class BlacksmithItems {

	// Durability
	public ItemStack getDurabilityItem(int level, String itemtype) {
		ItemStack item = new ItemStack(Material.IRON_INGOT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4[Lv " + level + "] §cDurability Augment");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		lore.add("§7Level " + level + " Durability Augment§0");
		
		if(itemtype.equals("armor")) {
			lore.add("§7Effect: Increases armor max durability");
			int offset = (10 * ((level / 5) - 1)) + 1;
			lore.add("§7Potency: §e" + ((int)(Math.random() * 10) + offset + 1));
		}
		if(itemtype.equals("weapon")) {
			lore.add("§7Effect: Increases weapon max durability");
			int offset = (20 * ((level / 5) - 1));
			lore.add("§7Potency: §e" + ((int)(Math.random() * 20) + offset + 1));
		}
		meta.setLore(lore);	
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		
		return item;
	}

	// Durability
	public ItemStack getDurabilityItem(int level, String itemtype, int potency) {
		ItemStack item = new ItemStack(Material.IRON_INGOT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4[Lv " + level + "] §cDurability Augment");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();
		lore.add("§7Level " + level + " Durability Augment§0");
		
		if(itemtype.equals("armor")) {
			lore.add("§7Effect: Increases armor max durability");
			lore.add("§7Potency: §e" + potency);
		}
		if(itemtype.equals("weapon")) {
			lore.add("§7Effect: Increases weapon max durability");
			lore.add("§7Potency: §e" + potency);
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		
		return item;
	}
	
	// Repair
	public ItemStack getRepairItem(int level) {
		ItemStack item = new ItemStack(Material.PRISMARINE_CRYSTALS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§4[Lv " + level + "] §cRepair Kit");
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		List<String> lore = new ArrayList<String>();

		lore.add("§6Right click to use§0");
		lore.add("§cOnly works on quest items");
		lore.add("§7Compatibility: Up to §4Lv " + level);
		lore.add("§7Effect: Restores durability of an item");
		lore.add("§7Potency: §e40%");
		meta.setLore(lore);
		item.setItemMeta(meta);
		item.addUnsafeEnchantment(Enchantment.DURABILITY, level);
		NBTItem nbti = new NBTItem(item);
		nbti.setInteger("level", level);
		nbti.setDouble("potency", 40D);
		
		return nbti.getItem(user);
	}
}
