package me.Neoblade298.NeoProfessions.Legacy;

import java.util.ArrayList;
import java.util.ListIterator;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
	CommonItems cItems;
	Util util;
	
	
	public Converter(Main main) {
		this.main = main;
		bItems = new BlacksmithItems();
		sItems = new StonecutterItems();
		mItems = new MasonItems();
		oldMItems = new MasonItemsLegacy();
		cItems = new CommonItems();
		util = new Util();
	}

	public ItemStack convertItem(ItemStack item) {
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getEnchantmentLevel(Enchantment.DURABILITY) < 10) {
			ItemMeta meta = item.getItemMeta();
			ArrayList<String> lore = (ArrayList<String>) meta.getLore();
			String idLine = meta.getLore().get(0);
			
			if (idLine.contains("Right click")) {	// Repair kit
				return convertRepairKit(lore, item.getAmount());
			}
			else if (idLine.contains("Durability")) {
				return convertDurabilityItem(lore, item.getAmount());
			}
			else if (idLine.contains("Essence")) {
				return convertEssence(lore, item.getAmount());
			}
			else if (idLine.contains("Ore")) {
				return convertOre(lore, item.getAmount());
			}
			else if (idLine.contains("Gem")) {
				return convertGem(lore, item.getAmount());
			}
			else if (idLine.contains("Charm")) {
				return convertCharm(item, lore);
			}
		}
		return item;
	}
	
	private ItemStack convertRepairKit(ArrayList<String> lore, int amount) {
		int potency = Integer.parseInt(lore.get(4).split(" ")[1].substring(2,4));
		int oldLevel = ((potency % 25) / 5) + 1;
		int newLevel = (oldLevel + 1) * 10;
		return bItems.getRepairItem(newLevel);
	}
	
	private ItemStack convertDurabilityItem(ArrayList<String> lore, int amount) {
		String type = lore.get(1).split(" ")[2];
		int oldLevel = Integer.parseInt(lore.get(0).split(" ")[1].replaceAll("§e", ""));
		int newLevel = (oldLevel + 1) * 10;
		int potency = Integer.parseInt(lore.get(2).split(" ")[1].replaceAll("§e", ""));
		return util.setAmount(bItems.getDurabilityItem(newLevel, type, potency), amount);
	}
	
	private ItemStack convertEssence(ArrayList<String> lore, int amount) {
		int oldLevel = Integer.parseInt(lore.get(0).split(" ")[1]);
		int newLevel = (oldLevel + 1) * 10;
		return util.setAmount(cItems.getEssence(newLevel, true), amount);
	}
	
	private ItemStack convertOre(ArrayList<String> lore, int amount) {
		String oreName = lore.get(0).split(" ")[2].replaceAll("§e", "");
		String type = null;
		switch (oreName) {
		case "Ruby": 
			type = "strength";
			break;
		case "Amethyst":
			type = "dexterity";
			break;
		case "Sapphire":
			type = "intelligence";
			break;
		case "Emerald":
			type = "spirit";
			break;
		case "Topaz":
			type = "perception";
			break;
		case "Garnet":
			type = "vitality";
			break;
		case "Adamantium":
			type = "endurance";
			break;
		}
		int oldLevel = Integer.parseInt(lore.get(0).split(" ")[1].replaceAll("§e", ""));
		int newLevel = (oldLevel + 1) * 10;
		return util.setAmount(sItems.getOre(type, newLevel), amount);
	}
	
	private ItemStack convertGem(ArrayList<String> lore, int amount) {
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
			return util.setAmount(sItems.getWeaponGem(type, newLevel, isOverloaded), amount);
		}
		else {
			return util.setAmount(sItems.getArmorGem(type, newLevel, isOverloaded), amount);
		}
	}
	
	private ItemStack convertCharm(ItemStack item, ArrayList<String> lore) {
		if (item.isSimilar(oldMItems.getDropCharm(false))) {
			return util.setAmount(mItems.getDropCharm(false), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getDropCharm(true))) {
			return util.setAmount(mItems.getDropCharm(true), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getExpCharm(false))) {
			return util.setAmount(mItems.getExpCharm(false), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getExpCharm(true))) {
			return util.setAmount(mItems.getExpCharm(true), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getHungerCharm())) {
			return util.setAmount(mItems.getHungerCharm(), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getLootingCharm(false))) {
			return util.setAmount(mItems.getLootingCharm(false), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getLootingCharm(true))) {
			return util.setAmount(mItems.getLootingCharm(true), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getQuickEatCharm())) {
			return util.setAmount(mItems.getQuickEatCharm(), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getRecoveryCharm())) {
			return util.setAmount(mItems.getRecoveryCharm(), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getSecondChanceCharm())) {
			return util.setAmount(mItems.getSecondChanceCharm(), item.getAmount());
		}
		else if (item.isSimilar(oldMItems.getTravelerCharm())) {
			return util.setAmount(mItems.getTravelerCharm(), item.getAmount());
		}
		return null;
	}
	
	private ItemStack convertGear(ItemStack item, ItemMeta meta, ArrayList<String> lore) {
		// First change durability
		if (util.isWeapon(item)) {
			util.setMaxDurability(item, 1400);
		}
		else if (util.isArmor(item) && lore.get(0).contains("Reinforced")) { 
			util.setMaxDurability(item, 900);
		}
		else if (util.isArmor(item) && lore.get(0).contains("Infused")) { 
			util.setMaxDurability(item, 700);
		}
		
		String oldRarity = util.getItemRarity(item);
		String oldTier = util.getItemType(item);
		int newLevel = 0;
		
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
		
		ListIterator<String> iter = lore.listIterator();
		while (iter.hasNext()) {
			String line = iter.next();
			if (line.contains("Strength") && oldTier.equals("Bow")) {
				iter.remove();
			}
			if (line.contains("Endurance") && oldTier.equals("Infused")) {
				iter.remove();
			}
			if (line.contains("Tier:")) {
				iter.remove();
				iter.add("//TODO");
			}
		}
		return item;
	}
}
