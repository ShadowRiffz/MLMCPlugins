package me.Neoblade298.NeoProfessions.Methods;

import java.util.HashMap;

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
	me.neoblade298.neogear.Main neogear;

	// Constants
	final int DURABILITY_ESSENCE = 5;
	final int REPAIR_ESSENCE = 1;
	final int UNBREAKING_ESSENCE_BASE = 2;
	final int UNBREAKING_ESSENCE_PER_LVL = 4;
	final int PROTECTION_ESSENCE_BASE = 2;
	final int PROTECTION_ESSENCE_PER_LVL = 4;
	final int REFORGE_ESSENCE_PER_LVL = 4;
	final int SCRAP_COST = 100;
	final int DECONSTRUCT_COST = 250;
	final int DECONSTRUCT_AMOUNT = 4;
	final int LEVEL_INTERVAL = 5;
	
	// Prices
	HashMap<Integer, Integer> CREATE_REPAIR_GOLD;
	HashMap<Integer, Integer> CREATE_DURABILITY_GOLD;
	HashMap<Integer, Integer> INCREASE_ENCHANTMENT_GOLD;
	HashMap<Integer, Integer> REFORGE_GOLD;

	public BlacksmithMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
		util = new Util();
		common = new CommonItems();
		bItems = new BlacksmithItems();
		cm = main.cManager;
		neogear = main.neogear;
		
		CREATE_REPAIR_GOLD = new HashMap<Integer, Integer>();
		CREATE_REPAIR_GOLD.put(5, 10);
		CREATE_REPAIR_GOLD.put(10, 25);
		CREATE_REPAIR_GOLD.put(15, 50);
		CREATE_REPAIR_GOLD.put(20, 200);
		CREATE_REPAIR_GOLD.put(25, 400);
		CREATE_REPAIR_GOLD.put(30, 600);
		CREATE_REPAIR_GOLD.put(35, 800);
		CREATE_REPAIR_GOLD.put(40, 1000);
		CREATE_REPAIR_GOLD.put(45, 1200);
		CREATE_REPAIR_GOLD.put(50, 1400);
		CREATE_REPAIR_GOLD.put(55, 1600);
		CREATE_REPAIR_GOLD.put(60, 2000);
		
		CREATE_DURABILITY_GOLD = new HashMap<Integer, Integer>();
		CREATE_DURABILITY_GOLD.put(5, 100);
		CREATE_DURABILITY_GOLD.put(10, 250);
		CREATE_DURABILITY_GOLD.put(15, 500);
		CREATE_DURABILITY_GOLD.put(20, 1000);
		CREATE_DURABILITY_GOLD.put(25, 2000);
		CREATE_DURABILITY_GOLD.put(30, 4000);
		CREATE_DURABILITY_GOLD.put(35, 6000);
		CREATE_DURABILITY_GOLD.put(40, 8000);
		CREATE_DURABILITY_GOLD.put(45, 10000);
		CREATE_DURABILITY_GOLD.put(50, 12000);
		CREATE_DURABILITY_GOLD.put(55, 16000);
		CREATE_DURABILITY_GOLD.put(60, 20000);
		
		INCREASE_ENCHANTMENT_GOLD = new HashMap<Integer, Integer>();
		INCREASE_ENCHANTMENT_GOLD.put(5, 100);
		INCREASE_ENCHANTMENT_GOLD.put(10, 200);
		INCREASE_ENCHANTMENT_GOLD.put(15, 400);
		INCREASE_ENCHANTMENT_GOLD.put(20, 600);
		INCREASE_ENCHANTMENT_GOLD.put(25, 800);
		INCREASE_ENCHANTMENT_GOLD.put(30, 1000);
		INCREASE_ENCHANTMENT_GOLD.put(35, 1500);
		INCREASE_ENCHANTMENT_GOLD.put(40, 2000);
		INCREASE_ENCHANTMENT_GOLD.put(45, 2500);
		INCREASE_ENCHANTMENT_GOLD.put(50, 3000);
		INCREASE_ENCHANTMENT_GOLD.put(55, 4000);
		INCREASE_ENCHANTMENT_GOLD.put(60, 5000);
		
		REFORGE_GOLD = new HashMap<Integer, Integer>();
		REFORGE_GOLD.put(5, 250);
		REFORGE_GOLD.put(10, 500);
		REFORGE_GOLD.put(15, 1000);
		REFORGE_GOLD.put(20, 2000);
		REFORGE_GOLD.put(25, 4000);
		REFORGE_GOLD.put(30, 6000);
		REFORGE_GOLD.put(35, 8000);
		REFORGE_GOLD.put(40, 10000);
		REFORGE_GOLD.put(45, 12000);
		REFORGE_GOLD.put(50, 16000);
		REFORGE_GOLD.put(55, 24000);
		REFORGE_GOLD.put(60, 32000);
	}

	public void createDurabilityItem(Player p, String item, String itemtype, int level) {
		int slot = p.getInventory().firstEmpty();
		int perm = (level - LEVEL_INTERVAL) / 10;
		if (perm < 0)
			perm = 0;
		if (slot != -1) {
			if (perm <= 0 || p.hasPermission("blacksmith." + item + "." + itemtype + "." + perm)) {
				if (cm.hasEnough(p, "essence", level, DURABILITY_ESSENCE)) {
					if (main.getEconomy().has(p, CREATE_DURABILITY_GOLD.get(level))) {
						p.getInventory().addItem(bItems.getDurabilityItem(level, itemtype));
						cm.subtract(p, "essence", level, DURABILITY_ESSENCE);
						econ.withdrawPlayer(p, CREATE_DURABILITY_GOLD.get(level));
						util.sendMessage(p, "&7Successfully created level " + level + " durability gem!");
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
				util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			util.sendMessage(p, "&cYour inventory is full!");
		}
	}

	public void createRepairItem(Player p, String item, int level) {
		int slot = p.getInventory().firstEmpty();
		int perm = (level - LEVEL_INTERVAL) / 10;
		if (slot != -1) {
			if (perm <= 0 || p.hasPermission("blacksmith." + item + "." + perm)) {
				if (cm.hasEnough(p, "essence", level, REPAIR_ESSENCE)) {
					if (econ.has(p, CREATE_REPAIR_GOLD.get(level))) {
						p.getInventory().addItem(util.setAmount(bItems.getRepairItem(level), 2));
						cm.subtract(p, "essence", level, REPAIR_ESSENCE);
						econ.withdrawPlayer(p, CREATE_REPAIR_GOLD.get(level));
						util.sendMessage(p, "&7Successfully created level " + level + " repair kit!");
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
				util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			util.sendMessage(p, "&cYour inventory is full!");
		}
	}

	public void upgradeUnbreaking(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (!item.getType().equals(Material.AIR)) {
			if (item.containsEnchantment(Enchantment.DURABILITY)) {
				int enchLevel = item.getEnchantments().get(Enchantment.DURABILITY);
				int upgradeLevel = enchLevel + 1;
				int itemLevel = util.getItemLevel(item);
				if (enchLevel <= 5) {
					if ((enchLevel <= 3 && p.hasPermission("blacksmith.upgrade.unbreaking.3"))
							|| (enchLevel > 3 && p.hasPermission("blacksmith.upgrade.unbreaking." + enchLevel))) {
						if (itemLevel != -1) {
							if (cm.hasEnough(p, "essence", itemLevel,
									(UNBREAKING_ESSENCE_PER_LVL * enchLevel) - UNBREAKING_ESSENCE_BASE)) {
								if (econ.has(p, INCREASE_ENCHANTMENT_GOLD.get(itemLevel) * enchLevel)) {
									item.addUnsafeEnchantment(Enchantment.DURABILITY, upgradeLevel);
									cm.subtract(p, "essence", itemLevel,
											(UNBREAKING_ESSENCE_PER_LVL * enchLevel) - UNBREAKING_ESSENCE_BASE);
									econ.withdrawPlayer(p, INCREASE_ENCHANTMENT_GOLD.get(itemLevel) * enchLevel);
									util.sendMessage(p,
											"&7Successfully upgraded unbreaking to level " + upgradeLevel + "!");
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
				int enchLevel = item.getEnchantments().get(Enchantment.PROTECTION_ENVIRONMENTAL);
				int upgradeLevel = enchLevel + 1;
				int itemLevel = util.getItemLevel(item);
				if (enchLevel <= 5) {
					if ((enchLevel <= 3 && p.hasPermission("blacksmith.upgrade.protection.3"))
							|| (enchLevel > 3 && p.hasPermission("blacksmith.upgrade.protection." + enchLevel))) {
						if (itemLevel != -1) {
							if (cm.hasEnough(p, "essence", itemLevel,
									(PROTECTION_ESSENCE_PER_LVL * enchLevel) - PROTECTION_ESSENCE_BASE)) {
								if (econ.has(p, INCREASE_ENCHANTMENT_GOLD.get(itemLevel) * enchLevel)) {
									item.addUnsafeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, upgradeLevel);
									cm.subtract(p, "essence", itemLevel,
											(PROTECTION_ESSENCE_PER_LVL * enchLevel) - PROTECTION_ESSENCE_BASE);
									econ.withdrawPlayer(p, INCREASE_ENCHANTMENT_GOLD.get(itemLevel) * enchLevel);
									util.sendMessage(p,
											"&7Successfully upgraded protection to level " + upgradeLevel + "!");
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
					util.sendMessage(p, "&cCannot upgrade protection any further!");
				}
			}
			else {
				util.sendMessage(p, "&cItem doesn't have a protection enchantment to upgrade!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void reforgeItem(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (!item.getType().equals(Material.AIR)) {
			String type = util.getItemType(item);

			if (type != null) {
				// Fix type
				switch (type) {
				case "reinforcedhelmet":
					type = "rhelmet";
					break;
				case "reinforcedchestplate":
					type = "rchestplate";
					break;
				case "reinforcedleggings":
					type = "rleggings";
					break;
				case "reinforcedboots":
					type = "rboots";
					break;
				case "infusedhelmet":
					type = "ihelmet";
					break;
				case "infusedchestplate":
					type = "ichestplate";
					break;
				case "infusedleggings":
					type = "ileggings";
					break;
				case "infusedboots":
					type = "iboots";
					break;
				}
				int itemLevel = util.getItemLevel(item);
				int perm = ((itemLevel + (10 - itemLevel % 10)) / 10) - 2;
				if (perm < 0)
					perm = 0;
				String rarity = util.getItemRarity(item);
				if (itemLevel != -1 && rarity != null) {
					if (perm <= 0 || p.hasPermission("blacksmith.reforge." + perm)) {
						if (!rarity.equalsIgnoreCase("Artifact")) {
							if (cm.hasEnough(p, "essence", itemLevel, REFORGE_ESSENCE_PER_LVL * perm)) {
								if (econ.has(p, REFORGE_GOLD.get(itemLevel))) {
									cm.subtract(p, "essence", itemLevel, REFORGE_ESSENCE_PER_LVL * perm);
									p.getInventory().removeItem(item);
									econ.withdrawPlayer(p, REFORGE_GOLD.get(itemLevel));
									p.getInventory().addItem(
											neogear.settings.get(type).get(itemLevel).generateItem(rarity, itemLevel));
									util.sendMessage(p, "&7Successfully reforged item!");
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
							util.sendMessage(p, "&cArtifacts cannot be rerolled!");
						}
					}
					else {
						util.sendMessage(p, "&cYou do not yet have the required skill!");
					}
				}
				else {
					util.sendMessage(p, "&cYou cannot reforge non-quest items!");
				}
			}
			else {
				util.sendMessage(p, "&cYou cannot reforge non-quest items!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void scrapItem(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (!item.getType().equals(Material.AIR)) {
			int itemLevel = util.getItemLevel(item);
			int perm = ((itemLevel + (10 - itemLevel % 10)) / 10) - 2;
			if (perm < 0)
				perm = 0;
			if (itemLevel != -1) {
				if (perm <= 0 || p.hasPermission("blacksmith.scrap." + perm)) {
					if (econ.has(p, SCRAP_COST)) {
						p.getInventory().removeItem(item);
						econ.withdrawPlayer(p, SCRAP_COST);
						cm.add(p, "essence", itemLevel, 1);
						util.sendMessage(p, "&7Successfully scrapped item!");
					}
					else {
						util.sendMessage(p, "&cYou lack the gold to scrap this!");
					}
				}
				else {
					util.sendMessage(p, "&cYou do not yet have the required skill!");
				}
			}
			else {
				util.sendMessage(p, "&cYou cannot scrap non-quest items!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}

	public void deconstructItem(Player p, int level, int amount) {
		if (level >= 10) {
			if (cm.hasEnough(p, "essence", level, amount)) {
				if (p.hasPermission("blacksmith.deconstruct")) {
					if (econ.has(p, DECONSTRUCT_COST)) {
						econ.withdrawPlayer(p, DECONSTRUCT_COST * amount);
						cm.subtract(p, "essence", level, amount);
						cm.add(p, "essence", level - LEVEL_INTERVAL, amount * DECONSTRUCT_AMOUNT);
						util.sendMessage(p, "&7Successfully deconstructed!");
					}
					else {
						util.sendMessage(p, "&cYou lack the gold to deconstruct this!");
					}
				}
				else {
					util.sendMessage(p, "&cYou do not yet have the required skill!");
				}
			}
			else {
				util.sendMessage(p, "&cYou do not have enough essence!");
			}
		}
		else {
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
