package me.Neoblade298.NeoProfessions.Methods;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Utilities.CulinarianUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class CulinarianMethods {
	
	Main main;
	Economy econ;
	
	// Constants
	final static int GARNISH_COST = 500;
	final static int GARNISH_ESSENCE = 1;
	final static int PRESERVE_COST = 500;
	final static int PRESERVE_ESSENCE = 1;
	final static int SPICE_COST = 500;
	final static int SPICE_ESSENCE = 1;
	
	public CulinarianMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}
	
	public void garnish(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.equals(Material.AIR)) {
			int level = CulinarianUtils.getFoodLevel(item);
			if(p.hasPermission("culinarian.garnish." + level)) {
				boolean isFood = false;
				boolean isBoosted = false;
				for(String line : item.getItemMeta().getLore()) {
					if(line.contains("Recipe")) {
						isFood = true;
					}
					if(line.contains("Garnished")) {
						isBoosted = true;
					}
					if(line.contains("Spiced")) {
						isBoosted = true;
					}
					if(line.contains("Preserved")) {
						isBoosted = true;
					}
				}
				if(isFood) {
					if(!isBoosted) {
						if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), GARNISH_ESSENCE)) {
							if(econ.has(p, GARNISH_COST)) {
								ItemMeta meta = item.getItemMeta();
								ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
								lore.add("§9Garnished (1.3x Attribute Boost)");
								meta.setLore(lore);
								item.setItemMeta(meta);
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), GARNISH_ESSENCE));
								econ.withdrawPlayer(p, GARNISH_COST);
								Util.sendMessage(p, "&7Successfully garnished dish!");
							}
							else {
								Util.sendMessage(p, "&cYou gold the materials to garnish this!");
							}
						}
						else {
							Util.sendMessage(p, "&cYou lack the materials to garnish this!");
						}
					}
					else {
						Util.sendMessage(p, "&cThis food cannot be boosted any further!");
					}
				}
				else {
					Util.sendMessage(p, "&cCannot garnish this item!");
				}
			}
			else {
				Util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}

}
