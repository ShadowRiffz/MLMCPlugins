package me.Neoblade298.NeoProfessions.Legacy;

import java.util.ArrayList;
import java.util.ListIterator;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Items.MasonItems;
import me.Neoblade298.NeoProfessions.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class Converter {
	Main main;
	BlacksmithItems bItems;
	StonecutterItems sItems;
	MasonItems mItems;
	MasonItemsLegacy oldMItems;
	MasonMethodsLegacy mMethods;
	MasonUtilsLegacy mUtils;
	CommonItems cItems;
	Util util;
	CurrencyManager cm;
	
	
	public Converter(Main main) {
		this.main = main;
		bItems = new BlacksmithItems();
		sItems = new StonecutterItems();
		mItems = new MasonItems();
		oldMItems = new MasonItemsLegacy();
		mUtils = new MasonUtilsLegacy();
		cItems = new CommonItems();
		util = new Util();
		cm = main.cManager;
	}

	public ItemStack convertItem(Player p, ItemStack item) {
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getEnchantmentLevel(Enchantment.DURABILITY) < 10) {
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = (ArrayList<String>) meta.getLore();
			String idLine = meta.getLore().get(0);
			
			if (idLine.contains("Right click")) {	// Repair kit
				System.out.println("1");
				return convertRepairKit(lore);
			}
			else if (idLine.contains("Durability")) {
				System.out.println("2");
				return convertDurabilityItem(lore);
			}
			else if (idLine.contains("Essence")) {
				System.out.println("3");
				return convertEssence(p, lore, item.getAmount());
			}
			else if (idLine.contains("Ore")) {
				System.out.println("4");
				return convertOre(p, lore, item.getAmount());
			}
			else if (idLine.contains("Gem")) {
				System.out.println("5");
				return convertGem(lore);
			}
			else if (idLine.contains("Charm")) {
				System.out.println("6");
				return convertCharm(item, lore);
			}
		}
		System.out.println("Fail");
		return item;
	}
	
	private ItemStack convertRepairKit(ArrayList<String> lore) {
		int potency = Integer.parseInt(lore.get(4).split(" ")[1].substring(2,4));
		int oldLevel = ((potency % 25) / 5) + 1;
		int newLevel = (oldLevel + 1) * 10;
		return bItems.getRepairItem(newLevel);
	}
	
	private ItemStack convertDurabilityItem(ArrayList<String> lore) {
		String type = lore.get(1).split(" ")[2];
		int oldLevel = Integer.parseInt(lore.get(0).split(" ")[1].replaceAll("§e", ""));
		int newLevel = (oldLevel + 1) * 10;
		int potency = Integer.parseInt(lore.get(2).split(" ")[1].replaceAll("§e", ""));
		return bItems.getDurabilityItem(newLevel, type, potency);
	}
	
	private ItemStack convertEssence(Player p, ArrayList<String> lore, int amt) {
		int oldLevel = Integer.parseInt(lore.get(0).split(" ")[1]);
		int newLevel = (oldLevel + 1) * 10;
		cm.add(p, "essence", newLevel, amt);
		return null;
	}
	
	private ItemStack convertOre(Player p, ArrayList<String> lore, int amt) {
		String oreName = lore.get(0).split(" ")[2].replaceAll("§e", "");
		int oldLevel = Integer.parseInt(lore.get(0).split(" ")[1].replaceAll("§e", ""));
		int newLevel = (oldLevel + 1) * 10;
		cm.add(p, oreName.toLowerCase(), newLevel, amt);
		return null;
	}
	
	private ItemStack convertGem(ArrayList<String> lore) {
		int oldLevel = Integer.parseInt(lore.get(0).split(" ")[1].replaceAll("§e", ""));
		int newLevel = (oldLevel + 1) * 10;
		String itemType = lore.get(1).split(" ")[2];
		String type = lore.get(1).split(" ")[3];
		boolean isOverloaded = false;
		if (lore.size() > 3) {
			isOverloaded = true;
			type = type.substring(0, type.length() - 1);
		}
		
		if (itemType.equalsIgnoreCase("weapon")) {
			return sItems.getWeaponGem(type, newLevel, isOverloaded);
		}
		else {
			return sItems.getArmorGem(type, newLevel, isOverloaded);
		}
	}
	
	private ItemStack convertCharm(ItemStack item, ArrayList<String> lore) {
		if (item.isSimilar(oldMItems.getDropCharm(false))) {
			return mItems.getDropCharm(false);
		}
		else if (item.isSimilar(oldMItems.getDropCharm(true))) {
			return mItems.getDropCharm(true);
		}
		else if (item.isSimilar(oldMItems.getExpCharm(false))) {
			return mItems.getExpCharm(false);
		}
		else if (item.isSimilar(oldMItems.getExpCharm(true))) {
			return mItems.getExpCharm(true);
		}
		else if (item.isSimilar(oldMItems.getHungerCharm())) {
			return mItems.getHungerCharm();
		}
		else if (item.isSimilar(oldMItems.getLootingCharm(false))) {
			return mItems.getLootingCharm(false);
		}
		else if (item.isSimilar(oldMItems.getLootingCharm(true))) {
			return mItems.getLootingCharm(true);
		}
		else if (item.isSimilar(oldMItems.getQuickEatCharm())) {
			return mItems.getQuickEatCharm();
		}
		else if (item.isSimilar(oldMItems.getRecoveryCharm())) {
			return mItems.getRecoveryCharm();
		}
		else if (item.isSimilar(oldMItems.getSecondChanceCharm())) {
			return mItems.getSecondChanceCharm();
		}
		else if (item.isSimilar(oldMItems.getTravelerCharm())) {
			return mItems.getTravelerCharm();
		}
		return null;
	}
	
	public ItemStack convertGear(Player p, ItemStack item, ItemMeta meta, ArrayList<String> lore) {
		if (item != null) {
			if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
				String oldRarity = util.getItemRarity(item);
				String oldType = util.getItemType(item);
				int newLevel = 0;
				
	
				// Fix type
				switch (oldType) {
				case "reinforcedhelmet":
					oldType = "Reinforced Helmet";
					break;
				case "reinforcedchestplate":
					oldType = "Reinforced Chestplate";
					break;
				case "reinforcedleggings":
					oldType = "Reinforced Leggings";
					break;
				case "reinforcedboots":
					oldType = "Reinforced Boots";
					break;
				case "infusedhelmet":
					oldType = "Infused Helmet";
					break;
				case "infusedchestplate":
					oldType = "Infused Chestplate";
					break;
				case "infusedleggings":
					oldType = "Infused Leggings";
					break;
				case "infusedboots":
					oldType = "Infused Boots";
					break;
				}
				
				switch (oldRarity) {
				case "rare":
					newLevel = 5;
					break;
				case "unique":
					newLevel = 10;
					break;
				case "epic":
					newLevel = 25;
					break;
				case "angelic":
					newLevel = 45;
					break;
				case "mythic":
					newLevel = 60;
					break;
				}
				lore.add(1, "§7Level Req: " + newLevel);
				
				// Remove slots
				ArrayList<ItemStack> itemsToReturn = new ArrayList<ItemStack>();
				ArrayList<Integer> slotsToParse = new ArrayList<Integer>();
				for (int i = 1; i <= 3; i++) {
					if (mUtils.isSlotUsed(item, i)) {
						int num = mUtils.getSlotNum(item, i);
						itemsToReturn.add(mUtils.parseUnslot(item, i, false));
						slotsToParse.add(i);
						lore.set(num, convertLevel(lore.get(num)));
					}
					else if (mUtils.isSlotAvailable(item, i)) {
						int num = mUtils.getSlotNum(item, i);
						lore.set(num, convertLevel(lore.get(num)));
					}
				}
				
				// Check if sufficient inventory spaces
				ItemStack[] inv = p.getInventory().getStorageContents();
				int empty = 0;
				for (int i = 0; i < inv.length; i++) {
					if (inv[i] == null) empty++;
				}
				
				if (empty >= itemsToReturn.size()) {
					ListIterator<String> iter = lore.listIterator();
					while (iter.hasNext()) {
						String line = iter.next();
						if (line.contains("Strength") && oldType.equals("bow")) {
							iter.remove();
						}
						if (line.contains("Endurance") && oldType.contains("infused")) {
							iter.remove();
						}
						if (line.contains("Tier:")) {
							iter.remove();
							iter.add("§7Tier: §9Rare " + oldType);
						}
					}
					meta.setLore(lore);
					item.setItemMeta(meta);
					// Parse slots
					for (int i : slotsToParse) {
						itemsToReturn.add(mUtils.parseUnslot(item, i, true));
					}
	
					// Change durability
					if (util.isWeapon(item)) {
						util.setMaxDurability(item, 1400);
					}
					else if (util.isArmor(item) && lore.get(0).contains("Reinforced")) { 
						util.setMaxDurability(item, 900);
					}
					else if (util.isArmor(item) && lore.get(0).contains("Infused")) { 
						util.setMaxDurability(item, 700);
					}
				}
			}
		}
		return item;
	}
	
	private String convertLevel(String line) {
		String args[] = line.split(" ");
		switch (args[1]) {
			case "1": args[1] = "5"; break;
			case "2": args[1] = "10"; break;
			case "3": args[1] = "20"; break;
			case "4": args[1] = "45"; break;
			case "5": args[1] = "60"; break;
		}
		String returned = args[0];
		for (int i = 1; i < args.length - 1; i++) {
			returned += args[i];
			returned += " ";
		}
		returned += args[args.length - 1];
		return returned;
	}
}
