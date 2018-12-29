package me.Neoblade298.NeoProfessions.Utilities;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MasonUtils {
	
	final static int MAX_LEVEL = 5;
	
	public static void createSlot(ItemStack item, int level) {
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		boolean hasBonus = false;
		int slotLine = -1;
		for(int i = 0; i < lore.size(); i++) {
			if(lore.get(i).contains("Bonus")) {
				hasBonus = true;
			}
			if(lore.get(i).contains("Durability")) {
				slotLine = i;
			}
		}
		
		lore.add(slotLine, "§8(Lv " + level + " Slot)");
		if(!hasBonus) {
			lore.add(slotLine, "§9[Bonus Attributes]");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static int countSlots(ItemStack item) {
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		int count = 0;
		boolean hasBonus = false;
		for(String line : lore) {
			if (!hasBonus) {
				if(line.contains("Bonus")) {
					hasBonus = true;
				}
			}
			else {
				if(!line.contains("Durability")) {
					count++;
				}
			}
		}
		return count;
	}
	
	public static boolean isSlotAvailable(ItemStack item, int slot) {
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		int count = 0;
		boolean hasBonus = false;
		for(String line : lore) {
			if (!hasBonus) {
				if(line.contains("Bonus")) {
					hasBonus = true;
				}
			}
			else {
				count++;
				// If the matching slot is empty, return true
				if(slot == count) {
					if(line.contains("Slot")) {
						return true;
					}
					else {
						return false;
					}
				}
			}
		}
		return false;
	}
	
	public static String getSlotLine(ItemStack item, int slot) {
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		int count = 0;
		boolean hasBonus = false;
		for(String line : lore) {
			if (!hasBonus) {
				if(line.contains("Bonus")) {
					hasBonus = true;
				}
			}
			else {
				count++;
				// If the matching slot is empty, return true
				if(slot == count) {
					return line;
				}
			}
		}
		return null;
	}
}
