package me.Neoblade298.NeoProfessions.Methods;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoProfessions.CurrencyManager;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neogear.listeners.DurabilityListener;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class ProfessionsMethods {

	static Professions main;
	static Economy econ;
	static CurrencyManager cm;

	// Constants

	public ProfessionsMethods(Professions main) {
		ProfessionsMethods.main = main;
		econ = main.getEconomy();
		cm = main.cManager;
	}
	
	public static void createSlot(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		NBTItem nbti = new NBTItem(item);
		
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			Util.sendMessage(p, "&cThis item cannot be slotted!");
			return;
		}
		if (!nbti.hasKey("gear")) {
			Util.sendMessage(p, "&cItem is outdated! Use /prof convert to update it! Make sure it's a valid quest gear!");
			return;
		}
		
		int slotsCreated = nbti.getInteger("slotsCreated");
		int slotsMax = nbti.getInteger("slotsMax");
		
		if (slotsCreated >= slotsMax) {
			Util.sendMessage(p, "&cThis item cannot hold any more slots!");
			return;
		}
		int newSlot = slotsCreated + 1;
		int newLine = nbti.getInteger("slot" + slotsCreated + "Line") + 1;
		nbti.setInteger("slotsCreated", newSlot);
		nbti.setInteger("slot" + newSlot + "Line", newLine);
		nbti.applyNBT(item);
		
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		lore.add(newLine, "§8[Empty Slot]");
		meta.setLore(lore);
		item.setItemMeta(meta);
	}
	
	public static void givePaint(Player p, String red, String blue, String green) {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setDisplayName("§nDye");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§c§lRed: §f" + red);
		lore.add("§a§lGreen: §f" + blue);
		lore.add("§9§lBlue: §f" + green);
		meta.setLore(lore);
		Color color = Color.fromRGB(Integer.parseInt(red), Integer.parseInt(blue), Integer.parseInt(green));
		meta.setColor(color);
		item.setItemMeta(meta);
		p.getInventory().addItem(item);
	}
	
	public static void artifactItem(Player p) {
		PlayerInventory inv = p.getInventory();
		ItemStack item = inv.getItemInMainHand();
		
		if (!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			Util.sendMessage(p, "&cThis is not a quest gear!");
			return;
		}
		// Get the item type and level
		NBTItem nbti = new NBTItem(item);
		if (!nbti.hasKey("gear")) {
			Util.sendMessage(p, "&cItem is outdated! Use /prof convert to update it! Make sure it's a valid quest gear!");
			return;
		}
		
		if (!nbti.getString("rarity").equals("legendary")) {
			Util.sendMessage(p, "&cItem is not legendary! Only legendary items can be artifacted!");
			return;
		}
		
		nbti = new NBTItem(item);
		nbti.setString("rarity", "artifact");
		int slotsMax = nbti.getInteger("slotsMax") + 1;
		nbti.setInteger("slotsMax", slotsMax);
		nbti.applyNBT(item);
		ItemMeta meta = item.getItemMeta();
		ArrayList<String> lore = (ArrayList<String>) meta.getLore();
		
		// Display name color change only if it had the default legendary color
		if (meta.getDisplayName().startsWith("§4")) {
			meta.setDisplayName("§b" + ChatColor.stripColor(meta.getDisplayName()));
		}
		
		// Replace item tier
		lore.set(2, "§7Rarity: §bArtifact");
		lore.set(4, "§7Max Slots: " + slotsMax);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
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
		
		// Change nbt rarity
		DurabilityListener.fullRepairItem(p, item);
		
		Bukkit.broadcastMessage("§4[§c§lMLMC§4] §e" + p.getName() + " §7has converted their item into an §bArtifact§7!");
	}
}
