package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class DrinksRecipeItems {
	
	public static ItemStack getBlackWidow() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(0, 0, 0));
		meta.setDisplayName("§9Black Widow");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getPinkPanther() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 165, 230));
		meta.setDisplayName("§9Pink Panther");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getMidnightKiss() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(153, 0, 153));
		meta.setDisplayName("§9Midnight Kiss");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getMidnightBlue() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(0, 0, 75));
		meta.setDisplayName("§9Midnight Blue");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getGoodAndEvil() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 235, 150));
		meta.setDisplayName("§9Good and Evil");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getThorHammer() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(45, 10, 45));
		meta.setDisplayName("§9Thor's Hammer");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getJackFrost() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(145, 255, 250));
		meta.setDisplayName("§9Jack Frost");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getWhiteRussian() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 250, 215));
		meta.setDisplayName("§9White Russian");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getSwampWater() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(45, 255, 60));
		meta.setDisplayName("§9Swamp Water");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getBlueMotorcycle() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(110, 150, 250));
		meta.setDisplayName("§9Blue Motorcycle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getRedDeath() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(230, 115, 20));
		meta.setDisplayName("§9Red Death");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getBombsicle() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(0, 215, 255));
		meta.setDisplayName("§9Bombsicle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getSweetTart() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(250, 95, 220));
		meta.setDisplayName("§9Sweet Tart");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getPinaColada() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 230, 250));
		meta.setDisplayName("§9Pina Colada");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getMargaritaOnTheRocks() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(200, 255, 210));
		meta.setDisplayName("§9Margarita on the Rocks");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getBloodyMary() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 75, 75));
		meta.setDisplayName("§9Bloody Mary");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
}
