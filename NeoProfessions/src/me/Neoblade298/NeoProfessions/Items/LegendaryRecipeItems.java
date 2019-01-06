package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class LegendaryRecipeItems {
	
	public static ItemStack getDragonScrambledEggs() {
		ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lDragon Scrambled Eggs");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getNeoFullCourseSpecial() {
		ItemStack item = new ItemStack(Material.COOKED_BEEF);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lNeo's Full Course Special");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getTobiasFamousCake() {
		ItemStack item = new ItemStack(Material.CAKE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lTobias' Famous Cake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getLowDistrictCheeseSteak() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lLow District Cheese Steak");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getMattiforniaRoll() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lMattifornia Roll");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 10);
	}
	
	public static ItemStack getTilanSalad() {
		ItemStack item = new ItemStack(Material.RABBIT_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lTilan's Salad");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getSuperSundae() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lSuper's Sundae");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ArrayList<ItemStack> getDragonScrambledEggsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(Util.setAmount(Tier3RecipeItems.getScrambledEggs(), 3));
		recipe.add(Util.setAmount(IngredientRecipeItems.getPepper(), 2));
		recipe.add(Util.setAmount(IngredientRecipeItems.getTomato(), 2));
		recipe.add(IngredientRecipeItems.getSalt());
		recipe.add(IngredientRecipeItems.getSpices());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getNeoFullCourseSpecialRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(Util.setAmount(IngredientRecipeItems.getBeetrootSauce(), 2));
		recipe.add(Util.setAmount(IngredientRecipeItems.getGreens(), 2));
		recipe.add(IngredientRecipeItems.getSalt());
		recipe.add(Tier2RecipeItems.getAle());
		recipe.add(Tier2RecipeItems.getSteakWithGreenBeans());
		recipe.add(Tier3RecipeItems.getGardenSalad());
		recipe.add(Tier3RecipeItems.getLoadedBakedPotato());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getTobiasFamousCakeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(Util.setAmount(IngredientRecipeItems.getSalt(), 2));
		recipe.add(Util.setAmount(Tier3RecipeItems.getSpongeCake(), 2));
		recipe.add(Util.setAmount(IngredientRecipeItems.getHoney(), 3));
		recipe.add(IngredientRecipeItems.getYeast());
		recipe.add(Tier1RecipeItems.getIceCream());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getLowDistrictCheeseSteakRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(Util.setAmount(IngredientRecipeItems.getToast(), 2));
		recipe.add(Util.setAmount(IngredientRecipeItems.getCheese(), 4));
		recipe.add(Tier3RecipeItems.getZestySteak());
		recipe.add(IngredientRecipeItems.getSalt());
		recipe.add(IngredientRecipeItems.getSpices());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getMattiforniaRollRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.RAW_FISH));
		recipe.add(Util.setData(new ItemStack(Material.RAW_FISH), 1));
		recipe.add(Util.setData(new ItemStack(Material.RAW_FISH), 2));
		recipe.add(Util.setData(new ItemStack(Material.RAW_FISH), 3));
		recipe.add(Util.setAmount(IngredientRecipeItems.getRice(), 3));
		recipe.add(Util.setAmount(IngredientRecipeItems.getExoticGreens(), 2));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getTilanSaladRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(Tier1RecipeItems.getCuredFlesh());
		recipe.add(IngredientRecipeItems.getTomato());
		recipe.add(IngredientRecipeItems.getCorn());
		recipe.add(IngredientRecipeItems.getCheese());
		recipe.add(IngredientRecipeItems.getExoticGreens());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getSuperSundaeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.DIAMOND));
		recipe.add(new ItemStack(Material.SUGAR, 2));
		recipe.add(new ItemStack(Material.ICE, 2));
		recipe.add(Tier1RecipeItems.getHotChocolate());
		recipe.add(Tier1RecipeItems.getIceCream());
		return recipe;
	}
}
