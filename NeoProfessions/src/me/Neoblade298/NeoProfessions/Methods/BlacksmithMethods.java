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
import net.milkbowl.vault.economy.Economy;

public class BlacksmithMethods {

	Main main;
	Economy econ;
	Util util;
	CommonItems common;
	BlacksmithItems bItems;
	CurrencyManager cm;

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
	}

	public void createDurabilityItem(Player p, String item, String itemtype, int level) {
		int slot = p.getInventory().firstEmpty();
		int perm = (level / 10) - 1;
		if (slot != -1) {
			if (p.hasPermission("blacksmith." + item + "." + itemtype + "." + perm)) {
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
		int perm = (level / 10) - 1;
		if (slot != -1) {
			if (p.hasPermission("blacksmith." + item + "." + perm)) {
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
						if (p.hasPermission("blacksmith.reforge." + perm)) {
							if (cm.hasEnough(p, "essence", itemLevel, REFORGE_ESSENCE_PER_LVL * perm)) {
								if (econ.has(p, REFORGE_COST_BASE * Math.pow(REFORGE_COST_MULT, perm))) {
									cm.hasEnough(p, "essence", itemLevel, REFORGE_ESSENCE_PER_LVL * perm);
									p.getInventory().removeItem(item);
									econ.withdrawPlayer(p, REFORGE_COST_BASE * Math.pow(REFORGE_COST_MULT, perm));
									p.getInventory().addItem(main.neogear.settings.get(rarity).get(itemLevel)
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
					if (p.hasPermission("blacksmith.scrap." + perm)) {
						if (econ.has(p, SCRAP_COST)) {
							p.getInventory().removeItem(item);
							econ.withdrawPlayer(p, SCRAP_COST);
							p.getInventory().addItem(common.getEssence(itemLevel, false));
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

	public void deconstructItem(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		if (!item.getType().equals(Material.AIR)) {
			if (util.isGearReworked(item)) {
				item.setAmount(1);
				int itemLevel = util.getEssenceLevel(item);
				if (itemLevel >= 10) {
					if (p.hasPermission("blacksmith.deconstruct")) {
						if (econ.has(p, DECONSTRUCT_COST)) {
							p.getInventory().removeItem(item);
							econ.withdrawPlayer(p, DECONSTRUCT_COST);
							p.getInventory().addItem(util.setAmount(
									common.getEssence(itemLevel - LEVEL_INTERVAL, false), DECONSTRUCT_AMOUNT));
							util.sendMessage(p, "&cSuccessfully deconstructed item!");
						} else {
							util.sendMessage(p, "&cYou lack the gold to deconstruct this!");
						}
					} else {
						util.sendMessage(p, "&cYou do not yet have the required skill!");
					}
				} else {
					util.sendMessage(p, "&cYou can only deconstruct essences, and they must be at least level 10!");
				}
			} else {
				util.sendMessage(p, "&cItem is no longer supported by the server!");
			}
		} else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void resetPlayer(Player p) {
		String name = p.getName();

		// Clean out perms
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.professed");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.profess.account");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.weapon.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.weapon.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.weapon.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.weapon.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.weapon.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.armor.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.armor.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.armor.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.armor.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.durability.armor.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.repair.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.repair.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.repair.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.repair.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.repair.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.upgrade.unbreaking.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.upgrade.unbreaking.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.upgrade.unbreaking.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.upgrade.unbreaking.6");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.upgrade.protection.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.upgrade.protection.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.upgrade.protection.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.upgrade.protection.6");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.reforge.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.reforge.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.reforge.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.reforge.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.reforge.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.scrap.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.scrap.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.scrap.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.scrap.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.scrap.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),
				"lp user " + name + " permission unset blacksmith.deconstruct");

		// Reset profession
		SkillAPI.getPlayerData(p).reset("profession");
	}
}
