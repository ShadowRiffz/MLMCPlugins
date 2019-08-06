package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class IngredientRecipeItems {
	Util util;
	public IngredientRecipeItems() {
		util = new Util();
	}

	// Ingredients
	public ArrayList<ItemStack> getIngredients() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getSalt());
		list.add(getSpices());
		list.add(getGreens());
		list.add(getToast());
		list.add(getOil());
		list.add(getTomato());
		list.add(getButter());
		list.add(getLemon());
		list.add(getCorn());
		list.add(getHoney());
		list.add(getYeast());
		list.add(getBeetrootSauce());
		list.add(getPasta());
		list.add(getOnion());
		list.add(getHops());
		list.add(getCheese());
		list.add(getTortilla());
		list.add(getExoticGreens());
		list.add(getRice());
		list.add(getPopcorn());
		list.add(getPepper());
		return list;
	}
	
	public ItemStack getSalt() {
		ItemStack item = new ItemStack(Material.SUGAR);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSalt");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSpices() {
		ItemStack item = new ItemStack(Material.WHEAT_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSpices");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getGreens() {
		ItemStack item = new ItemStack(Material.CACTUS_GREEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eGreens");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getToast() {
		ItemStack item = new ItemStack(Material.BREAD);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eToast");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getOil() {
		ItemStack item = new ItemStack(Material.SPLASH_POTION);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eOil");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTomato() {
		ItemStack item = new ItemStack(Material.APPLE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eTomato");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getButter() {
		ItemStack item = new ItemStack(Material.DANDELION_YELLOW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eButter");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLemon() {
		ItemStack item = new ItemStack(Material.GLISTERING_MELON_SLICE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eLemon");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCorn() {
		ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eCorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHoney() {
		ItemStack item = new ItemStack(Material.DANDELION_YELLOW);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eHoney");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getYeast() {
		ItemStack item = new ItemStack(Material.MELON_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eYeast");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBeetrootSauce() {
		ItemStack item = new ItemStack(Material.BEETROOT_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eBeetroot Sauce");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPasta() {
		ItemStack item = new ItemStack(Material.BEETROOT_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§ePasta");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getOnion() {
		ItemStack item = new ItemStack(Material.BEETROOT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eOnion");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHops() {
		ItemStack item = new ItemStack(Material.COCOA_BEANS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eHops");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCheese() {
		ItemStack item = new ItemStack(Material.ORANGE_DYE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eCheese");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTortilla() {
		ItemStack item = new ItemStack(Material.COOKIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eTortilla");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getExoticGreens() {
		ItemStack item = new ItemStack(Material.CACTUS_GREEN);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eExotic Greens");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getRice() {
		ItemStack item = new ItemStack(Material.PUMPKIN_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eRice");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPopcorn() {
		ItemStack item = new ItemStack(Material.BEETROOT_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§ePopcorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPepper() {
		ItemStack item = new ItemStack(Material.SUGAR_CANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§ePeppers");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 21");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getVodka() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 255, 255));
		meta.setDisplayName("§eVodka");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 22");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getRum() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(153, 51, 0));
		meta.setDisplayName("§eRum");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 23");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTequila() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 204, 102));
		meta.setDisplayName("§eTequila");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 24");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getSaltRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSpicesRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.NETHER_WART));
		recipe.add(new ItemStack(Material.WHEAT_SEEDS));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.REDSTONE));
		recipe.add(new ItemStack(Material.CACTUS_GREEN));
		return recipe;
	}
	
	public ArrayList<ItemStack> getGreensRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.OAK_LEAVES));
		return recipe;
	}
	
	public ArrayList<ItemStack> getToastRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD));
		return recipe;
	}
	
	public ArrayList<ItemStack> getOilRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_COD));
		return recipe;
	}
	
	public ArrayList<ItemStack> getTomatoRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BEETROOT));
		recipe.add(new ItemStack(Material.POPPY));
		return recipe;
	}
	
	public ArrayList<ItemStack> getButterRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.STICK));
		return recipe;
	}
	
	public ArrayList<ItemStack> getLemonRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GOLD_NUGGET, 2));
		recipe.add(new ItemStack(Material.APPLE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getCornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GOLDEN_CARROT));
		recipe.add(new ItemStack(Material.BLAZE_ROD));
		return recipe;
	}
	
	public ArrayList<ItemStack> getHoneyRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUNFLOWER));
		recipe.add(new ItemStack(Material.SUGAR));
		return recipe;
	}
	
	public ArrayList<ItemStack> getYeastRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.BLAZE_POWDER));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBeetrootSauceRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BEETROOT, 6));
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(util.setAmount(getSpices(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPastaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getOnionRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BEETROOT));
		recipe.add(new ItemStack(Material.FERMENTED_SPIDER_EYE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getHopsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.VINE, 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getCheeseRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getTortillaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT, 3));
		recipe.add(util.setAmount(getCorn(), 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getExoticGreensRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.CACTUS));
		recipe.add(new ItemStack(Material.VINE));
		recipe.add(new ItemStack(Material.OAK_LEAVES));
		recipe.add(new ItemStack(Material.CHORUS_FRUIT));
		return recipe;
	}
	
	public ArrayList<ItemStack> getRiceRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT_SEEDS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPopcornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(getCorn());
		return recipe;
	}
	
	public ArrayList<ItemStack> getPepperRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.CACTUS));
		recipe.add(new ItemStack(Material.LAVA_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getVodkaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.POTATO));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(getYeast());
		return recipe;
	}
	
	public ArrayList<ItemStack> getRumRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(getYeast());
		return recipe;
	}
	
	public ArrayList<ItemStack> getTequilaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.CACTUS));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(getYeast());
		return recipe;
	}
}
