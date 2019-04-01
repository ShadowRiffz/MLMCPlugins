package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class Tier3RecipeItems {
	Util util;
	IngredientRecipeItems ingr;
	public Tier3RecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
	}
	
	public ArrayList<ItemStack> getTier3Recipes() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getGardenSalad());
		list.add(getChickenLeg());
		list.add(getApplePorkchops());
		list.add(getScrambledEggs());
		list.add(getLambKabob());
		list.add(getZestySteak());
		list.add(getLoadedBakedPotato());
		list.add(getSweetBuns());
		list.add(getSpaghettiBolognese());
		list.add(getFilletedSalmon());
		list.add(getRoastBream());
		list.add(getSmoothie());
		list.add(getHeartyBurrito());
		list.add(getHeroSandwich());
		list.add(getLambStew());
		list.add(getHoneyGlazedChicken());
		list.add(getAppleCider());
		list.add(getMushroomPasty());
		list.add(getButteredWortes());
		list.add(getSpongeCake());
		list.add(getChickenParmesan());
		return list;
	}
	
	public ItemStack getGardenSalad() {
		ItemStack item = new ItemStack(Material.RABBIT_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Garden Salad");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChickenLeg() {
		ItemStack item = new ItemStack(Material.COOKED_CHICKEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chicken Leg");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getApplePorkchops() {
		ItemStack item = new ItemStack(Material.GRILLED_PORK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Apple Porkchops");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getScrambledEggs() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Scrambled Eggs");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return util.setData(item, 11);
	}
	
	public ItemStack getLambKabob() {
		ItemStack item = new ItemStack(Material.COOKED_MUTTON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lamb Kabob");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getZestySteak() {
		ItemStack item = new ItemStack(Material.COOKED_BEEF);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Zesty Steak");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLoadedBakedPotato() {
		ItemStack item = new ItemStack(Material.BAKED_POTATO);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Loaded Baked Potato");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSweetBuns() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sweet Buns");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSpaghettiBolognese() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Spaghetti Bolognese");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return util.setData(item, 1);
	}
	
	public ItemStack getFilletedSalmon() {
		ItemStack item = new ItemStack(Material.COOKED_FISH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Filleted Salmon");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return util.setData(item, 1);
	}
	
	public ItemStack getRoastBream() {
		ItemStack item = new ItemStack(Material.COOKED_FISH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Roast Bream");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSmoothie() {
		ItemStack item = new ItemStack(Material.BEETROOT_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Smoothie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHeartyBurrito() {
		ItemStack item = new ItemStack(Material.ROTTEN_FLESH);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hearty Burrito");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHeroSandwich() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hero Sandwich");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLambStew() {
		ItemStack item = new ItemStack(Material.RABBIT_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lamb Stew");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHoneyGlazedChicken() {
		ItemStack item = new ItemStack(Material.COOKED_CHICKEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Honey Glazed Chicken");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getAppleCider() {
		ItemStack item = new ItemStack(Material.WATER_BUCKET);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Apple Cider");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMushroomPasty() {
		ItemStack item = new ItemStack(Material.RABBIT_STEW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mushroom Pasty");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getButteredWortes() {
		ItemStack item = new ItemStack(Material.MUSHROOM_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Buttered Wortes");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSpongeCake() {
		ItemStack item = new ItemStack(Material.PUMPKIN_PIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sponge Cake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChickenParmesan() {
		ItemStack item = new ItemStack(Material.COOKED_CHICKEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chicken Parmesan");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 21");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getGardenSaladRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(util.setAmount(ingr.getGreens(), 3));
		recipe.add(util.setAmount(ingr.getTomato(), 2));
		recipe.add(ingr.getCorn());
		return recipe;
	}
	
	public ArrayList<ItemStack> getChickenLegRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN, 2));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getOil());
		return recipe;
	}
	
	public ArrayList<ItemStack> getApplePorkchopsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GRILLED_PORK, 2));
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getScrambledEggsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.EGG, 3));
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		recipe.add(ingr.getOil());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLambKabobRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.STICK));
		recipe.add(new ItemStack(Material.COOKED_MUTTON, 2));
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		recipe.add(ingr.getOil());
		recipe.add(ingr.getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getZestySteakRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(ingr.getBeetrootSauce());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLoadedBakedPotatoRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BAKED_POTATO));
		recipe.add(new ItemStack(Material.GRILLED_PORK));
		recipe.add(util.setAmount(ingr.getButter(), 2));
		recipe.add(ingr.getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSweetBunsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT, 3));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.SUGAR, 3));
		recipe.add(ingr.getYeast());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSpaghettiBologneseRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(util.setAmount(ingr.getPasta(), 3));
		recipe.add(util.setAmount(ingr.getTomato(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getFilletedSalmonRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setData(new ItemStack(Material.COOKED_FISH), 1));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getOnion());
		recipe.add(ingr.getButter());
		return recipe;
	}
	
	public ArrayList<ItemStack> getRoastBreamRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_FISH));
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		recipe.add(ingr.getButter());
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getOnion());
		recipe.add(ingr.getLemon());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSmoothieRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(new ItemStack(Material.MELON));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(new ItemStack(Material.ICE));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(ingr.getLemon());
		recipe.add(ingr.getExoticGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHeartyBurritoRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(new ItemStack(Material.POTATO_ITEM));
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getBeetrootSauce());
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getTortilla());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHeroSandwichRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GRILLED_PORK));
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getToast());
		recipe.add(ingr.getTomato());
		recipe.add(ingr.getGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLambStewRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.POTATO_ITEM));
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(new ItemStack(Material.COOKED_MUTTON));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getHops());
		recipe.add(ingr.getGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHoneyGlazedChickenRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(util.setAmount(ingr.getHoney(), 2));
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getPepper());
		return recipe;
	}
	
	public ArrayList<ItemStack> getAppleCiderRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.APPLE, 3));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMushroomPastyRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.RED_MUSHROOM));
		recipe.add(ingr.getOil());
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getButteredWortesRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(util.setAmount(ingr.getGreens(), 2));
		recipe.add(util.setAmount(ingr.getButter(), 2));
		recipe.add(ingr.getOnion());
		recipe.add(ingr.getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSpongeCakeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(new ItemStack(Material.WHEAT, 3));
		recipe.add(ingr.getYeast());
		recipe.add(ingr.getButter());
		return recipe;
	}
	
	public ArrayList<ItemStack> getChickenParmesanRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(util.setAmount(ingr.getTomato(), 2));
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getSpices());
		return recipe;
	}
}
