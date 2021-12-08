package me.Neoblade298.NeoProfessions.Methods;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.Gear;
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
		
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			Util.sendMessage(p, "&cItem is outdated! Repair your item first to update it!");
			return;
		}
		if (!econ.has(p, ARTIFACT_PRICE)) {
			Util.sendMessage(p, "&cYou don't have enough money!!");
			return;
		}
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		
		// Get the item type and level
		NBTItem nbti = new NBTItem(item);
		int level = nbti.getInteger("level");
		String type = nbti.getString("gear");
		
		// Generate a random artifact of the same type
		ItemStack artifact = Gear.settings.get(type).get(level).generateItem("artifact", level);
		
		// Before changing anything, check if the versions match, check if the item is legendary
		if (new NBTItem(artifact).getInteger("version") != nbti.getInteger("version")) {
			Util.sendMessage(p, "&cItem is outdated! Repair your item first to update it!");
			return;
		}
		if (!nbti.getString("rarity").equals("legendary")) {
			Util.sendMessage(p, "&cItem is not legendary! Only legendary items can be artifacted!");
			return;
		}
		
		// Display name color change only if it had the default legendary color
		if (meta.getDisplayName().startsWith("§4")) {
			meta.setDisplayName("§b" + ChatColor.stripColor(meta.getDisplayName()));
		}
		
		// Replace item tier
		lore.set(2, "§7Rarity: §bArtifact " + type);
		
		// Change item type to netherite
		String mat = item.getType().name();
		if (mat.endsWith("HELMET")) {
			item.setType(Material.NETHERITE_HELMET);
		}
		else if (mat.endsWith("CHESTPLATE")) {
			item.setType(Material.NETHERITE_HELMET);
		}
		else if (mat.endsWith("LEGGINGS")) {
			item.setType(Material.NETHERITE_HELMET);
		}
		else if (mat.endsWith("BOOTS")) {
			item.setType(Material.NETHERITE_HELMET);
		}
		else if (mat.endsWith("SWORD")) {
			item.setType(Material.NETHERITE_SWORD);
		}
		else if (mat.endsWith("AXE")) {
			item.setType(Material.NETHERITE_AXE);
		}
		else if (mat.endsWith("HOE")) {
			item.setType(Material.NETHERITE_HOE);
		}
		
		
		// Find start and end of base attributes
		int start = -1, end = -1;
		for (int i = 0; i < lore.size(); i++) {
			String line = lore.get(i);
			
			if (line.contains("-")) {
				if (start == -1) {
					start = i + 1;
				}
				else {
					end = i + 1;
				}
			}
		}
		
		// Replace attributes
		ArrayList<String> artifactLore = (ArrayList<String>) artifact.getItemMeta().getLore();
		for (int i = start; i <= end; i++) {
			lore.set(i, artifactLore.get(i));
		}
		
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		if (Util.isArmor(item)) {
			util.setMaxDurability(item, util.getMaxDurability(item) + 100);
		}
		else {
			util.setMaxDurability(item, util.getMaxDurability(item) + 200);
		}
		util.setCurrentDurability(item, util.getMaxDurability(item));
		
		// Change nbt rarity
		nbti = new NBTItem(item);
		nbti.setString("rarity", "artifact");
		nbti.applyNBT(item);
		
		Bukkit.broadcastMessage("§4[§c§lMLMC§4] §e" + p.getName() + " §7has converted their item into an §bArtifact§7!");
		econ.withdrawPlayer(p, ARTIFACT_PRICE);
	}
}
