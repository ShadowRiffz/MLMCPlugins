package me.Neoblade298.NeoProfessions.Methods;

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
	static final int GEM_COST_PER_LVL = 1500;
	static final int GEM_ESSENCE = 8;
	static final int GEM_ORES = 8;
	static final int REFINE_COST = 500;
	static final int REFINE_ORE = 3;
	static final int REFINE_ESSENCE_0 = 7;
	static final int REFINE_ESSENCE_1 = 6;
	static final int REFINE_ESSENCE_2 = 5;
	static final int REFINE_ESSENCE_3 = 4;
	static final int LEVEL_INTERVAL = 5;

	public StonecutterMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
		stonecutterUtils = new StonecutterUtils();
		sItems = new StonecutterItems();
		util = new Util();
		common = new CommonItems();
		cm = main.cManager;
	}

	public void createGem(Player p, String attr, String type, int level) {
		attr = attr.toLowerCase();
		String ore = stonecutterUtils.getOreFromAttribute(attr);
		if (p.getInventory().firstEmpty() != -1) {
			if (p.hasPermission("stonecutter.attribute." + attr)) {
				int perm = ((level - LEVEL_INTERVAL) / 10) - 1;
				if (p.hasPermission("stonecutter.gem." + type + "." + perm)) {
					if (cm.hasEnough(p, "essence", level, GEM_ESSENCE)) {
						if (cm.hasEnough(p, ore, level, GEM_ORES)) {
							if (econ.has(p, GEM_COST_PER_LVL * perm)) {
								cm.subtract(p, "essence", level, GEM_ESSENCE);
								cm.subtract(p, ore, level, GEM_ORES);
								if (type.equalsIgnoreCase("weapon")) {
									p.getInventory().addItem(sItems.getWeaponGem(attr, level, false));
								} else {
									p.getInventory().addItem(sItems.getArmorGem(attr, level, false));
								}
								econ.withdrawPlayer(p, GEM_COST_PER_LVL * perm);
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
				int perm = ((level - LEVEL_INTERVAL) / 10) - 1;
				if (p.hasPermission("stonecutter.overload." + type + "." + perm)) {
					if (cm.hasEnough(p, "essence", level, GEM_ESSENCE)) {
						if (cm.hasEnough(p, ore, level, GEM_ORES)) {
							if (econ.has(p, GEM_COST_PER_LVL * level)) {
								cm.subtract(p, "essence", level, GEM_ESSENCE);
								cm.subtract(p, ore, level, GEM_ORES);
								if (type.equalsIgnoreCase("weapon")) {
									p.getInventory().addItem(sItems.getWeaponGem(attr, level, true));
								} else {
									p.getInventory().addItem(sItems.getArmorGem(attr, level, true));
								}
								econ.withdrawPlayer(p, GEM_COST_PER_LVL * level);
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

		int perm = ((level - LEVEL_INTERVAL) / 10);
		if (perm <= 1 || p.hasPermission("stonecutter.refine.tier." + perm)) {
			if (econ.has(p, REFINE_COST * amount)) {
				// Check if enough essence
				if (cm.hasEnough(p, type, oldLevel, cost * amount)) {
					cm.subtract(p, type, oldLevel, cost * amount);
					cm.add(p, type, level, amount);
					econ.withdrawPlayer(p, REFINE_COST);
					util.sendMessage(p, "&7Successfully refined &e" + amount + " " + type + "&7!");
				} else {
					util.sendMessage(p, "&cYou lack the materials to refine this!");
					return;
				}
			} else {
				util.sendMessage(p, "&cYou lack the gold to refine this!");
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
