package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class MasonMethods {
	
	Main main;
	Economy econ;
	
	// Constants
	static final int MAX_SLOTS = 3;
	static final int ENGRAVE_ESSENCE_PER_SLOT = 12;
	static final int ENGRAVE_GOLD_BASE = -10000;
	static final int ENGRAVE_GOLD_PER_LVL = 15000;
	
	// Prices
	static final int BASIC_EXP_GOLD = 5000;
	static final int BASIC_EXP_ESSENCE = 10;
	static final int BASIC_EXP_LEVEL = 2;
	static final int BASIC_DROP_GOLD = 10000;
	static final int BASIC_DROP_ESSENCE = 16;
	static final int BASIC_DROP_LEVEL = 3;
	static final int BASIC_LOOTING_GOLD = 25000;
	static final int BASIC_LOOTING_ESSENCE = 24;
	static final int BASIC_LOOTING_LEVEL = 2;
	static final int BASIC_TRAVELER_GOLD = 3000;
	static final int BASIC_TRAVELER_ESSENCE = 8;
	static final int BASIC_TRAVELER_LEVEL = 1;
	static final int BASIC_RECOVERY_GOLD = 10000;
	static final int BASIC_RECOVERY_ESSENCE = 16;
	static final int BASIC_RECOVERY_LEVEL = 2;

	static final int ADVANCED_HUNGER_GOLD = 25000;
	static final int ADVANCED_HUNGER_ESSENCE = 32;
	static final int ADVANCED_HUNGER_LEVEL = 4;
	static final int ADVANCED_EXP_GOLD = 5000;
	static final int ADVANCED_EXP_ESSENCE = 16;
	static final int ADVANCED_EXP_LEVEL = 3;
	static final int ADVANCED_DROP_GOLD = 10000;
	static final int ADVANCED_DROP_ESSENCE = 24;
	static final int ADVANCED_DROP_LEVEL = 4;
	static final int ADVANCED_LOOTING_GOLD = 50000;
	static final int ADVANCED_LOOTING_ESSENCE = 36;
	static final int ADVANCED_LOOTING_LEVEL = 3;
	static final int ADVANCED_SECONDCHANCE_GOLD = 25000;
	static final int ADVANCED_SECONDCHANCE_ESSENCE = 24;
	static final int ADVANCED_SECONDCHANCE_LEVEL = 4;
	
	
	public MasonMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}

	public void createSlot(Player p, int level) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (p.hasPermission("mason.engrave.tier." + level)) {
			int numSlots = MasonUtils.countSlots(item);
			if(numSlots < MAX_SLOTS) {
				if (p.hasPermission(("mason.engrave.max."  + (numSlots + 1)))) {
					if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), ENGRAVE_ESSENCE_PER_SLOT * (numSlots + 1))) {
						if(econ.has(p, ENGRAVE_GOLD_BASE + (ENGRAVE_GOLD_PER_LVL * level))) {
							MasonUtils.createSlot(item, level);
							p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), ENGRAVE_ESSENCE_PER_SLOT * (numSlots + 1)));
							econ.withdrawPlayer(p, ENGRAVE_GOLD_BASE + (ENGRAVE_GOLD_PER_LVL * level));
							Util.sendMessage(p, "&7Successfully created slot!");
						}
						else {
							Util.sendMessage(p, "&cYou lack the gold to create this!");
						}
					}
					else {
						Util.sendMessage(p, "&cYou lack the materials to create this!");
					}
				}
				else {
					Util.sendMessage(p, "&cYou do not yet have the required skill to create more slots!");
				}
			}
			else {
				Util.sendMessage(p, "&cThis item cannot have any more slots!");
			}
		}
		else {
			Util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
		}
	}

	public void createBasicCharm(Player p, String type) {
		if (p.hasPermission("mason.charm.basic")) {
			if (p.getInventory().firstEmpty() != -1) {
				int gold = 0;
				int essence = 0;
				int level = 0;
				switch (type) {
				case "exp":
					gold = BASIC_EXP_GOLD;
					essence = BASIC_EXP_ESSENCE;
					level = BASIC_EXP_LEVEL;
					break;
				case "drop":
					gold = BASIC_DROP_GOLD;
					essence = BASIC_DROP_ESSENCE;
					level = BASIC_DROP_LEVEL;
					break;
				case "looting":
					gold = BASIC_LOOTING_GOLD;
					essence = BASIC_LOOTING_ESSENCE;
					level = BASIC_LOOTING_LEVEL;
					break;
				case "traveler":
					gold = BASIC_TRAVELER_GOLD;
					essence = BASIC_TRAVELER_ESSENCE;
					level = BASIC_TRAVELER_LEVEL;
					break;
				case "recovery":
					gold = BASIC_RECOVERY_GOLD;
					essence = BASIC_RECOVERY_ESSENCE;
					level = BASIC_RECOVERY_LEVEL;
					break;
				}
				if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), essence)) {
					if(econ.has(p, gold)) {
						p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), essence));
						econ.withdrawPlayer(p, gold);
						Util.sendMessage(p, "&7Successfully created charm!");
					}
					else {
						Util.sendMessage(p, "&cYou lack the gold to create this!");
					}
				}
				else {
					Util.sendMessage(p, "&cYou lack the materials to create this!");
				}
			}
			else {
				Util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			Util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
		}
	}

	public void createAdvancedCharm(Player p, String type) {
		if (p.hasPermission("mason.charm.advanced")) {
			if (p.getInventory().firstEmpty() != -1) {
				int gold = 0;
				int essence = 0;
				int level = 0;
				switch (type) {
				case "exp":
					gold = ADVANCED_EXP_GOLD;
					essence = ADVANCED_EXP_ESSENCE;
					level = ADVANCED_EXP_LEVEL;
					break;
				case "drop":
					gold = ADVANCED_DROP_GOLD;
					essence = ADVANCED_DROP_ESSENCE;
					level = ADVANCED_DROP_LEVEL;
					break;
				case "looting":
					gold = ADVANCED_LOOTING_GOLD;
					essence = ADVANCED_LOOTING_ESSENCE;
					level = ADVANCED_LOOTING_LEVEL;
					break;
				case "hunger":
					gold = ADVANCED_HUNGER_GOLD;
					essence = ADVANCED_HUNGER_ESSENCE;
					level = ADVANCED_HUNGER_LEVEL;
					break;
				case "secondchance":
					gold = ADVANCED_SECONDCHANCE_GOLD;
					essence = ADVANCED_SECONDCHANCE_ESSENCE;
					level = ADVANCED_SECONDCHANCE_LEVEL;
					break;
				}
				if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), essence)) {
					if(econ.has(p, gold)) {
						p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), essence));
						econ.withdrawPlayer(p, gold);
						Util.sendMessage(p, "&7Successfully created charm!");
					}
					else {
						Util.sendMessage(p, "&cYou lack the gold to create this!");
					}
				}
				else {
					Util.sendMessage(p, "&cYou lack the materials to create this!");
				}
			}
			else {
				Util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			Util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
		}
	}
	
	public void slot(Player p, int slot) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(MasonUtils.isSlotAvailable(item, slot)) {
			
		}
		else {
			Util.sendMessage(p, "&cThis slot is unavailable!");
		}
	}

}
