package me.Neoblade298.NeoProfessions.Legacy.Items;

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
		lore.add("§7Effects: §95% DEX");
		lore.add("§7Duration: §690 Seconds");
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
		lore.add("§7Effects: §95% SPR");
		lore.add("§7Duration: §6100 Seconds");
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
		lore.add("§7Effects: §94% END");
		lore.add("§7Duration: §6120 Seconds");
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
		lore.add("§7Effects: §94%VIT");
		lore.add("§7Duration: §6100 Seconds");
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
		lore.add("§7Effects: §96% SPR§7, §9-4% STR");
		lore.add("§7Duration: §6100 Seconds");
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
		lore.add("§7Effects: §97% STR§7, §9-4% END");
		lore.add("§7Duration: §680 Seconds");
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
		lore.add("§7Effects: §95% INT");
		lore.add("§7Duration: §6100 Seconds");
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
		lore.add("§7Effects: §94% PRC");
		lore.add("§7Duration: §690 Seconds");
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
		lore.add("§7Effects: §97% DEX");
		lore.add("§7Duration: §680 Seconds");
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
		lore.add("§7Effects: §95% STR");
		lore.add("§7Duration: §690 Seconds");
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
		lore.add("§7Effects: §95% INT");
		lore.add("§7Duration: §690 Seconds");
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
		lore.add("§7Effects: §96% VIT§7, §9-4% STR");
		lore.add("§7Duration: §690 Seconds");
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
		lore.add("§7Effects: §95.5% SPR");
		lore.add("§7Durations: §690 Seconds");
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
		lore.add("§7Effects: §95% END");
		lore.add("§7Duration: §660 Seconds");
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
		lore.add("§7Effects: §96% INT");
		lore.add("§7Duration: §660 Seconds");
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
		lore.add("§7Effects: §96% STR");
		lore.add("§7Duration: §660 Seconds");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getBlackWidowRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(new ItemStack(Material.BLACK_DYE));
		recipe.add(new ItemStack(Material.ICE));
		recipe.add(new ItemStack(Material.SPIDER_EYE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPinkPantherRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(new ItemStack(Material.PINK_DYE));
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMidnightKissRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getLemon());
		recipe.add(new ItemStack(Material.CYAN_DYE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMidnightBlueRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getTequila());
		recipe.add(new ItemStack(Material.LAPIS_LAZULI));
		recipe.add(new ItemStack(Material.SUGAR));
		return recipe;
	}
	
	public ArrayList<ItemStack> getGoodAndEvilRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getLemon());
		recipe.add(new ItemStack(Material.YELLOW_DYE));
		recipe.add(new ItemStack(Material.APPLE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getThorHammerRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(new ItemStack(Material.PURPLE_DYE));
		recipe.add(new ItemStack(Material.ICE));
		recipe.add(new ItemStack(Material.LAVA_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getJackFrostRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getHoney());
		recipe.add(new ItemStack(Material.LIGHT_BLUE_DYE));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getWhiteRussianRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(new ItemStack(Material.WHITE_DYE));
		recipe.add(new ItemStack(Material.COCOA_BEANS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSwampWaterRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getRum());
		recipe.add(ingr.getLemon());
		recipe.add(new ItemStack(Material.LIME_DYE));
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(new ItemStack(Material.MELON));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBlueMotorcycleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getLemon());
		recipe.add(new ItemStack(Material.LAPIS_LAZULI));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getRedDeathRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getRum());
		recipe.add(ingr.getLemon());
		recipe.add(new ItemStack(Material.ORANGE_DYE));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBombsicleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(t2.getLemonade());
		recipe.add(new ItemStack(Material.LIGHT_BLUE_DYE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSweetTartRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getLemon());
		recipe.add(new ItemStack(Material.PINK_DYE));
		recipe.add(new ItemStack(Material.MELON));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPinaColadaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getRum());
		recipe.add(new ItemStack(Material.WHITE_DYE));
		recipe.add(new ItemStack(Material.APPLE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMargaritaOnTheRocksRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getTequila());
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getLemon());
		recipe.add(new ItemStack(Material.GREEN_DYE));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBloodyMaryRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(common.getEssence(20, true));
		recipe.add(ingr.getVodka());
		recipe.add(ingr.getTomato());
		recipe.add(ingr.getGreens());
		recipe.add(new ItemStack(Material.RED_DYE));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
}
