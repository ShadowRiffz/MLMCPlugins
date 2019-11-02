package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sucy.skill.SkillAPI;

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
	MasonUtils masonUtils;
	Util util;
	CommonItems common;
	MasonItems mItems;
	
	// Constants
	static final int MAX_SLOTS = 3;
	static final int ENGRAVE_ESSENCE_BASE = 12;
	static final int ENGRAVE_ESSENCE_PER_SLOT = 6;
	static final int ENGRAVE_GOLD_BASE = 10000;
	static final int ENGRAVE_GOLD_PER_LVL = 5000;
	static final int UNENGRAVE_GOLD = 2000;
	static final int UNENGRAVE_ESSENCE = 2;
	static final int UNSLOT_ESSENCE = 4;
	static final int UNSLOT_GOLD_PER_LVL = 1500;
	
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
	static final int ADVANCED_SECONDCHANCE_GOLD = 5000;
	static final int ADVANCED_SECONDCHANCE_ESSENCE = 16;
	static final int ADVANCED_SECONDCHANCE_LEVEL = 3;
	static final int ADVANCED_QUICKEAT_GOLD = 18000;
	static final int ADVANCED_QUICKEAT_ESSENCE = 18;
	static final int ADVANCED_QUICKEAT_LEVEL = 3;
	
	
	public MasonMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
		this.listeners = main.masonListeners;
		masonUtils = new MasonUtils();
		mItems = new MasonItems();
		common = new CommonItems();
		util = new Util();
	}

	public void createSlot(Player p, int level) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
			if(p.hasPermission("mason.engrave.tier." + level)) {
				if(util.getItemLevel(item) >= level) {
					int numSlots = masonUtils.countSlots(item);
					if(numSlots < MAX_SLOTS) {
						if (p.hasPermission(("mason.engrave.max."  + (numSlots + 1)))) {
							if(p.getInventory().containsAtLeast(common.getEssence(level), ENGRAVE_ESSENCE_BASE + (ENGRAVE_ESSENCE_PER_SLOT * numSlots))) {
								if(econ.has(p, ENGRAVE_GOLD_BASE + (ENGRAVE_GOLD_PER_LVL * numSlots))) {
									masonUtils.createSlot(item, level);
									p.getInventory().removeItem(util.setAmount(common.getEssence(level), ENGRAVE_ESSENCE_BASE + (ENGRAVE_ESSENCE_PER_SLOT * numSlots)));
									econ.withdrawPlayer(p, ENGRAVE_GOLD_BASE + (ENGRAVE_GOLD_PER_LVL * numSlots));
									util.sendMessage(p, "&7Successfully created slot!");
								}
								else {
									util.sendMessage(p, "&cYou lack the gold to create this!");
								}
							}
							else {
								util.sendMessage(p, "&cYou lack the materials to create this!");
							}
						}
						else {
							util.sendMessage(p, "&cYou do not yet have the required skill to create more slots!");
						}
					}
					else {
						util.sendMessage(p, "&cThis item cannot have any more slots!");
					}
				}
				else {
					util.sendMessage(p, "&cThis item is not capable of holding a slot of this level!");
				}
			}
			else {
				util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
			}
		}
		else {
			util.sendMessage(p, "&cThis item cannot have slots!");
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
					item = mItems.getExpCharm(false);
					break;
				case "drop":
					gold = BASIC_DROP_GOLD;
					essence = BASIC_DROP_ESSENCE;
					level = BASIC_DROP_LEVEL;
					item = mItems.getDropCharm(false);
					break;
				case "looting":
					gold = BASIC_LOOTING_GOLD;
					essence = BASIC_LOOTING_ESSENCE;
					level = BASIC_LOOTING_LEVEL;
					item = mItems.getLootingCharm(false);
					break;
				case "traveler":
					gold = BASIC_TRAVELER_GOLD;
					essence = BASIC_TRAVELER_ESSENCE;
					level = BASIC_TRAVELER_LEVEL;
					item = mItems.getTravelerCharm();
					break;
				case "recovery":
					gold = BASIC_RECOVERY_GOLD;
					essence = BASIC_RECOVERY_ESSENCE;
					level = BASIC_RECOVERY_LEVEL;
					item = mItems.getRecoveryCharm();
					break;
				}
				if(p.getInventory().containsAtLeast(common.getEssence(level), essence)) {
					if(econ.has(p, gold)) {
						p.getInventory().removeItem(util.setAmount(common.getEssence(level), essence));
						econ.withdrawPlayer(p, gold);
						p.getInventory().addItem(item);
						util.sendMessage(p, "&7Successfully created charm!");
					}
					else {
						util.sendMessage(p, "&cYou lack the gold to create this!");
					}
				}
				else {
					util.sendMessage(p, "&cYou lack the materials to create this!");
				}
			}
			else {
				util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
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
					item = mItems.getExpCharm(true);
					break;
				case "drop":
					gold = ADVANCED_DROP_GOLD;
					essence = ADVANCED_DROP_ESSENCE;
					level = ADVANCED_DROP_LEVEL;
					item = mItems.getDropCharm(true);
					break;
				case "looting":
					gold = ADVANCED_LOOTING_GOLD;
					essence = ADVANCED_LOOTING_ESSENCE;
					level = ADVANCED_LOOTING_LEVEL;
					item = mItems.getLootingCharm(true);
					break;
				case "hunger":
					gold = ADVANCED_HUNGER_GOLD;
					essence = ADVANCED_HUNGER_ESSENCE;
					level = ADVANCED_HUNGER_LEVEL;
					item = mItems.getHungerCharm();
					break;
				case "secondchance":
					gold = ADVANCED_SECONDCHANCE_GOLD;
					essence = ADVANCED_SECONDCHANCE_ESSENCE;
					level = ADVANCED_SECONDCHANCE_LEVEL;
					item = mItems.getSecondChanceCharm();
					break;
				case "quickeat":
					gold = ADVANCED_QUICKEAT_GOLD;
					essence = ADVANCED_QUICKEAT_ESSENCE;
					level = ADVANCED_QUICKEAT_LEVEL;
					item = mItems.getQuickEatCharm();
					break;
				}
				if(p.getInventory().containsAtLeast(common.getEssence(level), essence)) {
					if(econ.has(p, gold)) {
						p.getInventory().removeItem(util.setAmount(common.getEssence(level), essence));
						econ.withdrawPlayer(p, gold);
						p.getInventory().addItem(item);
						util.sendMessage(p, "&7Successfully created charm!");
					}
					else {
						util.sendMessage(p, "&cYou lack the gold to create this!");
					}
				}
				else {
					util.sendMessage(p, "&cYou lack the materials to create this!");
				}
			}
			else {
				util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
		}
	}
	
	public void slot(Player p, int slot) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.getType().equals(Material.AIR)) {
			if(masonUtils.isSlotAvailable(item, slot)) {
				if(util.isArmor(item)) {
					int level = util.getItemLevel(item);
					if(p.hasPermission("mason.slot.armor." + level)) {
						listeners.prepItemSlot(p, item, slot);
					}
					else {
						util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
					}
				}
				else if(util.isWeapon(item)) {
					int level = util.getItemLevel(item);
					if(p.hasPermission("mason.slot.weapon." + level)) {
						listeners.prepItemSlot(p, item, slot);
					}
					else {
						util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
					}
				}
				else {
					util.sendMessage(p, "&cThis item cannot be slotted!");
				}
			}
			else {
				util.sendMessage(p, "&cThis slot is unavailable!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void unslot(Player p, int slot) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.getType().equals(Material.AIR)) {
			if(masonUtils.isSlotUsed(item, slot)) {
				int level = util.getItemLevel(item);
				String line = masonUtils.getSlotLine(item, slot);
				int slottedLevel = Character.getNumericValue(line.charAt(3));
				if(p.hasPermission("mason.unslot." + level)) {
					if(p.getInventory().firstEmpty() != -1) {
						if(p.getInventory().containsAtLeast(common.getEssence(slottedLevel), UNSLOT_ESSENCE)) {
							if(econ.has(p, UNSLOT_GOLD_PER_LVL * level)) {
								ItemStack returned = masonUtils.parseUnslot(p, slot);
								if(returned != null) {
									p.getInventory().removeItem(util.setAmount(common.getEssence(slottedLevel), UNSLOT_ESSENCE));
									econ.withdrawPlayer(p, UNSLOT_GOLD_PER_LVL * level);
									p.getInventory().addItem(returned);
									util.sendMessage(p, "&cSuccessfully unslotted item!");
								}
							}
							else {
								util.sendMessage(p, "&cYou lack the gold to do this!");
							}
						}
						else {
							util.sendMessage(p, "&cYou lack the materials to do this!");
						}
					}
					else {
						util.sendMessage(p, "&cYour inventory is full!");
					}
				}
				else {
					util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
				}
			}
			else {
				util.sendMessage(p, "&cThis slot is unused or does not exist!!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void removeSlot(Player p, int slot) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.getType().equals(Material.AIR)) {
			if(masonUtils.isSlotAvailable(item, slot)) {
				int level = util.getItemLevel(item);
				if(level != -1) {
					if(p.hasPermission("mason.engrave.tier." + level)) {
						if(p.getInventory().containsAtLeast(common.getEssence(level), UNENGRAVE_ESSENCE)) {
							if(econ.has(p, UNENGRAVE_GOLD)) {
								p.getInventory().removeItem(util.setAmount(common.getEssence(level), UNENGRAVE_ESSENCE));
								econ.withdrawPlayer(p, UNENGRAVE_GOLD);
								masonUtils.removeSlotLine(item, slot);
								util.sendMessage(p, "&7Successfully removed slot!");
							}
							else {
								util.sendMessage(p, "&cYou lack the gold to do this!");
							}
						}
						else {
							util.sendMessage(p, "&cYou lack the materials to do this!");
						}
					}
					else {
						util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
					}
				}
				else {
					util.sendMessage(p, "&cInvalid item!");
				}
			}
			else {
				util.sendMessage(p, "&cSlot either doesn't exist or is in use!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void resetPlayer(Player p) {
		String name = p.getName();
		
		// Clean out perms
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.professed");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.profess.account");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.tier.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.tier.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.tier.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.tier.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.tier.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.max.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.max.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.max.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.max.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.engrave.max.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.armor.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.armor.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.armor.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.armor.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.armor.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.weapon.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.weapon.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.weapon.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.weapon.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.slot.weapon.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.unslot.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.unslot.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.unslot.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.unslot.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.unslot.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.charm.basic");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove mason.charm.advanced");
		
		// Reset profession
		SkillAPI.getPlayerData(p).reset("profession");
	}

}
