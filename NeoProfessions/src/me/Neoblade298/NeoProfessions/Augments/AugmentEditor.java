package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;

public class AugmentEditor {
	ItemStack item;
	NBTItem nbti;
	
	public AugmentEditor(ItemStack item) {
		this.item = item;
		this.nbti = new NBTItem(item);
	}
	
	public Augment getAugment(int i) {
		String augmentName = nbti.getString("slot" + i + "Augment");
		if (AugmentManager.nameMap.containsKey(augmentName)) {
			int level = nbti.getInteger("slot" + i + "Level");
			return AugmentManager.nameMap.get(augmentName).createNew(level);
		}
		return null;
	}
	
	public boolean setAugment(Player p, Augment aug, int i) {
		String error = "[NeoProfessions] Could not set augment for " + p.getName() + " on slot " + i + ", ";
		if (nbti.getInteger("version") == 0) {
			Bukkit.getLogger().log(Level.INFO, error + "item not converted!");
			return false;
		}
		if (i < 1) {
			Bukkit.getLogger().log(Level.INFO, error + "<1 slot number!");
			return false;
		}
		if (i > nbti.getInteger("slotsCreated")) {
			Bukkit.getLogger().log(Level.INFO, error + "slot does not yet exist!");
			return false;
		}
		
		nbti.setString("slot" + i + "Augment", aug.getName());
		nbti.setInteger("slot" + i + "Level", aug.getLevel());
		this.item = nbti.getItem();
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		lore.set(nbti.getInteger("slot" + i + "Line"), aug.getLine());
		meta.setLore(lore);
		item.setItemMeta(meta);
		return true;
	}
	
	public boolean addSlot(Player p) {
		String error = "[NeoProfessions] Could not add slot for " + p.getName() + ", ";
		if (nbti.getInteger("version") == 0) {
			Bukkit.getLogger().log(Level.INFO, error + "item not converted!");
			return false;
		}
		int oldTotal = nbti.getInteger("slotsCreated");
		int newTotal = oldTotal + 1;
		if (newTotal > nbti.getInteger("slotsMax")) {
			Bukkit.getLogger().log(Level.INFO, error + "max slots reached!");
			return false;
		}
		
		nbti.setInteger("slotsCreated", newTotal);
		nbti.setInteger("slot" + newTotal + "Line", nbti.getInteger("slot" + oldTotal + "Line") + 1);
		this.item = nbti.getItem();
		ItemMeta meta = item.getItemMeta();
		
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		int slotNum = nbti.getInteger("slot" + newTotal + "Line") == 0 ? nbti.getInteger("slot" + newTotal + "Line") : lore.size() - 2;
		lore.add(slotNum, "§7[Empty Slot]");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return true;
	}
	
	public boolean convertItem(Player p) {
		String error = "[NeoProfessions] Could not convert item for " + p.getName() + ", ";
		if (nbti.getInteger("version") != 0) {
			Bukkit.getLogger().log(Level.INFO, error + "item already converted!");
			return false;
		}
		nbti.setInteger("version", 1);
		nbti.setString("type", "default");
		this.item = nbti.getItem();
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		MasonUtils masonUtils = new MasonUtils();
		
		boolean hasBonus = false;
		int bonusLine = -1;
		Random gen = new Random();
		for (int i = 0; i < lore.size(); i++) {
			String line = lore.get(i);
			if (line.contains("Level")) {
				nbti.setInteger("level", Integer.parseInt(line.split(" ")[2]));
				nbti.applyNBT(this.item);
				continue;
			}
			
			if (line.contains("Bonus Attributes")) {
				bonusLine = i;
				hasBonus = true;
				continue;
			}
			
			if (hasBonus) {
				if (line.contains("Slot")) {
					lore.set(i, "§8[Empty Slot]");
				}
				else if (line.contains("Durability")) {
					break;
				}
				else {
					lore.set(i, "§8[Empty Slot]");
					// Turn the string into an old augment
					int level = masonUtils.parseUnslot(p, i).getEnchantmentLevel(Enchantment.DURABILITY);
					// Choose a random augment
					String[] choices = (String[]) AugmentManager.nameMap.keySet().toArray();
					Augment aug = AugmentManager.nameMap.get(choices[gen.nextInt(choices.length)]).createNew(level);
					HashMap<Integer, ItemStack> failed = p.getInventory().addItem(aug.getItem());
					for (Integer num : failed.keySet()) {
						p.getWorld().dropItem(p.getLocation(), failed.get(num));
					}
				}
			}
		}
		if (bonusLine != -1) {
			lore.remove(bonusLine);
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		return true;
	}
}
