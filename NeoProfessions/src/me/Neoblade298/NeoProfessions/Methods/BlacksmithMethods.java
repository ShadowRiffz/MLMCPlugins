package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Utilities;
import me.Neoblade298.NeoProfessions.Items.BlacksmithItems;
import net.milkbowl.vault.economy.Economy;

public class BlacksmithMethods {
	
	Main main;
	Economy econ;
	
	public BlacksmithMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}

	public void createDurabilityItem(Player p, String profession, String item, String itemtype, int level) {
		int slot = p.getInventory().firstEmpty();
		if(slot == -1) {
			Utilities.sendMessage(p, "&cYour inventory is full!");
		}
		else {
			if (p.hasPermission("neoprofessions." + profession + "." + item + "." + itemtype + "." + level)) {
				if(p.getInventory().containsAtLeast(BlacksmithItems.getDurabilityReq(level, itemtype), 5)) {
					if(main.getEconomy().has(p, 4000 * level)) {
						p.getInventory().addItem(BlacksmithItems.getDurabilityItem(level, itemtype));
						p.getInventory().removeItem(BlacksmithItems.getDurabilityReq(level, itemtype));
						econ.withdrawPlayer(p, 4000 * level);
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
	}
}
