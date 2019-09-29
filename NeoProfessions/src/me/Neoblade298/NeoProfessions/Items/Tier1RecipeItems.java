package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.SkullCreator;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class Tier1RecipeItems {
	Util util;
	IngredientRecipeItems ingr;
	
	String BASE64_R1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzFkN2M4MTZmYzhjNjM2ZDdmNTBhOTNhMGJhN2FhZWZmMDZjOTZhNTYxNjQ1ZTllYjFiZWYzOTE2NTVjNTMxIn19fQ";
	String BASE64_R2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWYxOGI5YTQzNmEyN2Y4MTNjMjg3ZWI2NzM3OWVmOGFkYmZkYzcwYWZhZjMwNGM0M2IxNjZjZTk4NmRkOCJ9fX0";
	String BASE64_R3 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWU1OWE4MjNkNTIzYTMyNDJjNWMzODNmYjU2YmEzNzMxNjM5NDIwMmVlYmJlZThlYmI1NGY2MmRjYmFmYjJhYSJ9fX0";
	String BASE64_R4 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTBjODcxOWI2YjFmZGFlZTU5YTc4OTU1NTI2N2FkY2ZhMWU0MjYwODg5ZmRmNzA4ZTdjYTg3M2MwNTUxZDkifX19";
	String BASE64_R5 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzRhZTE5MmNlYzI4NTBiMjQ1YjgyM2ExNWNlNTVmMzMyZjA5YzQ5MWIxNWE5NjQ1Yzk4MmI4OGM1NjRkNGMyIn19fQ";
	String BASE64_R6 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDIxNjhkZmM1MTNjMzFiMWUxYzJkODUzZTgxNjkwMTUzODFjODM3OGVlZTIyNGFlNmRiYjFlMTI0ODVlOWE5OCJ9fX0";
	String BASE64_R7 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDhlOTRkZGQ3NjlhNWJlYTc0ODM3NmI0ZWM3MzgzZmQzNmQyNjc4OTRkN2MzYmVlMDExZThlNGY1ZmNkNyJ9fX0";
	String BASE64_R8 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTE3NGIzNGM1NDllZWQ4YmFmZTcyNzYxOGJhYjY4MjFhZmNiMTc4N2I1ZGVjZDFlZWNkNmMyMTNlN2U3YzZkIn19fQ";
	String BASE64_R9 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzIxNWUzZjRjYWZiNDk0NmNiOGM0ZjRiOGRjMTI0ZTMwMmU5MjdlNzBkZjJkMzQwYTIxMTU0NmFmMWFiYmEifX19";
	String BASE64_R10 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzJjMWMwMjA5YjEwZTFiOTk3YjQ2MTI4N2Y5ZDQyNmUzMTZhNGE2NjcyMjc4ZDRlNDYzYjE5OTI1ZmZmMjYifX19";
	String BASE64_R11 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjllYzlhYzQzMzE3YTg3ZjJmNDllM2MwODVjMWUzOWE0MjZkNTIyNTI2NDYzNjJiNjQ2ZGUyMjFmMWIyNzEifX19";
	String BASE64_R12 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTJiMzViZGE1ZWJkZjEzNWY0ZTcxY2U0OTcyNmZiZWM1NzM5ZjBhZGVkZjAxYzUxOWUyYWVhN2Y1MTk1MWVhMiJ9fX0";
	String BASE64_R13 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmJiZWNiYTUyMzE4MDVhYWFkZGE4MWQ3NjRiMDk2ZWVlNjJlZDJlNGNiNDQ3NDQ4NTQ0ZjUxODJiMDkxZjEwMSJ9fX0";
	String BASE64_R14 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdmOGU3NDY2NTgzMzE2YThhYTU1ZjZlMTgzMzQ1YTRjNGU5ZjEwYjlkODdmZDExNzU3OTVkMjZiOWIzNzI4In19fQ";
	String BASE64_R15 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ1NjZiYzczZGE4ZjA0YTc0YTE4MGJjM2VhMTk3ZThlNGI2YTEzZmI4ZDZhMTU2MGMwZjlhMmFjMThmNTQ4ZSJ9fX0";
	String BASE64_R16 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjk1NmNmM2E5ZWFkZGVjZmUzYTg0MmJlYzE3ZjkxNDM1YTc2ZDVhNzFlOGJmZjBlZDllYTNiM2I5OGFmYzhjIn19fQ";
	String BASE64_R17 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjM0OTkyMzg2ZWNmMWVhYzg1ZTE5ODk1NzhjNzIyZjhkNTVmZTM2OTE1YzkzOTZmNDMzMzdjZGQxNGM5MTIifX19";
	String BASE64_R18 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGFlY2U0MmQ1ZWFkZGQ2ZmM5MTU4YjY0YTgzZTc0Yjk0NTQ3ZWUxYjM2YWJjZTQ0NTFiNWM4Nzg4OWI0MTdiNSJ9fX0";
	String BASE64_R19 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzRhNzkxZTU1YjE1MDk5ZDE4M2IxOTU2MWY5NWM5Y2FlNGQyNjQ0NDgyODIxODhmMTE0YjYxZDRkM2RmMyJ9fX0";
	String BASE64_R20 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjAyMzE2MTZlYzYwMzIyY2M1MWUyZTE4NGVmMTg0ZTMzNTNmYTIxYjBhMTE0YjQyNzg0NTc0MDg5NjgxNDE1OCJ9fX0";
	
	public Tier1RecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
	}
	
	public ArrayList<ItemStack> getTier1Recipes() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getCuredFlesh());
		list.add(getSunflowerSeeds());
		list.add(getBoiledEgg());
		list.add(getIceCream());
		list.add(getHotChocolate());
		list.add(getGreenTea());
		list.add(getMashedPotatoes());
		list.add(getSpinach());
		list.add(getChocolateTruffle());
		list.add(getMusli());
		list.add(getCandiedApple());
		list.add(getMacAndCheese());
		list.add(getChocolateMilk());
		list.add(getCheeseTortilla());
		list.add(getSushi());
		list.add(getPottage());
		list.add(getButteredPopcorn());
		list.add(getChipsAndSalsa());
		list.add(getGruel());
		return list;
	}
	
	public ItemStack getCuredFlesh() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Cured Flesh");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSunflowerSeeds() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R2);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sunflower Seeds");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBoiledEgg() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R3);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Boiled Egg");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getIceCream() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Ice Cream");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHotChocolate() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hot Chocolate");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getGreenTea() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R6);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Green Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBarleyTea() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R7);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Barley Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMashedPotatoes() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R8);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mashed Potatoes");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSpinach() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R9);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Spinach");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChocolateTruffle() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R10);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chocolate Truffle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMusli() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R11);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Musli");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCandiedApple() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R12);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candied Apple");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMacAndCheese() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R13);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mac and Cheese");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChocolateMilk() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chocolate Milk");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCheeseTortilla() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R15);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Cheese Tortilla");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSushi() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R16);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sushi");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPottage() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R17);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Pottage");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getButteredPopcorn() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R18);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Buttered Popcorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChipsAndSalsa() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R19);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chips and Salsa");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getGruel() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R20);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Gruel");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 1 Recipe 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getCuredFleshRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.ROTTEN_FLESH));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSunflowerSeedsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUNFLOWER, 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBoiledEggRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.EGG));
		return recipe;
	}
	
	public ArrayList<ItemStack> getIceCreamRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.ICE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getHotChocolateRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COCOA_BEANS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getGreenTeaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.OAK_LEAVES));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBarleyTeaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.WHEAT));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMashedPotatoesRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.POTATO));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSpinachRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.VINE));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getChocolateTruffleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COCOA_BEANS));
		recipe.add(new ItemStack(Material.RED_MUSHROOM));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMusliRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT_SEEDS));
		recipe.add(new ItemStack(Material.BEETROOT_SEEDS));
		recipe.add(new ItemStack(Material.PUMPKIN_SEEDS));
		recipe.add(new ItemStack(Material.MELON_SEEDS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getCandiedAppleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getMacAndCheeseRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getPasta());
		recipe.add(ingr.getButter());
		recipe.add(ingr.getCheese());
		return recipe;
	}
	
	public ArrayList<ItemStack> getChocolateMilkRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.COCOA_BEANS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getCheeseTortillaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getCheese(), 3));
		recipe.add(ingr.getTortilla());
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSushiRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getExoticGreens(), 3));
		recipe.add(new ItemStack(Material.COD));
		recipe.add(util.setAmount(ingr.getRice(), 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPottageRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getCheese(), 3));
		recipe.add(ingr.getTortilla());
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getButteredPopcornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getPopcorn());
		recipe.add(util.setAmount(ingr.getButter(), 4));
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getChipsAndSalsaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getTortilla());
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getExoticGreens());
		recipe.add(ingr.getTomato());
		recipe.add(ingr.getPepper());
		return recipe;
	}
	
	public ArrayList<ItemStack> getGruelRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(ingr.getRice());
		return recipe;
	}
}
