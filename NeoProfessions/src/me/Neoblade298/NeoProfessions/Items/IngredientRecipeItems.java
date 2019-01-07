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
		meta.setDisplayName("§eGreens");
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
		lore.add("§6Ingredient 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 11);
	}
	
	public static ItemStack getLemon() {
		ItemStack item = new ItemStack(Material.SPECKLED_MELON);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eLemon");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getCorn() {
		ItemStack item = new ItemStack(Material.GOLDEN_CARROT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eCorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getHoney() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eHoney");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 11);
	}
	
	public static ItemStack getYeast() {
		ItemStack item = new ItemStack(Material.MELON_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eYeast");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getBeetrootSauce() {
		ItemStack item = new ItemStack(Material.BEETROOT_SOUP);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eBeetroot Sauce");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getPasta() {
		ItemStack item = new ItemStack(Material.BEETROOT_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§ePasta");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getOnion() {
		ItemStack item = new ItemStack(Material.BEETROOT);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eOnion");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getHops() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eHops");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 3);
	}
	
	public static ItemStack getCheese() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eCheese");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 14);
	}
	
	public static ItemStack getTortilla() {
		ItemStack item = new ItemStack(Material.COOKIE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eTortilla");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getExoticGreens() {
		ItemStack item = new ItemStack(Material.INK_SACK);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eExotic Greens");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return Util.setData(item, 2);
	}
	
	public static ItemStack getRice() {
		ItemStack item = new ItemStack(Material.PUMPKIN_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eRice");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getPopcorn() {
		ItemStack item = new ItemStack(Material.BEETROOT_SEEDS);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§ePopcorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getPepper() {
		ItemStack item = new ItemStack(Material.SUGAR_CANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§ePeppers");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 21");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack getVodka() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 255, 255));
		meta.setDisplayName("§eVodka");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 22");
		meta.setLore(lore);
		item.setItemMeta(meta);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		return item;
	}
	
	public static ItemStack getRum() {
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
	
	public static ItemStack getTequila() {
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
	
	public static ArrayList<ItemStack> getToastRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getOilRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_FISH));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getTomatoRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BEETROOT));
		recipe.add(new ItemStack(Material.RED_ROSE));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getButterRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.STICK));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getLemonRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GOLD_NUGGET, 2));
		recipe.add(new ItemStack(Material.APPLE));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getCornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GOLDEN_CARROT));
		recipe.add(new ItemStack(Material.BLAZE_ROD));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getHoneyRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.DOUBLE_PLANT));
		recipe.add(new ItemStack(Material.SUGAR));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getYeastRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.BLAZE_POWDER));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getBeetrootSauceRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BEETROOT, 6));
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(Util.setAmount(getSpices(), 2));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getPastaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(getSalt());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getOnionRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BEETROOT));
		recipe.add(new ItemStack(Material.FERMENTED_SPIDER_EYE));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getHopsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.VINE, 3));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getCheeseRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getTortillaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT, 3));
		recipe.add(Util.setAmount(getCorn(), 3));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getExoticGreensRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.CACTUS));
		recipe.add(new ItemStack(Material.VINE));
		recipe.add(new ItemStack(Material.LEAVES));
		recipe.add(new ItemStack(Material.CHORUS_FRUIT));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getRiceRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SEEDS));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getPopcornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(getCorn());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getPepperRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.CACTUS));
		recipe.add(new ItemStack(Material.LAVA_BUCKET));
		return recipe;
	}
	
	public static ArrayList<ItemStack> getVodkaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.POTATO_ITEM));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(getYeast());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getRumRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(getYeast());
		return recipe;
	}
	
	public static ArrayList<ItemStack> getTequilaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.CACTUS));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(getYeast());
		return recipe;
	}
}
