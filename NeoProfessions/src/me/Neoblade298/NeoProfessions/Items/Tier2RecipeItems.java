package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class Tier2RecipeItems {
	Util util;
	IngredientRecipeItems ingr;
	public Tier2RecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
	}
	
	public ArrayList<ItemStack> getTier2Recipes() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getSteakWithGreenBeans());
		list.add(getLemonade());
		list.add(getHoneyedHam());
		list.add(getCandyBar());
		list.add(getAle());
		list.add(getSandwich());
		list.add(getBeefStew());
		list.add(getExoticTea());
		list.add(getBartrandCornbread());
		list.add(getTunaSandwich());
		list.add(getApplePie());
		list.add(getBeer());
		list.add(getHashBrowns());
		list.add(getLemonTart());
		list.add(getWrappedChicken());
		list.add(getMysteryMeat());
		list.add(getTomatoSoup());
		list.add(getCornOnTheCob());
		list.add(getBreadPudding());
		list.add(getChickenPasta());
		return list;
	}
	
	public ItemStack getSteakWithGreenBeans() {
		ItemStack item = new ItemStack(Material.COOKED_BEEF);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Steak w/ Green Beans");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getButteredToast() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Buttered Toast");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLemonade() {
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lemonade");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHoneyedHam() {
		ItemStack item = new ItemStack(Material.GRILLED_PORK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Honeyed Ham");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCandyBar() {
		ItemStack item = new ItemStack(Material.COOKIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candy Bar");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getAle() {
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Ale");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSandwich() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sandwich");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBeefStew() {
		ItemStack item = new ItemStack(Material.RABBIT_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Beef Stew");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getExoticTea() {
		ItemStack item = new ItemStack(Material.MUSHROOM_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Exotic Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBartrandCornbread() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Bartrand's Cornbread");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTunaSandwich() {
		ItemStack item = new ItemStack(Material.COOKED_FISH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Tuna Sandwich");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getApplePie() {
		ItemStack item = new ItemStack(Material.PUMPKIN_PIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Apple Pie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBeer() {
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Beer");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHashBrowns() {
		ItemStack item = new ItemStack(Material.BAKED_POTATO);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hash Browns");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLemonTart() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lemon Tart");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return util.setData(item, 11);
	}
	
	public ItemStack getWrappedChicken() {
		ItemStack item = new ItemStack(Material.GRILLED_PORK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Wrapped Chicken");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMysteryMeat() {
		ItemStack item = new ItemStack(Material.MUSHROOM_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mystery Meat");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTomatoSoup() {
		ItemStack item = new ItemStack(Material.BEETROOT_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Tomato Soup");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCornOnTheCob() {
		ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Corn on the Cob");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBreadPudding() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Bread Pudding");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChickenPasta() {
		ItemStack item = new ItemStack(Material.COOKED_CHICKEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chicken Pasta");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 21");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getSteakWithGreenBeansRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(ingr.getGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getButteredToastRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getToast());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLemonadeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(ingr.getLemon());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHoneyedHamRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GRILLED_PORK));
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getCandyBarRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 3));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getAleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.RED_MUSHROOM));
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSandwichRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD, 2));
		recipe.add(new ItemStack(Material.GRILLED_PORK));
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getTomato());
		return recipe;
	}
	
	public ArrayList<ItemStack> getBeefStewRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getExoticTeaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.RED_MUSHROOM));
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getBartrandCornbreadRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getToast());
		recipe.add(ingr.getCorn());
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getTunaSandwichRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD, 2));
		recipe.add(new ItemStack(Material.COOKED_FISH));
		recipe.add(ingr.getBeetrootSauce());
		recipe.add(ingr.getGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getApplePieRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.APPLE, 3));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(new ItemStack(Material.SUGAR));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBeerRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(ingr.getYeast());
		recipe.add(ingr.getHops());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHashBrownsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.POTATO_ITEM));
		recipe.add(ingr.getBeetrootSauce());
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getLemonTartRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(ingr.getYeast());
		recipe.add(ingr.getLemon());
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getWrappedChickenRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(util.setAmount(ingr.getCheese(), 2));
		recipe.add(ingr.getPepper());
		recipe.add(ingr.getTortilla());
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMysteryMeatRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SPIDER_EYE));
		recipe.add(new ItemStack(Material.ROTTEN_FLESH));
		recipe.add(new ItemStack(Material.BONE));
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(ingr.getSpices());
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getTomatoSoupRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getTomato());
		return recipe;
	}
	
	public ArrayList<ItemStack> getCornOnTheCobRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.STICK));
		recipe.add(ingr.getButter());
		recipe.add(ingr.getCorn());
		return recipe;
	}
	
	public ArrayList<ItemStack> getBreadPuddingRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.BREAD));
		recipe.add(ingr.getHoney());
		recipe.add(util.setAmount(ingr.getSpices(), 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getChickenPastaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(ingr.getButter());
		recipe.add(util.setAmount(ingr.getPasta(), 2));
		recipe.add(util.setAmount(ingr.getGreens(), 3));
		return recipe;
	}
}
