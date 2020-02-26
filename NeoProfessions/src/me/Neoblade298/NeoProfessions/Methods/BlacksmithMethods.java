package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import com.sucy.skill.SkillAPI;

import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class BlacksmithMethods {

	Main main;
	Economy econ;
	Util util;
	CommonItems common;
	BlacksmithItems bItems;
	CurrencyManager cm;
	me.neoblade298.neogear.Main neogear;

	// Constants
	final int DURABILITY_COST_PER_LVL = 4000;
	final int DURABILITY_ESSENCE = 5;
	final int REPAIR_COST = 2000;
	final int REPAIR_ESSENCE = 1;
	final int UNBREAKING_COST_PER_LVL = 5000;
	final int PROTECTION_COST_PER_LVL = 5000;
	final int UNBREAKING_ESSENCE_BASE = 2;
	final int UNBREAKING_ESSENCE_PER_LVL = 4;
	final int PROTECTION_ESSENCE_BASE = 2;
	final int PROTECTION_ESSENCE_PER_LVL = 4;
	final int REFORGE_COST_BASE = 1000;
	final int REFORGE_COST_MULT = 2;
	final int REFORGE_ESSENCE_PER_LVL = 4;
	final int SCRAP_COST = 100;
	final int DECONSTRUCT_COST = 250;
	final int DECONSTRUCT_AMOUNT = 4;
	final int LEVEL_INTERVAL = 5;

	public BlacksmithMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
		util = new Util();
		common = new CommonItems();
		bItems = new BlacksmithItems();
		cm = main.cManager;
		neogear = main.neogear;
	}

	public void createDurabilityItem(Player p, String item, String itemtype, int level) {
		int slot = p.getInventory().firstEmpty();
		int perm = ((level - LEVEL_INTERVAL) / 10) - 1;
		if (slot != -1) {
			if (perm <= 0 || p.hasPermission("blacksmith." + item + "." + itemtype + "." + perm)) {
				if (cm.hasEnough(p, "essence", level, DURABILITY_ESSENCE)) {
					if (main.getEconomy().has(p, DURABILITY_COST_PER_LVL * perm)) {
						p.getInventory().addItem(bItems.getDurabilityItem(level, itemtype));
						cm.subtract(p, "essence", level, DURABILITY_ESSENCE);
						econ.withdrawPlayer(p, DURABILITY_COST_PER_LVL * perm);
						util.sendMessage(p, "&7Successfully created level " + level + " durability gem!");
					} else {
						util.sendMessage(p, "&cYou lack the gold to create this!");
					}
				} else {
					util.sendMessage(p, "&cYou lack the essence to create this!");
				}
			} else {
				util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		} else {
			util.sendMessage(p, "&cYour inventory is full!");
		}
	}

	public void createRepairItem(Player p, String item, int level) {
		int slot = p.getInventory().firstEmpty();
		int perm = ((level - LEVEL_INTERVAL) / 10) - 1;
		if (slot != -1) {
			if (perm <= 0 || p.hasPermission("blacksmith." + item + "." + perm)) {
				if (cm.hasEnough(p, "essence", level, REPAIR_ESSENCE)) {
					if (econ.has(p, REPAIR_COST)) {
						p.getInventory().addItem(util.setAmount(bItems.getRepairItem(level), 2));
						cm.subtract(p, "essence", level, REPAIR_ESSENCE);
						econ.withdrawPlayer(p, REPAIR_COST);
						util.sendMessage(p, "&7Successfully created level " + level + " repair kit!");
					} else {
						util.sendMessage(p, "&cYou lack the gold to create this!");
					}
				} else {
					util.sendMessage(p, "&cYou lack the essence to create this!");
				}
			} else {
				util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		} else {
			util.sendMessage(p, "&cYour inventory is full!");
		}
	}

	public void upgradeUnbreaking(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.getType().equals(Material.AIR)) {
			if(item.containsEnchantment(Enchantment.DURABILITY)) {
				if (util.isGearReworked(item)) {
					int enchLevel = item.getEnchantments().get(Enchantment.DURABILITY);
					int upgradeLevel = enchLevel + 1;
					int itemLevel = util.getItemLevel(item);
					if(upgradeLevel <= 6) {
						if(p.hasPermission("blacksmith.upgrade.unbreaking." + upgradeLevel)) {
							if(itemLevel != -1) {
								if (cm.hasEnough(p, "essence", itemLevel, (UNBREAKING_ESSENCE_PER_LVL * enchLevel) - UNBREAKING_ESSENCE_BASE)) {
									if(econ.has(p, UNBREAKING_COST_PER_LVL * enchLevel)) {
										item.addUnsafeEnchantment(Enchantment.DURABILITY, upgradeLevel);
										cm.subtract(p, "essence", itemLevel, (UNBREAKING_ESSENCE_PER_LVL * enchLevel) - UNBREAKING_ESSENCE_BASE);
										econ.withdrawPlayer(p, UNBREAKING_COST_PER_LVL * enchLevel);
										util.sendMessage(p, "&7Successfully upgraded unbreaking to level " + upgradeLevel + "!");
									}
									else {
										util.sendMessage(p, "&cYou lack the gold to create this!");
									}
								}
								else {
									util.sendMessage(p, "&cYou lack the essence to create this!");
								}
							}
							else {
								util.sendMessage(p, "&cYou cannot upgrade non-quest items!");
							}
						}
						else {
							util.sendMessage(p, "&cYou do not yet have the required skill!");
						}
					}
					else {
						util.sendMessage(p, "&cCannot upgrade unbreaking any further!");
					}
				}
				else {
					util.sendMessage(p, "&cItem is no longer supported by the server!");
				}
			}
			else {
				util.sendMessage(p, "&cItem doesn't have an unbreaking enchantment to upgrade!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void upgradeProtection(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (!item.getType().equals(Material.AIR)) {
			if (item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
				if (util.isGearReworked(item)) {
					int enchLevel = item.getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL);
					int upgradeLevel = enchLevel + 1;
					int itemLevel = util.getItemLevel(item);
					if (upgradeLevel <= 6) {
						if (p.hasPermission("blacksmith.upgrade.protection." + upgradeLevel)) {
							if (itemLevel != -1) {
								if (cm.hasEnough(p, "essence", itemLevel, (PROTECTION_ESSENCE_PER_LVL * enchLevel) - PROTECTION_ESSENCE_BASE)) {
									if (econ.has(p, PROTECTION_COST_PER_LVL * enchLevel)) {
										item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, upgradeLevel);
										cm.subtract(p, "essence", itemLevel, (PROTECTION_ESSENCE_PER_LVL * enchLevel) - PROTECTION_ESSENCE_BASE);
										econ.withdrawPlayer(p, PROTECTION_COST_PER_LVL * enchLevel);
										util.sendMessage(p,
												"&7Successfully upgraded protection to level " + upgradeLevel + "!");
									} else {
										util.sendMessage(p, "&cYou lack the gold to create this!");
									}
								} else {
									util.sendMessage(p, "&cYou lack the essence to create this!");
								}
							} else {
								util.sendMessage(p, "&cYou cannot upgrade non-quest items!");
							}
						} else {
							util.sendMessage(p, "&cYou do not yet have the required skill!");
						}
					} else {
						util.sendMessage(p, "&cCannot upgrade protection any further!");
					}
				} else {
					util.sendMessage(p, "&cItem is no longer supported by the server!");
				}
			} else {
				util.sendMessage(p, "&cItem doesn't have a protection enchantment to upgrade!");
			}
		} else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void reforgeItem(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (!item.getType().equals(Material.AIR)) {
			if (util.isGearReworked(item)) {
				String type = util.getItemType(item);
				if (type != null) {
					int itemLevel = util.getItemLevel(item);
					int perm = ((itemLevel + (10 - itemLevel % 10)) / 10) - 1;
					String rarity = util.getItemRarity(item);
					if (itemLevel != -1 && rarity != null) {
						if (perm <= 0 || p.hasPermission("blacksmith.reforge." + perm)) {
							if (cm.hasEnough(p, "essence", itemLevel, REFORGE_ESSENCE_PER_LVL * perm)) {
								if (econ.has(p, REFORGE_COST_BASE * Math.pow(REFORGE_COST_MULT, perm))) {
									cm.subtract(p, "essence", itemLevel, REFORGE_ESSENCE_PER_LVL * perm);
									p.getInventory().removeItem(item);
									econ.withdrawPlayer(p, REFORGE_COST_BASE * Math.pow(REFORGE_COST_MULT, perm));
									p.getInventory().addItem(neogear.settings.get(type).get(itemLevel)
											.generateItem(rarity, itemLevel));
									util.sendMessage(p, "&7Successfully reforged item!");
								} else {
									util.sendMessage(p, "&cYou lack the gold to create this!");
								}
							} else {
								util.sendMessage(p, "&cYou lack the essence to create this!");
							}
						} else {
							util.sendMessage(p, "&cYou do not yet have the required skill!");
						}
					} else {
						util.sendMessage(p, "&cYou cannot reforge non-quest items!");
					}
				} else {
					util.sendMessage(p, "&cYou cannot reforge non-quest items!");
				}
			} else {
				util.sendMessage(p, "&cItem is no longer supported by the server!");
			}
		} else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void scrapItem(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (!item.getType().equals(Material.AIR)) {
			if (util.isGearReworked(item)) {
				int itemLevel = util.getItemLevel(item);
				int perm = ((itemLevel + (10 - itemLevel % 10)) / 10) - 1;
				if (itemLevel != -1) {
					if (perm <= 0 || p.hasPermission("blacksmith.scrap." + perm)) {
						if (econ.has(p, SCRAP_COST)) {
							p.getInventory().removeItem(item);
							econ.withdrawPlayer(p, SCRAP_COST);
							cm.add(p, "essence", itemLevel, 1);
							util.sendMessage(p, "&cSuccessfully scrapped item!");
						} else {
							util.sendMessage(p, "&cYou lack the gold to scrap this!");
						}
					} else {
						util.sendMessage(p, "&cYou do not yet have the required skill!");
					}
				} else {
					util.sendMessage(p, "&cYou cannot scrap non-quest items!");
				}
			} else {
				util.sendMessage(p, "&cItem is no longer supported by the server!");
			}
		} else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void deconstructItem(Player p, int level, int amount) {
		if (level >= 10) {
			if (cm.hasEnough(p, "essence", level, amount)) {
				if (p.hasPermission("blacksmith.deconstruct")) {
					if (econ.has(p, DECONSTRUCT_COST)) {
						econ.withdrawPlayer(p, DECONSTRUCT_COST);
						cm.subtract(p, "essence", level, amount);
						cm.add(p, "essence", level - LEVEL_INTERVAL, amount * DECONSTRUCT_AMOUNT);
						util.sendMessage(p, "&cSuccessfully deconstructed!");
					} else {
						util.sendMessage(p, "&cYou lack the gold to deconstruct this!");
					}
				} else {
					util.sendMessage(p, "&cYou do not yet have the required skill!");
				}
			} else {
				util.sendMessage(p, "&cYou do not have enough essence!");
			}
		} else {
			util.sendMessage(p, "&cDeconstructed essences must be at least level 10!");
		}
	}

	public void resetPlayer(Player p) {
		String name = p.getName();

		// Clean out perms
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lpext removeall " + name + " blacksmith.");

		// Reset profession
		SkillAPI.getPlayerData(p).reset("profession");
	}
}
