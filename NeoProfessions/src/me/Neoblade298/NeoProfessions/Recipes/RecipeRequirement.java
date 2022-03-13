package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;

public interface RecipeRequirement {
	public boolean passesReq(Player p);
	public String failMessage(Player p);
	
	public default void useReq(Player p) {
		
	}
}
