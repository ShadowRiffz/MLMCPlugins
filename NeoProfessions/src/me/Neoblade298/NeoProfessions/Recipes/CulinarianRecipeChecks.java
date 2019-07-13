package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.DrinksRecipeItems;
import me.Neoblade298.NeoProfessions.Items.IngredientRecipeItems;

public class CulinarianRecipeChecks {
	
	Main main;
	IngredientRecipeItems ingr;
	DrinksRecipeItems drink;
	public CulinarianRecipeChecks(Main main) {
		this.main = main;
		ingr = new IngredientRecipeItems();
		drink = new DrinksRecipeItems();
	}

	
	public void checkVodka(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.Ingr22") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : ingr.getVodkaRecipe()) {
				if(!inv.containsAtLeast(i, 1)) {
					inv.setResult(null);
					break;
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkRum(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.Ingr23") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : ingr.getRumRecipe()) {
				if(!inv.containsAtLeast(i, 1)) {
					inv.setResult(null);
					break;
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkTequila(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.Ingr24") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : ingr.getTequilaRecipe()) {
				if(!inv.containsAtLeast(i, 1)) {
					inv.setResult(null);
					break;
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkBlackWidow(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink1") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getBlackWidowRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkPinkPanther(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink2") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getPinkPantherRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkMidnightKiss(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink3") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getMidnightKissRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkMidnightBlue(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink4") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getMidnightBlueRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 24);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkGoodAndEvil(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink5") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getGoodAndEvilRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkThorHammer(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink6") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getThorHammerRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkJackFrost(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink7") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getJackFrostRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkWhiteRussian(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink8") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getWhiteRussianRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkSwampWater(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink9") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getSwampWaterRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 23);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkBlueMotorcycle(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink10") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getBlueMotorcycleRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkRedDeath(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink11") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getRedDeathRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 23);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkBombsicle(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink12") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getBombsicleRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkSweetTart(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink13") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getSweetTartRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkPinaColada(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink14") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getPinaColadaRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 23);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkMargaritaOnTheRocks(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink15") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getMargaritaOnTheRocksRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 24);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	public void checkBloodyMary(Player p, CraftingInventory inv) {
		if(p.hasPermission("recipes.drink16") && p.hasPermission("culinarian.bartender")) {
			for(ItemStack i : drink.getBloodyMaryRecipe()) {
				if(i.getType().equals(Material.POTION)) {
					checkAlcoholBase(inv, 22);
				}
				else {
					if(!inv.containsAtLeast(i, 1)) {
						inv.setResult(null);
						break;
					}
				}
			}
		}
		else { 
			inv.setResult(null);
		}
	}
	
	private void checkAlcoholBase(CraftingInventory inv, int ingredient) {
		if(!inv.contains(Material.POTION, 1)) {
			inv.setResult(null);
		}
		else {
			for(int j = 1; j < inv.getContents().length; j++) {
				if(inv.getContents()[j] != null && inv.getContents()[j].getType().equals(Material.POTION)) {
					ItemStack pot = inv.getContents()[j];
					if(!pot.hasItemMeta() || !pot.getItemMeta().hasLore() || !pot.getItemMeta().getLore().get(0).contains("Ingredient " + ingredient)) {
						inv.setResult(null);
					}
				}
			}
		}
	}
}
