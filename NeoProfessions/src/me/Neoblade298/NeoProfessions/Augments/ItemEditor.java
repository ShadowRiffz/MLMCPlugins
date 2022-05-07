package me.Neoblade298.NeoProfessions.Augments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.Managers.AugmentManager;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.listeners.DurabilityListener;

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
		typeConverter.put("Infused Helmet", "ruhelmet");
		typeConverter.put("Infused Chestplate", "ruchestplate");
		typeConverter.put("Infused Leggings", "ruleggings");
		typeConverter.put("Infused Boots", "ruboots");
		
		maxSlotConverter.put("common", 0);
		maxSlotConverter.put("uncommon", 0);
		maxSlotConverter.put("rare", 1);
		maxSlotConverter.put("epic", 2);
		maxSlotConverter.put("legendary", 3);
		maxSlotConverter.put("artifact", 4);
	}
	
	public ItemEditor(ItemStack item) {
		this.item = item;
		this.nbti = new NBTItem(item);
	}
	
	public ItemStack getItem() {
		return item;
	}
	
	public Augment getAugment(int i) {
		String augmentName = nbti.getString("slot" + i + "Augment");
		if (AugmentManager.hasAugment(augmentName)) {
			int level = nbti.getInteger("slot" + i + "Level");
			return AugmentManager.getFromCache(augmentName, level);
		}
		return null;
	}
	
	public String unslotAugment(Player p, int i) {
		if (nbti.getInteger("version") == 0) {
			return "item version is old!";
		}
		if (i < 1) {
			return "<1 slot number!";
		}
		if (i > nbti.getInteger("slotsCreated")) {
			return "slot does not yet exist!";
		}
		if (!nbti.hasKey("slot" + i + "Augment")) {
			return "slot " + i + " is unused!";
		}

		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		lore.set(nbti.getInteger("slot" + i + "Line"), "§8[Empty Slot]");
		meta.setLore(lore);
		item.setItemMeta(meta);
		nbti = new NBTItem(item);
		nbti.removeKey("slot" + i + "Augment");
		nbti.removeKey("slot" + i + "Level");
		nbti.applyNBT(item);
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
	
	public String convertItem(Player p) {
		if (nbti.getInteger("version") != 0) {
			return "item already converted!";
		}
		
		if (!Util.isWeapon(item) && !Util.isArmor(item)) {
			return "item is not weapon or armor!";
		}
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		MasonUtils masonUtils = new MasonUtils();
		
		
		boolean hasBonus = false;
		boolean hasLevel = false;
		boolean wasMythic = false;
		boolean onAttributes = false;
		Random gen = new Random();
		int itemLevel = -1;
		int slots = 0;
		int slotsMax = 0;
		String rarity = "common";
		String displayname = "Sword";
		String name = "sword";
		

		for (Enchantment ench : meta.getEnchants().keySet()) {
			if (!ench.equals(Enchantment.QUICK_CHARGE) && !ench.equals(Enchantment.ARROW_INFINITE)) {
				meta.removeEnchant(ench);
			}
		}
		
		lore.add(0, ""); // Placeholder for Title
		lore.add(1, ""); // Placeholder for Type
		ListIterator<String> iter = lore.listIterator();
		int slotNum = 0;
		while (iter.hasNext()) {
			String line = iter.next();
			
			if (!hasBonus) {
				if (line.contains("Tier: ")) {
					String args[] = line.split(" ");
					displayname = args[2];
					for (int j = 3; j < args.length; j++) {
						displayname += " " + args[j];
					}
					
					// If the display type is an armor piece
					if (displayname.contains(" ")) {
						name = typeConverter.get(displayname);
						if (name.startsWith("ru")) {
							displayname = displayname.replaceFirst("Infused", "Ruinous");
						}
					}
					else {
						name = displayname.toLowerCase();
					}
					
					rarity = ChatColor.stripColor(args[1].toLowerCase());
					if (rarity.equalsIgnoreCase("mythic")) {
						wasMythic = true;
						rarity = "artifact";
					}
					if (name.contains("helmet") || name.contains("boots")) {
						slotsMax = 0;
					}
					else {
						slotsMax = maxSlotConverter.get(rarity);
					}
					
					if (rarity.contains("artifact") || rarity.contains("legendary")) {
						meta.addEnchant(Enchantment.LUCK, 1, true);
					}
					iter.remove();
					iter.add("§7Rarity: " + args[1]);
				}
				else if (line.contains("Base Attr")) {
					iter.remove();
					iter.add("§8§m-----");
					onAttributes = true;
				}
				else if (line.contains("Vitality") || line.contains("Perception") || line.contains("Regen")) {
					iter.remove();
				}
				
				else if (line.contains("Level")) {
					itemLevel = Integer.parseInt(line.split(" ")[2]);
					iter.remove();
					iter.add("§7Level: " + itemLevel);
					iter.add("§7Max Slots: " + slotsMax);
					hasLevel = true;
				}
				else if (line.contains("Bonus Attributes")) {
					iter.remove();
					iter.add("§8§m-----");
					onAttributes = false;
					hasBonus = true;
				}
				else if (line.contains("Durability")) {
					iter.previous();
					iter.add("§8§m-----");
					break;
				}
				
				else if (onAttributes && wasMythic) {
					iter.remove();
				}
			}
			
			// Bonus lines
			else {
				slotNum++;
				if (line.contains("Slot")) {
					iter.remove();
					if (slots < slotsMax) {
						iter.add("§8[Empty Slot]");
						slots++;
					}
				}
				else if (line.contains("Durability") && !line.contains("Max")) {
					break;
				}
				else {
					iter.remove();
					if (slots < slotsMax) {
						iter.add("§8[Empty Slot]");
						slots++;
					}
					// Turn the string into an old augment
					ItemStack oldAug = masonUtils.parseUnslot(p, slotNum);
					String oldAugName = oldAug.getItemMeta().getDisplayName();
					int level = oldAug.getEnchantmentLevel(Enchantment.DURABILITY);
					HashMap<Integer, ItemStack> failed = null;
					
					// Return it if it's a relic
					if (oldAug.getType().equals(Material.QUARTZ)) {
						failed = p.getInventory().addItem(oldAug);
					}
					// Return it if it's a drop or exp charm
					else if (oldAugName.contains("Exp")) {
						failed = p.getInventory().addItem(AugmentManager.getFromCache("Experience", level).getItem(p));
					}
					else if (oldAugName.contains("Drop")) {
						failed = p.getInventory().addItem(AugmentManager.getFromCache("Chest Chance", level).getItem(p));
					}
					else {
						ArrayList<String> table = AugmentManager.getDropTables().get("default");
						Augment aug = AugmentManager.getFromCache(table.get(gen.nextInt(table.size())), level);
						failed = p.getInventory().addItem(aug.getItem(p));
					}
					
					// Drops augments on the ground if inventory full
					if (failed != null) {
						for (Integer num : failed.keySet()) {
							p.getWorld().dropItem(p.getLocation(), failed.get(num));
						}
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
		meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		item.setItemMeta(meta);
		
		
		nbti = new NBTItem(item);
		nbti.setInteger("version", 1);
		nbti.setString("gear", name);
		nbti.setString("rarity", rarity);
		nbti.setInteger("level", itemLevel);
		nbti.setInteger("slotsCreated", slots);
		nbti.setInteger("slotsMax", slotsMax);
		nbti.applyNBT(item);

		DurabilityListener.fullRepairItem(p, item);
		
		setSlotNbt(item);
		return null;
	}
	
	private static void setSlotNbt(ItemStack item) {
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		HashMap<String, Integer> slotMap = new HashMap<String, Integer>();
		int numSlot = 1;
		for (int i = 0; i < lore.size(); i++) {
			String line = lore.get(i);
			if (line.contains("Empty")) {
				slotMap.put("slot" + numSlot++ + "Line", i);
			}
		}
		NBTItem nbti = new NBTItem(item);
		for (String key : slotMap.keySet()) {
			nbti.setInteger(key, slotMap.get(key));
		}
		nbti.applyNBT(item);
	}
}
