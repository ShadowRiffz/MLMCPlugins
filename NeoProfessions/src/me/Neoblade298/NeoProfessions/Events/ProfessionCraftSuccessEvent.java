package me.Neoblade298.NeoProfessions.Events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import me.Neoblade298.NeoProfessions.Recipes.Recipe;
import me.Neoblade298.NeoProfessions.Recipes.RecipeResult;

public class ProfessionCraftSuccessEvent extends Event {
	Player p;
	Recipe recipe;
	RecipeResult result;
    private static final HandlerList handlers = new HandlerList();

	public ProfessionCraftSuccessEvent(Player p, Recipe recipe, RecipeResult result) {
		this.recipe = recipe;
		this.result = result;
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public Recipe getRecipe() {
		return recipe;
	}
	
	public RecipeResult getResult() {
		return result;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
