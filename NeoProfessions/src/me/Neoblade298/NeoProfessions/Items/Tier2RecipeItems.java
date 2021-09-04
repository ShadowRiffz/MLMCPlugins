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

	String BASE64_R1 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2IwMWZjNTJjMGVhYTgwYzhhNTg0NmJhNGIwM2RlYjc2Y2NkYTUyOGMwOWI3ZjM0ZmFmZjAwOGNkNTQ0Nzc2OCJ9fX0";
	String BASE64_R2 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGEyNGRjOWYxNzMzZjAwZTY3NjUwOWEyMjFiOGE2NmE2ZTA1OWU1NTA0NWQwOGE3ZTRmYzA4ZDdkMzEwODc1OCJ9fX0";
	String BASE64_R3 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjJhMjFmNGRmZjUxOGViNWUxNDYwNDk3ODEyYjI0MGQxNTliNjM0ZDA0ZTI3NTQ1YmZhM2VhN2ZkM2RkNDgifX19";
	String BASE64_R4 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmMyYzU3YTk3ZDlmY2I0MmMxZmVkNDk5NGFmN2IzNzc0YzFiYTlmMTJjMGYxZWY3NjM2YzBmZTM1NjFkYzkifX19";
	String BASE64_R5 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM1MGFlZTg4MDEzZThmYWY0MjdlMTlmM2I4OTgyOGI4NmJiZjAzZGQyZjE3YzRjNzYwZDFkZGUyMmRlMyJ9fX0";
	String BASE64_R6 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWRiOTEyYzFlZDQ2MDg4OTBhZTU5NGUxYmY3ZGQ3MDM0ODY2NzVjZjY0NzU0ZmY5MmVlM2U0YWQzYWRiYyJ9fX0";
	String BASE64_R7 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNGEyMTJmNzBmYjY2NTQ4ZmRmMmFiY2RiY2U4ZjMxNzA0MzRiODk5NWUyMWQxNGY5YjFmZjE0OTdjNjQ4ZCJ9fX0";
	String BASE64_R8 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTZjYjZlNjM4YzY5N2FhZjZhOGM1ODViZWM3OWMyNmVkYTRjY2ExNmM4NGM3MjZiZGZkMzYxZjc2YzUxNTkifX19";
	String BASE64_R9 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDY3MmM1N2Y0YzdiOWU5NjJiNDViNTVkZDdiZDc4ODY4ODBkN2VlZjI2ZGI2YzJjY2UwM2M4ZmY4YzQ4In19fQ";
	String BASE64_R10 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjg5MjJjODQyM2QyODExODQwZjU2Yjc1YzQ1M2FlM2UyYmIzN2ZhMWQ3ZDIzNDhhNWVmOWI0NDU2YzM1ODIxYyJ9fX0";
	String BASE64_R11 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjdiOWYwOGFkYTRlOGJhNTg2YTA0ZWQyZTllMjVmZThiOWQ1NjhhNjY1MjQzZjljNjAzNzk5YTdjODk2NzM2In19fQ";
	String BASE64_R12 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDUzYzFlODdlNTM3ZjFhYjI3NzRkZGFmYjgzNDM5YjMzNmY0YTc3N2I0N2FkODJiY2IzMGQ1ZmNiZGY5YmMifX19";
	String BASE64_R13 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2M2YjRhN2JkODI0NDE2MTliYmMxMWQ5YjhlMGU2NGFlOGI5NWYyZTQwYjM5MjEzNTVmY2M1NDM0MzI2MDE3In19fQ";
	String BASE64_R14 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMGFiYTU0M2Y5Zjk1ODg2NjRhYmRhOTAzZTNhOTI5MjczMjY2YjA0ODQxZGI3Zjc1NjdjZmM2NzcxMDU5In19fQ";
	String BASE64_R15 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZWYwOTQ0NTZmZDc5NGI2NTMxZmM2ZGVjNmYzOTZiNjgwYjk1MzYwMDIwNjNlMTFjZTI0ZDBhNzRiMGI3ZDg4NSJ9fX0";
	String BASE64_R16 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTdiYTIyZDVkZjIxZTgyMWE2ZGU0YjhjOWQzNzNhM2FhMTg3ZDhhZTc0ZjI4OGE4MmQyYjYxZjI3MmU1In19fQ";
	String BASE64_R17 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWNiMjhhYmVkNTRjYjQ3NWIxOTdiZTEwYzk0MGUyYjJjZTJlZGFlM2FjZWRmZjJiNmUxZjYyM2ZhMmI1MmEwIn19fQ";
	String BASE64_R18 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGVhYTNhM2E2MmMzYjdmZDE2MzZhMTg1YzRjMzFmNWQ3NmIyZDM2NTNjNWFkMzA4ZTg4MTllMmU2MTAxOSJ9fX0";
	String BASE64_R19 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM5MWRmZmJlYTJmYzNmMmFkNzhhNjIzZjQ5YmY3ZTExMjE2OTQxMTJjMzc1OWZlZWQ0MTU2ZmMyYmE0NmMwIn19fQ";
	String BASE64_R20 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYzZjMzY1MjNjMmQxMWI4YzhlYTJlOTkyMjkxYzUyYTY1NDc2MGVjNzJkY2MzMmRhMmNiNjM2MTY0ODFlZSJ9fX0";
	String BASE64_R21 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWJlNDVhNWU5Yzk1ZDUyMjM2NDBlNzNlYzAyOGEwZDRiYTBlNTk0ZjU1Y2U0Nzc5MmRmNDA5N2IzMjRiZGRmIn19fQ";
	String BASE64_R22 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2Q1MzI1MjViNzkwNjRkNGI1MDRkODczMTE3OGI1YmFhYjkxODY2ZWI5YWI1M2QwZmRlN2YwOGMxY2MwZGY4YyJ9fX0="; 

	public Tier2RecipeItems() {
		util = new Util();
		ingr = new IngredientRecipeItems();
	}
	
	public ArrayList<ItemStack> getTier2Recipes() {
		ArrayList<ItemStack> list = new ArrayList<ItemStack>();
		list.add(getSteakWithGreenBeans());
		list.add(getButteredToast());
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
		lore.add("§7Effects: §9+40 END (180s)");
		lore.add("§7Hunger: §e6 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b 0MP");
		lore.add("§7Cooldown: §6300 Seconds");
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
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e3.5 Food Bars");
		lore.add("§7Restores §c75 HP§7, §b10 MP");
		lore.add("§7Cooldown: §660 Seconds");
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
		lore.add("§7Effects: §aSpeed II (10s)");
		lore.add("§7Hunger: §e2 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b40 MP");
		lore.add("§7Cooldown: §660 Seconds");
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
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e5 Food Bars");
		lore.add("§7Restores §c480 HP (20/5s)§7, §b0 MP");
		lore.add("§7Cooldown: §6240 Seconds");
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
		lore.add("§7Effects: §9+50 DEX (15s)§7, §9+50 STR (15s)§7, §aHaste II (15s)");
		lore.add("§7Hunger: §e2 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6120 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getAle() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R6);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Ale");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 6");
		lore.add("§7Effects: §9+70 STR (20s)§7, §aConfusion I (30s)§7,");
		lore.add("§aSlowness I (30s)§7, §aMining Fatigue I (30s)");
		lore.add("§7Hunger: §e2 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6120 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getSandwich() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R7);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Sandwich");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 7");
		lore.add("§7Effects: §9+15 STR (150s)§7, §9+15 INT (150s)§7,");
		lore.add("§9+15 DEX (150s)§7, §9+15 SPR (150s)");
		lore.add("§7Hunger: §e4 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6300 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBeefStew() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R8);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Beef Stew");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 8");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e3.5 Food Bars");
		lore.add("§7Restores §c540 HP (30/5s)§7, §b54 MP (3/5s)");
		lore.add("§7Cooldown: §6240 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getExoticTea() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R9);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Exotic Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 9");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e3 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b240 MP (10/5s)");
		lore.add("§7Cooldown: §6300 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBartrandCornbread() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R10);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Bartrand's Cornbread");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 10");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e4 Food Bars");
		lore.add("§7Restores §c150 HP§7, §b40 MP");
		lore.add("§7Cooldown: §690 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTunaSandwich() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R11);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Tuna Sandwich");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 11");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e4.5 Food Bars");
		lore.add("§7Restores §c900 HP (30/10s)§7, §b0 MP");
		lore.add("§7Cooldown: §6480 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getApplePie() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R12);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Apple Pie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 12");
		lore.add("§7Effects: §9+100 INT (10s)");
		lore.add("§7Hunger: §e4.5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §690 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBeer() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R13);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Beer");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 13");
		lore.add("§7Effects: §9+300 VIT (20s)§7, §aConfusion I (30s)§7, §aBlindness I (30s)");
		lore.add("§7Hunger: §e2 Food Bars");
		lore.add("§7Restores §c400 HP (40/2s)§7, §b0 MP");
		lore.add("§7Cooldown: §6120 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getHashBrowns() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R14);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Hash Browns");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 14");
		lore.add("§7Effects: §9+25 DEX (180s)");
		lore.add("§7Hunger: §e2.5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6300 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getLemonTart() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R15);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Lemon Tart");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 15");
		lore.add("§7Effects: §aSpeed II (10s)§7, §aCleanses all Negative Effects");
		lore.add("§7Hunger: §e3.5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6120 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getWrappedChicken() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R16);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Wrapped Chicken");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 16");
		lore.add("§7Effects: §9+30 STR (120s)");
		lore.add("§7Hunger: §e4 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6300 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMysteryMeat() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R17);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mystery Meat");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 17");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e3 Food Bars");
		lore.add("§7Restores §c840 HP (35/5s)§7, §b0 MP");
		lore.add("§7Cooldown: §6240 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getTomatoSoup() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R18);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Tomato Soup");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 18");
		lore.add("§7Effects: §9+30 SPR (120s)");
		lore.add("§7Hunger: §e3.5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6240 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCornOnTheCob() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R19);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Corn on the Cob");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 19");
		lore.add("§7Effects: §9+70 DEX (10s)");
		lore.add("§7Hunger: §e2.5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §690 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBreadPudding() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R20);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Bread Pudding");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 20");
		lore.add("§7Effects: §aSpeed III (5s)§7, §aJump Boost I (60s)");
		lore.add("§7Hunger: §e3 Food Bars");
		lore.add("§7Restores §c100 HP§7, §b30 MP");
		lore.add("§7Cooldown: §690 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getChickenPasta() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R21);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chicken Pasta");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 21");
		lore.add("§7Effects: §9+20 SPR (180s)§7, §9+20 INT (180s)");
		lore.add("§7Hunger: §e5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6300 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getPeanutButterJelly() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R22);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Peanut Butter & Jelly");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Tier 2 Recipe 22");
		lore.add("§7Effects: §9xxx");
		lore.add("§7Hunger: §exx Food Bars");
		lore.add("§7Restores §cxx HP§7, §bxx MP");
		lore.add("§7Cooldown: §6xxx Seconds");
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
	
	public ArrayList<ItemStack> getPeanutButterJellyRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.BREAD, 6));
		recipe.add(ingr.getGrapes());
		recipe.add(ingr.getNuts());
		recipe.add(ingr.getButter());
		return recipe;
	}
}
