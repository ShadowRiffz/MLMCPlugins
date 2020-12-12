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
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class CulinarianRecipes {
	
	Main main;
	IngredientRecipeItems ingr;
	DrinksRecipeItems drink;
	Util util;
	public CulinarianRecipes(Main main) {
		this.main = main;
		ingr = new IngredientRecipeItems();
		drink = new DrinksRecipeItems();
		util = new Util();
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
		ShapedRecipe recipe = new ShapedRecipe(key, util.setAmount(ingr.getVodka(), 3));
		recipe.shape("Y", "P", "W");
		recipe.setIngredient('Y', Material.PLAYER_HEAD);
		recipe.setIngredient('P', Material.POTATO);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		return recipe;
	}
	
	private Recipe getRumRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Rum");
		ShapedRecipe recipe = new ShapedRecipe(key, util.setAmount(ingr.getRum(), 3));
		recipe.shape("Y", "S", "W");
		recipe.setIngredient('Y', Material.PLAYER_HEAD);
		recipe.setIngredient('S', Material.SUGAR);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		return recipe;
	}
	
	private Recipe getTequilaRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Tequila");
		ShapedRecipe recipe = new ShapedRecipe(key, util.setAmount(ingr.getTequila(), 3));
		recipe.shape("Y", "C", "W");
		recipe.setIngredient('Y', Material.PLAYER_HEAD);
		recipe.setIngredient('C', Material.CACTUS);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		return recipe;
	}
	
	private Recipe getBlackWidowRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Black_Widow");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getBlackWidow(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.BLACK_DYE);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.SPIDER_EYE);
		return recipe;
	}
	
	private Recipe getPinkPantherRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Pink_Panther");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getPinkPanther(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.PINK_DYE);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.APPLE);
		return recipe;
	}
	
	private Recipe getMidnightKissRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Midnight_Kiss");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getMidnightKiss(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.CYAN_DYE);
		recipe.addIngredient(Material.PLAYER_HEAD);
		return recipe;
	}
	
	private Recipe getMidnightBlueRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Midnight_Blue");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getMidnightBlue(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.LAPIS_LAZULI);
		recipe.addIngredient(Material.SUGAR);
		return recipe;
	}
	
	private Recipe getGoodAndEvilRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Good_and_Evil");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getGoodAndEvil(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.YELLOW_DYE);
		recipe.addIngredient(Material.PLAYER_HEAD);
		recipe.addIngredient(Material.APPLE);
		return recipe;
	}
	
	private Recipe getThorHammerRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Thor_Hammer");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getThorHammer(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.PURPLE_DYE);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.LAVA_BUCKET);
		return recipe;
	}
	
	private Recipe getJackFrostRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Jack_Frost");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getJackFrost(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.LIGHT_BLUE_DYE);
		recipe.addIngredient(Material.PLAYER_HEAD);
		recipe.addIngredient(Material.ICE);
		return recipe;
	}
	
	private Recipe getWhiteRussianRecipe() {
		NamespacedKey key = new NamespacedKey(main, "White_Russian");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getWhiteRussian(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.WHITE_DYE);
		recipe.addIngredient(Material.COCOA_BEANS);
		return recipe;
	}
	
	private Recipe getSwampWaterRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Swamp_Water");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getSwampWater(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.LIME_DYE);
		recipe.addIngredient(Material.APPLE);
		recipe.addIngredient(Material.MELON);
		recipe.addIngredient(Material.PLAYER_HEAD);
		return recipe;
	}
	
	private Recipe getBlueMotorcycleRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Blue_Motorcycle");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getBlueMotorcycle(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.LAPIS_LAZULI);
		recipe.addIngredient(Material.PLAYER_HEAD);
		recipe.addIngredient(Material.ICE);
		return recipe;
	}
	
	private Recipe getRedDeathRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Red_Death");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getRedDeath(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.ORANGE_DYE);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.PLAYER_HEAD);
		return recipe;
	}
	
	private Recipe getBombsicleRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Bombsicle");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getBombsicle(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.LIGHT_BLUE_DYE);
		recipe.addIngredient(Material.PLAYER_HEAD);
		return recipe;
	}
	
	private Recipe getSweetTartRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Sweet_Tart");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getSweetTart(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.PINK_DYE);
		recipe.addIngredient(Material.MELON);
		recipe.addIngredient(Material.PLAYER_HEAD);
		return recipe;
	}
	
	private Recipe getPinaColadaRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Pina_Colada");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getPinaColada(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.WHITE_DYE);
		recipe.addIngredient(Material.APPLE);
		return recipe;
	}
	
	private Recipe getMargaritaOnTheRocksRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Margarita_on_the_Rocks");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getMargaritaOnTheRocks(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.GREEN_DYE);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.PLAYER_HEAD);
		recipe.addIngredient(Material.PLAYER_HEAD);
		return recipe;
	}
	
	private Recipe getBloodyMaryRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Bloody_Mary");
		ShapelessRecipe recipe = new ShapelessRecipe(key, util.setAmount(drink.getBloodyMary(), 3));
		recipe.addIngredient(Material.QUARTZ);
		recipe.addIngredient(Material.POTION);
		recipe.addIngredient(Material.RED_DYE);
		recipe.addIngredient(Material.PLAYER_HEAD);
		recipe.addIngredient(Material.ICE);
		recipe.addIngredient(Material.PLAYER_HEAD);
		return recipe;
	}
}
