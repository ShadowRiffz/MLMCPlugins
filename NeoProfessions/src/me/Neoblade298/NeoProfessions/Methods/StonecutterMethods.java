package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class StonecutterMethods {

	
	Main main;
	Economy econ;
	
	// Constants
	static final int GEM_COST_PER_LVL = 1500;
	static final int GEM_ESSENCE = 8;
	static final int GEM_ORES = 2;
	
	public StonecutterMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}
	
	public void createGem(Player p, String attr, String type, int level) {
		attr = attr.toLowerCase();
		if(p.getInventory().firstEmpty() != -1) {
			if(p.hasPermission("stonecutter.attribute." + attr)) {
				if(p.hasPermission("stonecutter.gem." + level)) {
					if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), GEM_ESSENCE)) {
						if(p.getInventory().containsAtLeast(StonecutterItems.getOre(attr, level), GEM_ORES)) {
							if(econ.has(p, GEM_COST_PER_LVL * level)) {
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), GEM_ESSENCE));
								p.getInventory().removeItem(Util.setAmount(StonecutterItems.getOre(attr, level), GEM_ORES));
								if(type.equalsIgnoreCase("weapon")) {
									p.getInventory().addItem(StonecutterItems.getWeaponGem(attr, level, false));
								}
								else {
									p.getInventory().addItem(StonecutterItems.getArmorGem(attr, level, false));
								}
								econ.withdrawPlayer(p, GEM_COST_PER_LVL * level);
								Util.sendMessage(p, "&7Successfully created level " + level + " " + attr + " gem!");
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
						Util.sendMessage(p, "&cYou lack the materials to create this!");
					}
				}
				else {
					Util.sendMessage(p, "&cYou do not yet have the required skill!");
				}
			}
			else {
				Util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			Util.sendMessage(p, "&cYour inventory is full!");
		}
	}
	
	public void createOverloadedGem(Player p, String attr, String type, int level) {
		attr = attr.toLowerCase();
		if(p.getInventory().firstEmpty() != -1) {
			if(p.hasPermission("stonecutter.attribute." + attr)) {
				if(p.hasPermission("stonecutter.overload." + level)) {
					if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), GEM_ESSENCE)) {
						if(p.getInventory().containsAtLeast(StonecutterItems.getOre(attr, level), GEM_ORES))
							if(econ.has(p, GEM_COST_PER_LVL * level)) {
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), GEM_ESSENCE));
								p.getInventory().removeItem(Util.setAmount(StonecutterItems.getOre(attr, level), GEM_ORES));
								if(type.equalsIgnoreCase("weapon")) {
									p.getInventory().addItem(StonecutterItems.getWeaponGem(attr, level, true));
								}
								else {
									p.getInventory().addItem(StonecutterItems.getArmorGem(attr, level, true));
								}
								econ.withdrawPlayer(p, GEM_COST_PER_LVL * level);
								Util.sendMessage(p, "&7Successfully created level " + level + " " + attr + " gem!");
							}
							else {
								Util.sendMessage(p, "&cYou lack the gold to create this!");
							}
						else {
							Util.sendMessage(p, "&cYou lack the materials to create this!");
						}
					}
					else {
						Util.sendMessage(p, "&cYou lack the materials to create this!");
					}
				}
				else {
					Util.sendMessage(p, "&cYou do not yet have the required skill!");
				}
			}
			else {
				Util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			Util.sendMessage(p, "&cYour inventory is full!");
		}
	}
	
	public void resetPlayer(Player p) {
		String name = p.getName();
		
		// Clean out perms
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.professed");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.attribute.strength");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.attribute.dexterity");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.attribute.intelligence");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.attribute.spirit");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.attribute.perception");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.attribute.endurance");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.attribute.vitality");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.refine.tier.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.refine.tier.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.refine.tier.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.refine.tier.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.refine.finesse.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.refine.finesse.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.refine.finesse.3");
		
		// Reset profession
		SkillAPI.getPlayerData(p).reset("profession");
	}
}
