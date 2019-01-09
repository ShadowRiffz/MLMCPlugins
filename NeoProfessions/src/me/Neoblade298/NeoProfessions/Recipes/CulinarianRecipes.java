package me.Neoblade298.NeoProfessions.Recipes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.DrinksRecipeItems;
import me.Neoblade298.NeoProfessions.Items.IngredientRecipeItems;

public class CulinarianRecipes {
	
	Main main;
	public CulinarianRecipes(Main main) {
		this.main = main;
	}

	public ArrayList<Recipe> getRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		recipes.add(getVodkaRecipe());
		recipes.add(getRumRecipe());
		recipes.add(getTequilaRecipe());
		recipes.add(getBlackWidowRecipe());
		recipes.add(getPinkPantherRecipe());
		recipes.add(getMidnightKissRecipe());
		recipes.add(getMidnightBlueRecipe());
		recipes.add(getGoodAndEvilRecipe());
		recipes.add(getThorHammerRecipe());
		recipes.add(getJackFrostRecipe());
		recipes.add(getWhiteRussianRecipe());
		recipes.add(getSwampWaterRecipe());
		recipes.add(getBlueMotorcycleRecipe());
		recipes.add(getRedDeathRecipe());
		recipes.add(getBombsicleRecipe());
		recipes.add(getSweetTartRecipe());
		recipes.add(getPinaColadaRecipe());
		recipes.add(getMargaritaOnTheRocksRecipe());
		recipes.add(getBloodyMaryRecipe());
		return recipes;
	}
	
	private Recipe getVodkaRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Vodka");
		ShapedRecipe recipe = new ShapedRecipe(key, IngredientRecipeItems.getVodka());
		recipe.shape("Y", "P", "W");
		recipe.setIngredient('Y', Material.MELON_SEEDS);
		recipe.setIngredient('P', Material.POTATO_ITEM);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		return recipe;
	}
	
	private Recipe getRumRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Rum");
		ShapedRecipe recipe = new ShapedRecipe(key, IngredientRecipeItems.getRum());
		recipe.shape("Y", "S", "W");
		recipe.setIngredient('Y', Material.MELON_SEEDS);
		recipe.setIngredient('S', Material.SUGAR);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		return recipe;
	}
	
	private Recipe getTequilaRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Tequila");
		ShapedRecipe recipe = new ShapedRecipe(key, IngredientRecipeItems.getTequila());
		recipe.shape("Y", "C", "W");
		recipe.setIngredient('Y', Material.MELON_SEEDS);
		recipe.setIngredient('C', Material.CACTUS);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		return recipe;
	}
	
	private Recipe getBlackWidowRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Black_Widow");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getBlackWidow());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.SPIDER_EYE);
		return recipe;
	}
	
	private Recipe getPinkPantherRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Pink_Panther");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getPinkPanther());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.APPLE);
		return recipe;
	}
	
	private Recipe getMidnightKissRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Midnight_Kiss");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getMidnightKiss());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.SPECKLED_MELON);
		return recipe;
	}
	
	private Recipe getMidnightBlueRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Midnight_Blue");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getMidnightBlue());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.SUGAR);
		return recipe;
	}
	
	private Recipe getGoodAndEvilRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Good_and_Evil");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getGoodAndEvil());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.SPECKLED_MELON);
		recipe.addIngredient(Material.APPLE);
		return recipe;
	}
	
	private Recipe getThorHammerRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Thor_Hammer");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getThorHammer());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.LAVA_BUCKET);
		return recipe;
	}
	
	private Recipe getJackFrostRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Jack_Frost");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getJackFrost());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(2, Material.INK_SACK);
		recipe.addIngredient(Material.ICE);
		return recipe;
	}
	
	private Recipe getWhiteRussianRecipe() {
		NamespacedKey key = new NamespacedKey(main, "White_Russian");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getWhiteRussian());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(2, Material.INK_SACK);
		return recipe;
	}
	
	private Recipe getSwampWaterRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Swamp_Water");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getSwampWater());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.APPLE);
		recipe.addIngredient(Material.MELON);
		recipe.addIngredient(Material.SPECKLED_MELON);
		return recipe;
	}
	
	private Recipe getBlueMotorcycleRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Blue_Motorcycle");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getBlueMotorcycle());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.MELON);
		recipe.addIngredient(Material.SPECKLED_MELON);
		return recipe;
	}
	
	private Recipe getRedDeathRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Red_Death");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getRedDeath());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.SPECKLED_MELON);
		return recipe;
	}
	
	private Recipe getBombsicleRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Bombsicle");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getBombsicle());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.WATER_BUCKET);
		return recipe;
	}
	
	private Recipe getSweetTartRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Sweet_Tart");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getSweetTart());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.MELON);
		recipe.addIngredient(Material.SPECKLED_MELON);
		return recipe;
	}
	
	private Recipe getPinaColadaRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Pina_Colada");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getPinaColada());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.APPLE);
		return recipe;
	}
	
	private Recipe getMargaritaOnTheRocksRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Margarita_on_the_Rocks");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getMargaritaOnTheRocks());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.INK_SACK);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.SUGAR);
		recipe.addIngredient(Material.SPECKLED_MELON);
		return recipe;
	}
	
	private Recipe getBloodyMaryRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Bloody_Mary");
		ShapelessRecipe recipe = new ShapelessRecipe(key, DrinksRecipeItems.getBloodyMary());
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(2, Material.INK_SACK);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.APPLE);
		return recipe;
	}
}
