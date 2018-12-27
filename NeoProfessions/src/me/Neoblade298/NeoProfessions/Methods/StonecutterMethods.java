package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;
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
	
	public StonecutterMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}
	
	public void createGem(Player p, String attr, int level) {
		attr = attr.toLowerCase();
		if(p.getInventory().firstEmpty() != -1) {
			if(p.hasPermission("stonecutter.attribute." + attr)) {
				if(p.hasPermission("stonecutter.gem." + level)) {
					if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), GEM_ESSENCE)) {
						if(econ.has(p, GEM_COST_PER_LVL * level)) {
							p.getInventory().addItem(StonecutterItems.getOre(attr, level));
							p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), GEM_ESSENCE));
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
}
