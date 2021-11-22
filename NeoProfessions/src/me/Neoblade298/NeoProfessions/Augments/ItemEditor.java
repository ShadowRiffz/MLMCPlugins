package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class ItemEditor {
	private static HashMap<String, String> typeConverter = new HashMap<String, String>();
	private static HashMap<String, Integer> maxSlotConverter = new HashMap<String, Integer>();
	ItemStack item;
	NBTItem nbti;
	
	static {
		typeConverter.put("Reinforced Helmet", "rhelmet");
		typeConverter.put("Reinforced Chestplate", "rchestplate");
		typeConverter.put("Reinforced Leggings", "rleggings");
		typeConverter.put("Reinforced Boots", "rboots");
		typeConverter.put("Infused Helmet", "ihelmet");
		typeConverter.put("Infused Chestplate", "ichestplate");
		typeConverter.put("Infused Leggings", "ileggings");
		typeConverter.put("Infused Boots", "iboots");
		
		maxSlotConverter.put("common", 0);
		maxSlotConverter.put("uncommon", 1);
		maxSlotConverter.put("rare", 2);
		maxSlotConverter.put("epic", 3);
		maxSlotConverter.put("legendary", 4);
		maxSlotConverter.put("artifact", 5);
	}
	
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
		System.out.println("2 " + aug);
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
			System.out.println(aug.getName() + " " + nbti.getString("slot" + j + "Augment") + " " + j);
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
			if (! ench.equals(Enchantment.QUICK_CHARGE)) {
				item.removeEnchantment(ench);
			}
		}
		
		boolean hasBonus = false;
		boolean hasLevel = false;
		boolean isEnchanted = false;
		Random gen = new Random();
		int itemLevel = -1;
		int slots = 0;
		int slotsMax = 0;
		String rarity = "common";
		String displayname = "Sword";
		String name = "sword";
		HashMap<String, Integer> nbtData = new HashMap<String, Integer>();
		
		lore.add(0, ""); // Placeholder for Title
		lore.add(1, ""); // Placeholder for Type
		ListIterator<String> iter = lore.listIterator();
		int i = -1;
		while (iter.hasNext()) {
			i++;
			String line = iter.next();
			
			if (!hasBonus) {
				if (line.contains("Tier: ")) {
					String args[] = line.split(" ");
					displayname = args[2];
					for (int j = 3; j < args.length; j++) {
						displayname += " " + args[j];
					}
					if (displayname.contains(" ")) {
						name = typeConverter.get(displayname);
					}
					else {
						name = displayname.toLowerCase();
					}
					
					rarity = ChatColor.stripColor(args[1].toLowerCase());
					slotsMax = maxSlotConverter.get(rarity);
					if (rarity.contains("artifact") || rarity.contains("legendary")) {
						meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
						if (isEnchanted) {
							meta.addEnchant(Enchantment.LUCK, 1, true);
						}
					}
					iter.remove();
					iter.add("§7Rarity: " + args[1]);
				}
				else if (line.contains("Base Attr")) {
					iter.remove();
					iter.add("§8§m-----");
				}
				
				else if (line.contains("Level")) {
					itemLevel = Integer.parseInt(line.split(" ")[2]);
					iter.remove();
					iter.add("§7Level: " + itemLevel);
					hasLevel = true;
				}
				else if (line.contains("Bonus Attributes")) {
					iter.remove();
					iter.add("§8§m-----");
					hasBonus = true;
				}
			}
			
			else {
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

		lore.set(0, "§7Title: Standard " + displayname);
		lore.set(1, "§7Type: " + displayname);
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		nbti = new NBTItem(item);
		nbti.setInteger("version", 1);
		nbti.setString("gear", name);
		nbti.setString("rarity", rarity);
		nbti.setInteger("level", itemLevel);
		for (String key : nbtData.keySet()) {
			nbti.setInteger(key, nbtData.get(key));
		}
		nbti.setInteger("slotsCreated", slots);
		nbti.setInteger("slotsMax", slotsMax);
		nbti.applyNBT(item);
		return null;
	}
}
