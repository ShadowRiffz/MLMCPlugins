package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class Tier1RecipeItems {
	Util util;
	IngredientRecipeItems ingr;
	public Tier1RecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
	}
	
	public ArrayList<ItemStack> getTier1Recipes() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getCuredFlesh());
		list.add(getSunflowerSeeds());
		list.add(getBoiledEgg());
		list.add(getIceCream());
		list.add(getHotChocolate());
		list.add(getGreenTea());
		list.add(getMashedPotatoes());
		list.add(getSpinach());
		list.add(getChocolateTruffle());
		list.add(getMusli());
		list.add(getCandiedApple());
		list.add(getMacAndCheese());
		list.add(getChocolateMilk());
		list.add(getCheeseTortilla());
		list.add(getSushi());
		list.add(getPottage());
		list.add(getButteredPopcorn());
		list.add(getChipsAndSalsa());
		list.add(getGruel());
		return list;
	}
	
	public ItemStack getCuredFlesh() {
		ItemStack item = new ItemStack(Material.COOKED_MUTTON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Cured Flesh");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSunflowerSeeds() {
		ItemStack item = new ItemStack(Material.PUMPKIN_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sunflower WHEAT_SEEDS");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBoiledEgg() {
		ItemStack item = new ItemStack(Material.FIREWORK_STAR);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Boiled Egg");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getIceCream() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Ice Cream");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHotChocolate() {
		ItemStack item = new ItemStack(Material.GREEN_DYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§(Hot Chocolate");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getGreenTea() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Green Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBarleyTea() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Barley Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMashedPotatoes() {
		ItemStack item = new ItemStack(Material.BAKED_POTATO);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mashed Potatoes");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSpinach() {
		ItemStack item = new ItemStack(Material.GREEN_DYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Spinach");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChocolateTruffle() {
		ItemStack item = new ItemStack(Material.SPIDER_EYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chocolate Truffle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMusli() {
		ItemStack item = new ItemStack(Material.WHEAT_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Musli");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCandiedApple() {
		ItemStack item = new ItemStack(Material.APPLE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candied Apple");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMacAndCheese() {
		ItemStack item = new ItemStack(Material.GLOWSTONE_DUST);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mac and Cheese");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChocolateMilk() {
		ItemStack item = new ItemStack(Material.COCOA_BEANS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chocolate Milk");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCheeseTortilla() {
		ItemStack item = new ItemStack(Material.COOKIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Cheese Tortilla");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSushi() {
		ItemStack item = new ItemStack(Material.LIME_DYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sushi");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPottage() {
		ItemStack item = new ItemStack(Material.MUSHROOM_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Pottage");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getButteredPopcorn() {
		ItemStack item = new ItemStack(Material.YELLOW_DYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Buttered Popcorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChipsAndSalsa() {
		ItemStack item = new ItemStack(Material.RED_DYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chips and Salsa");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getGruel() {
		ItemStack item = new ItemStack(Material.MILK_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Gruel");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getCuredFleshRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.ROTTEN_FLESH));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSunflowerSeedsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUNFLOWER, 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBoiledEggRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.EGG));
		return recipe;
	}
	
	public ArrayList<ItemStack> getIceCreamRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getHotChocolateRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COCOA_BEANS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getGreenTeaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.OAK_LEAVES));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBarleyTeaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.WHEAT));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMashedPotatoesRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.POTATO));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSpinachRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.VINE));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getChocolateTruffleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COCOA_BEANS));
		recipe.add(new ItemStack(Material.RED_MUSHROOM));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMusliRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT_SEEDS));
		recipe.add(new ItemStack(Material.BEETROOT_SEEDS));
		recipe.add(new ItemStack(Material.PUMPKIN_SEEDS));
		recipe.add(new ItemStack(Material.MELON_SEEDS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getCandiedAppleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getMacAndCheeseRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getPasta());
		recipe.add(ingr.getButter());
		recipe.add(ingr.getCheese());
		return recipe;
	}
	
	public ArrayList<ItemStack> getChocolateMilkRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.COCOA_BEANS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getCheeseTortillaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getCheese(), 3));
		recipe.add(ingr.getTortilla());
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSushiRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getExoticGreens(), 3));
		recipe.add(new ItemStack(Material.COD));
		recipe.add(util.setAmount(ingr.getRice(), 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPottageRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getCheese(), 3));
		recipe.add(ingr.getTortilla());
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getButteredPopcornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getPopcorn());
		recipe.add(util.setAmount(ingr.getButter(), 4));
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getChipsAndSalsaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getTortilla());
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getExoticGreens());
		recipe.add(ingr.getTomato());
		recipe.add(ingr.getPepper());
		return recipe;
	}
	
	public ArrayList<ItemStack> getGruelRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(ingr.getRice());
		return recipe;
	}
}
