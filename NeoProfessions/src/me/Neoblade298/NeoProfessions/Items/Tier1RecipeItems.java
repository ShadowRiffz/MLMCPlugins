package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class Tier1RecipeItems {
	public static ItemStack getCuredFlesh() {
		ItemStack item = new ItemStack(Material.COOKED_MUTTON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Cured Flesh");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSunflowerSeeds() {
		ItemStack item = new ItemStack(Material.PUMPKIN_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sunflower Seeds");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getBoiledEgg() {
		ItemStack item = new ItemStack(Material.FIREWORK_CHARGE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Boiled Egg");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getIceCream() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Ice Cream");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getHotChocolate() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§(Hot Chocolate");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 3);
	}
	
	public static ItemStack getGreenTea() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Green Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getBarleyTea() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Barley Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getMashedPotatoes() {
		ItemStack item = new ItemStack(Material.BAKED_POTATO);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mashed Potato");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSpinach() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Spinach");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 2);
	}
	
	public static ItemStack getChocolateTruffle() {
		ItemStack item = new ItemStack(Material.SPIDER_EYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chocolate Truffle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getMusli() {
		ItemStack item = new ItemStack(Material.SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Musli");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getCandiedApple() {
		ItemStack item = new ItemStack(Material.APPLE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candied Apple");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getMacAndCheese() {
		ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mac and Cheese");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getChocolateMilk() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chocolate Milk");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 3);
	}
	
	public static ItemStack getCheeseTortilla() {
		ItemStack item = new ItemStack(Material.COOKIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Cheese Tortilla");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSushi() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sushi");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 10);
	}
	
	public static ItemStack getPottage() {
		ItemStack item = new ItemStack(Material.MUSHROOM_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Pottage");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getButteredPopcorn() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Buttered Popcorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 11);
	}
	
	public static ItemStack getChipsAndSalsa() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chips and Salsa");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 1);
	}
	
	public static ItemStack getGruel() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("9Gruel");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ArrayList<ItemStack> getSaltRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		return recipe;
	}
}
