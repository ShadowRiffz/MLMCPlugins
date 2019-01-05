package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class RecipeItems {

	// Ingredients
	public static ItemStack getSalt() {
		ItemStack item = new ItemStack(Material.SUGAR);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSalt");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSpices() {
		ItemStack item = new ItemStack(Material.SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSpices");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getGreens() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSpices");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 2);
	}
	
	public static ItemStack getToast() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eToast");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getOil() {
		ItemStack item = new ItemStack(Material.SPLASH_POTION);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eOil");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getTomato() {
		ItemStack item = new ItemStack(Material.APPLE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eTomato");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getButter() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eButter");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 11);
	}
	
	public static ItemStack getLemon() {
		ItemStack item = new ItemStack(Material.SPECKLED_MELON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eLemon");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getCorn() {
		ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eCorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ArrayList<ItemStack> getSaltRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getSpicesRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.NETHER_STALK));
		recipe.add(new ItemStack(Material.SEEDS));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.REDSTONE));
		recipe.add(Util.setData(new ItemStack(Material.INK_SACK), 3));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getGreensRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.LEAVES, 3));
		return recipe;
	}
}
