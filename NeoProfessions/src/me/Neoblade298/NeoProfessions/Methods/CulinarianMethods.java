package me.Neoblade298.NeoProfessions.Methods;

import java.text.DecimalFormat;
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
	IngredientRecipeItems ingr;
	Tier1RecipeItems t1;
	Tier2RecipeItems t2;
	Tier3RecipeItems t3;
	LimitedEditionRecipeItems ler;
	LegendaryRecipeItems legend;
	DrinksRecipeItems drink;
	CommonItems common;
	DecimalFormat format;
	
	// Constants
	final static double GARNISH_BOOST_BASE = 1.1;
	final static double GARNISH_BOOST_PER_LVL = 0.15;
	final static double PRESERVE_BOOST_BASE = 0.9;
	final static double PRESERVE_BOOST_PER_LVL = -0.1;
	final static double SPICE_BOOST_BASE = 1.1;
	final static double SPICE_BOOST_PER_LVL = 0.15;
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
		common = new CommonItems();
		culinarianUtils = new CulinarianUtils();
		util = new Util();
		ingr = new IngredientRecipeItems();
		t1 = new Tier1RecipeItems();
		t2 = new Tier2RecipeItems();
		t3 = new Tier3RecipeItems();
		ler = new LimitedEditionRecipeItems();
		legend = new LegendaryRecipeItems();
		drink = new DrinksRecipeItems();
		format = new DecimalFormat("###.##");
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
							if(p.getInventory().containsAtLeast(common.getEssence(foodLevel), GARNISH_ESSENCE)) {
								if(econ.has(p, GARNISH_COST)) {
									ItemMeta meta = item.getItemMeta();
									ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
									double multiplier = GARNISH_BOOST_BASE + (GARNISH_BOOST_PER_LVL * (level - 1));
									lore.add("§9Garnished (" + format.format(multiplier) + "x Attribute Boost)");
									meta.setLore(lore);
									item.setItemMeta(meta);
									p.getInventory().addItem(util.setAmount(item, 1));
									p.getInventory().removeItem(common.getEssence(foodLevel));
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
							if(p.getInventory().containsAtLeast(common.getEssence(foodLevel), PRESERVE_ESSENCE)) {
								if(econ.has(p, PRESERVE_COST)) {
									ItemMeta meta = item.getItemMeta();
									ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
									double multiplier = PRESERVE_BOOST_BASE + (PRESERVE_BOOST_PER_LVL * (level - 1));
									lore.add("§9Preserved (" + format.format(multiplier) + "x Duration Boost)");
									meta.setLore(lore);
									item.setItemMeta(meta);
									p.getInventory().addItem(util.setAmount(item, 1));
									p.getInventory().removeItem(common.getEssence(foodLevel));
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
							if(p.getInventory().containsAtLeast(common.getEssence(foodLevel), SPICE_ESSENCE)) {
								if(econ.has(p, SPICE_COST)) {
									ItemMeta meta = item.getItemMeta();
									ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
									double multiplier = SPICE_BOOST_BASE + (SPICE_BOOST_PER_LVL * (level - 1));
									lore.add("§9Spiced (" + format.format(multiplier) + "x Restoration Boost)");
									meta.setLore(lore);
									item.setItemMeta(meta);
									p.getInventory().addItem(util.setAmount(item, 1));
									p.getInventory().removeItem(common.getEssence(foodLevel));
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
						if(p.getInventory().containsAtLeast(common.getEssence(foodLevel), GARNISH_ESSENCE)) {
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
							p.getInventory().addItem(common.getEssenceFragment(level));
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
						recipes = ingr.getIngredients();
					}
					else if(tier == 1) {
						recipes = t1.getTier1Recipes();
					}
					else if(tier == 2) {
						recipes = t2.getTier2Recipes();
					}
					else if(tier == 3) {
						recipes = t3.getTier3Recipes();
					}
					else if(tier == 4) {
						recipes = ler.getLimitedEditionRecipes();
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
				culinarianUtils.craftRecipeMax(p, econ, ingr.getSaltRecipe(), ingr.getSalt(), true, "Salt");
			}
			else if(recipe.equalsIgnoreCase("spices")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getSpicesRecipe(), ingr.getSpices(), false, "Spices");
			}
			else if(recipe.equalsIgnoreCase("greens")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getGreensRecipe(), ingr.getGreens(), false, "Greens");
			}
			else if(recipe.equalsIgnoreCase("toast")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getToastRecipe(), ingr.getToast(), true, "Toast");
			}
			else if(recipe.equalsIgnoreCase("oil")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getOilRecipe(), ingr.getOil(), true, "Oil");
			}
			else if(recipe.equalsIgnoreCase("tomato")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getTomatoRecipe(), ingr.getTomato(), false, "Tomato");
			}
			else if(recipe.equalsIgnoreCase("butter")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getButterRecipe(), ingr.getButter(), false, "Butter");
			}
			else if(recipe.equalsIgnoreCase("lemon")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getLemonRecipe(), ingr.getLemon(), false, "Lemon");
			}
			else if(recipe.equalsIgnoreCase("corn")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getCornRecipe(), ingr.getCorn(), false, "Corn");
			}
			else if(recipe.equalsIgnoreCase("honey")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getHoneyRecipe(), ingr.getHoney(), false, "Honey");
			}
			else if(recipe.equalsIgnoreCase("yeast")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getYeastRecipe(), ingr.getYeast(), false, "Yeast");
			}
			else if(recipe.equalsIgnoreCase("beetroot sauce")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getBeetrootSauceRecipe(), ingr.getBeetrootSauce(), false, "Beetroot Sauce");
			}
			else if(recipe.equalsIgnoreCase("pasta")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getPastaRecipe(), ingr.getPasta(), false, "Pasta");
			}
			else if(recipe.equalsIgnoreCase("onion")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getOnionRecipe(), ingr.getOnion(), false, "Onion");
			}
			else if(recipe.equalsIgnoreCase("hops")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getHopsRecipe(), ingr.getHops(), false, "Hops");
			}
			else if(recipe.equalsIgnoreCase("cheese")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getCheeseRecipe(), ingr.getCheese(), true, "Cheese");
			}
			else if(recipe.equalsIgnoreCase("tortilla")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getTortillaRecipe(), ingr.getTortilla(), false, "Tortilla");
			}
			else if(recipe.equalsIgnoreCase("exotic greens")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getExoticGreensRecipe(), ingr.getExoticGreens(), false, "Exotic Greens");
			}
			else if(recipe.equalsIgnoreCase("rice")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getRiceRecipe(), ingr.getRice(), true, "Rice");
			}
			else if(recipe.equalsIgnoreCase("popcorn")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getPopcornRecipe(), ingr.getPopcorn(), true, "Popcorn");
			}
			else if(recipe.equalsIgnoreCase("pepper")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getPepperRecipe(), ingr.getPepper(), false, "Pepper");
			}
			else if(recipe.equalsIgnoreCase("vodka") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getVodkaRecipe(), ingr.getVodka(), false, "Vodka");
			}
			else if(recipe.equalsIgnoreCase("rum") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getRumRecipe(), ingr.getRum(), false, "Rum");
			}
			else if(recipe.equalsIgnoreCase("tequila") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipeMax(p, econ, ingr.getTequilaRecipe(), ingr.getTequila(), false, "Tequila");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseIngredient(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.1")) {
			if(recipe.equalsIgnoreCase("salt")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getSaltRecipe(), ingr.getSalt(), true, "Salt");
			}
			else if(recipe.equalsIgnoreCase("spices")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getSpicesRecipe(), ingr.getSpices(), false, "Spices");
			}
			else if(recipe.equalsIgnoreCase("greens")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getGreensRecipe(), ingr.getGreens(), false, "Greens");
			}
			else if(recipe.equalsIgnoreCase("toast")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getToastRecipe(), ingr.getToast(), true, "Toast");
			}
			else if(recipe.equalsIgnoreCase("oil")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getOilRecipe(), ingr.getOil(), true, "Oil");
			}
			else if(recipe.equalsIgnoreCase("tomato")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getTomatoRecipe(), ingr.getTomato(), false, "Tomato");
			}
			else if(recipe.equalsIgnoreCase("butter")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getButterRecipe(), ingr.getButter(), false, "Butter");
			}
			else if(recipe.equalsIgnoreCase("lemon")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getLemonRecipe(), ingr.getLemon(), false, "Lemon");
			}
			else if(recipe.equalsIgnoreCase("corn")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getCornRecipe(), ingr.getCorn(), false, "Corn");
			}
			else if(recipe.equalsIgnoreCase("honey")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getHoneyRecipe(), ingr.getHoney(), false, "Honey");
			}
			else if(recipe.equalsIgnoreCase("yeast")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getYeastRecipe(), ingr.getYeast(), false, "Yeast");
			}
			else if(recipe.equalsIgnoreCase("beetroot sauce")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getBeetrootSauceRecipe(), ingr.getBeetrootSauce(), false, "Beetroot Sauce");
			}
			else if(recipe.equalsIgnoreCase("pasta")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getPastaRecipe(), ingr.getPasta(), false, "Pasta");
			}
			else if(recipe.equalsIgnoreCase("onion")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getOnionRecipe(), ingr.getOnion(), false, "Onion");
			}
			else if(recipe.equalsIgnoreCase("hops")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getHopsRecipe(), ingr.getHops(), false, "Hops");
			}
			else if(recipe.equalsIgnoreCase("cheese")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getCheeseRecipe(), ingr.getCheese(), true, "Cheese");
			}
			else if(recipe.equalsIgnoreCase("tortilla")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getTortillaRecipe(), ingr.getTortilla(), false, "Tortilla");
			}
			else if(recipe.equalsIgnoreCase("exotic greens")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getExoticGreensRecipe(), ingr.getExoticGreens(), false, "Exotic Greens");
			}
			else if(recipe.equalsIgnoreCase("rice")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getRiceRecipe(), ingr.getRice(), true, "Rice");
			}
			else if(recipe.equalsIgnoreCase("popcorn")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getPopcornRecipe(), ingr.getPopcorn(), true, "Popcorn");
			}
			else if(recipe.equalsIgnoreCase("pepper")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getPepperRecipe(), ingr.getPepper(), false, "Pepper");
			}
			else if(recipe.equalsIgnoreCase("vodka") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getVodkaRecipe(), ingr.getVodka(), false, "Vodka");
			}
			else if(recipe.equalsIgnoreCase("rum") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getRumRecipe(), ingr.getRum(), false, "Rum");
			}
			else if(recipe.equalsIgnoreCase("tequila") && p.hasPermission("culinarian.craft.3")) {
				culinarianUtils.craftRecipe(p, econ, amount, ingr.getTequilaRecipe(), ingr.getTequila(), false, "Tequila");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier1(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.2")) {
			if(recipe.equalsIgnoreCase("cured flesh")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getCuredFleshRecipe(), t1.getCuredFlesh(), true, "Cured Flesh");
			}
			else if(recipe.equalsIgnoreCase("sunflower seeds")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getSunflowerSeedsRecipe(), t1.getSunflowerSeeds(), false, "Sunflower Seeds");
			}
			else if(recipe.equalsIgnoreCase("boiled egg")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getBoiledEggRecipe(), t1.getBoiledEgg(), true, "Boiled Egg");
			}
			else if(recipe.equalsIgnoreCase("ice cream")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getIceCreamRecipe(), t1.getIceCream(), false, "Ice Cream");
			}
			else if(recipe.equalsIgnoreCase("hot chocolate")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getHotChocolateRecipe(), t1.getHotChocolate(), true, "Hot Chocolate");
			}
			else if(recipe.equalsIgnoreCase("green tea")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getGreenTeaRecipe(), t1.getGreenTea(), false, "Green Tea");
			}
			else if(recipe.equalsIgnoreCase("barley tea")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getBarleyTeaRecipe(), t1.getBarleyTea(), false, "Barley Tea");
			}
			else if(recipe.equalsIgnoreCase("mashed potatoes")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getMashedPotatoesRecipe(), t1.getMashedPotatoes(), false, "Mashed Potatoes");
			}
			else if(recipe.equalsIgnoreCase("spinach")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getSpinachRecipe(), t1.getSpinach(), false, "Spinach");
			}
			else if(recipe.equalsIgnoreCase("chocolate truffle")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getChocolateTruffleRecipe(), t1.getChocolateTruffle(), false, "Chocolate Truffle");
			}
			else if(recipe.equalsIgnoreCase("musli")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getMusliRecipe(), t1.getMusli(), false, "Musli");
			}
			else if(recipe.equalsIgnoreCase("candied apple")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getCandiedAppleRecipe(), t1.getCandiedApple(), false, "Candied Apple");
			}
			else if(recipe.equalsIgnoreCase("mac and cheese")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getMacAndCheeseRecipe(), t1.getMacAndCheese(), false, "Mac and Cheese");
			}
			else if(recipe.equalsIgnoreCase("chocolate milk")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getChocolateMilkRecipe(), t1.getChocolateMilk(), false, "Chocolate Milk");
			}
			else if(recipe.equalsIgnoreCase("cheese tortilla")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getCheeseTortillaRecipe(), t1.getCheeseTortilla(), false, "Cheese Tortilla");
			}
			else if(recipe.equalsIgnoreCase("sushi")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getSushiRecipe(), t1.getSushi(), false, "Sushi");
			}
			else if(recipe.equalsIgnoreCase("pottage")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getPottageRecipe(), t1.getPottage(), false, "Pottage");
			}
			else if(recipe.equalsIgnoreCase("buttered popcorn")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getButteredPopcornRecipe(), t1.getButteredPopcorn(), false, "Buttered Popcorn");
			}
			else if(recipe.equalsIgnoreCase("chips and salsa")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getChipsAndSalsaRecipe(), t1.getChipsAndSalsa(), false, "Chips and Salsa");
			}
			else if(recipe.equalsIgnoreCase("gruel")) {
				culinarianUtils.craftRecipeMax(p, econ, t1.getGruelRecipe(), t1.getGruel(), false, "Gruel");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier1(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.2")) {
			if(recipe.equalsIgnoreCase("cured flesh")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getCuredFleshRecipe(), t1.getCuredFlesh(), true, "Cured Flesh");
			}
			else if(recipe.equalsIgnoreCase("sunflower seeds")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getSunflowerSeedsRecipe(), t1.getSunflowerSeeds(), false, "Sunflower Seeds");
			}
			else if(recipe.equalsIgnoreCase("boiled egg")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getBoiledEggRecipe(), t1.getBoiledEgg(), true, "Boiled Egg");
			}
			else if(recipe.equalsIgnoreCase("ice cream")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getIceCreamRecipe(), t1.getIceCream(), false, "Ice Cream");
			}
			else if(recipe.equalsIgnoreCase("hot chocolate")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getHotChocolateRecipe(), t1.getHotChocolate(), true, "Hot Chocolate");
			}
			else if(recipe.equalsIgnoreCase("green tea")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getGreenTeaRecipe(), t1.getGreenTea(), false, "Green Tea");
			}
			else if(recipe.equalsIgnoreCase("barley tea")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getBarleyTeaRecipe(), t1.getBarleyTea(), false, "Barley Tea");
			}
			else if(recipe.equalsIgnoreCase("mashed potatoes")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getMashedPotatoesRecipe(), t1.getMashedPotatoes(), false, "Mashed Potatoes");
			}
			else if(recipe.equalsIgnoreCase("spinach")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getSpinachRecipe(), t1.getSpinach(), false, "Spinach");
			}
			else if(recipe.equalsIgnoreCase("chocolate truffle")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getChocolateTruffleRecipe(), t1.getChocolateTruffle(), false, "Chocolate Truffle");
			}
			else if(recipe.equalsIgnoreCase("musli")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getMusliRecipe(), t1.getMusli(), false, "Musli");
			}
			else if(recipe.equalsIgnoreCase("candied apple")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getCandiedAppleRecipe(), t1.getCandiedApple(), false, "Candied Apple");
			}
			else if(recipe.equalsIgnoreCase("mac and cheese")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getMacAndCheeseRecipe(), t1.getMacAndCheese(), false, "Mac and Cheese");
			}
			else if(recipe.equalsIgnoreCase("chocolate milk")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getChocolateMilkRecipe(), t1.getChocolateMilk(), false, "Chocolate Milk");
			}
			else if(recipe.equalsIgnoreCase("cheese tortilla")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getCheeseTortillaRecipe(), t1.getCheeseTortilla(), false, "Cheese Tortilla");
			}
			else if(recipe.equalsIgnoreCase("sushi")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getSushiRecipe(), t1.getSushi(), false, "Sushi");
			}
			else if(recipe.equalsIgnoreCase("pottage")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getPottageRecipe(), t1.getPottage(), false, "Pottage");
			}
			else if(recipe.equalsIgnoreCase("buttered popcorn")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getButteredPopcornRecipe(), t1.getButteredPopcorn(), false, "Buttered Popcorn");
			}
			else if(recipe.equalsIgnoreCase("chips and salsa")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getChipsAndSalsaRecipe(), t1.getChipsAndSalsa(), false, "Chips and Salsa");
			}
			else if(recipe.equalsIgnoreCase("gruel")) {
				culinarianUtils.craftRecipe(p, econ, amount, t1.getGruelRecipe(), t1.getGruel(), false, "Gruel");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier2(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.3")) {
			if(recipe.equalsIgnoreCase("steak w/ green beans")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getSteakWithGreenBeansRecipe(), t2.getSteakWithGreenBeans(), false, "Steak w/ Green Beans");
			}
			else if(recipe.equalsIgnoreCase("buttered toast")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getButteredToastRecipe(), t2.getButteredToast(), false, "Buttered Toast");
			}
			else if(recipe.equalsIgnoreCase("lemonade")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getLemonadeRecipe(), t2.getLemonade(), false, "Lemonade");
			}
			else if(recipe.equalsIgnoreCase("honeyed ham")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getHoneyedHamRecipe(), t2.getHoneyedHam(), false, "Honeyed Ham");
			}
			else if(recipe.equalsIgnoreCase("candy bar")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getCandyBarRecipe(), t2.getCandyBar(), false, "Candy Bar");
			}
			else if(recipe.equalsIgnoreCase("ale")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getAleRecipe(), t2.getAle(), false, "Ale");
			}
			else if(recipe.equalsIgnoreCase("sandwich")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getSandwichRecipe(), t2.getSandwich(), false, "Sandwich");
			}
			else if(recipe.equalsIgnoreCase("beef stew")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getBeefStewRecipe(), t2.getBeefStew(), false, "Beef Stew");
			}
			else if(recipe.equalsIgnoreCase("exotic tea")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getExoticTeaRecipe(), t2.getExoticTea(), false, "Exotic Tea");
			}
			else if(recipe.equalsIgnoreCase("bartrand's cornbread")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getBartrandCornbreadRecipe(), t2.getBartrandCornbread(), false, "Bartrand's Cornbread");
			}
			else if(recipe.equalsIgnoreCase("tuna sandwich")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getTunaSandwichRecipe(), t2.getTunaSandwich(), false, "Tuna Sandwich");
			}
			else if(recipe.equalsIgnoreCase("apple pie")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getApplePieRecipe(), t2.getApplePie(), false, "Apple Pie");
			}
			else if(recipe.equalsIgnoreCase("beer")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getBeerRecipe(), t2.getBeer(), false, "Beer");
			}
			else if(recipe.equalsIgnoreCase("hash browns")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getHashBrownsRecipe(), t2.getHashBrowns(), false, "Hash Browns");
			}
			else if(recipe.equalsIgnoreCase("lemon tart")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getLemonTartRecipe(), t2.getLemonTart(), false, "Lemon Tart");
			}
			else if(recipe.equalsIgnoreCase("wrapped chicken")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getWrappedChickenRecipe(), t2.getWrappedChicken(), false, "Wrapped Chicken");
			}
			else if(recipe.equalsIgnoreCase("mystery meat")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getMysteryMeatRecipe(), t2.getMysteryMeat(), false, "Mystery Meat");
			}
			else if(recipe.equalsIgnoreCase("tomato soup")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getTomatoSoupRecipe(), t2.getTomatoSoup(), false, "Tomato Soup");
			}
			else if(recipe.equalsIgnoreCase("corn on the cob")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getCornOnTheCobRecipe(), t2.getCornOnTheCob(), false, "Corn on the Cob");
			}
			else if(recipe.equalsIgnoreCase("bread pudding")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getBreadPuddingRecipe(), t2.getBreadPudding(), false, "Bread Pudding");
			}
			else if(recipe.equalsIgnoreCase("chicken pasta")) {
				culinarianUtils.craftRecipeMax(p, econ, t2.getChickenPastaRecipe(), t2.getChickenPasta(), false, "Chicken Pasta");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier2(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.3")) {
			if(recipe.equalsIgnoreCase("steak w/ green beans")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getSteakWithGreenBeansRecipe(), t2.getSteakWithGreenBeans(), false, "Steak w/ Green Beans");
			}
			else if(recipe.equalsIgnoreCase("buttered toast")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getButteredToastRecipe(), t2.getButteredToast(), false, "Buttered Toast");
			}
			else if(recipe.equalsIgnoreCase("lemonade")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getLemonadeRecipe(), t2.getLemonade(), false, "Lemonade");
			}
			else if(recipe.equalsIgnoreCase("honeyed ham")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getHoneyedHamRecipe(), t2.getHoneyedHam(), false, "Honeyed Ham");
			}
			else if(recipe.equalsIgnoreCase("candy bar")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getCandyBarRecipe(), t2.getCandyBar(), false, "Candy Bar");
			}
			else if(recipe.equalsIgnoreCase("ale")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getAleRecipe(), t2.getAle(), false, "Ale");
			}
			else if(recipe.equalsIgnoreCase("sandwich")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getSandwichRecipe(), t2.getSandwich(), false, "Sandwich");
			}
			else if(recipe.equalsIgnoreCase("beef stew")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getBeefStewRecipe(), t2.getBeefStew(), false, "Beef Stew");
			}
			else if(recipe.equalsIgnoreCase("exotic tea")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getExoticTeaRecipe(), t2.getExoticTea(), false, "Exotic Tea");
			}
			else if(recipe.equalsIgnoreCase("bartrand's cornbread")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getBartrandCornbreadRecipe(), t2.getBartrandCornbread(), false, "Bartrand's Cornbread");
			}
			else if(recipe.equalsIgnoreCase("tuna sandwich")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getTunaSandwichRecipe(), t2.getTunaSandwich(), false, "Tuna Sandwich");
			}
			else if(recipe.equalsIgnoreCase("apple pie")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getApplePieRecipe(), t2.getApplePie(), false, "Apple Pie");
			}
			else if(recipe.equalsIgnoreCase("beer")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getBeerRecipe(), t2.getBeer(), false, "Beer");
			}
			else if(recipe.equalsIgnoreCase("hash browns")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getHashBrownsRecipe(), t2.getHashBrowns(), false, "Hash Browns");
			}
			else if(recipe.equalsIgnoreCase("lemon tart")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getLemonTartRecipe(), t2.getLemonTart(), false, "Lemon Tart");
			}
			else if(recipe.equalsIgnoreCase("wrapped chicken")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getWrappedChickenRecipe(), t2.getWrappedChicken(), false, "Wrapped Chicken");
			}
			else if(recipe.equalsIgnoreCase("mystery meat")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getMysteryMeatRecipe(), t2.getMysteryMeat(), false, "Mystery Meat");
			}
			else if(recipe.equalsIgnoreCase("tomato soup")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getTomatoSoupRecipe(), t2.getTomatoSoup(), false, "Tomato Soup");
			}
			else if(recipe.equalsIgnoreCase("corn on the cob")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getCornOnTheCobRecipe(), t2.getCornOnTheCob(), false, "Corn on the Cob");
			}
			else if(recipe.equalsIgnoreCase("bread pudding")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getBreadPuddingRecipe(), t2.getBreadPudding(), false, "Bread Pudding");
			}
			else if(recipe.equalsIgnoreCase("chicken pasta")) {
				culinarianUtils.craftRecipe(p, econ, amount, t2.getChickenPastaRecipe(), t2.getChickenPasta(), false, "Chicken Pasta");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier3(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.4")) {
			if(recipe.equalsIgnoreCase("garden salad")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getGardenSaladRecipe(), t3.getGardenSalad(), false, "Garden Salad");
			}
			else if(recipe.equalsIgnoreCase("chicken leg")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getChickenLegRecipe(), t3.getChickenLeg(), false, "Chicken Leg");
			}
			else if(recipe.equalsIgnoreCase("apple porkchops")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getApplePorkchopsRecipe(), t3.getApplePorkchops(), false, "Apple Porkchops");
			}
			else if(recipe.equalsIgnoreCase("scrambled eggs")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getScrambledEggsRecipe(), t3.getScrambledEggs(), false, "Scrambled Eggs");
			}
			else if(recipe.equalsIgnoreCase("lamb kabob")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getLambKabobRecipe(), t3.getLambKabob(), false, "Lamb Kabob");
			}
			else if(recipe.equalsIgnoreCase("zesty steak")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getZestySteakRecipe(), t3.getZestySteak(), false, "Zesty Steak");
			}
			else if(recipe.equalsIgnoreCase("loaded baked potato")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getLoadedBakedPotatoRecipe(), t3.getLoadedBakedPotato(), false, "Loaded Baked Potato");
			}
			else if(recipe.equalsIgnoreCase("sweet buns")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getSweetBunsRecipe(), t3.getSweetBuns(), false, "Sweet Buns");
			}
			else if(recipe.equalsIgnoreCase("spaghetti bolognese")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getSpaghettiBologneseRecipe(), t3.getSpaghettiBolognese(), false, "Spaghetti Bolognese");
			}
			else if(recipe.equalsIgnoreCase("filleted salmon")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getFilletedSalmonRecipe(), t3.getFilletedSalmon(), false, "Filleted Salmon");
			}
			else if(recipe.equalsIgnoreCase("roast bream")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getRoastBreamRecipe(), t3.getRoastBream(), false, "Roast Bream");
			}
			else if(recipe.equalsIgnoreCase("smoothie")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getSmoothieRecipe(), t3.getSmoothie(), false, "Smoothie");
			}
			else if(recipe.equalsIgnoreCase("hearty burrito")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getHeartyBurritoRecipe(), t3.getHeartyBurrito(), false, "Hearty Burrito");
			}
			else if(recipe.equalsIgnoreCase("hero sandwich")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getHeroSandwichRecipe(), t3.getHeroSandwich(), false, "Hero Sandwich");
			}
			else if(recipe.equalsIgnoreCase("lamb stew")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getLambStewRecipe(), t3.getLambStew(), false, "Lamb Stew");
			}
			else if(recipe.equalsIgnoreCase("honey glazed chicken")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getHoneyGlazedChickenRecipe(), t3.getHoneyGlazedChicken(), false, "Honey Glazed Chicken");
			}
			else if(recipe.equalsIgnoreCase("apple cider")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getAppleCiderRecipe(), t3.getAppleCider(), false, "Apple Cider");
			}
			else if(recipe.equalsIgnoreCase("mushroom pasty")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getMushroomPastyRecipe(), t3.getMushroomPasty(), false, "Mushroom Pasty");
			}
			else if(recipe.equalsIgnoreCase("buttered wortes")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getButteredWortesRecipe(), t3.getButteredWortes(), false, "Buttered Wortes");
			}
			else if(recipe.equalsIgnoreCase("sponge cake")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getSpongeCakeRecipe(), t3.getSpongeCake(), false, "Sponge Cake");
			}
			else if(recipe.equalsIgnoreCase("chicken parmesan")) {
				culinarianUtils.craftRecipeMax(p, econ, t3.getChickenParmesanRecipe(), t3.getChickenParmesan(), false, "Chicken Parmesan");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseTier3(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.4")) {
			if(recipe.equalsIgnoreCase("garden salad")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getGardenSaladRecipe(), t3.getGardenSalad(), false, "Garden Salad");
			}
			else if(recipe.equalsIgnoreCase("chicken leg")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getChickenLegRecipe(), t3.getChickenLeg(), false, "Chicken Leg");
			}
			else if(recipe.equalsIgnoreCase("apple porkchops")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getApplePorkchopsRecipe(), t3.getApplePorkchops(), false, "Apple Porkchops");
			}
			else if(recipe.equalsIgnoreCase("scrambled eggs")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getScrambledEggsRecipe(), t3.getScrambledEggs(), false, "Scrambled Eggs");
			}
			else if(recipe.equalsIgnoreCase("lamb kabob")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getLambKabobRecipe(), t3.getLambKabob(), false, "Lamb Kabob");
			}
			else if(recipe.equalsIgnoreCase("zesty steak")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getZestySteakRecipe(), t3.getZestySteak(), false, "Zesty Steak");
			}
			else if(recipe.equalsIgnoreCase("loaded baked potato")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getLoadedBakedPotatoRecipe(), t3.getLoadedBakedPotato(), false, "Loaded Baked Potato");
			}
			else if(recipe.equalsIgnoreCase("sweet buns")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getSweetBunsRecipe(), t3.getSweetBuns(), false, "Sweet Buns");
			}
			else if(recipe.equalsIgnoreCase("spaghetti bolognese")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getSpaghettiBologneseRecipe(), t3.getSpaghettiBolognese(), false, "Spaghetti Bolognese");
			}
			else if(recipe.equalsIgnoreCase("filleted salmon")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getFilletedSalmonRecipe(), t3.getFilletedSalmon(), false, "Filleted Salmon");
			}
			else if(recipe.equalsIgnoreCase("roast bream")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getRoastBreamRecipe(), t3.getRoastBream(), false, "Roast Bream");
			}
			else if(recipe.equalsIgnoreCase("smoothie")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getSmoothieRecipe(), t3.getSmoothie(), false, "Smoothie");
			}
			else if(recipe.equalsIgnoreCase("hearty burrito")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getHeartyBurritoRecipe(), t3.getHeartyBurrito(), false, "Hearty Burrito");
			}
			else if(recipe.equalsIgnoreCase("hero sandwich")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getHeroSandwichRecipe(), t3.getHeroSandwich(), false, "Hero Sandwich");
			}
			else if(recipe.equalsIgnoreCase("lamb stew")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getLambStewRecipe(), t3.getLambStew(), false, "Lamb Stew");
			}
			else if(recipe.equalsIgnoreCase("honey glazed chicken")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getHoneyGlazedChickenRecipe(), t3.getHoneyGlazedChicken(), false, "Honey Glazed Chicken");
			}
			else if(recipe.equalsIgnoreCase("apple cider")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getAppleCiderRecipe(), t3.getAppleCider(), false, "Apple Cider");
			}
			else if(recipe.equalsIgnoreCase("mushroom pasty")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getMushroomPastyRecipe(), t3.getMushroomPasty(), false, "Mushroom Pasty");
			}
			else if(recipe.equalsIgnoreCase("buttered wortes")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getButteredWortesRecipe(), t3.getButteredWortes(), false, "Buttered Wortes");
			}
			else if(recipe.equalsIgnoreCase("sponge cake")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getSpongeCakeRecipe(), t3.getSpongeCake(), false, "Sponge Cake");
			}
			else if(recipe.equalsIgnoreCase("chicken parmesan")) {
				culinarianUtils.craftRecipe(p, econ, amount, t3.getChickenParmesanRecipe(), t3.getChickenParmesan(), false, "Chicken Parmesan");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseLimitedEdition(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.5")) {
			if(recipe.equalsIgnoreCase("candy corn")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getCandyCornRecipe(), ler.getCandyCorn(), false, "Candy Corn");
			}
			else if(recipe.equalsIgnoreCase("witch's brew")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getWitchBrewRecipe(), ler.getWitchBrew(), false, "Witch's Brew");
			}
			else if(recipe.equalsIgnoreCase("baked eyeball")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getBakedEyeballRecipe(), ler.getBakedEyeball(), true, "Baked Eyeball");
			}
			else if(recipe.equalsIgnoreCase("candy cane")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getCandyCaneRecipe(), ler.getCandyCane(), false, "Candy Cane");
			}
			else if(recipe.equalsIgnoreCase("gingerbread")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getGingerbreadRecipe(), ler.getGingerbread(), false, "Gingerbread");
			}
			else if(recipe.equalsIgnoreCase("smoked ham")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getSmokedHamRecipe(), ler.getSmokedHam(), true, "Smoked Ham");
			}
			else if(recipe.equalsIgnoreCase("eggnog")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getEggnogRecipe(), ler.getEggnog(), false, "Eggnog");
			}
			else if(recipe.equalsIgnoreCase("lasagna")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getLasagnaRecipe(), ler.getLasagna(), false, "Lasagna");
			}
			else if(recipe.equalsIgnoreCase("lemon soda")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getLemonSodaRecipe(), ler.getLemonSoda(), false, "Lemon Soda");
			}
			else if(recipe.equalsIgnoreCase("olympian gyro")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getOlympianGyroRecipe(), ler.getOlympianGyro(), false, "Olympian Gyro");
			}
			else if(recipe.equalsIgnoreCase("cupcake")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getCupcakeRecipe(), ler.getCupcake(), false, "Cupcake");
			}
			else if(recipe.equalsIgnoreCase("fish and chips")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getFishAndChipsRecipe(), ler.getFishAndChips(), false, "Fish and Chips");
			}
			else if(recipe.equalsIgnoreCase("escargot")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getEscargotRecipe(), ler.getEscargot(), false, "Escargot");
			}
			else if(recipe.equalsIgnoreCase("vitalac")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getVitalacRecipe(), ler.getVitalac(), false, "Vitalac");
			}
			else if(recipe.equalsIgnoreCase("belgian waffle")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getBelgianWaffleRecipe(), ler.getBelgianWaffle(), false, "Belgian Waffle");
			}
			else if(recipe.equalsIgnoreCase("violet's dirt pie")) {
				culinarianUtils.craftRecipeMax(p, econ, ler.getVioletDirtPieRecipe(), ler.getVioletDirtPie(), false, "Violet's Dirt Pie");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseLimitedEdition(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.5")) {
			if(recipe.equalsIgnoreCase("candy corn")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getCandyCornRecipe(), ler.getCandyCorn(), false, "Candy Corn");
			}
			else if(recipe.equalsIgnoreCase("witch's brew")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getWitchBrewRecipe(), ler.getWitchBrew(), false, "Witch's Brew");
			}
			else if(recipe.equalsIgnoreCase("baked eyeball")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getBakedEyeballRecipe(), ler.getBakedEyeball(), true, "Baked Eyeball");
			}
			else if(recipe.equalsIgnoreCase("candy cane")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getCandyCaneRecipe(), ler.getCandyCane(), false, "Candy Cane");
			}
			else if(recipe.equalsIgnoreCase("gingerbread")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getGingerbreadRecipe(), ler.getGingerbread(), false, "Gingerbread");
			}
			else if(recipe.equalsIgnoreCase("smoked ham")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getSmokedHamRecipe(), ler.getSmokedHam(), true, "Smoked Ham");
			}
			else if(recipe.equalsIgnoreCase("eggnog")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getEggnogRecipe(), ler.getEggnog(), false, "Eggnog");
			}
			else if(recipe.equalsIgnoreCase("lasagna")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getLasagnaRecipe(), ler.getLasagna(), false, "Lasagna");
			}
			else if(recipe.equalsIgnoreCase("lemon soda")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getLemonSodaRecipe(), ler.getLemonSoda(), false, "Lemon Soda");
			}
			else if(recipe.equalsIgnoreCase("olympian gyro")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getOlympianGyroRecipe(), ler.getOlympianGyro(), false, "Olympian Gyro");
			}
			else if(recipe.equalsIgnoreCase("cupcake")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getCupcakeRecipe(), ler.getCupcake(), false, "Cupcake");
			}
			else if(recipe.equalsIgnoreCase("fish and chips")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getFishAndChipsRecipe(), ler.getFishAndChips(), false, "Fish and Chips");
			}
			else if(recipe.equalsIgnoreCase("escargot")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getEscargotRecipe(), ler.getEscargot(), false, "Escargot");
			}
			else if(recipe.equalsIgnoreCase("vitalac")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getVitalacRecipe(), ler.getVitalac(), false, "Vitalac");
			}
			else if(recipe.equalsIgnoreCase("belgian waffle")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getBelgianWaffleRecipe(), ler.getBelgianWaffle(), false, "Belgian Waffle");
			}
			else if(recipe.equalsIgnoreCase("violet's dirt pie")) {
				culinarianUtils.craftRecipe(p, econ, amount, ler.getVioletDirtPieRecipe(), ler.getVioletDirtPie(), false, "Violet's Dirt Pie");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseLegendary(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.5")) {
			if(recipe.equalsIgnoreCase("dragon scrambled eggs")) {
				culinarianUtils.craftRecipeMax(p, econ, legend.getDragonScrambledEggsRecipe(), legend.getDragonScrambledEggs(), false, "Dragon Scrambled Eggs");
			}
			else if(recipe.equalsIgnoreCase("neo's full course special")) {
				culinarianUtils.craftRecipeMax(p, econ, legend.getNeoFullCourseSpecialRecipe(), legend.getNeoFullCourseSpecial(), false, "Neo's Full Course Special");
			}
			else if(recipe.equalsIgnoreCase("tobias' famous cake")) {
				culinarianUtils.craftRecipeMax(p, econ, legend.getTobiasFamousCakeRecipe(), legend.getTobiasFamousCake(), false, "Tobias' Famous Cake");
			}
			else if(recipe.equalsIgnoreCase("low district cheese steak")) {
				culinarianUtils.craftRecipeMax(p, econ, legend.getLowDistrictCheeseSteakRecipe(), legend.getLowDistrictCheeseSteak(), false, "Low District Cheese Steak");
			}
			else if(recipe.equalsIgnoreCase("mattifornia roll")) {
				culinarianUtils.craftRecipeMax(p, econ, legend.getMattiforniaRollRecipe(), legend.getMattiforniaRoll(), false, "Mattifornia Roll");
			}
			else if(recipe.equalsIgnoreCase("tilan's salad")) {
				culinarianUtils.craftRecipeMax(p, econ, legend.getTilanSaladRecipe(), legend.getTilanSalad(), false, "Tilan's Salad");
			}
			else if(recipe.equalsIgnoreCase("super's sundae")) {
				culinarianUtils.craftRecipeMax(p, econ, legend.getSuperSundaeRecipe(), legend.getSuperSundae(), false, "Super's Sundae");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseLegendary(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.5")) {
			if(recipe.equalsIgnoreCase("dragon scrambled eggs")) {
				culinarianUtils.craftRecipe(p, econ, amount, legend.getDragonScrambledEggsRecipe(), legend.getDragonScrambledEggs(), false, "Dragon Scrambled Eggs");
			}
			else if(recipe.equalsIgnoreCase("neo's full course special")) {
				culinarianUtils.craftRecipe(p, econ, amount, legend.getNeoFullCourseSpecialRecipe(), legend.getNeoFullCourseSpecial(), false, "Neo's Full Course Special");
			}
			else if(recipe.equalsIgnoreCase("tobias' famous cake")) {
				culinarianUtils.craftRecipe(p, econ, amount, legend.getTobiasFamousCakeRecipe(), legend.getTobiasFamousCake(), false, "Tobias' Famous Cake");
			}
			else if(recipe.equalsIgnoreCase("low district cheese steak")) {
				culinarianUtils.craftRecipe(p, econ, amount, legend.getLowDistrictCheeseSteakRecipe(), legend.getLowDistrictCheeseSteak(), false, "Low District Cheese Steak");
			}
			else if(recipe.equalsIgnoreCase("mattifornia roll")) {
				culinarianUtils.craftRecipe(p, econ, amount, legend.getMattiforniaRollRecipe(), legend.getMattiforniaRoll(), false, "Mattifornia Roll");
			}
			else if(recipe.equalsIgnoreCase("tilan's salad")) {
				culinarianUtils.craftRecipe(p, econ, amount, legend.getTilanSaladRecipe(), legend.getTilanSalad(), false, "Tilan's Salad");
			}
			else if(recipe.equalsIgnoreCase("super's sundae")) {
				culinarianUtils.craftRecipe(p, econ, amount, legend.getSuperSundaeRecipe(), legend.getSuperSundae(), false, "Super's Sundae");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseDrink(Player p, String recipe) {
		if(p.hasPermission("culinarian.craft.4")) {
			if(recipe.equalsIgnoreCase("black widow")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getBlackWidowRecipe(), drink.getBlackWidow(), true, "Black Widow");
			}
			else if(recipe.equalsIgnoreCase("pink panther")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getPinkPantherRecipe(), drink.getPinkPanther(), false, "Pink Panther");
			}
			else if(recipe.equalsIgnoreCase("midnight kiss")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getMidnightKissRecipe(), drink.getMidnightKiss(), false, "Midnight Kiss");
			}
			else if(recipe.equalsIgnoreCase("midnight blue")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getMidnightBlueRecipe(), drink.getMidnightBlue(), false, "Midnight Blue");
			}
			else if(recipe.equalsIgnoreCase("good and evil")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getGoodAndEvilRecipe(), drink.getGoodAndEvil(), false, "Good and Evil");
			}
			else if(recipe.equalsIgnoreCase("thor's hammer")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getThorHammerRecipe(), drink.getThorHammer(), false, "Thor's Hammer");
			}
			else if(recipe.equalsIgnoreCase("jack frost")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getJackFrostRecipe(), drink.getJackFrost(), false, "Jack Frost");
			}
			else if(recipe.equalsIgnoreCase("white russian")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getWhiteRussianRecipe(), drink.getWhiteRussian(), false, "White Russian");
			}
			else if(recipe.equalsIgnoreCase("swamp water")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getSwampWaterRecipe(), drink.getSwampWater(), false, "Swamp Water");
			}
			else if(recipe.equalsIgnoreCase("blue motorcycle")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getBlueMotorcycleRecipe(), drink.getBlueMotorcycle(), false, "Blue Motorcycle");
			}
			else if(recipe.equalsIgnoreCase("red death")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getRedDeathRecipe(), drink.getRedDeath(), false, "Red Death");
			}
			else if(recipe.equalsIgnoreCase("bombsicle")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getBombsicleRecipe(), drink.getBombsicle(), false, "Bombsicle");
			}
			else if(recipe.equalsIgnoreCase("sweet tart")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getSweetTartRecipe(), drink.getSweetTart(), false, "Sweet Tart");
			}
			else if(recipe.equalsIgnoreCase("pina colada")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getPinaColadaRecipe(), drink.getPinaColada(), false, "Pina Colada");
			}
			else if(recipe.equalsIgnoreCase("margarita on the rocks")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getMargaritaOnTheRocksRecipe(), drink.getMargaritaOnTheRocks(), false, "Margarita on the Rocks");
			}
			else if(recipe.equalsIgnoreCase("bloody mary")) {
				culinarianUtils.craftRecipeMax(p, econ, drink.getBloodyMaryRecipe(), drink.getBloodyMary(), false, "Bloody Marry");
			}
		}
		else {
			util.sendMessage(p, "&cYou do not yet have the required skill!");
		}
	}
	
	public void parseDrink(Player p, String recipe, int amount) {
		if(p.hasPermission("culinarian.craft.4")) {
			if(recipe.equalsIgnoreCase("black widow")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getBlackWidowRecipe(), drink.getBlackWidow(), true, "Black Widow");
			}
			else if(recipe.equalsIgnoreCase("pink panther")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getPinkPantherRecipe(), drink.getPinkPanther(), false, "Pink Panther");
			}
			else if(recipe.equalsIgnoreCase("midnight kiss")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getMidnightKissRecipe(), drink.getMidnightKiss(), false, "Midnight Kiss");
			}
			else if(recipe.equalsIgnoreCase("midnight blue")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getMidnightBlueRecipe(), drink.getMidnightBlue(), false, "Midnight Blue");
			}
			else if(recipe.equalsIgnoreCase("good and evil")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getGoodAndEvilRecipe(), drink.getGoodAndEvil(), false, "Good and Evil");
			}
			else if(recipe.equalsIgnoreCase("thor's hammer")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getThorHammerRecipe(), drink.getThorHammer(), false, "Thor's Hammer");
			}
			else if(recipe.equalsIgnoreCase("jack frost")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getJackFrostRecipe(), drink.getJackFrost(), false, "Jack Frost");
			}
			else if(recipe.equalsIgnoreCase("white russian")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getWhiteRussianRecipe(), drink.getWhiteRussian(), false, "White Russian");
			}
			else if(recipe.equalsIgnoreCase("swamp water")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getSwampWaterRecipe(), drink.getSwampWater(), false, "Swamp Water");
			}
			else if(recipe.equalsIgnoreCase("blue motorcycle")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getBlueMotorcycleRecipe(), drink.getBlueMotorcycle(), false, "Blue Motorcycle");
			}
			else if(recipe.equalsIgnoreCase("red death")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getRedDeathRecipe(), drink.getRedDeath(), false, "Red Death");
			}
			else if(recipe.equalsIgnoreCase("bombsicle")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getBombsicleRecipe(), drink.getBombsicle(), false, "Bombsicle");
			}
			else if(recipe.equalsIgnoreCase("sweet tart")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getSweetTartRecipe(), drink.getSweetTart(), false, "Sweet Tart");
			}
			else if(recipe.equalsIgnoreCase("pina colada")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getPinaColadaRecipe(), drink.getPinaColada(), false, "Pina Colada");
			}
			else if(recipe.equalsIgnoreCase("margarita on the rocks")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getMargaritaOnTheRocksRecipe(), drink.getMargaritaOnTheRocks(), false, "Margarita on the Rocks");
			}
			else if(recipe.equalsIgnoreCase("bloody mary")) {
				culinarianUtils.craftRecipe(p, econ, amount, drink.getBloodyMaryRecipe(), drink.getBloodyMary(), false, "Bloody Marry");
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
