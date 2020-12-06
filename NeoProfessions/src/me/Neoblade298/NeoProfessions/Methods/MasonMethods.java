package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sucy.skill.SkillAPI;

import me.Neoblade298.NeoProfessions.CurrencyManager;
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
	CurrencyManager cm;

	// Constants
	static final int MAX_SLOTS = 3;
	static final int ENGRAVE_ESSENCE_BASE = 12;
	static final int ENGRAVE_ESSENCE_PER_SLOT = 12;
	static final int ENGRAVE_GOLD_BASE = 10000;
	static final int ENGRAVE_GOLD_PER_LVL = 5000;
	static final int UNENGRAVE_GOLD = 2000;
	static final int UNENGRAVE_ESSENCE = 2;
	static final int UNSLOT_ESSENCE = 4;
	static final int UNSLOT_GOLD_PER_LVL = 1500;
	static final int LEVEL_INTERVAL = 5;

	// Prices
	static final int BASIC_EXP_GOLD = 5000;
	static final int BASIC_EXP_ESSENCE = 10;
	static final int BASIC_EXP_LEVEL = 30;
	static final int BASIC_DROP_GOLD = 10000;
	static final int BASIC_DROP_ESSENCE = 16;
	static final int BASIC_DROP_LEVEL = 40;
	static final int BASIC_LOOTING_GOLD = 25000;
	static final int BASIC_LOOTING_ESSENCE = 24;
	static final int BASIC_LOOTING_LEVEL = 30;
	static final int BASIC_TRAVELER_GOLD = 3000;
	static final int BASIC_TRAVELER_ESSENCE = 8;
	static final int BASIC_TRAVELER_LEVEL = 20;
	static final int BASIC_RECOVERY_GOLD = 10000;
	static final int BASIC_RECOVERY_ESSENCE = 16;
	static final int BASIC_RECOVERY_LEVEL = 30;

	static final int ADVANCED_HUNGER_GOLD = 25000;
	static final int ADVANCED_HUNGER_ESSENCE = 32;
	static final int ADVANCED_HUNGER_LEVEL = 50;
	static final int ADVANCED_EXP_GOLD = 5000;
	static final int ADVANCED_EXP_ESSENCE = 16;
	static final int ADVANCED_EXP_LEVEL = 40;
	static final int ADVANCED_DROP_GOLD = 10000;
	static final int ADVANCED_DROP_ESSENCE = 24;
	static final int ADVANCED_DROP_LEVEL = 50;
	static final int ADVANCED_LOOTING_GOLD = 50000;
	static final int ADVANCED_LOOTING_ESSENCE = 36;
	static final int ADVANCED_LOOTING_LEVEL = 40;
	static final int ADVANCED_SECONDCHANCE_GOLD = 5000;
	static final int ADVANCED_SECONDCHANCE_ESSENCE = 16;
	static final int ADVANCED_SECONDCHANCE_LEVEL = 40;
	static final int ADVANCED_QUICKEAT_GOLD = 18000;
	static final int ADVANCED_QUICKEAT_ESSENCE = 18;
	static final int ADVANCED_QUICKEAT_LEVEL = 40;

	public MasonMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
		this.listeners = main.masonListeners;
		masonUtils = new MasonUtils();
		mItems = new MasonItems();
		common = new CommonItems();
		util = new Util();
		cm = main.cManager;
	}

	public void giveSlot(Player p, int maxlevel, int maxslots, int gold) {
		ItemStack item = p.getInventory().getItemInMainHand();
		int level = util.getItemLevel(item);
		if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
			if (util.isGearReworked(item)) {
				if (util.getItemLevel(item) <= maxlevel) {
					int numSlots = masonUtils.countSlots(item);
					if (numSlots < maxslots) {
						if (econ.has(p, gold)) {
							masonUtils.createSlot(item, level);
							econ.withdrawPlayer(p, gold);
							util.sendMessage(p, "&7Successfully created slot!");
						}
						else {
							util.sendMessage(p, "&cYou lack the gold to purchase this!");
						}
					}
					else {
						util.sendMessage(p, "&cYou cannot buy any more slots!");
					}
				}
				else {
					util.sendMessage(p, "&cThis item is too high level!");
				}
			}
			else {
				util.sendMessage(p, "&cItem is no longer supported by the server!");
			}
		}
		else {
			util.sendMessage(p, "&cThis item cannot have slots!");
		}
	}

	public void setSlot(Player p, int maxlevel, int slot, int gold) {
		ItemStack itemWithSlot = p.getInventory().getItemInMainHand();
		ItemStack itemToSlot = p.getInventory().getItemInOffHand();
		if (!itemWithSlot.getType().equals(Material.AIR)) {
			if (!itemToSlot.getType().equals(Material.AIR)) {
				if (masonUtils.isSlotAvailable(itemWithSlot, slot)) {
					int slotLevel = masonUtils.getSlotLevel(slot, itemWithSlot);

					String slotType = masonUtils.slotType(itemToSlot);
					if (slotType != null) {
						if (masonUtils.isGearReworked(itemToSlot)) {
							if (masonUtils.getAugmentLevel(itemToSlot) == slotLevel
									|| (itemToSlot.getType().equals(Material.PRISMARINE_CRYSTALS)
											&& masonUtils.getAugmentLevel(itemToSlot) <= slotLevel)) {
								int level = util.getItemLevel(itemWithSlot);
								if (level <= maxlevel) {
									if (masonUtils.getAugmentLevel(itemToSlot) == slotLevel ||
											(itemToSlot.getType().equals(Material.PRISMARINE_CRYSTALS) && masonUtils.getAugmentLevel(itemToSlot) <= slotLevel)) {
										if ((util.isArmor(itemWithSlot) && slotType.equalsIgnoreCase("aattribute"))
												|| (util.isWeapon(itemWithSlot) && slotType.equalsIgnoreCase("wattribute"))
												|| !(slotType.equalsIgnoreCase("aattribute")
														|| slotType.equalsIgnoreCase("wattribute"))) {
											if (econ.has(p, gold)) {
												boolean success = false;
												switch (slotType) {
												case "durability":
													success = masonUtils.parseDurability(itemWithSlot, itemToSlot, slot);
													break;
												case "wattribute":
													success = masonUtils.parseAttribute(itemWithSlot, itemToSlot, slot);
													break;
												case "aattribute":
													success = masonUtils.parseAttribute(itemWithSlot, itemToSlot, slot);
													break;
												case "overload":
													success = masonUtils.parseOverload(itemWithSlot, itemToSlot, slot);
													break;
												case "charm":
													success = masonUtils.parseCharm(p, itemWithSlot, itemToSlot, slot);
													break;
												case "relic":
													if (masonUtils.hasRelic(itemWithSlot)) {
														success = masonUtils.parseRelic(p, itemWithSlot, itemToSlot, slot);
													}
													else {
														util.sendMessage(p, "&cOnly one relic may be sloted per item!");
													}
													break;
												}
												if (success) {
													itemToSlot.setAmount(itemToSlot.getAmount() - 1);
													econ.withdrawPlayer(p, gold);
													util.sendMessage(p, "&7Successfully slotted item!");
												}
												else {
													util.sendMessage(p, "&cFailed to slot item!");
												}
											}
											else {
												util.sendMessage(p, "&cYou lack the gold to do this!");
											}
										}
										else {
											util.sendMessage(p, "&cThis augment is incompatible with this item type!");
										}
									}
									else {
										util.sendMessage(p, "&cThis item must be the same level as this slot (or be a charm)!");
									}
								}
								else {
									util.sendMessage(p, "&cThis item is too high level!");
								}
							}
							else {
								util.sendMessage(p, "&cThis item must be the same level as this slot (or be a charm)!");
							}
						}
						else {
							util.sendMessage(p, "&cItem is no longer supported by the server!");
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
				util.sendMessage(p, "&cOffhand is empty!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void createSlot(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		int level = util.getItemLevel(item);

		int perm = ((level - LEVEL_INTERVAL) / 10) - 1;
		if (perm < 1)
			perm = 1;
		if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
			if (util.isGearReworked(item)) {
				if (p.hasPermission("mason.engrave.tier." + perm)) {
					int numSlots = masonUtils.countSlots(item);
					if (numSlots < MAX_SLOTS) {
						if (p.hasPermission(("mason.engrave.max." + (numSlots + 1)))) {
							int essence = ENGRAVE_ESSENCE_BASE + (ENGRAVE_ESSENCE_PER_SLOT * numSlots);
							if (cm.hasEnough(p, "essence", level, essence)) {
								if (econ.has(p, ENGRAVE_GOLD_BASE + (ENGRAVE_GOLD_PER_LVL * numSlots))) {
									masonUtils.createSlot(item, level);
									cm.subtract(p, "essence", level, essence);
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
					util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
				}
			}
			else {
				util.sendMessage(p, "&cItem is no longer supported by the server!");
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
				if (cm.hasEnough(p, "essence", level, essence)) {
					if (econ.has(p, gold)) {
						cm.subtract(p, "essence", level, essence);
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
				if (cm.hasEnough(p, "essence", level, essence)) {
					if (econ.has(p, gold)) {
						cm.subtract(p, "essence", level, essence);
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
		if (!item.getType().equals(Material.AIR)) {
			if (util.isGearReworked(item)) {
				if (masonUtils.isSlotAvailable(item, slot)) {
					if (util.isArmor(item)) {
						int level = util.getItemLevel(item);
						int perm = ((level - LEVEL_INTERVAL) / 10) - 1;
						if (perm < 1)
							perm = 1;
						if (p.hasPermission("mason.slot.armor." + perm)) {
							listeners.prepItemSlot(p, item, slot);
						}
						else {
							util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
						}
					}
					else if (util.isWeapon(item)) {
						int level = util.getItemLevel(item);
						int perm = ((level - LEVEL_INTERVAL) / 10) - 1;
						if (p.hasPermission("mason.slot.weapon." + perm)) {
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
				util.sendMessage(p, "&cItem is no longer supported by the server!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void unslot(Player p, int slot) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (!item.getType().equals(Material.AIR)) {
			if (util.isGearReworked(item)) {
				if (masonUtils.isSlotUsed(item, slot)) {
					int level = util.getItemLevel(item);
					int perm = (level / 10) - 1;
					if (perm < 0)
						perm = 0;
					int slottedLevel = level;
					if (p.hasPermission("mason.unslot." + perm)) {
						if (p.getInventory().firstEmpty() != -1) {
							if (cm.hasEnough(p, "essence", slottedLevel, UNSLOT_ESSENCE)) {
								if (econ.has(p, UNSLOT_GOLD_PER_LVL * perm)) {
									ItemStack returned = masonUtils.parseUnslot(p, slot);
									if (returned != null) {
										cm.subtract(p, "essence", slottedLevel, UNSLOT_ESSENCE);
										econ.withdrawPlayer(p, UNSLOT_GOLD_PER_LVL * perm);
										p.getInventory().addItem(returned);
										util.sendMessage(p, "&7Successfully unslotted item!");
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
				util.sendMessage(p, "&cItem is no longer supported by the server!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void removeSlot(Player p, int slot) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (!item.getType().equals(Material.AIR)) {
			if (util.isGearReworked(item)) {
				if (masonUtils.isSlotAvailable(item, slot)) {
					int level = util.getItemLevel(item);
					int perm = ((level - LEVEL_INTERVAL) / 10) - 1;
					if (perm < 0)
						perm = 0;
					if (level != -1) {
						if (p.hasPermission("mason.engrave.tier." + perm)) {
							if (cm.hasEnough(p, "essence", level, UNENGRAVE_ESSENCE)) {
								if (econ.has(p, UNENGRAVE_GOLD)) {
									cm.subtract(p, "essence", level, UNENGRAVE_ESSENCE);
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
				util.sendMessage(p, "&cItem is no longer supported by the server!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void resetPlayer(Player p) {
		String name = p.getName();

		// Clean out perms
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lpext removeall " + name + " mason.");

		// Reset profession
		SkillAPI.getPlayerData(p).reset("profession");
	}

}
