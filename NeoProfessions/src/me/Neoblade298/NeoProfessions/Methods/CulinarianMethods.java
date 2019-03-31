package me.Neoblade298.NeoProfessions.Methods;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.sucy.skill.SkillAPI;

import me.Neoblade298.NeoProfessions.Main;
import me.Neoblade298.NeoProfessions.Items.CommonItems;
import me.Neoblade298.NeoProfessions.Items.DrinksRecipeItems;
import me.Neoblade298.NeoProfessions.Items.IngredientRecipeItems;
import me.Neoblade298.NeoProfessions.Items.LegendaryRecipeItems;
import me.Neoblade298.NeoProfessions.Items.LimitedEditionRecipeItems;
import me.Neoblade298.NeoProfessions.Items.Tier1RecipeItems;
import me.Neoblade298.NeoProfessions.Items.Tier2RecipeItems;
import me.Neoblade298.NeoProfessions.Items.Tier3RecipeItems;
import me.Neoblade298.NeoProfessions.Utilities.CulinarianUtils;
import me.Neoblade298.NeoProfessions.Utilities.Util;
import net.milkbowl.vault.economy.Economy;

public class CulinarianMethods {
	
	Main main;
	Economy econ;
	CulinarianUtils culinarianUtils;
	Util util;
	
	// Constants
	final static double GARNISH_BOOST_BASE = 1.1;
	final static double GARNISH_BOOST_PER_LVL = 0.1;
	final static double PRESERVE_BOOST_BASE = 0.9;
	final static double PRESERVE_BOOST_PER_LVL = -0.1;
	final static double SPICE_BOOST_BASE = 1.1;
	final static double SPICE_BOOST_PER_LVL = 0.1;
	final static int GARNISH_COST = 500;
	final static int GARNISH_ESSENCE = 1;
	final static int PRESERVE_COST = 500;
	final static int PRESERVE_ESSENCE = 1;
	final static int SPICE_COST = 500;
	final static int SPICE_ESSENCE = 1;
	final static int CRAFT_COST = 50;
	final static int ASSIMILATE_COST = 250;
	final static int SPECIAL_COST = 5000;
	final static int SPECIAL_AMOUNT = 16;
	final static int REMEDY_COST = 1000;
	final static int REMEDY_ESSENCE = 1;
	
	public CulinarianMethods(Main main) {
		this.main = main;
		this.econ = main.getEconomy();
		culinarianUtils = new CulinarianUtils();
		util = new Util();
	}
	
	public void garnish(Player p) {
		ItemStack oldItem = p.getInventory().getItemInMainHand().clone();
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		if(!item.getType().equals(Material.AIR)) {
			int invSpace = p.getInventory().firstEmpty();
			if(invSpace != -1) {
				int level = 0;
				if(p.hasPermission("culinarian.garnish.3")) {
					level = 3;
				}
				else if(p.hasPermission("culinarian.garnish.2")) {
					level = 2;
				}
				else if(p.hasPermission("culinarian.garnish.1")) {
					level = 1;
				}
				
				if(level != 0) {
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
						int foodLevel = culinarianUtils.getRecipeLevel(item);
						if(!isBoosted) {
							if(p.getInventory().containsAtLeast(CommonItems.getEssence(foodLevel), GARNISH_ESSENCE)) {
								if(econ.has(p, GARNISH_COST)) {
									ItemMeta meta = item.getItemMeta();
									ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
									double multiplier = GARNISH_BOOST_BASE + (GARNISH_BOOST_PER_LVL * (level - 1));
									lore.add("§9Garnished (" + multiplier + "x Attribute Boost)");
									meta.setLore(lore);
									item.setItemMeta(meta);
									p.getInventory().addItem(util.setAmount(item, 1));
									p.getInventory().removeItem(util.setAmount(oldItem, 1));
									econ.withdrawPlayer(p, GARNISH_COST);
									util.sendMessage(p, "&7Successfully garnished dish!");
								}
								else {
									util.sendMessage(p, "&cYou lack the gold to garnish this!");
								}
							}
							else {
								util.sendMessage(p, "&cYou lack the materials to garnish this!");
							}
						}
						else {
							util.sendMessage(p, "&cThis food cannot be boosted any further!");
						}
					}
					else {
						util.sendMessage(p, "&cCannot garnish this item!");
					}
				}
				else {
					util.sendMessage(p, "&cYou do not yet have the required skill!");
				}
			}
			else {
				util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void preserve(Player p) {
		ItemStack oldItem = p.getInventory().getItemInMainHand().clone();
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		if(!item.getType().equals(Material.AIR)) {
			int invSpace = p.getInventory().firstEmpty();
			if(invSpace != -1) {
				int level = 0;
				if(p.hasPermission("culinarian.garnish.3")) {
					level = 3;
				}
				else if(p.hasPermission("culinarian.garnish.2")) {
					level = 2;
				}
				else if(p.hasPermission("culinarian.garnish.1")) {
					level = 1;
				}
				
				if(level != 0) {
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
						int foodLevel = culinarianUtils.getRecipeLevel(item);
						if(!isBoosted) {
							if(p.getInventory().containsAtLeast(CommonItems.getEssence(foodLevel), PRESERVE_ESSENCE)) {
								if(econ.has(p, PRESERVE_COST)) {
									ItemMeta meta = item.getItemMeta();
									ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
									double multiplier = PRESERVE_BOOST_BASE + (PRESERVE_BOOST_PER_LVL * (level - 1));
									lore.add("§9Preserved (" + multiplier + "x Duration Boost)");
									meta.setLore(lore);
									item.setItemMeta(meta);
									p.getInventory().addItem(util.setAmount(item, 1));
									p.getInventory().removeItem(util.setAmount(oldItem, 1));
									econ.withdrawPlayer(p, PRESERVE_COST);
									util.sendMessage(p, "&7Successfully preserved dish!");
								}
								else {
									util.sendMessage(p, "&cYou gold the materials to preserve  this!");
								}
							}
							else {
								util.sendMessage(p, "&cYou lack the materials to preserve this!");
							}
						}
						else {
							util.sendMessage(p, "&cThis food cannot be boosted any further!");
						}
					}
					else {
						util.sendMessage(p, "&cCannot preserve this item!");
					}
				}
				else {
					util.sendMessage(p, "&cYou do not yet have the required skill!");
				}
			}
			else {
				util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void spice(Player p) {
		ItemStack oldItem = p.getInventory().getItemInMainHand().clone();
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		if(!item.getType().equals(Material.AIR)) {
			int invSpace = p.getInventory().firstEmpty();
			if(invSpace != -1) {
				int level = 0;
				if(p.hasPermission("culinarian.garnish.3")) {
					level = 3;
				}
				else if(p.hasPermission("culinarian.garnish.2")) {
					level = 2;
				}
				else if(p.hasPermission("culinarian.garnish.1")) {
					level = 1;
				}
				
				if(level != 0) {
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
						int foodLevel = culinarianUtils.getRecipeLevel(item);
						if(!isBoosted) {
							if(p.getInventory().containsAtLeast(CommonItems.getEssence(foodLevel), SPICE_ESSENCE)) {
								if(econ.has(p, SPICE_COST)) {
									ItemMeta meta = item.getItemMeta();
									ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
									double multiplier = SPICE_BOOST_BASE + (SPICE_BOOST_PER_LVL * (level - 1));
									lore.add("§9Spiced (" + multiplier + "x Restoration Boost)");
									meta.setLore(lore);
									item.setItemMeta(meta);
									p.getInventory().addItem(util.setAmount(item, 1));
									p.getInventory().removeItem(util.setAmount(oldItem, 1));
									econ.withdrawPlayer(p, SPICE_COST);
									util.sendMessage(p, "&7Successfully spiced dish!");
								}
								else {
									util.sendMessage(p, "&cYou lack the gold to spice this!");
								}
							}
							else {
								util.sendMessage(p, "&cYou lack the materials to spice this!");
							}
						}
						else {
							util.sendMessage(p, "&cThis food cannot be boosted any further!");
						}
					}
					else {
						util.sendMessage(p, "&cCannot spice this item!");
					}
				}
				else {
					util.sendMessage(p, "&cYou do not yet have the required skill!");
				}
			}
			else {
				util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void remedy(Player p, String status) {
		ItemStack oldItem = p.getInventory().getItemInMainHand().clone();
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		if(!item.getType().equals(Material.AIR)) {
			int invSpace = p.getInventory().firstEmpty();
			if(invSpace != -1) {
				if(p.hasPermission("culinarian.remedy." + status)) {
					boolean isFood = false;
					for(String line : item.getItemMeta().getLore()) {
						if(line.contains("Recipe")) {
							isFood = true;
							break;
						}
					}
					if(isFood) {
						int foodLevel = culinarianUtils.getRecipeLevel(item);
						if(p.getInventory().containsAtLeast(CommonItems.getEssence(foodLevel), GARNISH_ESSENCE)) {
							if(econ.has(p, GARNISH_COST)) {
								ItemMeta meta = item.getItemMeta();
								ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
								lore.add("§9Remedies " + status);
								meta.setLore(lore);
								item.setItemMeta(meta);
								p.getInventory().addItem(util.setAmount(item, 1));
								p.getInventory().removeItem(util.setAmount(oldItem, 1));
								econ.withdrawPlayer(p, GARNISH_COST);
								util.sendMessage(p, "&7Successfully remedied dish!");
							}
							else {
								util.sendMessage(p, "&cYou lack the gold to remedy this!");
							}
						}
						else {
							util.sendMessage(p, "&cYou lack the materials to remedy this!");
						}
					}
					else {
						util.sendMessage(p, "&cCannot remedy this item!");
					}
				}
				else {
					util.sendMessage(p, "&cYou do not yet have the required skill!");
				}
			}
			else {
				util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void assimilate(Player p) {
		ItemStack item = p.getInventory().getItemInMainHand().clone();
		if(!item.getType().equals(Material.AIR)) {
			int slot = p.getInventory().firstEmpty();
			if(slot != -1) {
				if(item.getType().equals(Material.PAPER) && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().getLore().get(0).contains(("#"))) {
					int level = culinarianUtils.getRecipeLevel(item);
					if(p.hasPermission("culinarian.assimilate." + level)) {
						if(econ.has(p, ASSIMILATE_COST)) {
							p.getInventory().removeItem(util.setAmount(item, 1));
							p.getInventory().addItem(CommonItems.getEssenceFragment(level));
							econ.withdrawPlayer(p, ASSIMILATE_COST);
							util.sendMessage(p, "&7Successfully assimilated recipe!");
						}
						else {
							util.sendMessage(p, "&cYou lack the gold to assimilate this!");
						}
					}
					else {
						util.sendMessage(p, "&cYou do not yet have the required skill!");
					}
				}
				else {
					util.sendMessage(p, "&cYou may only assimilate recipe papers!");
				}
			}
			else {
				util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			util.sendMessage(p, "&cMain hand is empty!");
		}
	}
	
	public void giveSpecial(Player p) {
		int level = 0;
		if(p.hasPermission("culinarian.special.5")) {
			level = 5;
		}
		else if(p.hasPermission("culinarian.special.4")) {
			level = 4;
		}
		else if(p.hasPermission("culinarian.special.3")) {
			level = 3;
		}
		else if(p.hasPermission("culinarian.special.2")) {
			level = 2;
		}
		else if(p.hasPermission("culinarian.special.1")) {
			level = 1;
		}
		
		if(level != 0) {
			int slot = p.getInventory().firstEmpty();
			if(slot != -1) {
				if(econ.has(p, SPECIAL_COST)) {
					// Randomize the tier to be received, 0 = ingr, 1-3 = tier 1-3, 4 = limited edition
					Random gen = new Random();
					int tier = gen.nextInt(level);
					ArrayList<ItemStack> recipes = null;
					if(tier == 0) {
						recipes = IngredientRecipeItems.getIngredients();
					}
					else if(tier == 1) {
						recipes = Tier1RecipeItems.getTier1Recipes();
					}
					else if(tier == 2) {
						recipes = Tier2RecipeItems.getTier2Recipes();
					}
					else if(tier == 3) {
						recipes = Tier3RecipeItems.getTier3Recipes();
					}
					else if(tier == 4) {
						recipes = LimitedEditionRecipeItems.getLimitedEditionRecipes();
					}
					ItemStack item = recipes.get(gen.nextInt(recipes.size()));
					item.setAmount(SPECIAL_AMOUNT);
					p.getInventory().addItem(item);
					econ.withdrawPlayer(p, SPECIAL_COST);
					util.sendMessage(p, "&7Successfully received daily special!");
				}
				else {
					util.sendMessage(p, "&cYou lack the gold to do this!");
				}
			}
			else {
				util.sendMessage(p, "&cYour inventory is full!");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseIngredient(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.1")) {
			if(recipe.equalsIgnoreCase("salt")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getSaltRecipe(), IngredientRecipeItems.getSalt(), true, "Salt");
			}
			else if(recipe.equalsIgnoreCase("spices")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getSpicesRecipe(), IngredientRecipeItems.getSpices(), false, "Spices");
			}
			else if(recipe.equalsIgnoreCase("greens")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getGreensRecipe(), IngredientRecipeItems.getGreens(), false, "Greens");
			}
			else if(recipe.equalsIgnoreCase("toast")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getToastRecipe(), IngredientRecipeItems.getToast(), true, "Toast");
			}
			else if(recipe.equalsIgnoreCase("oil")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getOilRecipe(), IngredientRecipeItems.getOil(), true, "Oil");
			}
			else if(recipe.equalsIgnoreCase("tomato")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getTomatoRecipe(), IngredientRecipeItems.getTomato(), false, "Tomato");
			}
			else if(recipe.equalsIgnoreCase("butter")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getButterRecipe(), IngredientRecipeItems.getButter(), false, "Butter");
			}
			else if(recipe.equalsIgnoreCase("lemon")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getLemonRecipe(), IngredientRecipeItems.getLemon(), false, "Lemon");
			}
			else if(recipe.equalsIgnoreCase("corn")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getCornRecipe(), IngredientRecipeItems.getCorn(), false, "Corn");
			}
			else if(recipe.equalsIgnoreCase("honey")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getHoneyRecipe(), IngredientRecipeItems.getHoney(), false, "Honey");
			}
			else if(recipe.equalsIgnoreCase("yeast")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getYeastRecipe(), IngredientRecipeItems.getYeast(), false, "Yeast");
			}
			else if(recipe.equalsIgnoreCase("beetroot sauce")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getBeetrootSauceRecipe(), IngredientRecipeItems.getBeetrootSauce(), false, "Beetroot Sauce");
			}
			else if(recipe.equalsIgnoreCase("pasta")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getPastaRecipe(), IngredientRecipeItems.getPasta(), false, "Pasta");
			}
			else if(recipe.equalsIgnoreCase("onion")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getOnionRecipe(), IngredientRecipeItems.getOnion(), false, "Onion");
			}
			else if(recipe.equalsIgnoreCase("hops")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getHopsRecipe(), IngredientRecipeItems.getHops(), false, "Hops");
			}
			else if(recipe.equalsIgnoreCase("cheese")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getCheeseRecipe(), IngredientRecipeItems.getCheese(), true, "Cheese");
			}
			else if(recipe.equalsIgnoreCase("tortilla")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getTortillaRecipe(), IngredientRecipeItems.getTortilla(), false, "Tortilla");
			}
			else if(recipe.equalsIgnoreCase("exotic greens")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getExoticGreensRecipe(), IngredientRecipeItems.getExoticGreens(), false, "Exotic Greens");
			}
			else if(recipe.equalsIgnoreCase("rice")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getRiceRecipe(), IngredientRecipeItems.getRice(), true, "Rice");
			}
			else if(recipe.equalsIgnoreCase("popcorn")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getPopcornRecipe(), IngredientRecipeItems.getPopcorn(), true, "Popcorn");
			}
			else if(recipe.equalsIgnoreCase("pepper")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getPepperRecipe(), IngredientRecipeItems.getPepper(), false, "Pepper");
			}
			else if(recipe.equalsIgnoreCase("vodka") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getVodkaRecipe(), IngredientRecipeItems.getVodka(), false, "Vodka");
			}
			else if(recipe.equalsIgnoreCase("rum") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getRumRecipe(), IngredientRecipeItems.getRum(), false, "Rum");
			}
			else if(recipe.equalsIgnoreCase("tequila") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipeMax(p, econ, IngredientRecipeItems.getTequilaRecipe(), IngredientRecipeItems.getTequila(), false, "Tequila");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseIngredient(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.1")) {
			if(recipe.equalsIgnoreCase("salt")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getSaltRecipe(), IngredientRecipeItems.getSalt(), true, "Salt");
			}
			else if(recipe.equalsIgnoreCase("spices")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getSpicesRecipe(), IngredientRecipeItems.getSpices(), false, "Spices");
			}
			else if(recipe.equalsIgnoreCase("greens")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getGreensRecipe(), IngredientRecipeItems.getGreens(), false, "Greens");
			}
			else if(recipe.equalsIgnoreCase("toast")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getToastRecipe(), IngredientRecipeItems.getToast(), true, "Toast");
			}
			else if(recipe.equalsIgnoreCase("oil")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getOilRecipe(), IngredientRecipeItems.getOil(), true, "Oil");
			}
			else if(recipe.equalsIgnoreCase("tomato")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getTomatoRecipe(), IngredientRecipeItems.getTomato(), false, "Tomato");
			}
			else if(recipe.equalsIgnoreCase("butter")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getButterRecipe(), IngredientRecipeItems.getButter(), false, "Butter");
			}
			else if(recipe.equalsIgnoreCase("lemon")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getLemonRecipe(), IngredientRecipeItems.getLemon(), false, "Lemon");
			}
			else if(recipe.equalsIgnoreCase("corn")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getCornRecipe(), IngredientRecipeItems.getCorn(), false, "Corn");
			}
			else if(recipe.equalsIgnoreCase("honey")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getHoneyRecipe(), IngredientRecipeItems.getHoney(), false, "Honey");
			}
			else if(recipe.equalsIgnoreCase("yeast")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getYeastRecipe(), IngredientRecipeItems.getYeast(), false, "Yeast");
			}
			else if(recipe.equalsIgnoreCase("beetroot sauce")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getBeetrootSauceRecipe(), IngredientRecipeItems.getBeetrootSauce(), false, "Beetroot Sauce");
			}
			else if(recipe.equalsIgnoreCase("pasta")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getPastaRecipe(), IngredientRecipeItems.getPasta(), false, "Pasta");
			}
			else if(recipe.equalsIgnoreCase("onion")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getOnionRecipe(), IngredientRecipeItems.getOnion(), false, "Onion");
			}
			else if(recipe.equalsIgnoreCase("hops")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getHopsRecipe(), IngredientRecipeItems.getHops(), false, "Hops");
			}
			else if(recipe.equalsIgnoreCase("cheese")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getCheeseRecipe(), IngredientRecipeItems.getCheese(), true, "Cheese");
			}
			else if(recipe.equalsIgnoreCase("tortilla")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getTortillaRecipe(), IngredientRecipeItems.getTortilla(), false, "Tortilla");
			}
			else if(recipe.equalsIgnoreCase("exotic greens")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getExoticGreensRecipe(), IngredientRecipeItems.getExoticGreens(), false, "Exotic Greens");
			}
			else if(recipe.equalsIgnoreCase("rice")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getRiceRecipe(), IngredientRecipeItems.getRice(), true, "Rice");
			}
			else if(recipe.equalsIgnoreCase("popcorn")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getPopcornRecipe(), IngredientRecipeItems.getPopcorn(), true, "Popcorn");
			}
			else if(recipe.equalsIgnoreCase("pepper")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getPepperRecipe(), IngredientRecipeItems.getPepper(), false, "Pepper");
			}
			else if(recipe.equalsIgnoreCase("vodka") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getVodkaRecipe(), IngredientRecipeItems.getVodka(), false, "Vodka");
			}
			else if(recipe.equalsIgnoreCase("rum") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getRumRecipe(), IngredientRecipeItems.getRum(), false, "Rum");
			}
			else if(recipe.equalsIgnoreCase("tequila") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipe(p, econ, amount, IngredientRecipeItems.getTequilaRecipe(), IngredientRecipeItems.getTequila(), false, "Tequila");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier1(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.2")) {
			if(recipe.equalsIgnoreCase("cured flesh")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getCuredFleshRecipe(), Tier1RecipeItems.getCuredFlesh(), true, "Cured Flesh");
			}
			else if(recipe.equalsIgnoreCase("sunflower seeds")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getSunflowerSeedsRecipe(), Tier1RecipeItems.getSunflowerSeeds(), false, "Sunflower Seeds");
			}
			else if(recipe.equalsIgnoreCase("boiled egg")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getBoiledEggRecipe(), Tier1RecipeItems.getBoiledEgg(), true, "Boiled Egg");
			}
			else if(recipe.equalsIgnoreCase("ice cream")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getIceCreamRecipe(), Tier1RecipeItems.getIceCream(), false, "Ice Cream");
			}
			else if(recipe.equalsIgnoreCase("hot chocolate")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getHotChocolateRecipe(), Tier1RecipeItems.getHotChocolate(), true, "Hot Chocolate");
			}
			else if(recipe.equalsIgnoreCase("green tea")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getGreenTeaRecipe(), Tier1RecipeItems.getGreenTea(), false, "Green Tea");
			}
			else if(recipe.equalsIgnoreCase("barley tea")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getBarleyTeaRecipe(), Tier1RecipeItems.getBarleyTea(), false, "Barley Tea");
			}
			else if(recipe.equalsIgnoreCase("mashed potatoes")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getMashedPotatoesRecipe(), Tier1RecipeItems.getMashedPotatoes(), false, "Mashed Potatoes");
			}
			else if(recipe.equalsIgnoreCase("spinach")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getSpinachRecipe(), Tier1RecipeItems.getSpinach(), false, "Spinach");
			}
			else if(recipe.equalsIgnoreCase("chocolate truffle")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getChocolateTruffleRecipe(), Tier1RecipeItems.getChocolateTruffle(), false, "Chocolate Truffle");
			}
			else if(recipe.equalsIgnoreCase("musli")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getMusliRecipe(), Tier1RecipeItems.getMusli(), false, "Musli");
			}
			else if(recipe.equalsIgnoreCase("candied apple")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getCandiedAppleRecipe(), Tier1RecipeItems.getCandiedApple(), false, "Candied Apple");
			}
			else if(recipe.equalsIgnoreCase("mac and cheese")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getMacAndCheeseRecipe(), Tier1RecipeItems.getMacAndCheese(), false, "Mac and Cheese");
			}
			else if(recipe.equalsIgnoreCase("chocolate milk")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getChocolateMilkRecipe(), Tier1RecipeItems.getChocolateMilk(), false, "Chocolate Milk");
			}
			else if(recipe.equalsIgnoreCase("cheese tortilla")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getCheeseTortillaRecipe(), Tier1RecipeItems.getCheeseTortilla(), false, "Cheese Tortilla");
			}
			else if(recipe.equalsIgnoreCase("sushi")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getSushiRecipe(), Tier1RecipeItems.getSushi(), false, "Sushi");
			}
			else if(recipe.equalsIgnoreCase("pottage")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getPottageRecipe(), Tier1RecipeItems.getPottage(), false, "Pottage");
			}
			else if(recipe.equalsIgnoreCase("buttered popcorn")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getButteredPopcornRecipe(), Tier1RecipeItems.getButteredPopcorn(), false, "Buttered Popcorn");
			}
			else if(recipe.equalsIgnoreCase("chips and salsa")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getChipsAndSalsaRecipe(), Tier1RecipeItems.getChipsAndSalsa(), false, "Chips and Salsa");
			}
			else if(recipe.equalsIgnoreCase("gruel")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier1RecipeItems.getGruelRecipe(), Tier1RecipeItems.getGruel(), false, "Gruel");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier1(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.2")) {
			if(recipe.equalsIgnoreCase("cured flesh")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getCuredFleshRecipe(), Tier1RecipeItems.getCuredFlesh(), true, "Cured Flesh");
			}
			else if(recipe.equalsIgnoreCase("sunflower seeds")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getSunflowerSeedsRecipe(), Tier1RecipeItems.getSunflowerSeeds(), false, "Sunflower Seeds");
			}
			else if(recipe.equalsIgnoreCase("boiled egg")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getBoiledEggRecipe(), Tier1RecipeItems.getBoiledEgg(), true, "Boiled Egg");
			}
			else if(recipe.equalsIgnoreCase("ice cream")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getIceCreamRecipe(), Tier1RecipeItems.getIceCream(), false, "Ice Cream");
			}
			else if(recipe.equalsIgnoreCase("hot chocolate")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getHotChocolateRecipe(), Tier1RecipeItems.getHotChocolate(), true, "Hot Chocolate");
			}
			else if(recipe.equalsIgnoreCase("green tea")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getGreenTeaRecipe(), Tier1RecipeItems.getGreenTea(), false, "Green Tea");
			}
			else if(recipe.equalsIgnoreCase("barley tea")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getBarleyTeaRecipe(), Tier1RecipeItems.getBarleyTea(), false, "Barley Tea");
			}
			else if(recipe.equalsIgnoreCase("mashed potatoes")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getMashedPotatoesRecipe(), Tier1RecipeItems.getMashedPotatoes(), false, "Mashed Potatoes");
			}
			else if(recipe.equalsIgnoreCase("spinach")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getSpinachRecipe(), Tier1RecipeItems.getSpinach(), false, "Spinach");
			}
			else if(recipe.equalsIgnoreCase("chocolate truffle")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getChocolateTruffleRecipe(), Tier1RecipeItems.getChocolateTruffle(), false, "Chocolate Truffle");
			}
			else if(recipe.equalsIgnoreCase("musli")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getMusliRecipe(), Tier1RecipeItems.getMusli(), false, "Musli");
			}
			else if(recipe.equalsIgnoreCase("candied apple")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getCandiedAppleRecipe(), Tier1RecipeItems.getCandiedApple(), false, "Candied Apple");
			}
			else if(recipe.equalsIgnoreCase("mac and cheese")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getMacAndCheeseRecipe(), Tier1RecipeItems.getMacAndCheese(), false, "Mac and Cheese");
			}
			else if(recipe.equalsIgnoreCase("chocolate milk")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getChocolateMilkRecipe(), Tier1RecipeItems.getChocolateMilk(), false, "Chocolate Milk");
			}
			else if(recipe.equalsIgnoreCase("cheese tortilla")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getCheeseTortillaRecipe(), Tier1RecipeItems.getCheeseTortilla(), false, "Cheese Tortilla");
			}
			else if(recipe.equalsIgnoreCase("sushi")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getSushiRecipe(), Tier1RecipeItems.getSushi(), false, "Sushi");
			}
			else if(recipe.equalsIgnoreCase("pottage")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getPottageRecipe(), Tier1RecipeItems.getPottage(), false, "Pottage");
			}
			else if(recipe.equalsIgnoreCase("buttered popcorn")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getButteredPopcornRecipe(), Tier1RecipeItems.getButteredPopcorn(), false, "Buttered Popcorn");
			}
			else if(recipe.equalsIgnoreCase("chips and salsa")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getChipsAndSalsaRecipe(), Tier1RecipeItems.getChipsAndSalsa(), false, "Chips and Salsa");
			}
			else if(recipe.equalsIgnoreCase("gruel")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier1RecipeItems.getGruelRecipe(), Tier1RecipeItems.getGruel(), false, "Gruel");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier2(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.3")) {
			if(recipe.equalsIgnoreCase("steak w/ green beans")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getSteakWithGreenBeansRecipe(), Tier2RecipeItems.getSteakWithGreenBeans(), false, "Steak w/ Green Beans");
			}
			else if(recipe.equalsIgnoreCase("buttered toast")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getButteredToastRecipe(), Tier2RecipeItems.getButteredToast(), false, "Buttered Toast");
			}
			else if(recipe.equalsIgnoreCase("lemonade")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getLemonadeRecipe(), Tier2RecipeItems.getLemonade(), false, "Lemonade");
			}
			else if(recipe.equalsIgnoreCase("honeyed ham")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getHoneyedHamRecipe(), Tier2RecipeItems.getHoneyedHam(), false, "Honeyed Ham");
			}
			else if(recipe.equalsIgnoreCase("candy bar")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getCandyBarRecipe(), Tier2RecipeItems.getCandyBar(), false, "Candy Bar");
			}
			else if(recipe.equalsIgnoreCase("ale")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getAleRecipe(), Tier2RecipeItems.getAle(), false, "Ale");
			}
			else if(recipe.equalsIgnoreCase("sandwich")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getSandwichRecipe(), Tier2RecipeItems.getSandwich(), false, "Sandwich");
			}
			else if(recipe.equalsIgnoreCase("beef stew")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getBeefStewRecipe(), Tier2RecipeItems.getBeefStew(), false, "Beef Stew");
			}
			else if(recipe.equalsIgnoreCase("exotic tea")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getExoticTeaRecipe(), Tier2RecipeItems.getExoticTea(), false, "Exotic Tea");
			}
			else if(recipe.equalsIgnoreCase("bartrand's cornbread")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getBartrandCornbreadRecipe(), Tier2RecipeItems.getBartrandCornbread(), false, "Bartrand's Cornbread");
			}
			else if(recipe.equalsIgnoreCase("tuna sandwich")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getTunaSandwichRecipe(), Tier2RecipeItems.getTunaSandwich(), false, "Tuna Sandwich");
			}
			else if(recipe.equalsIgnoreCase("apple pie")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getApplePieRecipe(), Tier2RecipeItems.getApplePie(), false, "Apple Pie");
			}
			else if(recipe.equalsIgnoreCase("beer")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getBeerRecipe(), Tier2RecipeItems.getBeer(), false, "Beer");
			}
			else if(recipe.equalsIgnoreCase("hash browns")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getHashBrownsRecipe(), Tier2RecipeItems.getHashBrowns(), false, "Hash Browns");
			}
			else if(recipe.equalsIgnoreCase("lemon tart")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getLemonTartRecipe(), Tier2RecipeItems.getLemonTart(), false, "Lemon Tart");
			}
			else if(recipe.equalsIgnoreCase("wrapped chicken")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getWrappedChickenRecipe(), Tier2RecipeItems.getWrappedChicken(), false, "Wrapped Chicken");
			}
			else if(recipe.equalsIgnoreCase("mystery meat")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getMysteryMeatRecipe(), Tier2RecipeItems.getMysteryMeat(), false, "Mystery Meat");
			}
			else if(recipe.equalsIgnoreCase("tomato soup")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getTomatoSoupRecipe(), Tier2RecipeItems.getTomatoSoup(), false, "Tomato Soup");
			}
			else if(recipe.equalsIgnoreCase("corn on the cob")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getCornOnTheCobRecipe(), Tier2RecipeItems.getCornOnTheCob(), false, "Corn on the Cob");
			}
			else if(recipe.equalsIgnoreCase("bread pudding")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getBreadPuddingRecipe(), Tier2RecipeItems.getBreadPudding(), false, "Bread Pudding");
			}
			else if(recipe.equalsIgnoreCase("chicken pasta")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier2RecipeItems.getChickenPastaRecipe(), Tier2RecipeItems.getChickenPasta(), false, "Chicken Pasta");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier2(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.3")) {
			if(recipe.equalsIgnoreCase("steak w/ green beans")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getSteakWithGreenBeansRecipe(), Tier2RecipeItems.getSteakWithGreenBeans(), false, "Steak w/ Green Beans");
			}
			else if(recipe.equalsIgnoreCase("buttered toast")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getButteredToastRecipe(), Tier2RecipeItems.getButteredToast(), false, "Buttered Toast");
			}
			else if(recipe.equalsIgnoreCase("lemonade")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getLemonadeRecipe(), Tier2RecipeItems.getLemonade(), false, "Lemonade");
			}
			else if(recipe.equalsIgnoreCase("honeyed ham")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getHoneyedHamRecipe(), Tier2RecipeItems.getHoneyedHam(), false, "Honeyed Ham");
			}
			else if(recipe.equalsIgnoreCase("candy bar")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getCandyBarRecipe(), Tier2RecipeItems.getCandyBar(), false, "Candy Bar");
			}
			else if(recipe.equalsIgnoreCase("ale")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getAleRecipe(), Tier2RecipeItems.getAle(), false, "Ale");
			}
			else if(recipe.equalsIgnoreCase("sandwich")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getSandwichRecipe(), Tier2RecipeItems.getSandwich(), false, "Sandwich");
			}
			else if(recipe.equalsIgnoreCase("beef stew")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getBeefStewRecipe(), Tier2RecipeItems.getBeefStew(), false, "Beef Stew");
			}
			else if(recipe.equalsIgnoreCase("exotic tea")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getExoticTeaRecipe(), Tier2RecipeItems.getExoticTea(), false, "Exotic Tea");
			}
			else if(recipe.equalsIgnoreCase("bartrand's cornbread")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getBartrandCornbreadRecipe(), Tier2RecipeItems.getBartrandCornbread(), false, "Bartrand's Cornbread");
			}
			else if(recipe.equalsIgnoreCase("tuna sandwich")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getTunaSandwichRecipe(), Tier2RecipeItems.getTunaSandwich(), false, "Tuna Sandwich");
			}
			else if(recipe.equalsIgnoreCase("apple pie")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getApplePieRecipe(), Tier2RecipeItems.getApplePie(), false, "Apple Pie");
			}
			else if(recipe.equalsIgnoreCase("beer")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getBeerRecipe(), Tier2RecipeItems.getBeer(), false, "Beer");
			}
			else if(recipe.equalsIgnoreCase("hash browns")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getHashBrownsRecipe(), Tier2RecipeItems.getHashBrowns(), false, "Hash Browns");
			}
			else if(recipe.equalsIgnoreCase("lemon tart")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getLemonTartRecipe(), Tier2RecipeItems.getLemonTart(), false, "Lemon Tart");
			}
			else if(recipe.equalsIgnoreCase("wrapped chicken")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getWrappedChickenRecipe(), Tier2RecipeItems.getWrappedChicken(), false, "Wrapped Chicken");
			}
			else if(recipe.equalsIgnoreCase("mystery meat")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getMysteryMeatRecipe(), Tier2RecipeItems.getMysteryMeat(), false, "Mystery Meat");
			}
			else if(recipe.equalsIgnoreCase("tomato soup")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getTomatoSoupRecipe(), Tier2RecipeItems.getTomatoSoup(), false, "Tomato Soup");
			}
			else if(recipe.equalsIgnoreCase("corn on the cob")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getCornOnTheCobRecipe(), Tier2RecipeItems.getCornOnTheCob(), false, "Corn on the Cob");
			}
			else if(recipe.equalsIgnoreCase("bread pudding")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getBreadPuddingRecipe(), Tier2RecipeItems.getBreadPudding(), false, "Bread Pudding");
			}
			else if(recipe.equalsIgnoreCase("chicken pasta")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier2RecipeItems.getChickenPastaRecipe(), Tier2RecipeItems.getChickenPasta(), false, "Chicken Pasta");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier3(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.4")) {
			if(recipe.equalsIgnoreCase("garden salad")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getGardenSaladRecipe(), Tier3RecipeItems.getGardenSalad(), false, "Garden Salad");
			}
			else if(recipe.equalsIgnoreCase("chicken leg")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getChickenLegRecipe(), Tier3RecipeItems.getChickenLeg(), false, "Chicken Leg");
			}
			else if(recipe.equalsIgnoreCase("apple porkchops")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getApplePorkchopsRecipe(), Tier3RecipeItems.getApplePorkchops(), false, "Apple Porkchops");
			}
			else if(recipe.equalsIgnoreCase("scrambled eggs")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getScrambledEggsRecipe(), Tier3RecipeItems.getScrambledEggs(), false, "Scrambled Eggs");
			}
			else if(recipe.equalsIgnoreCase("lamb kabob")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getLambKabobRecipe(), Tier3RecipeItems.getLambKabob(), false, "Lamb Kabob");
			}
			else if(recipe.equalsIgnoreCase("zesty steak")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getZestySteakRecipe(), Tier3RecipeItems.getZestySteak(), false, "Zesty Steak");
			}
			else if(recipe.equalsIgnoreCase("loaded baked potato")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getLoadedBakedPotatoRecipe(), Tier3RecipeItems.getLoadedBakedPotato(), false, "Loaded Baked Potato");
			}
			else if(recipe.equalsIgnoreCase("sweet buns")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getSweetBunsRecipe(), Tier3RecipeItems.getSweetBuns(), false, "Sweet Buns");
			}
			else if(recipe.equalsIgnoreCase("spaghetti bolognese")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getSpaghettiBologneseRecipe(), Tier3RecipeItems.getSpaghettiBolognese(), false, "Spaghetti Bolognese");
			}
			else if(recipe.equalsIgnoreCase("filleted salmon")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getFilletedSalmonRecipe(), Tier3RecipeItems.getFilletedSalmon(), false, "Filleted Salmon");
			}
			else if(recipe.equalsIgnoreCase("roast bream")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getRoastBreamRecipe(), Tier3RecipeItems.getRoastBream(), false, "Roast Bream");
			}
			else if(recipe.equalsIgnoreCase("smoothie")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getSmoothieRecipe(), Tier3RecipeItems.getSmoothie(), false, "Smoothie");
			}
			else if(recipe.equalsIgnoreCase("hearty burrito")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getHeartyBurritoRecipe(), Tier3RecipeItems.getHeartyBurrito(), false, "Hearty Burrito");
			}
			else if(recipe.equalsIgnoreCase("hero sandwich")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getHeroSandwichRecipe(), Tier3RecipeItems.getHeroSandwich(), false, "Hero Sandwich");
			}
			else if(recipe.equalsIgnoreCase("lamb stew")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getLambStewRecipe(), Tier3RecipeItems.getLambStew(), false, "Lamb Stew");
			}
			else if(recipe.equalsIgnoreCase("honey glazed chicken")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getHoneyGlazedChickenRecipe(), Tier3RecipeItems.getHoneyGlazedChicken(), false, "Honey Glazed Chicken");
			}
			else if(recipe.equalsIgnoreCase("apple cider")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getAppleCiderRecipe(), Tier3RecipeItems.getAppleCider(), false, "Apple Cider");
			}
			else if(recipe.equalsIgnoreCase("mushroom pasty")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getMushroomPastyRecipe(), Tier3RecipeItems.getMushroomPasty(), false, "Mushroom Pasty");
			}
			else if(recipe.equalsIgnoreCase("buttered wortes")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getButteredWortesRecipe(), Tier3RecipeItems.getButteredWortes(), false, "Buttered Wortes");
			}
			else if(recipe.equalsIgnoreCase("sponge cake")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getSpongeCakeRecipe(), Tier3RecipeItems.getSpongeCake(), false, "Sponge Cake");
			}
			else if(recipe.equalsIgnoreCase("chicken parmesan")) {
				culinarianUtils.craftRecipeMax(p, econ, Tier3RecipeItems.getChickenParmesanRecipe(), Tier3RecipeItems.getChickenParmesan(), false, "Chicken Parmesan");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier3(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.4")) {
			if(recipe.equalsIgnoreCase("garden salad")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getGardenSaladRecipe(), Tier3RecipeItems.getGardenSalad(), false, "Garden Salad");
			}
			else if(recipe.equalsIgnoreCase("chicken leg")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getChickenLegRecipe(), Tier3RecipeItems.getChickenLeg(), false, "Chicken Leg");
			}
			else if(recipe.equalsIgnoreCase("apple porkchops")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getApplePorkchopsRecipe(), Tier3RecipeItems.getApplePorkchops(), false, "Apple Porkchops");
			}
			else if(recipe.equalsIgnoreCase("scrambled eggs")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getScrambledEggsRecipe(), Tier3RecipeItems.getScrambledEggs(), false, "Scrambled Eggs");
			}
			else if(recipe.equalsIgnoreCase("lamb kabob")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getLambKabobRecipe(), Tier3RecipeItems.getLambKabob(), false, "Lamb Kabob");
			}
			else if(recipe.equalsIgnoreCase("zesty steak")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getZestySteakRecipe(), Tier3RecipeItems.getZestySteak(), false, "Zesty Steak");
			}
			else if(recipe.equalsIgnoreCase("loaded baked potato")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getLoadedBakedPotatoRecipe(), Tier3RecipeItems.getLoadedBakedPotato(), false, "Loaded Baked Potato");
			}
			else if(recipe.equalsIgnoreCase("sweet buns")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getSweetBunsRecipe(), Tier3RecipeItems.getSweetBuns(), false, "Sweet Buns");
			}
			else if(recipe.equalsIgnoreCase("spaghetti bolognese")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getSpaghettiBologneseRecipe(), Tier3RecipeItems.getSpaghettiBolognese(), false, "Spaghetti Bolognese");
			}
			else if(recipe.equalsIgnoreCase("filleted salmon")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getFilletedSalmonRecipe(), Tier3RecipeItems.getFilletedSalmon(), false, "Filleted Salmon");
			}
			else if(recipe.equalsIgnoreCase("roast bream")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getRoastBreamRecipe(), Tier3RecipeItems.getRoastBream(), false, "Roast Bream");
			}
			else if(recipe.equalsIgnoreCase("smoothie")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getSmoothieRecipe(), Tier3RecipeItems.getSmoothie(), false, "Smoothie");
			}
			else if(recipe.equalsIgnoreCase("hearty burrito")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getHeartyBurritoRecipe(), Tier3RecipeItems.getHeartyBurrito(), false, "Hearty Burrito");
			}
			else if(recipe.equalsIgnoreCase("hero sandwich")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getHeroSandwichRecipe(), Tier3RecipeItems.getHeroSandwich(), false, "Hero Sandwich");
			}
			else if(recipe.equalsIgnoreCase("lamb stew")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getLambStewRecipe(), Tier3RecipeItems.getLambStew(), false, "Lamb Stew");
			}
			else if(recipe.equalsIgnoreCase("honey glazed chicken")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getHoneyGlazedChickenRecipe(), Tier3RecipeItems.getHoneyGlazedChicken(), false, "Honey Glazed Chicken");
			}
			else if(recipe.equalsIgnoreCase("apple cider")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getAppleCiderRecipe(), Tier3RecipeItems.getAppleCider(), false, "Apple Cider");
			}
			else if(recipe.equalsIgnoreCase("mushroom pasty")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getMushroomPastyRecipe(), Tier3RecipeItems.getMushroomPasty(), false, "Mushroom Pasty");
			}
			else if(recipe.equalsIgnoreCase("buttered wortes")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getButteredWortesRecipe(), Tier3RecipeItems.getButteredWortes(), false, "Buttered Wortes");
			}
			else if(recipe.equalsIgnoreCase("sponge cake")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getSpongeCakeRecipe(), Tier3RecipeItems.getSpongeCake(), false, "Sponge Cake");
			}
			else if(recipe.equalsIgnoreCase("chicken parmesan")) {
				culinarianUtils.craftRecipe(p, econ, amount, Tier3RecipeItems.getChickenParmesanRecipe(), Tier3RecipeItems.getChickenParmesan(), false, "Chicken Parmesan");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseLimitedEdition(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.5")) {
			if(recipe.equalsIgnoreCase("candy corn")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getCandyCornRecipe(), LimitedEditionRecipeItems.getCandyCorn(), false, "Candy Corn");
			}
			else if(recipe.equalsIgnoreCase("witch's brew")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getWitchBrewRecipe(), LimitedEditionRecipeItems.getWitchBrew(), false, "Witch's Brew");
			}
			else if(recipe.equalsIgnoreCase("baked eyeball")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getBakedEyeballRecipe(), LimitedEditionRecipeItems.getBakedEyeball(), true, "Baked Eyeball");
			}
			else if(recipe.equalsIgnoreCase("candy cane")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getCandyCaneRecipe(), LimitedEditionRecipeItems.getCandyCane(), false, "Candy Cane");
			}
			else if(recipe.equalsIgnoreCase("gingerbread")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getGingerbreadRecipe(), LimitedEditionRecipeItems.getGingerbread(), false, "Gingerbread");
			}
			else if(recipe.equalsIgnoreCase("smoked ham")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getSmokedHamRecipe(), LimitedEditionRecipeItems.getSmokedHam(), true, "Smoked Ham");
			}
			else if(recipe.equalsIgnoreCase("eggnog")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getEggnogRecipe(), LimitedEditionRecipeItems.getEggnog(), false, "Eggnog");
			}
			else if(recipe.equalsIgnoreCase("lasagna")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getLasagnaRecipe(), LimitedEditionRecipeItems.getLasagna(), false, "Lasagna");
			}
			else if(recipe.equalsIgnoreCase("lemon soda")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getLemonSodaRecipe(), LimitedEditionRecipeItems.getLemonSoda(), false, "Lemon Soda");
			}
			else if(recipe.equalsIgnoreCase("olympian gyro")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getOlympianGyroRecipe(), LimitedEditionRecipeItems.getOlympianGyro(), false, "Olympian Gyro");
			}
			else if(recipe.equalsIgnoreCase("cupcake")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getCupcakeRecipe(), LimitedEditionRecipeItems.getCupcake(), false, "Cupcake");
			}
			else if(recipe.equalsIgnoreCase("fish and chips")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getFishAndChipsRecipe(), LimitedEditionRecipeItems.getFishAndChips(), false, "Fish and Chips");
			}
			else if(recipe.equalsIgnoreCase("escargot")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getEscargotRecipe(), LimitedEditionRecipeItems.getEscargot(), false, "Escargot");
			}
			else if(recipe.equalsIgnoreCase("vitalac")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getVitalacRecipe(), LimitedEditionRecipeItems.getVitalac(), false, "Vitalac");
			}
			else if(recipe.equalsIgnoreCase("belgian waffle")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getBelgianWaffleRecipe(), LimitedEditionRecipeItems.getBelgianWaffle(), false, "Belgian Waffle");
			}
			else if(recipe.equalsIgnoreCase("violet's dirt pie")) {
				culinarianUtils.craftRecipeMax(p, econ, LimitedEditionRecipeItems.getVioletDirtPieRecipe(), LimitedEditionRecipeItems.getVioletDirtPie(), false, "Violet's Dirt Pie");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseLimitedEdition(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.5")) {
			if(recipe.equalsIgnoreCase("candy corn")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getCandyCornRecipe(), LimitedEditionRecipeItems.getCandyCorn(), false, "Candy Corn");
			}
			else if(recipe.equalsIgnoreCase("witch's brew")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getWitchBrewRecipe(), LimitedEditionRecipeItems.getWitchBrew(), false, "Witch's Brew");
			}
			else if(recipe.equalsIgnoreCase("baked eyeball")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getBakedEyeballRecipe(), LimitedEditionRecipeItems.getBakedEyeball(), true, "Baked Eyeball");
			}
			else if(recipe.equalsIgnoreCase("candy cane")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getCandyCaneRecipe(), LimitedEditionRecipeItems.getCandyCane(), false, "Candy Cane");
			}
			else if(recipe.equalsIgnoreCase("gingerbread")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getGingerbreadRecipe(), LimitedEditionRecipeItems.getGingerbread(), false, "Gingerbread");
			}
			else if(recipe.equalsIgnoreCase("smoked ham")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getSmokedHamRecipe(), LimitedEditionRecipeItems.getSmokedHam(), true, "Smoked Ham");
			}
			else if(recipe.equalsIgnoreCase("eggnog")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getEggnogRecipe(), LimitedEditionRecipeItems.getEggnog(), false, "Eggnog");
			}
			else if(recipe.equalsIgnoreCase("lasagna")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getLasagnaRecipe(), LimitedEditionRecipeItems.getLasagna(), false, "Lasagna");
			}
			else if(recipe.equalsIgnoreCase("lemon soda")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getLemonSodaRecipe(), LimitedEditionRecipeItems.getLemonSoda(), false, "Lemon Soda");
			}
			else if(recipe.equalsIgnoreCase("olympian gyro")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getOlympianGyroRecipe(), LimitedEditionRecipeItems.getOlympianGyro(), false, "Olympian Gyro");
			}
			else if(recipe.equalsIgnoreCase("cupcake")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getCupcakeRecipe(), LimitedEditionRecipeItems.getCupcake(), false, "Cupcake");
			}
			else if(recipe.equalsIgnoreCase("fish and chips")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getFishAndChipsRecipe(), LimitedEditionRecipeItems.getFishAndChips(), false, "Fish and Chips");
			}
			else if(recipe.equalsIgnoreCase("escargot")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getEscargotRecipe(), LimitedEditionRecipeItems.getEscargot(), false, "Escargot");
			}
			else if(recipe.equalsIgnoreCase("vitalac")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getVitalacRecipe(), LimitedEditionRecipeItems.getVitalac(), false, "Vitalac");
			}
			else if(recipe.equalsIgnoreCase("belgian waffle")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getBelgianWaffleRecipe(), LimitedEditionRecipeItems.getBelgianWaffle(), false, "Belgian Waffle");
			}
			else if(recipe.equalsIgnoreCase("violet's dirt pie")) {
				culinarianUtils.craftRecipe(p, econ, amount, LimitedEditionRecipeItems.getVioletDirtPieRecipe(), LimitedEditionRecipeItems.getVioletDirtPie(), false, "Violet's Dirt Pie");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseLegendary(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.5")) {
			if(recipe.equalsIgnoreCase("dragon scrambled eggs")) {
				culinarianUtils.craftRecipeMax(p, econ, LegendaryRecipeItems.getDragonScrambledEggsRecipe(), LegendaryRecipeItems.getDragonScrambledEggs(), false, "Dragon Scrambled Eggs");
			}
			else if(recipe.equalsIgnoreCase("neo's full course special")) {
				culinarianUtils.craftRecipeMax(p, econ, LegendaryRecipeItems.getNeoFullCourseSpecialRecipe(), LegendaryRecipeItems.getNeoFullCourseSpecial(), false, "Neo's Full Course Special");
			}
			else if(recipe.equalsIgnoreCase("tobias' famous cake")) {
				culinarianUtils.craftRecipeMax(p, econ, LegendaryRecipeItems.getTobiasFamousCakeRecipe(), LegendaryRecipeItems.getTobiasFamousCake(), false, "Tobias' Famous Cake");
			}
			else if(recipe.equalsIgnoreCase("low district cheese steak")) {
				culinarianUtils.craftRecipeMax(p, econ, LegendaryRecipeItems.getLowDistrictCheeseSteakRecipe(), LegendaryRecipeItems.getLowDistrictCheeseSteak(), false, "Low District Cheese Steak");
			}
			else if(recipe.equalsIgnoreCase("mattifornia roll")) {
				culinarianUtils.craftRecipeMax(p, econ, LegendaryRecipeItems.getMattiforniaRollRecipe(), LegendaryRecipeItems.getMattiforniaRoll(), false, "Mattifornia Roll");
			}
			else if(recipe.equalsIgnoreCase("tilan's salad")) {
				culinarianUtils.craftRecipeMax(p, econ, LegendaryRecipeItems.getTilanSaladRecipe(), LegendaryRecipeItems.getTilanSalad(), false, "Tilan's Salad");
			}
			else if(recipe.equalsIgnoreCase("super's sundae")) {
				culinarianUtils.craftRecipeMax(p, econ, LegendaryRecipeItems.getSuperSundaeRecipe(), LegendaryRecipeItems.getSuperSundae(), false, "Super's Sundae");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseLegendary(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.5")) {
			if(recipe.equalsIgnoreCase("dragon scrambled eggs")) {
				culinarianUtils.craftRecipe(p, econ, amount, LegendaryRecipeItems.getDragonScrambledEggsRecipe(), LegendaryRecipeItems.getDragonScrambledEggs(), false, "Dragon Scrambled Eggs");
			}
			else if(recipe.equalsIgnoreCase("neo's full course special")) {
				culinarianUtils.craftRecipe(p, econ, amount, LegendaryRecipeItems.getNeoFullCourseSpecialRecipe(), LegendaryRecipeItems.getNeoFullCourseSpecial(), false, "Neo's Full Course Special");
			}
			else if(recipe.equalsIgnoreCase("tobias' famous cake")) {
				culinarianUtils.craftRecipe(p, econ, amount, LegendaryRecipeItems.getTobiasFamousCakeRecipe(), LegendaryRecipeItems.getTobiasFamousCake(), false, "Tobias' Famous Cake");
			}
			else if(recipe.equalsIgnoreCase("low district cheese steak")) {
				culinarianUtils.craftRecipe(p, econ, amount, LegendaryRecipeItems.getLowDistrictCheeseSteakRecipe(), LegendaryRecipeItems.getLowDistrictCheeseSteak(), false, "Low District Cheese Steak");
			}
			else if(recipe.equalsIgnoreCase("mattifornia roll")) {
				culinarianUtils.craftRecipe(p, econ, amount, LegendaryRecipeItems.getMattiforniaRollRecipe(), LegendaryRecipeItems.getMattiforniaRoll(), false, "Mattifornia Roll");
			}
			else if(recipe.equalsIgnoreCase("tilan's salad")) {
				culinarianUtils.craftRecipe(p, econ, amount, LegendaryRecipeItems.getTilanSaladRecipe(), LegendaryRecipeItems.getTilanSalad(), false, "Tilan's Salad");
			}
			else if(recipe.equalsIgnoreCase("super's sundae")) {
				culinarianUtils.craftRecipe(p, econ, amount, LegendaryRecipeItems.getSuperSundaeRecipe(), LegendaryRecipeItems.getSuperSundae(), false, "Super's Sundae");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseDrink(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.4")) {
			if(recipe.equalsIgnoreCase("black widow")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getBlackWidowRecipe(), DrinksRecipeItems.getBlackWidow(), true, "Black Widow");
			}
			else if(recipe.equalsIgnoreCase("pink panther")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getPinkPantherRecipe(), DrinksRecipeItems.getPinkPanther(), false, "Pink Panther");
			}
			else if(recipe.equalsIgnoreCase("midnight kiss")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getMidnightKissRecipe(), DrinksRecipeItems.getMidnightKiss(), false, "Midnight Kiss");
			}
			else if(recipe.equalsIgnoreCase("midnight blue")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getMidnightBlueRecipe(), DrinksRecipeItems.getMidnightBlue(), false, "Midnight Blue");
			}
			else if(recipe.equalsIgnoreCase("good and evil")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getGoodAndEvilRecipe(), DrinksRecipeItems.getGoodAndEvil(), false, "Good and Evil");
			}
			else if(recipe.equalsIgnoreCase("thor's hammer")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getThorHammerRecipe(), DrinksRecipeItems.getThorHammer(), false, "Thor's Hammer");
			}
			else if(recipe.equalsIgnoreCase("jack frost")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getJackFrostRecipe(), DrinksRecipeItems.getJackFrost(), false, "Jack Frost");
			}
			else if(recipe.equalsIgnoreCase("white russian")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getWhiteRussianRecipe(), DrinksRecipeItems.getWhiteRussian(), false, "White Russian");
			}
			else if(recipe.equalsIgnoreCase("swamp water")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getSwampWaterRecipe(), DrinksRecipeItems.getSwampWater(), false, "Swamp Water");
			}
			else if(recipe.equalsIgnoreCase("blue motorcycle")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getBlueMotorcycleRecipe(), DrinksRecipeItems.getBlueMotorcycle(), false, "Blue Motorcycle");
			}
			else if(recipe.equalsIgnoreCase("red death")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getRedDeathRecipe(), DrinksRecipeItems.getRedDeath(), false, "Red Death");
			}
			else if(recipe.equalsIgnoreCase("bombsicle")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getBombsicleRecipe(), DrinksRecipeItems.getBombsicle(), false, "Bombsicle");
			}
			else if(recipe.equalsIgnoreCase("sweet tart")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getSweetTartRecipe(), DrinksRecipeItems.getSweetTart(), false, "Sweet Tart");
			}
			else if(recipe.equalsIgnoreCase("pina colada")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getPinaColadaRecipe(), DrinksRecipeItems.getPinaColada(), false, "Pina Colada");
			}
			else if(recipe.equalsIgnoreCase("margarita on the rocks")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getMargaritaOnTheRocksRecipe(), DrinksRecipeItems.getMargaritaOnTheRocks(), false, "Margarita on the Rocks");
			}
			else if(recipe.equalsIgnoreCase("bloody mary")) {
				culinarianUtils.craftRecipeMax(p, econ, DrinksRecipeItems.getBloodyMaryRecipe(), DrinksRecipeItems.getBloodyMary(), false, "Bloody Marry");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseDrink(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.4")) {
			if(recipe.equalsIgnoreCase("black widow")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getBlackWidowRecipe(), DrinksRecipeItems.getBlackWidow(), true, "Black Widow");
			}
			else if(recipe.equalsIgnoreCase("pink panther")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getPinkPantherRecipe(), DrinksRecipeItems.getPinkPanther(), false, "Pink Panther");
			}
			else if(recipe.equalsIgnoreCase("midnight kiss")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getMidnightKissRecipe(), DrinksRecipeItems.getMidnightKiss(), false, "Midnight Kiss");
			}
			else if(recipe.equalsIgnoreCase("midnight blue")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getMidnightBlueRecipe(), DrinksRecipeItems.getMidnightBlue(), false, "Midnight Blue");
			}
			else if(recipe.equalsIgnoreCase("good and evil")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getGoodAndEvilRecipe(), DrinksRecipeItems.getGoodAndEvil(), false, "Good and Evil");
			}
			else if(recipe.equalsIgnoreCase("thor's hammer")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getThorHammerRecipe(), DrinksRecipeItems.getThorHammer(), false, "Thor's Hammer");
			}
			else if(recipe.equalsIgnoreCase("jack frost")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getJackFrostRecipe(), DrinksRecipeItems.getJackFrost(), false, "Jack Frost");
			}
			else if(recipe.equalsIgnoreCase("white russian")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getWhiteRussianRecipe(), DrinksRecipeItems.getWhiteRussian(), false, "White Russian");
			}
			else if(recipe.equalsIgnoreCase("swamp water")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getSwampWaterRecipe(), DrinksRecipeItems.getSwampWater(), false, "Swamp Water");
			}
			else if(recipe.equalsIgnoreCase("blue motorcycle")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getBlueMotorcycleRecipe(), DrinksRecipeItems.getBlueMotorcycle(), false, "Blue Motorcycle");
			}
			else if(recipe.equalsIgnoreCase("red death")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getRedDeathRecipe(), DrinksRecipeItems.getRedDeath(), false, "Red Death");
			}
			else if(recipe.equalsIgnoreCase("bombsicle")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getBombsicleRecipe(), DrinksRecipeItems.getBombsicle(), false, "Bombsicle");
			}
			else if(recipe.equalsIgnoreCase("sweet tart")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getSweetTartRecipe(), DrinksRecipeItems.getSweetTart(), false, "Sweet Tart");
			}
			else if(recipe.equalsIgnoreCase("pina colada")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getPinaColadaRecipe(), DrinksRecipeItems.getPinaColada(), false, "Pina Colada");
			}
			else if(recipe.equalsIgnoreCase("margarita on the rocks")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getMargaritaOnTheRocksRecipe(), DrinksRecipeItems.getMargaritaOnTheRocks(), false, "Margarita on the Rocks");
			}
			else if(recipe.equalsIgnoreCase("bloody mary")) {
				culinarianUtils.craftRecipe(p, econ, amount, DrinksRecipeItems.getBloodyMaryRecipe(), DrinksRecipeItems.getBloodyMary(), false, "Bloody Marry");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void resetPlayer(Player p) {
		String name = p.getName();
		
		// Clean out perms
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.professed");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.profess.account");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.spice.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.spice.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.spice.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.garnish.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.garnish.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.garnish.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.preserve.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.preserve.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.preserve.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.assimilate.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.assimilate.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.assimilate.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.craft.1");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.craft.2");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.craft.3");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.craft.4");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.craft.5");
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex user " + name + " remove culinarian.bartender");
		
		// Reset profession
		SkillAPI.getPlayerData(p).reset("profession");
	}

}
