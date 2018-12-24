package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class MasonMethods {
	
	Main main;
	Economy econ;
	
	// Constants
	
	public MasonMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}

	public void createSlot(Player p, int level) {
		if (p.hasPermission("mason.engrave.tier." + level)) {
			/* int numSlots = MasonUtils.countSlots();
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
			}*/
		}
		else {
			Util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}

}
