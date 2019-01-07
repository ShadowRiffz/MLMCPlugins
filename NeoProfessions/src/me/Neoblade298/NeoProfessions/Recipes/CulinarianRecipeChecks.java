package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Items.IngredientRecipeItems;

public class CulinarianRecipeChecks {
	
	public static void checkVodka(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.Ingr22")) {
			for(ItemStack i : IngredientRecipeItems.getVodkaRecipe()) {
				if(!inv.containsAtLeast(i, 1)) {
					inv.setResult(null);
				}
			}
		}
	}
	
	public static void checkRum(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.Ingr23")) {
			for(ItemStack i : IngredientRecipeItems.getTequilaRecipe()) {
				if(!inv.containsAtLeast(i, 1)) {
					inv.setResult(null);
				}
			}
		}
	}
	
	public static void checkTequila(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.Ingr24")) {
			for(ItemStack i : IngredientRecipeItems.getTequilaRecipe()) {
				if(!inv.containsAtLeast(i, 1)) {
					inv.setResult(null);
				}
			}
		}
	}
}
