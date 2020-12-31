package me.Neoblade298.NeoProfessions.Methods;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import com.sucy.skill.SkillAPI;

import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Utilities.StonecutterUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class StonecutterMethods {

	Main main;
	Economy econ;
	StonecutterUtils stonecutterUtils;
	StonecutterItems sItems;
	Util util;
	CommonItems common;
	CurrencyManager cm;

	// Constants
	static final int GEM_ESSENCE = 8;
	static final int GEM_ORES = 8;
	static final int REFINE_ORE = 3;
	static final int REFINE_ESSENCE_0 = 7;
	static final int REFINE_ESSENCE_1 = 6;
	static final int REFINE_ESSENCE_2 = 5;
	static final int REFINE_ESSENCE_3 = 4;
	static final int LEVEL_INTERVAL = 5;
	
	// Prices
	HashMap<Integer, Integer> GEM_GOLD;
	HashMap<Integer, Integer> REFINE_GOLD;

	public StonecutterMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
		stonecutterUtils = new StonecutterUtils();
		sItems = new StonecutterItems();
		util = new Util();
		common = new CommonItems();
		cm = main.cManager;

		GEM_GOLD = new HashMap<Integer, Integer>();
		GEM_GOLD.put(5, 500);
		GEM_GOLD.put(10, 1000);
		GEM_GOLD.put(15, 1500);
		GEM_GOLD.put(20, 2000);
		GEM_GOLD.put(25, 2500);
		GEM_GOLD.put(30, 3000);
		GEM_GOLD.put(35, 3500);
		GEM_GOLD.put(40, 4000);
		GEM_GOLD.put(45, 4500);
		GEM_GOLD.put(50, 5500);
		GEM_GOLD.put(55, 6500);
		GEM_GOLD.put(60, 7500);

		REFINE_GOLD = new HashMap<Integer, Integer>();
		REFINE_GOLD.put(5, 10);
		REFINE_GOLD.put(10, 50);
		REFINE_GOLD.put(15, 100);
		REFINE_GOLD.put(20, 150);
		REFINE_GOLD.put(25, 200);
		REFINE_GOLD.put(30, 250);
		REFINE_GOLD.put(35, 300);
		REFINE_GOLD.put(40, 350);
		REFINE_GOLD.put(45, 400);
		REFINE_GOLD.put(50, 450);
		REFINE_GOLD.put(55, 500);
	}

	public void createGem(Player p, String attr, String type, int level) {
		attr = attr.toLowerCase();
		String ore = stonecutterUtils.getOreFromAttribute(attr);
		if (p.getInventory().firstEmpty() != -1) {
			if (p.hasPermission("stonecutter.attribute." + attr)) {
				int perm = (level - LEVEL_INTERVAL) / 10;
				if (perm < 0) perm = 0;
				if (perm <= 0 || p.hasPermission("stonecutter.gem." + type + "." + perm)) {
					if (cm.hasEnough(p, "essence", level, GEM_ESSENCE)) {
						if (cm.hasEnough(p, ore, level, GEM_ORES)) {
							if (econ.has(p, GEM_GOLD.get(level))) {
								cm.subtract(p, "essence", level, GEM_ESSENCE);
								cm.subtract(p, ore, level, GEM_ORES);
								if (type.equalsIgnoreCase("weapon")) {
									p.getInventory().addItem(sItems.getWeaponGem(attr, level, false));
								} else {
									p.getInventory().addItem(sItems.getArmorGem(attr, level, false));
								}
								econ.withdrawPlayer(p, GEM_GOLD.get(level));
								util.sendMessage(p, "&7Successfully created level " + level + " " + attr + " gem!");
							} else {
								util.sendMessage(p, "&cYou lack the gold to create this!");
							}
						} else {
							util.sendMessage(p, "&cYou lack the materials to create this!");
						}
					} else {
						util.sendMessage(p, "&cYou lack the materials to create this!");
					}
				} else {
					util.sendMessage(p, "&cYou do not yet have the required skill for this type of gem!");
				}
			} else {
				util.sendMessage(p, "&cYou do not yet have the required skill for this attribute!");
			}
		} else {
			util.sendMessage(p, "&cYour inventory is full!");
		}
	}

	public void createOverloadedGem(Player p, String attr, String type, int level) {
		attr = attr.toLowerCase();
		String ore = stonecutterUtils.getOreFromAttribute(attr);
		if (p.getInventory().firstEmpty() != -1) {
			if (p.hasPermission("stonecutter.attribute." + attr)) {
				int perm = (level - LEVEL_INTERVAL) / 10;
				if (perm < 0) perm = 0;
				if (perm <= 0 || p.hasPermission("stonecutter.overload." + type + "." + perm)) {
					if (cm.hasEnough(p, "essence", level, GEM_ESSENCE)) {
						if (cm.hasEnough(p, ore, level, GEM_ORES)) {
							if (econ.has(p, GEM_GOLD.get(level))) {
								cm.subtract(p, "essence", level, GEM_ESSENCE);
								cm.subtract(p, ore, level, GEM_ORES);
								if (type.equalsIgnoreCase("weapon")) {
									p.getInventory().addItem(sItems.getWeaponGem(attr, level, true));
								} else {
									p.getInventory().addItem(sItems.getArmorGem(attr, level, true));
								}
								econ.withdrawPlayer(p, GEM_GOLD.get(level));
								util.sendMessage(p, "&7Successfully created level " + level + " " + attr + " gem!");
							} else {
								util.sendMessage(p, "&cYou lack the gold to create this!");
							}
						}
						else {
							util.sendMessage(p, "&cYou lack the materials to create this!");
						}
					} else {
						util.sendMessage(p, "&cYou lack the materials to create this!");
					}
				} else {
					util.sendMessage(p, "&cYou do not yet have the required skill for this type of gem!");
				}
			} else {
				util.sendMessage(p, "&cYou do not yet have the required skill for this attribute!");
			}
		} else {
			util.sendMessage(p, "&cYour inventory is full!");
		}
	}

	public void refine(Player p, String type, int oldLevel, int amount) {
		int level = oldLevel + LEVEL_INTERVAL;

		// Find essence cost via perms
		int cost = REFINE_ESSENCE_0;
		if (p.hasPermission("stonecutter.refine.finesse.3")) {
			cost = REFINE_ESSENCE_3;
		} else if (p.hasPermission("stonecutter.refine.finesse.2")) {
			cost = REFINE_ESSENCE_2;
		} else if (p.hasPermission("stonecutter.refine.finesse.1")) {
			cost = REFINE_ESSENCE_1;
		}

		int perm = ((oldLevel - LEVEL_INTERVAL)/ 10);
		if (perm < 0) perm = 0;
		if (perm <= 1 || p.hasPermission("stonecutter.refine.tier." + perm)) {
			if (level >= 5 && level <= 60) {
				if (econ.has(p, REFINE_GOLD.get(level))) {
					// Check if enough essence
					if (cm.hasEnough(p, type, oldLevel, cost * amount)) {
						cm.subtract(p, type, oldLevel, cost * amount);
						cm.add(p, type, level, amount);
						econ.withdrawPlayer(p, REFINE_GOLD.get(oldLevel));
						util.sendMessage(p, "&7Successfully refined &e" + amount + " " + type + "&7!");
					} else {
						util.sendMessage(p, "&cYou lack the materials to refine this!");
						return;
					}
				} else {
					util.sendMessage(p, "&cYou lack the gold to refine this!");
					return;
				}
			}
			else {
				util.sendMessage(p, "&cInvalid level!");
				return;
			}
		} else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
			return;
		}
	}

	public void resetPlayer(Player p) {
		String name = p.getName();

		// Clean out perms
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "lpext removeall " + name + " stonecutter.");

		// Reset profession
		SkillAPI.getPlayerData(p).reset("profession");
	}
}
