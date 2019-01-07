package me.Neoblade298.NeoProfessions.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Recipes.CulinarianRecipeChecks;
import me.Neoblade298.NeoProfessions.Recipes.CulinarianRecipes;

public class CulinarianListeners implements Listener {
	
	Main main;
	CulinarianRecipes culinarianRecipes;
	public CulinarianListeners(Main main) {
		this.main = main;
		this.culinarianRecipes = main.culinarianRecipes;
	}
	
	@EventHandler
	public void onPrepItemCraft(PrepareItemCraftEvent e) {
		if(e.getInventory().getHolder() instanceof Player) {
			Player p = (Player) e.getInventory().getHolder();
			CraftingInventory inv = e.getInventory();
			if(inv.getResult() == null) {
				return;
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Vodka")) {
				CulinarianRecipeChecks.checkVodka(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Rum")) {
				CulinarianRecipeChecks.checkRum(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Tequila")) {
				CulinarianRecipeChecks.checkTequila(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Black Widow")) {
				CulinarianRecipeChecks.checkBlackWidow(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Pink Panther")) {
				CulinarianRecipeChecks.checkPinkPanther(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Midnight Kiss")) {
				CulinarianRecipeChecks.checkMidnightKiss(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Midnight Blue")) {
				CulinarianRecipeChecks.checkMidnightBlue(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Good and Evil")) {
				CulinarianRecipeChecks.checkGoodAndEvil(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Thor's Hammer")) {
				CulinarianRecipeChecks.checkThorHammer(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Jack Frost")) {
				CulinarianRecipeChecks.checkJackFrost(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("White Russian")) {
				CulinarianRecipeChecks.checkWhiteRussian(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Swamp Water")) {
				CulinarianRecipeChecks.checkSwampWater(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Blue Motorcycle")) {
				CulinarianRecipeChecks.checkBlueMotorcycle(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Red Death")) {
				CulinarianRecipeChecks.checkRedDeath(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Bombsicle")) {
				CulinarianRecipeChecks.checkBombsicle(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Sweet Tart")) {
				CulinarianRecipeChecks.checkSweetTart(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Pina Colada")) {
				CulinarianRecipeChecks.checkPinaColada(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Margarita on the Rocks")) {
				CulinarianRecipeChecks.checkMargaritaOnTheRocks(p, inv);
			}
			else if(inv.getResult().getItemMeta().getDisplayName().contains("Bloody Mary")) {
				CulinarianRecipeChecks.checkBloodyMary(p, inv);
			}
		}
	}

	
}
