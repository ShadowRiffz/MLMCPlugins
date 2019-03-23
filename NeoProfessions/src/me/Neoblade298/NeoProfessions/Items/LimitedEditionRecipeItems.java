package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class LimitedEditionRecipeItems {
	public static ArrayList<ItemStack> getLimitedEditionRecipes() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getCandyCorn());
		list.add(getWitchBrew());
		list.add(getBakedEyeball());
		list.add(getCandyCane());
		list.add(getGingerbread());
		list.add(getEggnog());
		list.add(getSmokedHam());
		list.add(getLasagna());
		list.add(getLemonSoda());
		list.add(getOlympianGyro());
		list.add(getCupcake());
		list.add(getFishAndChips());
		list.add(getEscargot());
		list.add(getVitalac());
		list.add(getBelgianWaffle());
		list.add(getVioletDirtPie());
		return list;
	}
	public static ItemStack getCandyCorn() {
		ItemStack item = new ItemStack(Material.GOLD_NUGGET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candy Corn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getWitchBrew() {
		ItemStack item = new ItemStack(Material.BEETROOT_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Witch's Brew");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getBakedEyeball() {
		ItemStack item = new ItemStack(Material.SPIDER_EYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Baked Eyeball");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCandyCane() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candy Cane");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 1);
	}

	public static ItemStack getGingerbread() {
		ItemStack item = new ItemStack(Material.COOKIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Gingerbread");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getEggnog() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Eggnog");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getSmokedHam() {
		ItemStack item = new ItemStack(Material.GRILLED_PORK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Smoked Ham");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getLasagna() {
		ItemStack item = new ItemStack(Material.BLAZE_ROD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lasagna");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getLemonSoda() {
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lemon Soda");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getOlympianGyro() {
		ItemStack item = new ItemStack(Material.COOKED_MUTTON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Olympian Gyro");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getCupcake() {
		ItemStack item = new ItemStack(Material.CHORUS_FRUIT_POPPED);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Cupcake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getFishAndChips() {
		ItemStack item = new ItemStack(Material.COOKED_FISH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Fish And Chips");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getEscargot() {
		ItemStack item = new ItemStack(Material.COOKED_CHICKEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Escargot");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getVitalac() {
		ItemStack item = new ItemStack(Material.COOKED_MUTTON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Vitalac");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getBelgianWaffle() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Belgian Waffle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public static ItemStack getVioletDirtPie() {
		ItemStack item = new ItemStack(Material.PUMPKIN_PIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Violet's Dirt Pie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ArrayList<ItemStack> getCandyCornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR, 5));
		recipe.add(IngredientRecipeItems.getCorn());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getWitchBrewRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SPIDER_EYE));
		recipe.add(new ItemStack(Material.BONE));
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(new ItemStack(Material.GLOWSTONE_DUST));
		recipe.add(new ItemStack(Material.REDSTONE));
		recipe.add(IngredientRecipeItems.getSalt());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getBakedEyeballRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.EYE_OF_ENDER));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getCandyCaneRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(Util.setData(new ItemStack(Material.INK_SACK, 4), 1));
		recipe.add(Util.setData(new ItemStack(Material.INK_SACK, 2), 15));
		recipe.add(new ItemStack(Material.SUGAR, 3));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getGingerbreadRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(IngredientRecipeItems.getSpices());
		recipe.add(IngredientRecipeItems.getGreens());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getSmokedHamRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GRILLED_PORK));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getEggnogRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(IngredientRecipeItems.getSpices());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getLasagnaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(Util.setAmount(IngredientRecipeItems.getPasta(), 4));
		recipe.add(IngredientRecipeItems.getTomato());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getLemonSodaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR, 2));
		recipe.add(new ItemStack(Material.IRON_INGOT, 4));
		recipe.add(new ItemStack(Material.PACKED_ICE));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(IngredientRecipeItems.getLemon());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getOlympianGyroRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD));
		recipe.add(IngredientRecipeItems.getBeetrootSauce());
		recipe.add(IngredientRecipeItems.getOnion());
		recipe.add(IngredientRecipeItems.getTomato());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getCupcakeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.WHEAT, 2));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(IngredientRecipeItems.getOil());
		recipe.add(IngredientRecipeItems.getButter());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getFishAndChipsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_FISH));
		recipe.add(new ItemStack(Material.POTATO_ITEM));
		recipe.add(IngredientRecipeItems.getLemon());
		recipe.add(IngredientRecipeItems.getSalt());
		recipe.add(IngredientRecipeItems.getOil());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getEscargotRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(Util.setAmount(IngredientRecipeItems.getSalt(), 4));
		recipe.add(Util.setAmount(IngredientRecipeItems.getButter(), 2));
		recipe.add(Util.setAmount(IngredientRecipeItems.getGreens(), 2));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getVitalacRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_MUTTON, 3));
		recipe.add(new ItemStack(Material.PORK, 2));
		recipe.add(Util.setAmount(IngredientRecipeItems.getSpices(), 3));
		recipe.add(IngredientRecipeItems.getPepper());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getBelgianWaffleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(IngredientRecipeItems.getYeast());
		recipe.add(IngredientRecipeItems.getHoney());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getVioletDirtPieRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.DIRT, 3));
		recipe.add(new ItemStack(Material.PUMPKIN_PIE, 2));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(Util.setAmount(IngredientRecipeItems.getHoney(), 3));
		return recipe;
	}
}
