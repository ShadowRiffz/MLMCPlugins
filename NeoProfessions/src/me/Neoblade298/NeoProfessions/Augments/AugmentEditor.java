package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;

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
		if (i < 1) {
			Bukkit.getLogger().log(Level.INFO, error + "<1 slot number!");
			return false;
		}
		if (i > nbti.getInteger("totalSlots")) {
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
		int oldTotal = nbti.getInteger("totalSlots");
		int newTotal = oldTotal + 1;
		if (newTotal > AugmentManager.MAX_SLOTS) {
			Bukkit.getLogger().log(Level.INFO, error + "max slots reached!");
			return false;
		}
		// Maybe need limit on max slots for different gear rarities
		
		nbti.setInteger("totalSlots", newTotal);
		nbti.setInteger("slot" + newTotal + "Line", nbti.getInteger("slot" + oldTotal + "Line") + 1);
		this.item = nbti.getItem();
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		lore.add(nbti.getInteger("slot" + newTotal + "Line"), "§7[Empty Slot]");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return true;
	}
}
