package me.Neoblade298.NeoProfessions.Methods;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class MasonMethods {
	
	Main main;
	Economy econ;
	
	// Constants
	static final int MAX_SLOTS = 3;
	static final int ENGRAVE_ESSENCE_PER_SLOT = 12;
	static final int ENGRAVE_GOLD_BASE = -10000;
	static final int ENGRAVE_GOLD_PER_LVL = 15000;
	
	public MasonMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}

	public void createSlot(Player p, int level) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if (p.hasPermission("mason.engrave.tier." + level)) {
			int numSlots = MasonUtils.countSlots(item);
			if(numSlots < MAX_SLOTS) {
				if (p.hasPermission(("mason.engrave.max."  + (numSlots + 1)))) {
					if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), ENGRAVE_ESSENCE_PER_SLOT * (numSlots + 1))) {
						if(econ.has(p, ENGRAVE_GOLD_BASE + (ENGRAVE_GOLD_PER_LVL * level))) {
							MasonUtils.createSlot(item, level);
							p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), ENGRAVE_ESSENCE_PER_SLOT * (numSlots + 1)));
							econ.withdrawPlayer(p, ENGRAVE_GOLD_BASE + (ENGRAVE_GOLD_PER_LVL * level));
							Util.sendMessage(p, "&7Successfully created slot!");
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
					Util.sendMessage(p, "&cYou do not yet have the required skill to create more slots!");
				}
			}
			else {
				Util.sendMessage(p, "&cThis item cannot have any more slots!");
			}
		}
		else {
			Util.sendMessage(p, "&cYou do not yet have the required skill for this tier!");
		}
	}

}
