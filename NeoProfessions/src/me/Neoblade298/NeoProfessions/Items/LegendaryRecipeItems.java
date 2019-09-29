package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.SkullCreator;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class LegendaryRecipeItems {
	Util util;
	IngredientRecipeItems ingr;
	Tier1RecipeItems t1;
	Tier2RecipeItems t2;
	Tier3RecipeItems t3;
	
	String BASE64_R1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFlZTYzMjVjZmNjMDVhNjBlNjFiNjQwNmI4YjdkZTE0NmFjOTNhOTA0YmI0YzRlZDcyNWZjZTc3ZTUzM2UwOCJ9fX0";
	String BASE64_R2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIxMjVkMTI5MTkwYzg4ZjhjNDRlNDJlOGM1MWFkNWVkYmU0NDVlNjc0ZDk4YWYwNjc2Yzc0NjA5NDg1YzE1In19fQ";
	String BASE64_R3 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTIxZDhkOWFlNTI3OGUyNmJjNDM5OTkyM2QyNWNjYjkxNzNlODM3NDhlOWJhZDZkZjc2MzE0YmE5NDM2OWUifX19";
	String BASE64_R4 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjJlNmFhYjgzMTFkZTFiNDdiOTQ1NWI4YjhhOGE0OWY3NDU2ODBkODI5ZjU1ODYxOTA4ODQ5ZDJhMjI1OTMifX19";
	String BASE64_R5 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTc5ZTQxYzE0NmI5NTQ5Nzg2YmRiNzEzOGYzNDlmZDdjMzk1MjkwMWY4N2NhOWMyMjU4OWNhMmFlNjQ4OCJ9fX0";
	String BASE64_R6 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlOTJlMTFhNjdiNTY5MzU0NDZhMjE0Y2FhMzcyM2QyOWU2ZGI1NmM1NWZhOGQ0MzE3OWE4YTMxNzZjNmMxIn19fQ";
	String BASE64_R7 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdlMzhiZTI4ZGZiM2Y3MDNmOTBhNDYwNzExOWZjZWY5NGJmM2UxYzgyNjIxYzI5MzA2ZjA1MzgyMTlkNGUxIn19fQ";

	public LegendaryRecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
		t1 = new Tier1RecipeItems();
		t2 = new Tier2RecipeItems();
		t3 = new Tier3RecipeItems();
	}
	
	public ItemStack getDragonScrambledEggs() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lDragon Scrambled Eggs");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getNeoFullCourseSpecial() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R2);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lNeo's Full Course Special");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTobiasFamousCake() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R3);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lTobias' Famous Cake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLowDistrictCheeseSteak() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lLow District Cheese Steak");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMattiforniaRoll() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lMattifornia Roll");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTilanSalad() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R6);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lTilan's Salad");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSuperSundae() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R7);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lSuper's Sundae");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getDragonScrambledEggsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(t3.getScrambledEggs(), 3));
		recipe.add(util.setAmount(ingr.getPepper(), 2));
		recipe.add(util.setAmount(ingr.getTomato(), 2));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getNeoFullCourseSpecialRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getBeetrootSauce(), 2));
		recipe.add(util.setAmount(ingr.getGreens(), 2));
		recipe.add(ingr.getSalt());
		recipe.add(t2.getAle());
		recipe.add(t2.getSteakWithGreenBeans());
		recipe.add(t3.getGardenSalad());
		recipe.add(t3.getLoadedBakedPotato());
		return recipe;
	}
	
	public ArrayList<ItemStack> getTobiasFamousCakeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		recipe.add(util.setAmount(t3.getSpongeCake(), 2));
		recipe.add(util.setAmount(ingr.getHoney(), 3));
		recipe.add(ingr.getYeast());
		recipe.add(t1.getIceCream());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLowDistrictCheeseSteakRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getToast(), 2));
		recipe.add(util.setAmount(ingr.getCheese(), 4));
		recipe.add(t3.getZestySteak());
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getMattiforniaRollRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COD));
		recipe.add(new ItemStack(Material.SALMON));
		recipe.add(new ItemStack(Material.PUFFERFISH));
		recipe.add(new ItemStack(Material.TROPICAL_FISH));
		recipe.add(util.setAmount(ingr.getRice(), 3));
		recipe.add(util.setAmount(ingr.getExoticGreens(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getTilanSaladRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(t1.getCuredFlesh());
		recipe.add(ingr.getTomato());
		recipe.add(ingr.getCorn());
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getExoticGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSuperSundaeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.DIAMOND));
		recipe.add(new ItemStack(Material.SUGAR, 2));
		recipe.add(new ItemStack(Material.ICE, 2));
		recipe.add(t1.getHotChocolate());
		recipe.add(t1.getIceCream());
		return recipe;
	}
}
