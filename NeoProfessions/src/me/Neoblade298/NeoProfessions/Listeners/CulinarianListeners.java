package me.Neoblade298.NeoProfessions.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.IngredientRecipeItems;
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
		}
	}

	
}
