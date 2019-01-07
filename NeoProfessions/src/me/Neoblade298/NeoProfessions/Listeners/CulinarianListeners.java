package me.Neoblade298.NeoProfessions.Listeners;

import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Recipes.CulinarianRecipeChecks;
import me.Neoblade298.NeoProfessions.Recipes.CulinarianRecipes;
import me.Neoblade298.NeoProfessions.Utilities.CulinarianUtils;

public class CulinarianListeners implements Listener {
	
	Main main;
	CulinarianRecipes culinarianRecipes;
	HashMap<Player, Integer> drunkness = new HashMap<Player, Integer>();
	Random gen;
	
	public CulinarianListeners(Main main) {
		this.main = main;
		this.culinarianRecipes = main.culinarianRecipes;
		Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
			public void run() {
				for(Player p : drunkness.keySet()) {
					drunkness.put(p, drunkness.get(p) - 1);
				}
			}
		});
		gen = new Random();
	}
	
	@EventHandler
	public void onDrink(PlayerItemConsumeEvent e) {
		ItemStack item = e.getItem();
		if(!item.hasItemMeta() || !item.getItemMeta().hasLore()) {
			return;
		}
		
		String id = item.getItemMeta().getLore().get(0);
		Player p = e.getPlayer();
		boolean canGetAttrs = p.getWorld().getName().equalsIgnoreCase("Argyll") ||
				p.getWorld().getName().equalsIgnoreCase("ClassPVP");
		if(id.contains("Drink 1")) {
			int drunk = 27 + gen.nextInt(7);
			if(drunkness.containsKey(p)) {
				drunkness.put(p, drunkness.get(p) + drunk);
				if(canGetAttrs) {
					
				}
			} else {
				drunkness.put(p, drunk);
			}
		}
		else if(id.contains("Drink 2")) {
			
		}
		else if(id.contains("Drink 3")) {
			
		}
		else if(id.contains("Drink 4")) {
			
		}
		else if(id.contains("Drink 5")) {
			
		}
		else if(id.contains("Drink 6")) {
			
		}
		else if(id.contains("Drink 7")) {
			
		}
		else if(id.contains("Drink 8")) {
			
		}
		else if(id.contains("Drink 9")) {
			
		}
		else if(id.contains("Drink 10")) {
			
		}
		else if(id.contains("Drink 11")) {
			
		}
		else if(id.contains("Drink 12")) {
			
		}
		else if(id.contains("Drink 13")) {
			
		}
		else if(id.contains("Drink 14")) {
			
		}
		else if(id.contains("Drink 15")) {
			
		}
		else if(id.contains("Drink 16")) {
			
		}
		if(drunkness.containsKey(p)) {
			CulinarianUtils.checkAlcohol(p, drunkness.get(p));
		}
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
