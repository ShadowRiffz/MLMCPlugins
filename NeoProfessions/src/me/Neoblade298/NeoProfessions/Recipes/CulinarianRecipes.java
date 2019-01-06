package me.Neoblade298.NeoProfessions.Recipes;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.IngredientRecipeItems;

public class CulinarianRecipes {
	
	Main main;
	public CulinarianRecipes(Main main) {
		this.main = main;
	}

	public ArrayList<Recipe> getRecipes() {
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();
		recipes.add(getVodkaRecipe());
		return recipes;
	}
	
	private Recipe getVodkaRecipe() {
		NamespacedKey key = new NamespacedKey(main, "Vodka");
		ShapedRecipe recipe = new ShapedRecipe(key, IngredientRecipeItems.getVodka());
		recipe.shape(" Y ", " P ", " W ");
		recipe.setIngredient('Y', Material.MELON_SEEDS);
		recipe.setIngredient('P', Material.POTATO_ITEM);
		recipe.setIngredient('W', Material.WATER_BUCKET);
		return recipe;
	}
}
