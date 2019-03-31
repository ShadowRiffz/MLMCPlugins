package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.sucy.skill.SkillAPI;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Items.StonecutterItems;
import me.Neoblade298.NeoProfessions.Utilities.StonecutterUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class StonecutterMethods {

	
	Main main;
	Economy econ;
	
	// Constants
	static final int GEM_COST_PER_LVL = 1500;
	static final int GEM_ESSENCE = 8;
	static final int GEM_ORES = 8;
	static final int REFINE_COST = 1000;
	static final int REFINE_ORE = 3;
	static final int REFINE_ESSENCE_0 = 10;
	static final int REFINE_ESSENCE_1 = 9;
	static final int REFINE_ESSENCE_2 = 7;
	static final int REFINE_ESSENCE_3 = 4;
	
	public StonecutterMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}
	
	public void createGem(Player p, String attr, String type, int level) {
		attr = attr.toLowerCase();
		if(p.getInventory().firstEmpty() != -1) {
			if(p.hasPermission("stonecutter.attribute." + attr)) {
				if(p.hasPermission("stonecutter.gem." + type + "." + level)) {
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
			if(p.hasPermission("stonecutter.attribute." + type + "." + attr)) {
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
	
	public void refine(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		if(!item.getType().equals(Material.AIR)) {
			int slot = p.getInventory().firstEmpty();
			if(slot != -1) {
				if(StonecutterUtils.isEssence(item)) {
					int oldLevel = Util.getEssenceLevel(item);
					int level = oldLevel + 1;
					if(p.hasPermission("stonecutter.refine." + oldLevel)) {
						if(econ.has(p, REFINE_COST)) {
							// Find essence cost via perms
							int cost = REFINE_ESSENCE_0;
							if(p.hasPermission("stonecutter.refine.finesse.3")) {
								cost = REFINE_ESSENCE_3;
							}
							else if(p.hasPermission("stonecutter.refine.finesse.2")) {
								cost = REFINE_ESSENCE_2;
							}
							else if(p.hasPermission("stonecutter.refine.finesse.1")) {
								cost = REFINE_ESSENCE_1;
							}
							
							// Check if enough essence
							if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), cost)) {
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), cost));
								p.getInventory().addItem(CommonItems.getEssence(level));
								econ.withdrawPlayer(p, REFINE_COST);
								Util.sendMessage(p, "&7Successfully refined essence!");
							}
							else {
								Util.sendMessage(p, "&cYou lack the essence to refine this!");
							}
						}
						else {
							Util.sendMessage(p, "&cYou lack the gold to refine this!");
						}
					}
					else {
						Util.sendMessage(p, "&cYou do not yet have the required skill!");
					}
				}
				else if(StonecutterUtils.isOre(item)) {
					String oreType = StonecutterUtils.getOreType(item);
					int oldLevel = item.getEnchantmentLevel(Enchantment.DURABILITY);
					int level = oldLevel + 1;
					if(p.hasPermission("stonecutter.refine." + oldLevel)) {
						if(econ.has(p, REFINE_COST)) {
							// Find essence cost via perms
							int cost = REFINE_ORE;
							// Check if enough essence
							if(p.getInventory().containsAtLeast(StonecutterItems.getOre(oreType, oldLevel), cost)) {
								p.getInventory().removeItem(Util.setAmount(StonecutterItems.getOre(oreType, oldLevel), cost));
								p.getInventory().addItem(StonecutterItems.getOre(oreType, level));
								econ.withdrawPlayer(p, REFINE_COST);
								Util.sendMessage(p, "&7Successfully refined essence!");
							}
							else {
								Util.sendMessage(p, "&cYou lack the essence to refine this!");
							}
						}
						else {
							Util.sendMessage(p, "&cYou lack the gold to refine this!");
						}
					}
					else {
						Util.sendMessage(p, "&cYou do not yet have the required skill!");
					}
				}
				else {
					Util.sendMessage(p, "&cYou can only refine essences or ores!");
				}
			}
			else {
				Util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void resetPlayer(Player p) {
		String name = p.getName();
		
		// Clean out perms
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.professed");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.profess.account");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.weapon.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.weapon.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.weapon.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.weapon.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.weapon.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.armor.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.armor.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.armor.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.armor.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.gem.armor.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.weapon.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.weapon.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.weapon.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.weapon.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.weapon.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.armor.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.armor.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.armor.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.armor.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove stonecutter.overload.armor.5");
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
