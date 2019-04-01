package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class LegendaryRecipeItems {
	Util util;
	IngredientRecipeItems ingr;
	Tier1RecipeItems t1;
	Tier2RecipeItems t2;
	Tier3RecipeItems t3;
	public LegendaryRecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
		t1 = new Tier1RecipeItems();
		t2 = new Tier2RecipeItems();
		t3 = new Tier3RecipeItems();
	}
	
	public ItemStack getDragonScrambledEggs() {
		ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lDragon Scrambled Eggs");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getNeoFullCourseSpecial() {
		ItemStack item = new ItemStack(Material.COOKED_BEEF);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lNeo's Full Course Special");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTobiasFamousCake() {
		ItemStack item = new ItemStack(Material.CAKE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lTobias' Famous Cake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLowDistrictCheeseSteak() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lLow District Cheese Steak");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMattiforniaRoll() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lMattifornia Roll");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return util.setData(item, 10);
	}
	
	public ItemStack getTilanSalad() {
		ItemStack item = new ItemStack(Material.RABBIT_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lTilan's Salad");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSuperSundae() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lSuper's Sundae");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getDragonScrambledEggsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(t3.getScrambledEggs(), 3));
		recipe.add(util.setAmount(ingr.getPepper(), 2));
		recipe.add(util.setAmount(ingr.getTomato(), 2));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getNeoFullCourseSpecialRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getBeetrootSauce(), 2));
		recipe.add(util.setAmount(ingr.getGreens(), 2));
		recipe.add(ingr.getSalt());
		recipe.add(t2.getAle());
		recipe.add(t2.getSteakWithGreenBeans());
		recipe.add(t3.getGardenSalad());
		recipe.add(t3.getLoadedBakedPotato());
		return recipe;
	}
	
	public ArrayList<ItemStack> getTobiasFamousCakeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		recipe.add(util.setAmount(t3.getSpongeCake(), 2));
		recipe.add(util.setAmount(ingr.getHoney(), 3));
		recipe.add(ingr.getYeast());
		recipe.add(t1.getIceCream());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLowDistrictCheeseSteakRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getToast(), 2));
		recipe.add(util.setAmount(ingr.getCheese(), 4));
		recipe.add(t3.getZestySteak());
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getMattiforniaRollRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.RAW_FISH));
		recipe.add(util.setData(new ItemStack(Material.RAW_FISH), 1));
		recipe.add(util.setData(new ItemStack(Material.RAW_FISH), 2));
		recipe.add(util.setData(new ItemStack(Material.RAW_FISH), 3));
		recipe.add(util.setAmount(ingr.getRice(), 3));
		recipe.add(util.setAmount(ingr.getExoticGreens(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getTilanSaladRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(t1.getCuredFlesh());
		recipe.add(ingr.getTomato());
		recipe.add(ingr.getCorn());
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getExoticGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSuperSundaeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.DIAMOND));
		recipe.add(new ItemStack(Material.SUGAR, 2));
		recipe.add(new ItemStack(Material.ICE, 2));
		recipe.add(t1.getHotChocolate());
		recipe.add(t1.getIceCream());
		return recipe;
	}
}
