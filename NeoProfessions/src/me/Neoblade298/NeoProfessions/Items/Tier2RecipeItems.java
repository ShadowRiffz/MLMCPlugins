package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Utilities.SkullCreator;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class Tier2RecipeItems {
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
	
	public Tier2RecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
	}
	
	public ArrayList<ItemStack> getTier2Recipes() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getSteakWithGreenBeans());
		list.add(getLemonade());
		list.add(getHoneyedHam());
		list.add(getCandyBar());
		list.add(getAle());
		list.add(getSandwich());
		list.add(getBeefStew());
		list.add(getExoticTea());
		list.add(getBartrandCornbread());
		list.add(getTunaSandwich());
		list.add(getApplePie());
		list.add(getBeer());
		list.add(getHashBrowns());
		list.add(getLemonTart());
		list.add(getWrappedChicken());
		list.add(getMysteryMeat());
		list.add(getTomatoSoup());
		list.add(getCornOnTheCob());
		list.add(getBreadPudding());
		list.add(getChickenPasta());
		return list;
	}
	
	public ItemStack getSteakWithGreenBeans() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Steak w/ Green Beans");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getButteredToast() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R2);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Buttered Toast");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLemonade() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R3);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lemonade");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHoneyedHam() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Honeyed Ham");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCandyBar() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candy Bar");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getAle() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Ale");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSandwich() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R6);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sandwich");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBeefStew() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R7);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Beef Stew");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getExoticTea() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R8);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Exotic Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBartrandCornbread() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R9);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Bartrand's Cornbread");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTunaSandwich() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R10);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Tuna Sandwich");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getApplePie() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R11);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Apple Pie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBeer() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R12);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Beer");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHashBrowns() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R13);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hash Browns");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLemonTart() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lemon Tart");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getWrappedChicken() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R15);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Wrapped Chicken");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMysteryMeat() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R16);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mystery Meat");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTomatoSoup() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R17);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Tomato Soup");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCornOnTheCob() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R18);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Corn on the Cob");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBreadPudding() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R19);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Bread Pudding");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChickenPasta() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R20);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chicken Pasta");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 21");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getSteakWithGreenBeansRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(ingr.getGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getButteredToastRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getToast());
		return recipe;
	}
	
	public ArrayList<ItemStack> getLemonadeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(ingr.getLemon());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHoneyedHamRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_PORKCHOP));
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getCandyBarRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COCOA_BEANS));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getAleRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.RED_MUSHROOM));
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getSandwichRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD, 2));
		recipe.add(new ItemStack(Material.COOKED_PORKCHOP));
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getTomato());
		return recipe;
	}
	
	public ArrayList<ItemStack> getBeefStewRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(ingr.getSalt());
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getExoticTeaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.RED_MUSHROOM));
		recipe.add(ingr.getSpices());
		return recipe;
	}
	
	public ArrayList<ItemStack> getBartrandCornbreadRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getToast());
		recipe.add(ingr.getCorn());
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getTunaSandwichRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD, 2));
		recipe.add(new ItemStack(Material.COOKED_COD));
		recipe.add(ingr.getBeetrootSauce());
		recipe.add(ingr.getGreens());
		return recipe;
	}
	
	public ArrayList<ItemStack> getApplePieRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.APPLE, 3));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(new ItemStack(Material.SUGAR));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBeerRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(ingr.getYeast());
		recipe.add(ingr.getHops());
		return recipe;
	}
	
	public ArrayList<ItemStack> getHashBrownsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.POTATO));
		recipe.add(ingr.getBeetrootSauce());
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getLemonTartRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(ingr.getYeast());
		recipe.add(ingr.getLemon());
		recipe.add(ingr.getHoney());
		return recipe;
	}
	
	public ArrayList<ItemStack> getWrappedChickenRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(util.setAmount(ingr.getCheese(), 2));
		recipe.add(ingr.getPepper());
		recipe.add(ingr.getTortilla());
		recipe.add(util.setAmount(ingr.getSpices(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMysteryMeatRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SPIDER_EYE));
		recipe.add(new ItemStack(Material.ROTTEN_FLESH));
		recipe.add(new ItemStack(Material.BONE));
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(ingr.getSpices());
		recipe.add(util.setAmount(ingr.getSalt(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getTomatoSoupRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(ingr.getSpices());
		recipe.add(ingr.getGreens());
		recipe.add(ingr.getTomato());
		return recipe;
	}
	
	public ArrayList<ItemStack> getCornOnTheCobRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.STICK));
		recipe.add(ingr.getButter());
		recipe.add(ingr.getCorn());
		return recipe;
	}
	
	public ArrayList<ItemStack> getBreadPuddingRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.BREAD));
		recipe.add(ingr.getHoney());
		recipe.add(util.setAmount(ingr.getSpices(), 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getChickenPastaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_CHICKEN));
		recipe.add(ingr.getButter());
		recipe.add(util.setAmount(ingr.getPasta(), 2));
		recipe.add(util.setAmount(ingr.getGreens(), 3));
		return recipe;
	}
}
