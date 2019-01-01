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
	
	public static String getAttributeType(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		for(int i = 0; i < lore.size(); i++) {
			if(lore.get(i).contains("Effect: Increases")) {
				String[] temp = lore.get(i).split(" ");
				if(temp[3].charAt(temp[3].length() - 1) == ',') {
					temp[3] = temp[3].substring(0, temp[3].length() - 1);
				}
				return temp[3].substring(0, 1).toUpperCase() + temp[3].substring(1, temp[3].length());
			}
		}
		return null;
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
		else if(line.contains("reduces durability")) {
			return "overload";
		}
		else if(line.contains("Increases weapon") || line.contains("Increases armor")) {
			return "attribute";
		}
		else if(charmLine.contains("Charm")) {
			return "charm";
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
	
	public static boolean parseAttribute(ItemStack itemWithSlot, ItemStack itemToSlot, int slot) {
		int potency = -1;
		for(String line : itemToSlot.getItemMeta().getLore()) {
			if(line.contains("Potency")) {
				potency = Integer.parseInt(line.substring(line.indexOf(":") + 4));
			}
		}
		if (potency == -1) {
			return false;
		}
		ItemMeta meta = itemWithSlot.getItemMeta();
		int slotLevel = getSlotLevel(itemWithSlot, slot);
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		lore.set(getSlotNum(itemWithSlot, slot), "§" + slotLevel + "§0§1§0§0§0§9" + getAttributeType(itemToSlot) + " +" + potency);
		meta.setLore(lore);
		itemWithSlot.setItemMeta(meta);
		return true;
	}
	
	public static boolean parseOverload(ItemStack itemWithSlot, ItemStack itemToSlot, int slot) {
		int potency = -1;
		int durabilityLoss = -1;
		String durabilityLossString = null;
		for(String line : itemToSlot.getItemMeta().getLore()) {
			if(line.contains("Potency")) {
				potency = Integer.parseInt(line.substring(line.indexOf(":") + 4));
			}
			if(line.contains("Durability Lost")) {
				durabilityLossString = line.substring(line.indexOf(":") + 4);
				durabilityLoss = Integer.parseInt(durabilityLossString);
			}
		}
		if (potency == -1) {
			return false;
		}
		if(Util.getMaxDurability(itemWithSlot) - durabilityLoss <= 0) {
			return false;
		}
		Util.setMaxDurability(itemWithSlot, Util.getMaxDurability(itemWithSlot) - durabilityLoss);
		String encodedDurabilityLoss = durabilityLossString.replaceAll("", "§");
		encodedDurabilityLoss = encodedDurabilityLoss.substring(0, durabilityLossString.length() - 1);
		ItemMeta meta = itemWithSlot.getItemMeta();
		int slotLevel = getSlotLevel(itemWithSlot, slot);
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		
		lore.set(getSlotNum(itemWithSlot, slot), "§" + slotLevel + "§0§2" + encodedDurabilityLoss + "§c" + getAttributeType(itemToSlot) + " +" + potency);
		meta.setLore(lore);
		itemWithSlot.setItemMeta(meta);
		return true;
	}
	
	public static boolean parseCharm(ItemStack itemWithSlot, ItemStack itemToSlot, int slot) {
		ItemMeta meta = itemWithSlot.getItemMeta();
		int slotLevel = getSlotLevel(itemWithSlot, slot);
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		
		String[] charmStrings = itemToSlot.getItemMeta().getLore().get(0).split(" ");
		String charm = "";
		for(int i = 2; i < charmStrings.length; i++) {
			charm += charmStrings[i];
			if(i < charmStrings.length - 1) {
				charm += " ";
			}
		}
		
		lore.set(getSlotNum(itemWithSlot, slot), "§" + slotLevel + "§0§3§0§0§0§9" + charm);
		meta.setLore(lore);
		itemWithSlot.setItemMeta(meta);
		return true;
	}
}
