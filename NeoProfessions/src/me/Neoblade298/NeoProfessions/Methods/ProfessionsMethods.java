package me.Neoblade298.NeoProfessions.Methods;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class ProfessionsMethods {

	Professions main;
	Economy econ;
	Util util;
	CurrencyManager cm;

	// Constants

	// Prices
	static final int ARTIFACT_PRICE = 1000000;

	public ProfessionsMethods(Professions main) {
		this.main = main;
		this.econ = main.getEconomy();
		util = new Util();
		cm = main.cManager;
	}
	
	public void artifactItem(Player p) {
		PlayerInventory inv = p.getInventory();
		ItemStack item = inv.getItemInMainHand();
		
		if (item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).contains("Legendary")) {
			if (econ.has(p, ARTIFACT_PRICE)) {
				ItemMeta meta = item.getItemMeta();
				ArrayList<String> lore = (ArrayList<String>) meta.getLore();
				
				// Display name color change
				meta.setDisplayName("§b" + ChatColor.stripColor(meta.getDisplayName()));
				
				// Get the item type and level
				String tierLine = lore.get(0);
				String levelLine = lore.get(1);
				String type = tierLine.substring(tierLine.indexOf('y') + 2);
				String parsedType = type.toLowerCase();
				int level = Integer.parseInt(levelLine.split(" ")[2]);
				
				// Replace item tier
				lore.set(0, "§7Tier: §bArtifact " + type);
				
				// Parse item type
				switch (type) {
				case "Reinforced Boots":
					parsedType = "rboots";
					item.setType(Material.NETHERITE_BOOTS);
					break;
				case "Reinforced Helmet":
					parsedType = "rhelmet";
					item.setType(Material.NETHERITE_HELMET);
					break;
				case "Reinforced Leggings":
					parsedType = "rleggings";
					item.setType(Material.NETHERITE_LEGGINGS);
					break;
				case "Reinforced Chestplate":
					parsedType = "rchestplate";
					item.setType(Material.NETHERITE_CHESTPLATE);
					break;
				case "Infused Boots":
					parsedType = "iboots";
					item.setType(Material.NETHERITE_BOOTS);
					break;
				case "Infused Helmet":
					parsedType = "ihelmet";
					item.setType(Material.NETHERITE_HELMET);
					break;
				case "Infused Leggings":
					parsedType = "ileggings";
					item.setType(Material.NETHERITE_LEGGINGS);
					break;
				case "Infused Chestplate":
					parsedType = "ichestplate";
					item.setType(Material.NETHERITE_CHESTPLATE);
					break;
				case "Sword":
					item.setType(Material.NETHERITE_SWORD);
					break;
				case "Axe":
					item.setType(Material.NETHERITE_AXE);
					break;
				case "Spellsword":
					item.setType(Material.NETHERITE_SWORD);
					break;
				case "Wand":
					item.setType(Material.NETHERITE_HOE);
					break;
				}
				
				// Generate a random artifact of the same type
				ItemStack artifact = main.neogear.settings.get(parsedType).get(level).generateItem("artifact", level);
				
				// Find start and end of base attributes
				int start = 0, end = 0;
				for (int i = 0; i < lore.size(); i++) {
					String line = lore.get(i);
					if (line.contains("Base Attributes")) {
						start = i + 1;
					}
					if (line.contains("Durability") || line.contains("Bonus Attributes")) {
						end = i - 1;
						break;
					}
				}
				
				// Replace attributes
				ArrayList<String> artifactLore = (ArrayList<String>) artifact.getItemMeta().getLore();
				for (int i = start; i <= end; i++) {
					lore.set(i, artifactLore.get(i));
				}
				
				meta.setLore(lore);
				item.setItemMeta(meta);
				
				if (util.isArmor(item)) {
					util.setMaxDurability(item, util.getMaxDurability(item) + 100);
				}
				else {
					util.setMaxDurability(item, util.getMaxDurability(item) + 200);
				}
				util.setCurrentDurability(item, util.getMaxDurability(item));
				
				Bukkit.broadcastMessage("§4[§c§lMLMC§4] §e" + p.getName() + " §7has converted their item into an §bArtifact§7!");
				econ.withdrawPlayer(p, ARTIFACT_PRICE);
			}
			else {
				Util.sendMessage(p, "&cYou don't have enough money!");
			}
		}
		else {
			Util.sendMessage(p, "&cItem is not valid for artifact conversion!");
		}
	}
	
	public void fixArtifact(Player p) {
		PlayerInventory inv = p.getInventory();
		ItemStack item = inv.getItemInMainHand();
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		
		// Get the item type and level
		String tierLine = lore.get(0);
		String type = tierLine.substring(tierLine.indexOf("ct") + 3);
		
		// Parse item type
		switch (type) {
		case "Reinforced Boots":
			item.setType(Material.NETHERITE_BOOTS);
			break;
		case "Reinforced Helmet":
			item.setType(Material.NETHERITE_HELMET);
			break;
		case "Reinforced Leggings":
			item.setType(Material.NETHERITE_LEGGINGS);
			break;
		case "Reinforced Chestplate":
			item.setType(Material.NETHERITE_CHESTPLATE);
			break;
		case "Infused Boots":
			item.setType(Material.NETHERITE_BOOTS);
			break;
		case "Infused Helmet":
			item.setType(Material.NETHERITE_HELMET);
			break;
		case "Infused Leggings":
			item.setType(Material.NETHERITE_LEGGINGS);
			break;
		case "Infused Chestplate":
			item.setType(Material.NETHERITE_CHESTPLATE);
			break;
		case "Sword":
			item.setType(Material.NETHERITE_SWORD);
			break;
		case "Axe":
			item.setType(Material.NETHERITE_AXE);
			break;
		case "Spellsword":
			item.setType(Material.NETHERITE_SWORD);
			break;
		case "Wand":
			item.setType(Material.NETHERITE_HOE);
			break;
		}
		Util.sendMessage(p, "&7Artifact fixed.");
	}

}
