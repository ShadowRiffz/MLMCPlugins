package me.Neoblade298.NeoProfessions.Utilities;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

public class Util {
	public void sendMessage(Player p, String m) {
		String message = "&4[&c&lMLMC&4] " + m;
		p.sendMessage(message.replaceAll("&", "§"));
	}

	public ItemStack setAmount(ItemStack item, int amount) {
		item.setAmount(amount);
		return item;
	}
	
	public int getEssenceLevel(ItemStack item) {
		if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return -1;
		}
		String lore  = item.getItemMeta().getLore().get(0);
		if(lore.contains("Essence")) {
			return item.getEnchantmentLevel(Enchantment.DURABILITY);
		}
		return -1;
	}
	
	public int getItemLevel(ItemStack item) {
		if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return -1;
		}
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
	
	public String getItemType(ItemStack item) {
		ArrayList<String> lore  = (ArrayList<String>) item.getItemMeta().getLore();
		String desc = null;
		
		// Find the tier string
		for(String line : lore) {
			if(line.contains("Tier:")) {
				desc = line;
				break;
			}
		}
		if (desc != null) {
			desc = ChatColor.stripColor(desc.substring(8));
			
			// Edge cases
			if(desc.equalsIgnoreCase("Common Helmet")) {
				desc = "Common Reinforced Helmet";
			}
			if(desc.equalsIgnoreCase("Common Chestplate")) {
				desc = "Common Reinforced Chestplate";
			}
			if(desc.equalsIgnoreCase("Common Leggings")) {
				desc = "Common Reinforced Leggings";
			}
			if(desc.equalsIgnoreCase("Common Boots")) {
				desc = "Common Reinforced Boots";
			}
			if(desc.equalsIgnoreCase("Uncommon Helmet")) {
				desc = "Uncommon Reinforced Helmet";
			}
			if(desc.equalsIgnoreCase("Uncommon Chestplate")) {
				desc = "Uncommon Reinforced Chestplate";
			}
			if(desc.equalsIgnoreCase("Uncommon Leggings")) {
				desc = "Uncommon Reinforced Leggings";
			}
			if(desc.equalsIgnoreCase("Uncommon Boots")) {
				desc = "Uncommon Reinforced Boots";
			}
		}
		
		return desc;
		
	}
	
	public boolean isWeapon(ItemStack item) {
		Material mat = item.getType();
		return
				mat.equals(Material.WOODEN_SWORD) ||
				mat.equals(Material.STONE_SWORD) ||
				mat.equals(Material.GOLDEN_SWORD) ||
				mat.equals(Material.IRON_SWORD) ||
				mat.equals(Material.DIAMOND_SWORD) ||
				mat.equals(Material.WOODEN_AXE) ||
				mat.equals(Material.STONE_AXE) ||
				mat.equals(Material.GOLDEN_AXE) ||
				mat.equals(Material.IRON_AXE) ||
				mat.equals(Material.DIAMOND_AXE) ||
				mat.equals(Material.WOODEN_HOE) ||
				mat.equals(Material.STONE_HOE) ||
				mat.equals(Material.GOLDEN_HOE) ||
				mat.equals(Material.IRON_HOE) ||
				mat.equals(Material.DIAMOND_HOE) ||
				mat.equals(Material.SHIELD) ||
				mat.equals(Material.BOW);
	}

	
	public boolean isArmor(ItemStack item) {
		Material mat = item.getType();
		return
				mat.equals(Material.LEATHER_HELMET) ||
				mat.equals(Material.GOLDEN_HELMET) ||
				mat.equals(Material.IRON_HELMET) ||
				mat.equals(Material.DIAMOND_HELMET) ||
				mat.equals(Material.LEATHER_CHESTPLATE) ||
				mat.equals(Material.GOLDEN_CHESTPLATE) ||
				mat.equals(Material.IRON_CHESTPLATE) ||
				mat.equals(Material.DIAMOND_CHESTPLATE) ||
				mat.equals(Material.LEATHER_LEGGINGS) ||
				mat.equals(Material.GOLDEN_LEGGINGS) ||
				mat.equals(Material.IRON_LEGGINGS) ||
				mat.equals(Material.DIAMOND_LEGGINGS) ||
				mat.equals(Material.LEATHER_BOOTS) ||
				mat.equals(Material.GOLDEN_BOOTS) ||
				mat.equals(Material.IRON_BOOTS) ||
				mat.equals(Material.DIAMOND_BOOTS);
	}
	
	public int getCurrentDurability(ItemStack item) {
		for(String line : item.getItemMeta().getLore()) {
			if(line.contains("§7Durability")) {
				line = ChatColor.stripColor(line);
				line = line.substring(line.indexOf(" ") + 1);
				String[] numbers = line.split(" / ");
				return Integer.parseInt(numbers[0]);
			}
		}
		return -1;
	}
	
	public int getMaxDurability(ItemStack item) {
		for(String line : item.getItemMeta().getLore()) {
			if(line.contains("§7Durability")) {
				line = ChatColor.stripColor(line);
				line = line.substring(line.indexOf(" ") + 1);
				String[] numbers = line.split(" / ");
				return Integer.parseInt(numbers[1]);
			}
		}
		return -1;
	}
	
	public void setCurrentDurability(ItemStack item, int durability) {
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		ItemMeta meta = item.getItemMeta();
		if(durability > getMaxDurability(item)) {
			durability = getMaxDurability(item);
		}
		
		for(int i = 0; i < lore.size(); i++) {
			if(lore.get(i).contains("§7Durability")) {
				lore.set(i, "§7Durability " + durability + " / " + getMaxDurability(item));
				meta.setLore(lore);
				item.setItemMeta(meta);
				double percentage = 1-((double)durability / (double)getMaxDurability(item));
				ItemMeta im = item.getItemMeta();
				((Damageable) im).setDamage((int) (item.getType().getMaxDurability()-1 * percentage));
				item.setItemMeta(im);
				return;
			}
		}
	}
	
	public void setMaxDurability(ItemStack item, int durability) {
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		ItemMeta meta = item.getItemMeta();
		
		for(int i = 0; i < lore.size(); i++) {
			if(lore.get(i).contains("§7Durability")) {
				if(getCurrentDurability(item) > durability) {
					lore.set(i, "§7Durability " + durability + " / " + durability);
				}
				else {
					lore.set(i, "§7Durability " + getCurrentDurability(item) + " / " + durability);
				}
				meta.setLore(lore);
				item.setItemMeta(meta);
				double percentage = 1-((double)getCurrentDurability(item) / (double)durability);
				ItemMeta im = item.getItemMeta();
				((Damageable) im).setDamage((int) ((item.getType().getMaxDurability()-1) * percentage));
				item.setItemMeta(im);
				return;
			}
		}
	}
}
