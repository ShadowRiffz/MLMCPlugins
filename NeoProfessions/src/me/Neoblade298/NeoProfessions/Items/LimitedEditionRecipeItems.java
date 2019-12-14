package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.SkullCreator;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class LimitedEditionRecipeItems {
	Util util;
	IngredientRecipeItems ingr;
	
	String BASE64_R1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmQ4Njc2N2Q0ZjFjYTI4NjhiNjc1Njk2YzcyMTc4Y2Q3NGM0ZWI2YzI4ZjNjN2FmYzgyZWRkMDkwY2EyY2E0NiJ9fX0";
	String BASE64_R2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWYxOGI5YTQzNmEyN2Y4MTNjMjg3ZWI2NzM3OWVmOGFkYmZkYzcwYWZhZjMwNGM0M2IxNjZjZTk4NmRkOCJ9fX0";
	String BASE64_R3 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGNlM2IxZjI2ZjY1MjVhZjgyZjViYTE0ZGJiOTNiNWU3YTU0M2Q0NzdmYThkYTEyM2RiZmFhNmQyMjlmZGE0ZiJ9fX0";
	String BASE64_R4 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmIyMTYxN2QyNzU1YmMyMGY4ZjdlMzg4ZjQ5ZTQ4NTgyNzQ1ZmVjMTZiYjE0Yzc3NmY3MTE4Zjk4YzU1ZTgifX19";
	String BASE64_R5 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTc5YzkzMmMzMWUxNmZkNjVjZTNjOTljY2E5ODY0NWFiMmYxNmIyNjIzYjVlMWU3MmM2ZGU2ODlhNjUxODdmIn19fQ";
	String BASE64_R6 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMWUyNGI4MWUzMzgwZGZkNDAzZTM4MzI0YmM5NzNlNDgxYmI3MGIzNDgyODUxZGI4MTZjYjdhMmM1YTU1NTA1ZSJ9fX0";
	String BASE64_R7 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzdjN2IyYzFmMjhkMWE0YzRlOGU0MjZjOTE3ZjQ4NGIxM2NmODAyMzdiZGFhMDc3YWJiNDFkNGM0MTBhYjU4NCJ9fX0";
	String BASE64_R8 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNThkNTgzZDNmNmExMjQ3NjM1OWVmODI4OGI2MGJhMzU1ZTMyYTNhNGQ5N2JjODUyMzIzY2U3NGM4OWU0NTE0MyJ9fX0";
	String BASE64_R9 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODY5NTZmZjE3ZjNhYjUyZWQ5N2QzNmUxNmFlOWI0YTU1NjM5ODJiNzgyNWMzNTYyNzhjMWZmMGEyZWU2NTZhIn19fQ";
	String BASE64_R10 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGYyODVhNGRkM2Q2YTRkOGE1NzMxNjU4NjhjMzFmOWI0YzYzZjk5M2Y1NzlkYzFmYzg2ODg0ZjY5NGM2OGIxIn19fQ";
	String BASE64_R11 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDE5MzVjMTcyYTg2MWMxYzc0ZTZlMWM4MGViYjIyNWJjMWIzMzg1Mzk4Mzc5YzY1MmRhODM4MzI2OGVkNGM4In19fQ";
	String BASE64_R12 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjJhYjczODQxZGM0YTFkMThkZWFkNTY1YjUwNmNlM2EwYmIwNzlmMjhiYzM5NDhmZWMwMTljYmI4OGEzMDllZSJ9fX0";
	String BASE64_R13 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmE4Y2JlNjAxNDUwYTI1MWNmMzRiMDNiZjFjYjBiYWIxMWJmNTdkMWQ5NzkyZmE4ZTA1MGRlYjllNzJjNmM5In19fQ";
	String BASE64_R14 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjdmOGU3NDY2NTgzMzE2YThhYTU1ZjZlMTgzMzQ1YTRjNGU5ZjEwYjlkODdmZDExNzU3OTVkMjZiOWIzNzI4In19fQ";
	String BASE64_R15 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjUyYzNmZDBmYWM2NGI5MzRjMTM0YTA2ZTY4NGZiMDQ3MmExNThhYWY2Nzk5NTMyMjdlNDc4YWYyZjgwMDQ1OCJ9fX0";
	String BASE64_R16 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjAzMjM5ZjcwYTNjMjUzNThiMjJhMjliZTFiZmQxYjk4NWVlOTVmMjg2MzQ4YjRlMGYyMmYzNDc0MDk4MjViOCJ9fX0";

	public LimitedEditionRecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
	}
	
	public ArrayList<ItemStack> getLimitedEditionRecipes() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getCandyCorn());
		list.add(getWitchBrew());
		list.add(getBakedEyeball());
		list.add(getCandyCane());
		list.add(getGingerbread());
		list.add(getEggnog());
		list.add(getSmokedHam());
		list.add(getLasagna());
		list.add(getLemonSoda());
		list.add(getOlympianGyro());
		list.add(getCupcake());
		list.add(getFishAndChips());
		list.add(getEscargot());
		list.add(getVitalac());
		list.add(getBelgianWaffle());
		list.add(getVioletDirtPie());
		return list;
	}
	public ItemStack getCandyCorn() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candy Corn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getWitchBrew() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R2);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Witch's Brew");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getBakedEyeball() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R3);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Baked Eyeball");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getCandyCane() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candy Cane");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getGingerbread() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Gingerbread");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getEggnog() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R6);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Eggnog");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getSmokedHam() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R7);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Smoked Ham");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getLasagna() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R8);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lasagna");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getLemonSoda() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R9);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lemon Soda");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getOlympianGyro() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R10);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Olympian Gyro");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getCupcake() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R11);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Cupcake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getFishAndChips() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R12);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Fish And Chips");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getEscargot() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R13);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Escargot");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getVitalac() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Vitalac");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getBelgianWaffle() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R15);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Belgian Waffle");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getVioletDirtPie() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R16);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Violet's Dirt Pie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getCandyCornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR, 5));
		recipe.add(ingr.getCorn());
		return recipe;
	}
	
	public ArrayList<ItemStack> getWitchBrewRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SPIDER_EYE));
		recipe.add(new ItemStack(Material.BONE));
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(new ItemStack(Material.GLOWSTONE_DUST));
		recipe.add(new ItemStack(Material.REDSTONE));
		recipe.add(ingr.getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getBakedEyeballRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.ENDER_EYE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getCandyCaneRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.RED_DYE, 4));
		recipe.add(new ItemStack(Material.WHITE_DYE, 2));
		recipe.add(new ItemStack(Material.SUGAR, 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getGingerbreadRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSmokedHamRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_PORKCHOP));
		return recipe;
	}
	
	public ArrayList<ItemStack> getEggnogRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLasagnaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(util.setAmount(ingr.getPasta(), 4));
		recipe.add(ingr.getTomato());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLemonSodaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR, 2));
		recipe.add(new ItemStack(Material.IRON_INGOT, 4));
		recipe.add(new ItemStack(Material.PACKED_ICE));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(ingr.getLemon());
		return recipe;
	}
	
	public ArrayList<ItemStack> getOlympianGyroRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD));
		recipe.add(new ItemStack(Material.COOKED_MUTTON));
		recipe.add(ingr.getBeetrootSauce());
		recipe.add(ingr.getOnion());
		recipe.add(ingr.getTomato());
		return recipe;
	}
	
	public ArrayList<ItemStack> getCupcakeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.WHEAT, 2));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(ingr.getOil());
		recipe.add(ingr.getButter());
		return recipe;
	}
	
	public ArrayList<ItemStack> getFishAndChipsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_COD));
		recipe.add(new ItemStack(Material.POTATO));
		recipe.add(ingr.getLemon());
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getOil());
		return recipe;
	}
	
	public ArrayList<ItemStack> getEscargotRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(util.setAmount(ingr.getSalt(), 4));
		recipe.add(util.setAmount(ingr.getButter(), 2));
		recipe.add(util.setAmount(ingr.getGreens(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getVitalacRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_MUTTON, 3));
		recipe.add(new ItemStack(Material.PORKCHOP, 2));
		recipe.add(util.setAmount(ingr.getSpices(), 3));
		recipe.add(ingr.getPepper());
		return recipe;
	}
	
	public ArrayList<ItemStack> getBelgianWaffleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(ingr.getYeast());
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getVioletDirtPieRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.DIRT, 3));
		recipe.add(new ItemStack(Material.PUMPKIN_PIE, 2));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(util.setAmount(ingr.getHoney(), 3));
		return recipe;
	}
}
