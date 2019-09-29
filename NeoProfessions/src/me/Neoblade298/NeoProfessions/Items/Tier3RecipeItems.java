package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.SkullCreator;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class Tier3RecipeItems {
	Util util;
	IngredientRecipeItems ingr;
	
	String BASE64_R1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWZlOTJlMTFhNjdiNTY5MzU0NDZhMjE0Y2FhMzcyM2QyOWU2ZGI1NmM1NWZhOGQ0MzE3OWE4YTMxNzZjNmMxIn19fQ";
	String BASE64_R2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjhmYjA1MDJhM2FhNWY4YmQzMmE1ZWE1ZTUxOWMzZGQzNTMyMzQxNzBkZmVmOTU5ZWU4YWRiOTQ4N2ZlYSJ9fX0";
	String BASE64_R3 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I4MTcyYzJkZmUzZjk0ZTkyZWVhZjcyZjc4ZmNmNjM0ZTk5NjY1N2ZmNzRkNzNkOTNlNTc4MTUyOGI0NTgzIn19fQ";
	String BASE64_R4 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjFlZTYzMjVjZmNjMDVhNjBlNjFiNjQwNmI4YjdkZTE0NmFjOTNhOTA0YmI0YzRlZDcyNWZjZTc3ZTUzM2UwOCJ9fX0";
	String BASE64_R5 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTIxMDdkMjg0ODM1ZmRmOTU4MzU5YjQ4NzFmMmM1OTNlNzJkNzdhNGNjZDk2NTdjY2Y2NDJmOTUzYjdiMzUifX19";
	String BASE64_R6 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNzhjZmQwNTg4YThhOTNiOGNkMjFiZGQyY2UxNjU0ODljYjM5Mzk0ODcxNGZkZDg1ZmIxMGU0NGQ0ODg2ZjYifX19";
	String BASE64_R7 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTMzZmE3ZDNlNjNiMjgwYTVkN2UyYmIwOTMzMmRmZjg2YjE3ZGVjZDJiMDllY2NkZDYyZGE1MjY1NTk3Zjc0ZCJ9fX0";
	String BASE64_R8 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWIzYzdkOGQ4OWRhZTE2YTI5ZWVhMWE0Mzk4ZWVlYmFiZmFmYmYwMjhkNmUyNWVjYWU5NzdlZmUzMGM4In19fQ";
	String BASE64_R9 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjIzZGZkNGNlNTc3ODY5MTQ5NjhhMmIzYmE4MjE3M2JhN2M2Y2I4YmMwODQ0MmQ0MTMwY2IwOWM0YTAwMDk4MCJ9fX0";
	String BASE64_R10 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzVjYzc3ODE5Yzk5M2MxOTZjM2E1NzhkZGNlMGVmNmIyMGVhMTc1ZDk5ZTYxNTRmNmI1NTRjMTFiODRmZDU0MiJ9fX0";
	String BASE64_R11 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmVkMjljMGI0ZjE3NWI2NDBhZmNhYjQ0NWJkMTI5YzhmYjhiYTdjNDY1MTJjYTU2M2YxMDExOTU5MzJhZDdmNCJ9fX0";
	String BASE64_R12 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTRkMjFkYjRkZGU5MmExZDFmNjg5MGJmNzhhN2UxNDIxY2Y5NDAxNzkyMTAzMGQxMjM5NjdiZmQ2ZGNkMWFlYSJ9fX0";
	String BASE64_R13 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTM4N2E2MjFlMjY2MTg2ZTYwNjgzMzkyZWIyNzRlYmIyMjViMDQ4NjhhYjk1OTE3N2Q5ZGMxODFkOGYyODYifX19";
	String BASE64_R14 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTQ5NjU4OWZiNWMxZjY5Mzg3YjdmYjE3ZDkyMzEyMDU4ZmY2ZThlYmViM2ViODllNGY3M2U3ODE5NjExM2IifX19";
	String BASE64_R15 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWU5ZTQwZjJkOGZjZTVmOThhNmEwNDk3MjNhNDAzMDI2NjU1MTNmZTdlMTA0MWVmZGZlZmZlZDgyMjc3Yzc3YSJ9fX0";
	String BASE64_R16 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjA2NTU1NzA2YjY0MWZkYWY0MzZjMDc2NjNmOTIzYWZjNWVlNzIxNDZmOTAxOTVmYjMzN2I5ZGU3NjY1ODhkIn19fQ";
	String BASE64_R17 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmViZjUxYTNhNTJiNzQ3MmEyODVjNjU4Mjg0Njg4YmNiZTU3Y2Q5ZjZmYWE3YTNlNGMyNmE2MTA1MjU0In19fQ";
	String BASE64_R18 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTIxOGM0YTAwMDQ3YmJmZDdmMzVhNzRjYTNkOTQ4OTgyY2NhZWNkMjhjMjcwOGMyZGFjMDcwZDNlYmYxZTRhYyJ9fX0";
	String BASE64_R19 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2RjM2MzNTEyZWZlY2FkOWQ2NTdiOGNlNTM1YWM2YWU3NWRmNzc4OTQ5YmM2MzU5ODdjZDI4MjY2ZDIxMyJ9fX0";
	String BASE64_R20 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmVkODcwNDYxMGFlYzI5ZmVmOTc5ZWM3ZmNmNTEzZTE3YTczN2U1MWE2MWIyYjI2ZDc0YTVmZGM1ZGE3OSJ9fX0";
	String BASE64_R21 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDNlMjBhMjZjYmI1NzQwYTE1OGRhOTkxZWY5NGRjZDMyZDQ0N2U5YWMwM2FhMGU4ZjgyOWE0OTgzMDYxOWExMCJ9fX0";
	
	public Tier3RecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
	}
	
	public ArrayList<ItemStack> getTier3Recipes() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getGardenSalad());
		list.add(getChickenLeg());
		list.add(getApplePorkchops());
		list.add(getScrambledEggs());
		list.add(getLambKabob());
		list.add(getZestySteak());
		list.add(getLoadedBakedPotato());
		list.add(getSweetBuns());
		list.add(getSpaghettiBolognese());
		list.add(getFilletedSalmon());
		list.add(getRoastBream());
		list.add(getSmoothie());
		list.add(getHeartyBurrito());
		list.add(getHeroSandwich());
		list.add(getLambStew());
		list.add(getHoneyGlazedChicken());
		list.add(getAppleCider());
		list.add(getMushroomPasty());
		list.add(getButteredWortes());
		list.add(getSpongeCake());
		list.add(getChickenParmesan());
		return list;
	}
	
	public ItemStack getGardenSalad() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Garden Salad");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChickenLeg() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R2);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chicken Leg");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getApplePorkchops() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R3);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Apple Porkchops");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getScrambledEggs() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Scrambled Eggs");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLambKabob() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lamb Kabob");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getZestySteak() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R6);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Zesty Steak");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLoadedBakedPotato() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R7);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Loaded Baked Potato");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSweetBuns() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R8);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sweet Buns");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSpaghettiBolognese() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R9);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Spaghetti Bolognese");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getFilletedSalmon() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R10);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Filleted Salmon");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getRoastBream() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R11);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Roast Bream");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSmoothie() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R12);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Smoothie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHeartyBurrito() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R13);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hearty Burrito");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHeroSandwich() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hero Sandwich");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLambStew() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R15);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lamb Stew");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHoneyGlazedChicken() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R16);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Honey Glazed Chicken");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getAppleCider() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R17);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Apple Cider");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMushroomPasty() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R18);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mushroom Pasty");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getButteredWortes() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R19);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Buttered Wortes");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSpongeCake() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R20);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sponge Cake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChickenParmesan() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R21);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chicken Parmesan");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 3 Recipe 21");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getGardenSaladRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(util.setAmount(ingr.getGreens(), 3));
		recipe.add(util.setAmount(ingr.getTomato(), 2));
		recipe.add(ingr.getCorn());
		return recipe;
	}
	
	public ArrayList<ItemStack> getChickenLegRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN, 2));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getOil());
		return recipe;
	}
	
	public ArrayList<ItemStack> getApplePorkchopsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_PORKCHOP, 2));
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getScrambledEggsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.EGG, 3));
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		recipe.add(ingr.getOil());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLambKabobRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.STICK));
		recipe.add(new ItemStack(Material.COOKED_MUTTON, 2));
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		recipe.add(ingr.getOil());
		recipe.add(ingr.getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getZestySteakRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(ingr.getBeetrootSauce());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLoadedBakedPotatoRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BAKED_POTATO));
		recipe.add(new ItemStack(Material.COOKED_PORKCHOP));
		recipe.add(util.setAmount(ingr.getButter(), 2));
		recipe.add(ingr.getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSweetBunsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT, 3));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.SUGAR, 3));
		recipe.add(ingr.getYeast());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSpaghettiBologneseRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(util.setAmount(ingr.getPasta(), 3));
		recipe.add(util.setAmount(ingr.getTomato(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getFilletedSalmonRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_SALMON));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getOnion());
		recipe.add(ingr.getButter());
		return recipe;
	}
	
	public ArrayList<ItemStack> getRoastBreamRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_COD));
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		recipe.add(ingr.getButter());
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getOnion());
		recipe.add(ingr.getLemon());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSmoothieRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.APPLE));
		recipe.add(new ItemStack(Material.MELON));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(new ItemStack(Material.ICE));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(ingr.getLemon());
		recipe.add(ingr.getExoticGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHeartyBurritoRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(new ItemStack(Material.POTATO));
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getBeetrootSauce());
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getTortilla());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHeroSandwichRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_PORKCHOP));
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getToast());
		recipe.add(ingr.getTomato());
		recipe.add(ingr.getGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLambStewRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.POTATO));
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(new ItemStack(Material.COOKED_MUTTON));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getHops());
		recipe.add(ingr.getGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHoneyGlazedChickenRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(util.setAmount(ingr.getHoney(), 2));
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getPepper());
		return recipe;
	}
	
	public ArrayList<ItemStack> getAppleCiderRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.APPLE, 3));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMushroomPastyRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.RED_MUSHROOM));
		recipe.add(ingr.getOil());
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getButteredWortesRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(util.setAmount(ingr.getGreens(), 2));
		recipe.add(util.setAmount(ingr.getButter(), 2));
		recipe.add(ingr.getOnion());
		recipe.add(ingr.getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSpongeCakeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(new ItemStack(Material.WHEAT, 3));
		recipe.add(ingr.getYeast());
		recipe.add(ingr.getButter());
		return recipe;
	}
	
	public ArrayList<ItemStack> getChickenParmesanRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(util.setAmount(ingr.getTomato(), 2));
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getSpices());
		return recipe;
	}
}
