package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Util;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import net.milkbowl.vault.economy.Economy;

public class BlacksmithMethods {
	
	Main main;
	Economy econ;
	
	// Constants
	int DURABILITY_COST_PER_LVL = 4000;
	int DURABILITY_ESSENCE = 5;
	int REPAIR_COST = 2000;
	int REPAIR_ESSENCE = 3;
	int UPGRADE_COST_PER_LVL = 5000;
	int UPGRADE_ESSENCE_PER_LVL = 5;
	int REFORGE_COST_BASE = 1000;
	int REFORGE_COST_MULT = 2;
	int REFORGE_ESSENCE_PER_LVL = 6;
	
	public BlacksmithMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}

	public void createDurabilityItem(Player p, String item, String itemtype, int level) {
		int slot = p.getInventory().firstEmpty();
		if(slot != -1) {
			if (p.hasPermission("blacksmith." + item + "." + itemtype + "." + level)) {
				if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), DURABILITY_ESSENCE)) {
					if(main.getEconomy().has(p, DURABILITY_COST_PER_LVL * level)) {
						p.getInventory().addItem(BlacksmithItems.getDurabilityItem(level, itemtype));
						p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), DURABILITY_ESSENCE));
						econ.withdrawPlayer(p, DURABILITY_COST_PER_LVL * level);
						Util.sendMessage(p, "&7Successfully created level " + level + " durability gem!");
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
				Util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			Util.sendMessage(p, "&cYour inventory is full!");
		}
	}

	public void createRepairItem(Player p, String item, int level) {
		int slot = p.getInventory().firstEmpty();
		if(slot != -1) {
			if (p.hasPermission("blacksmith." + item + "." + level)) {
				if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), REPAIR_ESSENCE)) {
					if(econ.has(p, REPAIR_COST)) {
						p.getInventory().addItem(BlacksmithItems.getRepairItem(level));
						p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), REPAIR_ESSENCE));
						econ.withdrawPlayer(p, REPAIR_COST);
						Util.sendMessage(p, "&7Successfully created level " + level + " repair kit!");
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
				Util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			Util.sendMessage(p, "&cYour inventory is full!");
		}
	}
	
	public void upgradeItem(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(item.equals(Material.AIR)) {
			if(item.containsEnchantment(Enchantment.DURABILITY)) {
				int enchLevel = item.getEnchantments().get(Enchantment.DURABILITY);
				int upgradeLevel = enchLevel + 1;
				int itemLevel = Util.getItemLevel(item);
				if(upgradeLevel <= 6) {
					if(p.hasPermission("blacksmith.upgrade.unbreaking." + upgradeLevel)) {
						if(itemLevel != -1) {
							if(p.getInventory().containsAtLeast(CommonItems.getEssence(itemLevel), UPGRADE_ESSENCE_PER_LVL * enchLevel)) {
								if(econ.has(p, UPGRADE_COST_PER_LVL * enchLevel)) {
									item.addUnsafeEnchantment(Enchantment.DURABILITY, upgradeLevel);
									p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(itemLevel), UPGRADE_ESSENCE_PER_LVL * enchLevel));
									econ.withdrawPlayer(p, UPGRADE_COST_PER_LVL * enchLevel);
									Util.sendMessage(p, "&7Successfully upgraded unbreaking to level " + upgradeLevel + "!");
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
							Util.sendMessage(p, "&cYou cannot upgrade non-quest items!");
						}
					}
					else {
						Util.sendMessage(p, "&cYou do not yet have the required skill!");
					}
				}
				else {
					Util.sendMessage(p, "&cCannot upgrade unbreaking any further!");
				}
			}
			else {
				Util.sendMessage(p, "&cItem doesn't have an unbreaking enchantment to upgrade!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void reforgeItem(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(item.equals(Material.AIR)) {
			System.out.println(item);
			String type = Util.getItemType(item);
			if(type != null) {
				int itemLevel = Util.getItemLevel(item);
				if(itemLevel != -1) {
					if(p.hasPermission("blacksmith.reforge." + itemLevel)) {
						if(p.getInventory().containsAtLeast(CommonItems.getEssence(itemLevel), REFORGE_ESSENCE_PER_LVL * itemLevel)) {
							if(econ.has(p, REFORGE_COST_BASE * Math.pow(REFORGE_COST_MULT, itemLevel))) {
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(itemLevel), REFORGE_ESSENCE_PER_LVL * itemLevel));
								p.getInventory().removeItem(item);
								econ.withdrawPlayer(p,  REFORGE_COST_BASE * Math.pow(REFORGE_COST_MULT, itemLevel));
								ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
								Bukkit.dispatchCommand(console, "mlmctier give " + p.getName() + " " + type);
								Util.sendMessage(p, "&7Successfully reforged item!");
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
						Util.sendMessage(p, "&cYou do not yet have the required skill!");
					}
				}
				else {
					Util.sendMessage(p, "&cYou cannot reforge non-quest items!");
				}
			}
			else {
				Util.sendMessage(p, "&cYou cannot reforge non-quest items!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}
}
