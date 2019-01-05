package me.Neoblade298.NeoProfessions.Methods;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Items.IngredientItems;
import me.Neoblade298.NeoProfessions.Utilities.CulinarianUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class CulinarianMethods {
	
	Main main;
	Economy econ;
	
	// Constants
	final static int GARNISH_COST = 500;
	final static int GARNISH_ESSENCE = 1;
	final static int PRESERVE_COST = 500;
	final static int PRESERVE_ESSENCE = 1;
	final static int SPICE_COST = 500;
	final static int SPICE_ESSENCE = 1;
	final static int CRAFT_COST = 50;
	
	public CulinarianMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
	}
	
	public void garnish(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.equals(Material.AIR)) {
			int level = CulinarianUtils.getFoodLevel(item);
			if(p.hasPermission("culinarian.garnish." + level)) {
				boolean isFood = false;
				boolean isBoosted = false;
				for(String line : item.getItemMeta().getLore()) {
					if(line.contains("Recipe")) {
						isFood = true;
					}
					if(line.contains("Garnished")) {
						isBoosted = true;
					}
					if(line.contains("Spiced")) {
						isBoosted = true;
					}
					if(line.contains("Preserved")) {
						isBoosted = true;
					}
				}
				if(isFood) {
					if(!isBoosted) {
						if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), GARNISH_ESSENCE)) {
							if(econ.has(p, GARNISH_COST)) {
								ItemMeta meta = item.getItemMeta();
								ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
								lore.add("§9Garnished (1.3x Attribute Boost)");
								meta.setLore(lore);
								item.setItemMeta(meta);
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), GARNISH_ESSENCE));
								econ.withdrawPlayer(p, GARNISH_COST);
								Util.sendMessage(p, "&7Successfully garnished dish!");
							}
							else {
								Util.sendMessage(p, "&cYou gold the materials to garnish this!");
							}
						}
						else {
							Util.sendMessage(p, "&cYou lack the materials to garnish this!");
						}
					}
					else {
						Util.sendMessage(p, "&cThis food cannot be boosted any further!");
					}
				}
				else {
					Util.sendMessage(p, "&cCannot garnish this item!");
				}
			}
			else {
				Util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void preserve(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.equals(Material.AIR)) {
			int level = CulinarianUtils.getFoodLevel(item);
			if(p.hasPermission("culinarian.preserve." + level)) {
				boolean isFood = false;
				boolean isBoosted = false;
				for(String line : item.getItemMeta().getLore()) {
					if(line.contains("Recipe")) {
						isFood = true;
					}
					if(line.contains("Garnished")) {
						isBoosted = true;
					}
					if(line.contains("Spiced")) {
						isBoosted = true;
					}
					if(line.contains("Preserved")) {
						isBoosted = true;
					}
				}
				if(isFood) {
					if(!isBoosted) {
						if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), PRESERVE_ESSENCE)) {
							if(econ.has(p, PRESERVE_COST)) {
								ItemMeta meta = item.getItemMeta();
								ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
								lore.add("§9Preserved (1.3x Duration Boost)");
								meta.setLore(lore);
								item.setItemMeta(meta);
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), PRESERVE_ESSENCE));
								econ.withdrawPlayer(p, PRESERVE_COST);
								Util.sendMessage(p, "&7Successfully preserved dish!");
							}
							else {
								Util.sendMessage(p, "&cYou gold the materials to preserve  this!");
							}
						}
						else {
							Util.sendMessage(p, "&cYou lack the materials to preserve this!");
						}
					}
					else {
						Util.sendMessage(p, "&cThis food cannot be boosted any further!");
					}
				}
				else {
					Util.sendMessage(p, "&cCannot preserve this item!");
				}
			}
			else {
				Util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void spice(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand();
		if(!item.equals(Material.AIR)) {
			int level = CulinarianUtils.getFoodLevel(item);
			if(p.hasPermission("culinarian.spice." + level)) {
				boolean isFood = false;
				boolean isBoosted = false;
				for(String line : item.getItemMeta().getLore()) {
					if(line.contains("Recipe")) {
						isFood = true;
					}
					if(line.contains("Garnished")) {
						isBoosted = true;
					}
					if(line.contains("Spiced")) {
						isBoosted = true;
					}
					if(line.contains("Preserved")) {
						isBoosted = true;
					}
				}
				if(isFood) {
					if(!isBoosted) {
						if(p.getInventory().containsAtLeast(CommonItems.getEssence(level), SPICE_ESSENCE)) {
							if(econ.has(p, SPICE_COST)) {
								ItemMeta meta = item.getItemMeta();
								ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
								lore.add("§9Spiced (1.3x Restoration Boost)");
								meta.setLore(lore);
								item.setItemMeta(meta);
								p.getInventory().removeItem(Util.setAmount(CommonItems.getEssence(level), SPICE_ESSENCE));
								econ.withdrawPlayer(p, SPICE_COST);
								Util.sendMessage(p, "&7Successfully spiced dish!");
							}
							else {
								Util.sendMessage(p, "&cYou lack the gold to spice this!");
							}
						}
						else {
							Util.sendMessage(p, "&cYou lack the materials to spice this!");
						}
					}
					else {
						Util.sendMessage(p, "&cThis food cannot be boosted any further!");
					}
				}
				else {
					Util.sendMessage(p, "&cCannot spice this item!");
				}
			}
			else {
				Util.sendMessage(p, "&cYou do not yet have the required skill!");
			}
		}
		else {
			Util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void parseIngredient(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.1")) {
			if(recipe.equalsIgnoreCase("salt")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getSaltRecipe(), IngredientItems.getSalt(), true, "Salt");
			}
			else if(recipe.equalsIgnoreCase("spices")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getSpicesRecipe(), IngredientItems.getSpices(), false, "Spices");
			}
			else if(recipe.equalsIgnoreCase("greens")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getGreensRecipe(), IngredientItems.getGreens(), false, "Greens");
			}
			else if(recipe.equalsIgnoreCase("toast")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getToastRecipe(), IngredientItems.getToast(), true, "Toast");
			}
			else if(recipe.equalsIgnoreCase("oil")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getOilRecipe(), IngredientItems.getOil(), true, "Oil");
			}
			else if(recipe.equalsIgnoreCase("tomato")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getTomatoRecipe(), IngredientItems.getTomato(), false, "Tomato");
			}
			else if(recipe.equalsIgnoreCase("butter")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getButterRecipe(), IngredientItems.getButter(), false, "Butter");
			}
			else if(recipe.equalsIgnoreCase("lemon")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getLemonRecipe(), IngredientItems.getLemon(), false, "Lemon");
			}
			else if(recipe.equalsIgnoreCase("corn")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getCornRecipe(), IngredientItems.getCorn(), false, "Corn");
			}
			else if(recipe.equalsIgnoreCase("honey")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getHoneyRecipe(), IngredientItems.getHoney(), false, "Honey");
			}
			else if(recipe.equalsIgnoreCase("yeast")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getYeastRecipe(), IngredientItems.getYeast(), false, "Yeast");
			}
			else if(recipe.equalsIgnoreCase("beetroot sauce")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getBeetrootSauceRecipe(), IngredientItems.getBeetrootSauce(), false, "Beetroot Sauce");
			}
			else if(recipe.equalsIgnoreCase("pasta")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getPastaRecipe(), IngredientItems.getPasta(), false, "Pasta");
			}
			else if(recipe.equalsIgnoreCase("onion")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getOnionRecipe(), IngredientItems.getOnion(), false, "Onion");
			}
			else if(recipe.equalsIgnoreCase("hops")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getHopsRecipe(), IngredientItems.getHops(), false, "Hops");
			}
			else if(recipe.equalsIgnoreCase("cheese")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getCheeseRecipe(), IngredientItems.getCheese(), true, "Cheese");
			}
			else if(recipe.equalsIgnoreCase("tortilla")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getTortillaRecipe(), IngredientItems.getTortilla(), false, "Tortilla");
			}
			else if(recipe.equalsIgnoreCase("exotic greens")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getExoticGreensRecipe(), IngredientItems.getExoticGreens(), false, "Exotic Greens");
			}
			else if(recipe.equalsIgnoreCase("rice")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getRiceRecipe(), IngredientItems.getRice(), true, "Rice");
			}
			else if(recipe.equalsIgnoreCase("popcorn")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getPopcornRecipe(), IngredientItems.getPopcorn(), true, "Popcorn");
			}
			else if(recipe.equalsIgnoreCase("pepper")) {
				CulinarianUtils.craftRecipeMax(p, econ, IngredientItems.getPepperRecipe(), IngredientItems.getPepper(), false, "Pepper");
			}
		}
		else {
			Util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseIngredient(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.1")) {
			if(recipe.equalsIgnoreCase("salt")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getSaltRecipe(), IngredientItems.getSalt(), true, "Salt");
			}
			else if(recipe.equalsIgnoreCase("spices")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getSpicesRecipe(), IngredientItems.getSpices(), false, "Spices");
			}
			else if(recipe.equalsIgnoreCase("greens")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getGreensRecipe(), IngredientItems.getGreens(), false, "Greens");
			}
			else if(recipe.equalsIgnoreCase("toast")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getToastRecipe(), IngredientItems.getToast(), true, "Toast");
			}
			else if(recipe.equalsIgnoreCase("oil")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getOilRecipe(), IngredientItems.getOil(), true, "Oil");
			}
			else if(recipe.equalsIgnoreCase("tomato")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getTomatoRecipe(), IngredientItems.getTomato(), false, "Tomato");
			}
			else if(recipe.equalsIgnoreCase("butter")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getButterRecipe(), IngredientItems.getButter(), false, "Butter");
			}
			else if(recipe.equalsIgnoreCase("lemon")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getLemonRecipe(), IngredientItems.getLemon(), false, "Lemon");
			}
			else if(recipe.equalsIgnoreCase("corn")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getCornRecipe(), IngredientItems.getCorn(), false, "Corn");
			}
			else if(recipe.equalsIgnoreCase("honey")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getHoneyRecipe(), IngredientItems.getHoney(), false, "Honey");
			}
			else if(recipe.equalsIgnoreCase("yeast")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getYeastRecipe(), IngredientItems.getYeast(), false, "Yeast");
			}
			else if(recipe.equalsIgnoreCase("beetroot sauce")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getBeetrootSauceRecipe(), IngredientItems.getBeetrootSauce(), false, "Beetroot Sauce");
			}
			else if(recipe.equalsIgnoreCase("pasta")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getPastaRecipe(), IngredientItems.getPasta(), false, "Pasta");
			}
			else if(recipe.equalsIgnoreCase("onion")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getOnionRecipe(), IngredientItems.getOnion(), false, "Onion");
			}
			else if(recipe.equalsIgnoreCase("hops")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getHopsRecipe(), IngredientItems.getHops(), false, "Hops");
			}
			else if(recipe.equalsIgnoreCase("cheese")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getCheeseRecipe(), IngredientItems.getCheese(), true, "Cheese");
			}
			else if(recipe.equalsIgnoreCase("tortilla")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getTortillaRecipe(), IngredientItems.getTortilla(), false, "Tortilla");
			}
			else if(recipe.equalsIgnoreCase("exotic greens")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getExoticGreensRecipe(), IngredientItems.getExoticGreens(), false, "Exotic Greens");
			}
			else if(recipe.equalsIgnoreCase("rice")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getRiceRecipe(), IngredientItems.getRice(), true, "Rice");
			}
			else if(recipe.equalsIgnoreCase("popcorn")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getPopcornRecipe(), IngredientItems.getPopcorn(), true, "Popcorn");
			}
			else if(recipe.equalsIgnoreCase("pepper")) {
				CulinarianUtils.craftRecipe(p, econ, amount, IngredientItems.getPepperRecipe(), IngredientItems.getPepper(), false, "Pepper");
			}
		}
		else {
			Util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}

}
