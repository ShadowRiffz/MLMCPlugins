package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface RecipeResult {
	public void giveResult(Player p);
	public ItemStack getResultItem();
}
