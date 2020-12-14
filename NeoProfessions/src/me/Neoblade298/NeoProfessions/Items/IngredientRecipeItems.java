package me.Neoblade298.NeoProfessions.Items;

import java.util.ArrayList;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import me.Neoblade298.NeoProfessions.Utilities.SkullCreator;
import me.Neoblade298.NeoProfessions.Utilities.Util;

public class IngredientRecipeItems {
	Util util;
	public IngredientRecipeItems() {
		util = new Util();
	}
	
	String BASE64_R1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTk0Y2M3MjVlM2VjNDJkOTc0NjQwNDMxODBiNmEyYTZiNmYwZGU4OGNkZjY0NmM2NDk0NTIwODM2YTQ4YThiNyJ9fX0";
	String BASE64_R2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTYxYTkxOGMwYzQ5YmE4ZDA1M2U1MjJjYjkxYWJjNzQ2ODkzNjdiNGQ4YWEwNmJmYzFiYTkxNTQ3MzA5ODVmZiJ9fX0";
	String BASE64_R3 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzk4NDcyM2Q0NzhhNzliYWQ1ZWI3OTY5ZjgzOWJiZGQxYWY3MzgyNGJjODhkMGJkZGFjZDFlZjgxNzYyNWMwMCJ9fX0";
	String BASE64_R4 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTE5OTdkYTY0MDQzYjI4NDgyMjExNTY0M2E2NTRmZGM0ZThhNzIyNjY2NGI0OGE0ZTFkYmI1NTdiNWMwZmUxNCJ9fX0";
	String BASE64_R5 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTg5NTBkMWEwYjhjY2YwNjNlNzUxOWZlMzY5Y2FkMTVjY2UzMjA1NmZhNDcyYTk5YWEzMGI1ZmI0NDhmZjYxNCJ9fX0";
	String BASE64_R6 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzhiNzUyZTUyMzJiMDM5YjFlNzVlNDU0MTgzYTE5MmQ0MDU3YjdjYTgzMmY3YzI0YTVmZDg2Nzk2OWNiNGQifX19";
	String BASE64_R7 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjY2YjE5ZjdkNjM1ZDAzNDczODkxZGYzMzAxN2M1NDkzNjMyMDlhOGY2MzI4YTg1NDJjMjEzZDA4NTI1ZSJ9fX0";
	String BASE64_R8 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOTU3ZmQ1NmNhMTU5Nzg3NzkzMjRkZjUxOTM1NGI2NjM5YThkOWJjMTE5MmM3YzNkZTkyNWEzMjliYWVmNmMifX19";
	String BASE64_R9 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjBlMWE1YWFkZjNmYTc1NjcyNGMyZjkxNmVhOWM1MmFjNzlmOGIxM2EyY2ZmZTkxNDg1MjFlZTNjYmJmYSJ9fX0";
	String BASE64_R10 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTZkY2M4ZjM3YWM5OWQ5NTFlY2JjNWRmNWU4NTgyMTMzZjVmMjMwN2U3NjlhZjZiNmNmZmY0MjgyMTgwNjcifX19";
	String BASE64_R11 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmUxYzdhYzQzNmMwZTQ5NzJkMDkzYThmNjMzMDY0MDllM2ZhNTMyMWRiNGJmMTA2ZjRlZjFkODBjYzM2MDc3NCJ9fX0";
	String BASE64_R12 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjliZTgzOTA1OTY4ZTJlMzQwZjY2YTRkNzM4OTUyYTJkMTUxMjEyYmUyNDhiNjM0ZWJkNzNiZGU2Njc1MzIifX19";
	String BASE64_R13 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmRjNTVmNDNlM2Q4NDIwOTc1MTg5YTcwZmVhNGRmNzc5MWU0ZTEwMGI0MTYzZDYxNWI5YzBkMjBiMWRmYjJiZSJ9fX0";
	String BASE64_R14 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNlMDM2ZTMyN2NiOWQ0ZDhmZWYzNjg5N2E4OTYyNGI1ZDliMThmNzA1Mzg0Y2UwZDdlZDFlMWZjN2Y1NiJ9fX0";
	String BASE64_R15 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGNhNGM3M2QyMWJiM2JlMzM5YWI5NzM3ZTQ4NmNhMTA1NmJmZjhkOTg4M2I5NzM5MGViMzI2OTFmNDk2OSJ9fX0";
	String BASE64_R16 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzZjMDFiZmZlY2ZkYWI2ZDNjMGYxYTdjNmRmNmFhMTkzNmYyYWE3YTUxYjU0YTRkMzIzZTFjYWNiYzUzOSJ9fX0";
	String BASE64_R17 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGQ1NjZiYzczZGE4ZjA0YTc0YTE4MGJjM2VhMTk3ZThlNGI2YTEzZmI4ZDZhMTU2MGMwZjlhMmFjMThmNTQ4ZSJ9fX0";
	String BASE64_R18 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDI3NDIzZDVjY2U0YTQ3MmE3NjBjZTVlODYyY2EzNWY3MDU4ZDU4N2I3ZWFiNjllYmVjOTIwYzc3M2U3MzA3In19fQ";
	String BASE64_R19 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNjljMmRkZjJiZDc0YTQ2NTVlOGYwMTUzYTc0NTNlNjdkYjJhMjFkYmZhYzY3NTY3ODk0ODFhZGJlYzQ4M2EifX19";
	String BASE64_R20 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWViZjRiZDhkMjE2OWYxMDI3MzhiZmUyYmY3Y2ZlZDY5NTc3YjdjZjY3MjFhZmI3YTYyNGE4NTcwM2JiZDRiIn19fQ";
	String BASE64_R21 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMmIzMWE4MTEyNWI1M2VjOGZmMTFkMDlkYjM3YTJkZDQ0MmM0MjdiNWMzNmQ0YzZiYTVjMTlkY2QwMmU2MzEifX19";
	String BASE64_R25 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWU1OTM1ODYzYzUzYTk5NmY1MzM0ZTkwZjU1ZGU1MzhlODNmZmM1ZjZiMGI4ZTgzYTRkYzRmNmU2YjEyMDgifX19";
	String BASE64_R26 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOWI4NzhhOTFlZTQyNzhkMTZlZjE1MTc1ZWQ4ZTI4NjE1NDFkZTc5NzQ3NWNmNGE0NzMyOTE1ODc2YzZlOWEifX19";
	
	// Ingredients
	public ArrayList<ItemStack> getIngredients() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getSalt());
		list.add(getSpices());
		list.add(getGreens());
		list.add(getToast());
		list.add(getOil());
		list.add(getTomato());
		list.add(getButter());
		list.add(getLemon());
		list.add(getCorn());
		list.add(getHoney());
		list.add(getYeast());
		list.add(getBeetrootSauce());
		list.add(getPasta());
		list.add(getOnion());
		list.add(getHops());
		list.add(getCheese());
		list.add(getTortilla());
		list.add(getExoticGreens());
		list.add(getRice());
		list.add(getPopcorn());
		list.add(getPepper());
		list.add(getGrapes());
		list.add(getNuts());
		return list;
	}
	
	public ItemStack getSalt() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSalt");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 1");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSpices() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R2);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eSpices");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 2");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getGreens() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R3);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eGreens");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 3");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getToast() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R4);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eToast");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 4");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getOil() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R5);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eOil");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 5");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTomato() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R6);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eTomato");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 6");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getButter() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R7);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eButter");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 7");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLemon() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R8);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eLemon");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 8");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCorn() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R9);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eCorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 9");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHoney() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R10);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eHoney");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 10");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getYeast() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R11);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eYeast");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 11");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBeetrootSauce() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R12);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eBeetroot Sauce");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 12");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPasta() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R13);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§ePasta");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 13");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getOnion() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eOnion");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 14");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHops() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R15);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eHops");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 15");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCheese() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R16);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eCheese");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 16");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTortilla() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R17);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eTortilla");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 17");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getExoticGreens() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R18);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eExotic Greens");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 18");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getRice() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R19);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eRice");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 19");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPopcorn() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R20);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§ePopcorn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 20");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPepper() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R21);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§ePeppers");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 21");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getVodka() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 255, 255));
		meta.setDisplayName("§eVodka");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 22");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getRum() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(153, 51, 0));
		meta.setDisplayName("§eRum");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 23");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTequila() {
		ItemStack item = new ItemStack(Material.POTION);
		PotionMeta meta = (PotionMeta) item.getItemMeta();
		meta.setColor(Color.fromRGB(255, 204, 102));
		meta.setDisplayName("§eTequila");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 24");
		meta.setLore(lore);
		meta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getGrapes() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R25);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eGrapes");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 25");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getNuts() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R25);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§eNuts");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Ingredient 26");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ArrayList<ItemStack> getSaltRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getSpicesRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.NETHER_WART));
		recipe.add(new ItemStack(Material.WHEAT_SEEDS));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.REDSTONE));
		recipe.add(new ItemStack(Material.COCOA_BEANS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getGreensRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.OAK_LEAVES));
		return recipe;
	}
	
	public ArrayList<ItemStack> getToastRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD));
		return recipe;
	}
	
	public ArrayList<ItemStack> getOilRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.COOKED_COD));
		return recipe;
	}
	
	public ArrayList<ItemStack> getTomatoRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BEETROOT));
		recipe.add(new ItemStack(Material.POPPY));
		return recipe;
	}
	
	public ArrayList<ItemStack> getButterRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(new ItemStack(Material.STICK));
		return recipe;
	}
	
	public ArrayList<ItemStack> getLemonRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GOLD_NUGGET, 2));
		recipe.add(new ItemStack(Material.APPLE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getCornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.GOLDEN_CARROT));
		recipe.add(new ItemStack(Material.BLAZE_ROD));
		return recipe;
	}
	
	public ArrayList<ItemStack> getHoneyRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUNFLOWER));
		recipe.add(new ItemStack(Material.SUGAR));
		return recipe;
	}
	
	public ArrayList<ItemStack> getYeastRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.BLAZE_POWDER));
		return recipe;
	}
	
	public ArrayList<ItemStack> getBeetrootSauceRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BEETROOT, 6));
		recipe.add(new ItemStack(Material.BOWL));
		recipe.add(util.setAmount(getSpices(), 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPastaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.EGG));
		recipe.add(getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getOnionRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BEETROOT));
		recipe.add(new ItemStack(Material.FERMENTED_SPIDER_EYE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getHopsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.VINE, 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getCheeseRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getTortillaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT, 3));
		recipe.add(util.setAmount(getCorn(), 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getExoticGreensRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.CACTUS));
		recipe.add(new ItemStack(Material.VINE));
		recipe.add(new ItemStack(Material.OAK_LEAVES));
		recipe.add(new ItemStack(Material.CHORUS_FRUIT));
		return recipe;
	}
	
	public ArrayList<ItemStack> getRiceRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT_SEEDS));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPopcornRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(getCorn());
		return recipe;
	}
	
	public ArrayList<ItemStack> getPepperRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.CACTUS));
		recipe.add(new ItemStack(Material.LAVA_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getVodkaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.POTATO));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(getYeast());
		return recipe;
	}
	
	public ArrayList<ItemStack> getRumRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(getYeast());
		return recipe;
	}
	
	public ArrayList<ItemStack> getTequilaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.CACTUS));
		recipe.add(new ItemStack(Material.WATER_BUCKET));
		recipe.add(getYeast());
		return recipe;
	}
	
	public ArrayList<ItemStack> getGrapesRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SWEET_BERRIES));
		return recipe;
	}
	
	public ArrayList<ItemStack> getNutsRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.OAK_SAPLING));
		return recipe;
	}
}
