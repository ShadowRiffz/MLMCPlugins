package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Items.MasonItems;
import me.Neoblade298.NeoProfessions.Listeners.MasonListeners;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class MasonMethods {
	
	Main main;
	Economy econ;
	MasonListeners listeners;
	
	// Constants
	static final int MAX_SLOTS = 3;
	static final int ENGRAVE_ESSENCE_PER_SLOT = 12;
	static final int ENGRAVE_GOLD_BASE = -10000;
	static final int ENGRAVE_GOLD_PER_LVL = 15000;
	static final int UNENGRAVE_GOLD = 2000;
	static final int UNENGRAVE_ESSENCE = 2;
	static final int UNSLOT_ESSENCE = 4;
	static final int UNSLOT_GOLD_PER_LVL = 5000;
	
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
		this.listeners = main.masonListeners;
	}

	public void createSlot(Player p, int level) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (p.hasPermission("mason.engrave.tier." + level)) {
			if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
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
		else {
			Util.sendMessage(p, "&cThis item cannot have slots!");
		}
	}

	public void createBasicCharm(Player p, String type) {
		if (p.hasPermission("mason.charm.basic")) {
			if (p.getInventory().firstEmpty() != -1) {
				int gold = 0;
				int essence = 0;
				int level = 0;
				ItemStack item = null;
				switch (type) {
				case "exp":
					gold = BASIC_EXP_GOLD;
					essence = BASIC_EXP_ESSENCE;
					level = BASIC_EXP_LEVEL;
					item = MasonItems.getExpCharm(false);
					break;
				case "drop":
					gold = BASIC_DROP_GOLD;
					essence = BASIC_DROP_ESSENCE;
					level = BASIC_DROP_LEVEL;
					item = MasonItems.getDropCharm(false);
					break;
				case "looting":
					gold = BASIC_LOOTING_GOLD;
					essence = BASIC_LOOTING_ESSENCE;
					level = BASIC_LOOTING_LEVEL;
					item = MasonItems.getLootingCharm(false);
					break;
				case "traveler":
					gold = BASIC_TRAVELER_GOLD;
					essence = BASIC_TRAVELER_ESSENCE;
					level = BASIC_TRAVELER_LEVEL;
					item = MasonItems.getTravelerCharm();
					break;
				case "recovery":
					gold = BASIC_RECOVERY_GOLD;
					essence = BASIC_RECOVERY_ESSENCE;
					level = BASIC_RECOVERY_LEVEL;
					item = MasonItems.getRecoveryCharm();
					break;
				}
				if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), essence)) {
					if(econ.has(p, gold)) {
						p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), essence));
						econ.withdrawPlayer(p, gold);
						p.getInventory().addItem(item);
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
				ItemStack item = null;
				switch (type) {
				case "exp":
					gold = ADVANCED_EXP_GOLD;
					essence = ADVANCED_EXP_ESSENCE;
					level = ADVANCED_EXP_LEVEL;
					item = MasonItems.getExpCharm(true);
					break;
				case "drop":
					gold = ADVANCED_DROP_GOLD;
					essence = ADVANCED_DROP_ESSENCE;
					level = ADVANCED_DROP_LEVEL;
					item = MasonItems.getDropCharm(true);
					break;
				case "looting":
					gold = ADVANCED_LOOTING_GOLD;
					essence = ADVANCED_LOOTING_ESSENCE;
					level = ADVANCED_LOOTING_LEVEL;
					item = MasonItems.getLootingCharm(true);
					break;
				case "hunger":
					gold = ADVANCED_HUNGER_GOLD;
					essence = ADVANCED_HUNGER_ESSENCE;
					level = ADVANCED_HUNGER_LEVEL;
					item = MasonItems.getHungerCharm();
					break;
				case "secondchance":
					gold = ADVANCED_SECONDCHANCE_GOLD;
					essence = ADVANCED_SECONDCHANCE_ESSENCE;
					level = ADVANCED_SECONDCHANCE_LEVEL;
					item = MasonItems.getSecondChanceCharm();
					break;
				}
				if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), essence)) {
					if(econ.has(p, gold)) {
						p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), essence));
						econ.withdrawPlayer(p, gold);
						p.getInventory().addItem(item);
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
		if(!item.equals(Material.AIR)) {
			if(MasonUtils.isSlotAvailable(item, slot)) {
				if(Util.isArmor(item)) {
					int level = Util.getItemLevel(item);
					if(p.hasPermission("mason.slot.armor." + level)) {
						listeners.prepItemSlot(p, item, slot);
					}
					else {
						Util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
					}
				}
				else if(Util.isWeapon(item)) {
					int level = Util.getItemLevel(item);
					if(p.hasPermission("mason.slot.weapon." + level)) {
						listeners.prepItemSlot(p, item, slot);
					}
					else {
						Util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
					}
				}
				else {
					Util.sendMessage(p, "&cThis item cannot be slotted!");
				}
			}
			else {
				Util.sendMessage(p, "&cThis slot is unavailable!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void unslot(Player p, int slot) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.equals(Material.AIR)) {
			if(MasonUtils.isSlotUsed(item, slot)) {
				int level = Util.getItemLevel(item);
				if(p.hasPermission("mason.unslot." + level)) {
					if(p.getInventory().firstEmpty() != -1) {
						if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), UNSLOT_ESSENCE)) {
							if(econ.has(p, UNSLOT_GOLD_PER_LVL * level)) {
								ItemStack returned = MasonUtils.parseUnslot(p, slot);
								if(returned != null) {
									p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), UNSLOT_ESSENCE));
									econ.withdrawPlayer(p, UNSLOT_GOLD_PER_LVL * level);
									p.getInventory().addItem(returned);
									Util.sendMessage(p, "&cSuccessfully unslotted item!");
								}
							}
							else {
								Util.sendMessage(p, "&cYou lack the gold to do this!");
							}
						}
						else {
							Util.sendMessage(p, "&cYou lack the materials to do this!");
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
			else {
				Util.sendMessage(p, "&cThis slot is unused or does not exist!!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void removeSlot(Player p, int slot) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.equals(Material.AIR)) {
			if(MasonUtils.isSlotAvailable(item, slot)) {
				int level = Util.getItemLevel(item);
				if(level != -1) {
					if(p.hasPermission("mason.engrave." + level)) {
						if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), UNENGRAVE_ESSENCE)) {
							if(econ.has(p, UNENGRAVE_GOLD)) {
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), UNENGRAVE_ESSENCE));
								econ.withdrawPlayer(p, UNENGRAVE_GOLD);
								MasonUtils.removeSlotLine(item, slot);
								Util.sendMessage(p, "&7Successfully removed slot!");
							}
							else {
								Util.sendMessage(p, "&cYou lack the gold to do this!");
							}
						}
						else {
							Util.sendMessage(p, "&cYou lack the materials to do this!");
						}
					}
					else {
						Util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
					}
				}
				else {
					Util.sendMessage(p, "&cInvalid item!");
				}
			}
			else {
				Util.sendMessage(p, "&cSlot either doesn't exist or is in use!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}

}
