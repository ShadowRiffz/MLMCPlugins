package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Utilities;
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
	
	public BlacksmithMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}

	public void createDurabilityItem(Player p, String item, String itemtype, int level) {
		int slot = p.getInventory().firstEmpty();
		if(slot != -1) {
			if (p.hasPermission("neoprofessions.blacksmith." + item + "." + itemtype + "." + level)) {
				if(p.getInventory().containsAtLeast(CommonItems.getEssence(level, 1), DURABILITY_ESSENCE)) {
					if(main.getEconomy().has(p, DURABILITY_COST_PER_LVL * level)) {
						p.getInventory().addItem(BlacksmithItems.getDurabilityItem(level, itemtype));
						
						p.getInventory().removeItem(CommonItems.getEssence(level, DURABILITY_ESSENCE));
						econ.withdrawPlayer(p, DURABILITY_COST_PER_LVL * level);
					}
					else {
						Utilities.sendMessage(p, "&cYou lack the gold to create this!");
					}
				}
				else {
					Utilities.sendMessage(p, "&cYou lack the materials to create this!");
				}
			}
			else {
				Utilities.sendMessage(p, "&cInsufficient permissions!");
			}
		}
		else {
			Utilities.sendMessage(p, "&cYour inventory is full!");
		}
	}

	public void createRepairItem(Player p, String item, int level) {
		int slot = p.getInventory().firstEmpty();
		if(slot != -1) {
			if (p.hasPermission("neoprofessions.blacksmith." + item + "." + level)) {
				if(p.getInventory().containsAtLeast(CommonItems.getEssence(level, 1), REPAIR_ESSENCE)) {
					if(main.getEconomy().has(p, REPAIR_COST)) {
						p.getInventory().addItem(BlacksmithItems.getRepairItem(level));
						p.getInventory().removeItem(CommonItems.getEssence(level, REPAIR_ESSENCE));
						econ.withdrawPlayer(p, REPAIR_COST);
					}
					else {
						Utilities.sendMessage(p, "&cYou lack the gold to create this!");
					}
				}
				else {
					Utilities.sendMessage(p, "&cYou lack the materials to create this!");
				}
			}
			else {
				Utilities.sendMessage(p, "&cInsufficient permissions!");
			}
		}
		else {
			Utilities.sendMessage(p, "&cYour inventory is full!");
		}
	}
}
