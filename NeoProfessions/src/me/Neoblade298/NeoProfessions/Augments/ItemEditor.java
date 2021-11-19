package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class ItemEditor {
	ItemStack item;
	NBTItem nbti;
	
	public ItemEditor(ItemStack item) {
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
	
	public String setAugment(Player p, Augment aug, int i) {
		if (nbti.getInteger("version") == 0) {
			return "item version is old!";
		}
		if (i < 1) {
			return "<1 slot number!";
		}
		if (i > nbti.getInteger("slotsCreated")) {
			return "slot does not yet exist!";
		}
		
		for (int j = 1; j <= nbti.getInteger("slotsCreated"); j++) {
			if (j == i) {
				continue;
			}
			if (nbti.getString("slot" + j + "Augment").equals(aug.getName())) {
				return "same augment is already slotted!";
			}
		}
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		lore.set(nbti.getInteger("slot" + i + "Line"), aug.getLine());
		meta.setLore(lore);
		item.setItemMeta(meta);
		nbti = new NBTItem(item);
		nbti.setString("slot" + i + "Augment", aug.getName());
		nbti.setInteger("slot" + i + "Level", aug.getLevel());
		nbti.applyNBT(item);
		return null;
	}
	
	public String addSlot(Player p) {
		if (nbti.getInteger("version") == 0) {
			return "item not converted!";
		}
		int oldTotal = nbti.getInteger("slotsCreated");
		int newTotal = oldTotal + 1;
		if (newTotal > nbti.getInteger("slotsMax")) {
			return "max slots reached!";
		}
		
		ItemMeta meta = item.getItemMeta();
		
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		int slotNum = nbti.getInteger("slot" + newTotal + "Line") == 0 ? nbti.getInteger("slot" + newTotal + "Line") : lore.size() - 2;
		lore.add(slotNum, "§7[Empty Slot]");
		meta.setLore(lore);
		item.setItemMeta(meta);
		nbti = new NBTItem(item);
		nbti.setInteger("slotsCreated", newTotal);
		nbti.setInteger("slot" + newTotal + "Line", nbti.getInteger("slot" + oldTotal + "Line") + 1);
		nbti.applyNBT(item);
		return null;
	}
	
	public String convertItem(Player p) {
		if (nbti.getInteger("version") != 0) {
			return "item already converted!";
		}
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		MasonUtils masonUtils = new MasonUtils();
		
		if (!Util.isWeapon(item) && !Util.isArmor(item)) {
			return "item is not weapon or armor!";
		}
		
		for (Enchantment ench : item.getEnchantments().keySet()) {
			item.removeEnchantment(ench);
		}
		
		boolean hasBonus = false;
		boolean hasLevel = false;
		boolean isEnchanted = false;
		int bonusLine = -1;
		Random gen = new Random();
		int itemLevel = -1;
		int slots = 0;
		int slotsMax = 0;
		int linesRemoved = 0;
		HashMap<String, Integer> nbtData = new HashMap<String, Integer>();
		for (int i = 0; i < lore.size(); i++) {
			String line = lore.get(i);
			
			if (!hasBonus) {
				if (line.contains("Tier: ")) {
					if (line.contains("Artifact")) {
						isEnchanted = true;
						slotsMax = 5;
					}
					else if (line.contains("Legendary")) {
						isEnchanted = true;
						slotsMax = 4;
					}
					else if (line.contains("Epic")) {
						slotsMax = 3;
					}
					else if (line.contains("Rare")) {
						slotsMax = 2;
					}
					else if (line.contains("Uncommon")) {
						slotsMax = 1;
					}
					else if (line.contains("Common")) {
						slotsMax = 0;
					}
				}
				
				if (line.contains("Level")) {
					itemLevel = Integer.parseInt(line.split(" ")[2]);
					hasLevel = true;
					continue;
				}
				if (line.contains("Bonus Attributes")) {
					bonusLine = i;
					hasBonus = true;
					continue;
				}
			}
			
			
			if (hasBonus) {
				if (line.contains("Slot")) {
					lore.set(i, "§8[Empty Slot]");
					slots++;
					nbtData.put("slot" + slots + "Line", i);
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

		if (!hasLevel) {
			return "item is not eligible for conversion!";
		}
		
		if (bonusLine != -1) {
			lore.remove(bonusLine);
			linesRemoved++;
		}
		lore.remove("§9[Base Attributes]");
		linesRemoved++;
		
		
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		if (isEnchanted) {
			item.addEnchantment(Enchantment.DURABILITY, 1);
		}
		nbti = new NBTItem(item);
		nbti.setInteger("version", 1);
		nbti.setString("gear", "default");
		nbti.setInteger("level", itemLevel);
		for (String key : nbtData.keySet()) {
			nbti.setInteger(key, nbtData.get(key) - linesRemoved);
		}
		nbti.setInteger("slotsCreated", slots);
		nbti.setInteger("slotsMax", slotsMax);
		nbti.applyNBT(item);
		return null;
	}
}
