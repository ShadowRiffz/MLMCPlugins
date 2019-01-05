package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class Tier3RecipeItems {
	
	public static ItemStack getGardenSalad() {
		ItemStack item = new ItemStack(Material.RABBIT_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Garden Salad");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getChickenLeg() {
		ItemStack item = new ItemStack(Material.COOKED_CHICKEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chicken Leg");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getApplePorkchops() {
		ItemStack item = new ItemStack(Material.GRILLED_PORK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Apple Porkchops");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getScrambledEggs() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Scrambled Eggs");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 11);
	}
	
	public static ItemStack getLambKabob() {
		ItemStack item = new ItemStack(Material.COOKED_MUTTON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lamb Kabob");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getZestySteak() {
		ItemStack item = new ItemStack(Material.COOKED_BEEF);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Zesty Steak");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getLoadedBakedPotato() {
		ItemStack item = new ItemStack(Material.BAKED_POTATO);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Loaded Baked Potato");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSweetBuns() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sweet Buns");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSpaghettiBolognese() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Spaghetti Bolognese");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 1);
	}
	
	public static ItemStack getFilletedSalmon() {
		ItemStack item = new ItemStack(Material.COOKED_FISH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Filleted Salmon");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 1);
	}
	
	public static ItemStack getRoastBream() {
		ItemStack item = new ItemStack(Material.COOKED_FISH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Roast Bream");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSmoothie() {
		ItemStack item = new ItemStack(Material.BEETROOT_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Smoothie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getHeartyBurrito() {
		ItemStack item = new ItemStack(Material.ROTTEN_FLESH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hearty Burrito");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getHeroSandwich() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hero Sandwich");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getLambStew() {
		ItemStack item = new ItemStack(Material.RABBIT_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lamb Stew");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getHoneyGlazedChicken() {
		ItemStack item = new ItemStack(Material.COOKED_CHICKEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Honey Glazed Chicken");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getAppleCider() {
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Apple Cider");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getMushroomPasty() {
		ItemStack item = new ItemStack(Material.RABBIT_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mushroom Pasty");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getButteredWortes() {
		ItemStack item = new ItemStack(Material.MUSHROOM_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Buttered Wortes");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSpongeCake() {
		ItemStack item = new ItemStack(Material.PUMPKIN_PIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sponge Cake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getChickenParmesan() {
		ItemStack item = new ItemStack(Material.COOKED_CHICKEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chicken Parmesan");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 21");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ArrayList<ItemStack> getSteakWithGreenBeansRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(IngredientRecipeItems.getGreens());
		return recipe;
	}
}
