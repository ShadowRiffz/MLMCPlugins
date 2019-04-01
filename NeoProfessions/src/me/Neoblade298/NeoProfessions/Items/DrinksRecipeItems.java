package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class DrinksRecipeItems {
	CommonItems common;
	IngredientRecipeItems ingr;
	Tier2RecipeItems t2;
	Util util;
	public DrinksRecipeItems() {
		common = new CommonItems();
		ingr = new IngredientRecipeItems();
		util = new Util();
		t2 = new Tier2RecipeItems();
	}
	
	public ArrayList<ItemStack> getDrinks() {
		ArrayList<ItemStack> recipes = new ArrayList<ItemStack>();
		recipes.add(getBlackWidow());
		recipes.add(getPinkPanther());
		recipes.add(getMidnightKiss());
		recipes.add(getMidnightBlue());
		recipes.add(getGoodAndEvil());
		recipes.add(getThorHammer());
		recipes.add(getSwampWater());
		recipes.add(getRedDeath());
		recipes.add(getBlueMotorcycle());
		recipes.add(getPinaColada());
		recipes.add(getMargaritaOnTheRocks());
		recipes.add(getJackFrost());
		recipes.add(getSweetTart());
		recipes.add(getBombsicle());
		recipes.add(getBloodyMary());
		recipes.add(getWhiteRussian());
		return recipes;
	}
	
	public ItemStack getBlackWidow() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(0, 0, 0));
		meta.setDisplayName("§9Black Widow");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 1");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPinkPanther() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 165, 230));
		meta.setDisplayName("§9Pink Panther");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 2");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMidnightKiss() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(153, 0, 153));
		meta.setDisplayName("§9Midnight Kiss");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 3");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMidnightBlue() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(0, 0, 75));
		meta.setDisplayName("§9Midnight Blue");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 4");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getGoodAndEvil() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 235, 150));
		meta.setDisplayName("§9Good and Evil");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 5");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getThorHammer() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(45, 10, 45));
		meta.setDisplayName("§9Thor's Hammer");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 6");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getJackFrost() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(145, 255, 250));
		meta.setDisplayName("§9Jack Frost");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 7");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getWhiteRussian() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 250, 215));
		meta.setDisplayName("§9White Russian");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 8");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSwampWater() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(45, 255, 60));
		meta.setDisplayName("§9Swamp Water");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 9");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBlueMotorcycle() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(110, 150, 250));
		meta.setDisplayName("§9Blue Motorcycle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 10");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getRedDeath() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(230, 115, 20));
		meta.setDisplayName("§9Red Death");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 11");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBombsicle() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(0, 215, 255));
		meta.setDisplayName("§9Bombsicle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 12");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSweetTart() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(250, 95, 220));
		meta.setDisplayName("§9Sweet Tart");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 13");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPinaColada() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 230, 250));
		meta.setDisplayName("§9Pina Colada");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 14");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMargaritaOnTheRocks() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(200, 255, 210));
		meta.setDisplayName("§9Margarita on the Rocks");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 15");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBloodyMary() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 75, 75));
		meta.setDisplayName("§9Bloody Mary");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Drink 16");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getBlackWidowRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 0));
		recipe.add(new ItemStack(Material.ICE));
		recipe.add(new ItemStack(Material.SPIDER_EYE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPinkPantherRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 9));
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMidnightKissRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getLemon());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 6));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMidnightBlueRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getTequila());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 4));
		recipe.add(new ItemStack(Material.SUGAR));
		return recipe;
	}
	
	public ArrayList<ItemStack> getGoodAndEvilRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getLemon());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 11));
		recipe.add(new ItemStack(Material.APPLE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getThorHammerRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 5));
		recipe.add(new ItemStack(Material.ICE));
		recipe.add(new ItemStack(Material.LAVA_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getJackFrostRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getHoney());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 12));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getWhiteRussianRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 15));
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSwampWaterRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getRum());
		recipe.add(ingr.getLemon());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 10));
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(new ItemStack(Material.MELON));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBlueMotorcycleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getLemon());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 4));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getRedDeathRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getRum());
		recipe.add(ingr.getLemon());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 14));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBombsicleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(t2.getLemonade());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 12));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSweetTartRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getLemon());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 9));
		recipe.add(new ItemStack(Material.MELON));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPinaColadaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getRum());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 15));
		recipe.add(new ItemStack(Material.APPLE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMargaritaOnTheRocksRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getTequila());
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getLemon());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 2));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBloodyMaryRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(2));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getTomato());
		recipe.add(ingr.getGreens());
		recipe.add(util.setData(new ItemStack(Material.INK_SACK), 1));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
}
