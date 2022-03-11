package me.Neoblade298.NeoProfessions.Utilities;

import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.lumine.xikage.mythicmobs.MythicMobs;
import me.Neoblade298.NeoProfessions.Legacy.BlacksmithItems;
import me.Neoblade298.NeoProfessions.Legacy.MasonItems;
import me.Neoblade298.NeoProfessions.Legacy.StonecutterItems;

public class MasonUtils {

	final int LEVEL_INTERVAL = 5;
	Util util;
	BlacksmithItems bItems;
	StonecutterItems sItems;
	MasonItems mItems;

	public MasonUtils() {
		util = new Util();
		bItems = new BlacksmithItems();
		sItems = new StonecutterItems();
		mItems = new MasonItems();
	}

	public int getAugmentLevel(ItemStack item) {
		return item.getEnchantmentLevel(Enchantment.DURABILITY);
	}

	public void createSlot(ItemStack item, int level) {
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		boolean hasBonus = false;
		int slotLine = -1;
		for (int i = 0; i < lore.size(); i++) {
			if (lore.get(i).contains("Bonus")) {
				hasBonus = true;
			}
			if (lore.get(i).contains("Durability")) {
				slotLine = i;
			}
		}

		lore.add(slotLine, "§8(Lv " + level + " Slot)");
		if (!hasBonus) {
			lore.add(slotLine, "§9[Bonus Attributes]");
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public String getSlotLine(ItemStack item, int slot) {
		ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
		int count = 0;
		boolean hasBonus = false;
		for (String line : lore) {
			if (!hasBonus) {
				if (line.contains("Bonus")) {
					hasBonus = true;
				}
			} else {
				count++;
				if (slot == count) {
					return line;
				}
			}
		}
		return null;
	}

	public String getSlotLineAttribute(String line) {
		if (line.contains("Strength")) {
			return "strength";
		} else if (line.contains("Dexterity")) {
			return "dexterity";
		} else if (line.contains("Intelligence")) {
			return "intelligence";
		} else if (line.contains("Spirit")) {
			return "spirit";
		} else if (line.contains("Perception")) {
			return "perception";
		} else if (line.contains("Endurance")) {
			return "endurance";
		} else if (line.contains("Vitality")) {
			return "vitality";
		} else {
			return null;
		}
	}

	public ItemStack parseUnslot(Player p, int slot) {
		ItemStack item = p.getInventory().getItemInMainHand();
		String line = getSlotLine(item, slot);
		String[] words = line.split(" ");
		int level = util.getItemLevel(item);

		// Parse the line and revert the lore
		// chars 1 3 5 = slotLevel, chars 7 9 11 = slottedLevel, char 13 = slotType,
		// chars 15 17 19 = durability loss
		// slotType: 0 = Durability, 1 = attribute, 2 = overload, 3 = charm
		char color = line.charAt(1);
		int slottedLevel = level;
		int slotType = -1;
		String attr = getSlotLineAttribute(line);
		boolean isArmor = Util.isArmor(item);
		int potency = -1;

		if (line.contains("Durability")) {
			slotType = 0;
		}
		else if (StringUtils.isNumeric(words[1].substring(1))) {
			if (color == '9') {
				slotType = 1;
			}
			else {
				slotType = 2;
			}
		}
		else if (line.contains("Charm")) {
			slotType = 3;
		}
		else if (line.contains("Relic")) {
			slotType = 4;
		}

		switch (slotType) {
		// Max durability
		case 0:
			potency = Integer.parseInt(line.substring(line.indexOf("+") + 1, line.length()));
			if (isArmor) {
				return bItems.getDurabilityItem(slottedLevel, "armor", potency);
			} else {
				return bItems.getDurabilityItem(slottedLevel, "weapon", potency);
			}
			// Normal gem
		case 1:
			potency = Integer.parseInt(line.substring(line.indexOf("+") + 1, line.length()));
			if (isArmor) {
				return sItems.getArmorGem(attr, slottedLevel, false, potency);
			} else {
				return sItems.getWeaponGem(attr, slottedLevel, false, potency);
			}

			// Overload gem
		case 2:
			potency = Integer.parseInt(line.substring(line.indexOf("+") + 1, line.length()));
			if (isArmor) {
				return sItems.getArmorGem(attr, slottedLevel, true, potency);
			} else {
				return sItems.getWeaponGem(attr, slottedLevel, true, potency);
			}

			// Charm
		case 3:
			if (line.contains("Advanced")) {
				if (line.contains("Exp")) {
					return mItems.getExpCharm(true);
				} else if (line.contains("Drop")) {
					return mItems.getDropCharm(true);
				} else if (line.contains("Looting")) {
					return mItems.getLootingCharm(true);
				}
			} else {
				if (line.contains("Exp")) {
					return mItems.getExpCharm(false);
				} else if (line.contains("Drop")) {
					return mItems.getDropCharm(false);
				} else if (line.contains("Looting")) {
					return mItems.getLootingCharm(false);
				} else if (line.contains("Recovery")) {
					return mItems.getRecoveryCharm();
				} else if (line.contains("Traveler")) {
					return mItems.getTravelerCharm();
				} else if (line.contains("Second Chance")) {
					return mItems.getSecondChanceCharm();
				} else if (line.contains("Hunger")) {
					return mItems.getHungerCharm();
				} else if (line.contains("Quick Eat")) {
					return mItems.getQuickEatCharm();
				}
			}
			// Relic
		case 4:
			String[] relicStrings = line.split(" ");
			String relic = "Relic" + relicStrings[2];
			for (int i = 3; i < relicStrings.length; i++) {
				relic += relicStrings[i];
			}
			return MythicMobs.inst().getItemManager().getItemStack(relic);
		}
		return null;
	}
}
