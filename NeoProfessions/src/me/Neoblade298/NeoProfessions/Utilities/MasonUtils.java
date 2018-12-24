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
			if(!hasBonus) {
				if(lore.get(i).contains("Bonus")) {
					hasBonus = true;
					slotLine = i + 1;
				}
			}
			else {
				if(lore.get(i).contains("Durability")) {
					slotLine = i;
				}
			}
		}
		
		lore.add(slotLine, "§8(Lv " + level + " Slot)");
		if(!hasBonus) {
			lore.add(slotLine, "§9[Bonus Attributes]");
		}
		
		
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
	
	// Finds the lowest level slot that fits the level
	public static int getAvailableSlot(ItemStack item, int level) {
		// First find the bonus attributes line
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		int bonusLine = -1;
		int availableSlot = -1;
		for(int i = 0; i < lore.size(); i++) {
			if(lore.get(i).contains("Bonus")) {
				bonusLine = i;
				break;
			}
		}
		
		// Only search for an available slot if a bonus line is found
		if(bonusLine != -1) {
			for(int i = level; i <= MAX_LEVEL; i++) {
				for(int j = bonusLine + 1; j <= lore.size(); j++) {
					if(lore.get(j).contains("Slot") && lore.get(j).contains(Integer.toString(i))) {
						return j;
					}
				}
			}
		}
		return availableSlot;
	}

}
