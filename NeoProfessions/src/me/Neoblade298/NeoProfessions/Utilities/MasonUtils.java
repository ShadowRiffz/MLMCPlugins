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
	
	public static int getSlotNum(ItemStack item, int slot) {
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		int count = 0;
		int lineNum = 0;
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
					return lineNum;
				}
			}
			lineNum++;
		}
		return -1;
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
	public static int getSlotLevel(ItemStack item, int slot) {
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
					return Integer.parseInt(line.substring(line.indexOf(" ") + 1, line.indexOf(" ") + 2));
				}
			}
		}
		return -1;
	}
	
	public static void removeSlotLine(ItemStack item, int slot) {
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		ItemMeta meta  = item.getItemMeta();
		int count = 0;
		int lineNum = 0;
		boolean hasBonus = false;
		for(String line : lore) {
			if (!hasBonus) {
				if(line.contains("Bonus")) {
					hasBonus = true;
				}
			}
			else {
				count++;
				if(slot == count) {
					lore.remove(lineNum);
					break;
				}
			}
			lineNum++;
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static String slotType(ItemStack item) {
		if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return null;
		}
		
		String line = item.getItemMeta().getLore().get(1);
		String charmLine = item.getItemMeta().getLore().get(0);
		if(line.contains("max durability")) {
			return "durability";
		}
		else if(line.contains("Increases weapon") || line.contains("Increases armor")) {
			return "attribute";
		}
		else if(line.contains("reduces durability")) {
			return "overload";
		}
		else if(charmLine.contains("Advanced EXP")) {
			return "advancedexp";
		}
		else if(charmLine.contains("Advanced Gold")) {
			return "advancedgold";
		}
		else if(charmLine.contains("Advanced Drop")) {
			return "advanceddrop";
		}
		else if(charmLine.contains("EXP")) {
			return "exp";
		}
		else if(charmLine.contains("Gold")) {
			return "gold";
		}
		else if(charmLine.contains("Drop")) {
			return "drop";
		}
		else if(charmLine.contains("Traveler")) {
			return "traveler";
		}
		else if(charmLine.contains("Recovery")) {
			return "recovery";
		}
		else if(charmLine.contains("Hunger")) {
			return "hunger";
		}
		else if(charmLine.contains("Second Chance")) {
			return "secondchance";
		}
		return null;
	}
	
	public static boolean parseDurability(ItemStack itemWithSlot, ItemStack itemToSlot, int slot) {
		int potency = -1;
		for(String line : itemToSlot.getItemMeta().getLore()) {
			if(line.contains("Potency")) {
				potency = Integer.parseInt(line.substring(line.indexOf(":") + 4));
			}
		}
		if (potency == -1) {
			return false;
		}
		Util.setMaxDurability(itemWithSlot, potency + Util.getMaxDurability(itemWithSlot));
		ItemMeta meta = itemWithSlot.getItemMeta();
		int slotLevel = getSlotLevel(itemWithSlot, slot);
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		lore.set(getSlotNum(itemWithSlot, slot), "§" + slotLevel + "§0§0§0§0§0§9Max Durability +" + potency);
		meta.setLore(lore);
		itemWithSlot.setItemMeta(meta);
		return true;
	}
}
