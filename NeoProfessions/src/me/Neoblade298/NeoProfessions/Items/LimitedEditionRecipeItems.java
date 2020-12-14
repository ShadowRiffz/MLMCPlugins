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
	Tier1RecipeItems t1;
	
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
	String BASE64_R17 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzc1MmQ3NWU4ZGUxNzExMTc4ZmJjMDI4ZDg4ZmU0ZWY5MDhiODkzZGM1Yzg4N2IzMzlkMDFmYjg4OGEyNCJ9fX0";
	String BASE64_R18 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc1NTg0ZTZmZDU0Y2EwMWRmNGVmZmQ1Zjc0NmIyZDgzYTU4OWRlNjc3NzU1NzU2YmI1OGQ5ZWEyODQ1MTYifX19";
	String BASE64_R19 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZGUyNjExNTE4MDM3NTM2NmJiMzRlZDE2YzI2Y2UyZDA0ZDkxZjdmNTI3Mjc2YWMyNDcwZTllYmFmMmRhZjM1In19fQ==";
	String BASE64_R20 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTMyMzExODI5NWRlNjRjNWMzNDI5N2MyNWQxMWU0MWE4MjI4MDA0ZTRkOTFmOWQ1YzA4YzhlMWJlNDcyZDJhMyJ9fX0=";
	String BASE64_R21 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I3MGYyZmI1ZWJmNDlmNzlmZjNlODczNjE2ODYzYWU1ZDM2MmZiYmZjMzFhZWYyZGZiOTNkNmUxN2RiZjIifX19";
	String BASE64_R22 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmVkZDgzYTRhNTNiZGM2MjM2NGFiYTM2OTNhODNlMjhlMmQ2Nzk0Nzk0YmQ5MWQ0NTM1YzhlZmEzMzQ4YzIifX19";
	String BASE64_R23 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGM1MGFlZTg4MDEzZThmYWY0MjdlMTlmM2I4OTgyOGI4NmJiZjAzZGQyZjE3YzRjNzYwZDFkZGUyMmRlMyJ9fX0=";
	String BASE64_R24 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTU0MTgxOWU4Zjc5NDVmYWY3YThmN2NjMTc1ZGNjNjRkNGUzOTc3MzE3ZjAxMjc0ZTNmZGYxOGE1NTE5NDQyMSJ9fX0=";
	String BASE64_R25 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTI5YzcxMTU1YjNmYjQ5ZDNlNDhkNTI0MGMxZDY3MzY2NzU0ODhiMzg0OWU5NmFlZWNlNDIzYzhlMzM5YTMwYiJ9fX0=";
	String BASE64_R26 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2I2MmVjMWRkNWQwNGIyMGY0MjkwZTgzOWZlYzg3M2NjZDc0NzI3MTY0MDU5MGE5Mjk5N2I1ODQ2YTAyYzYxOSJ9fX0=";

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
		list.add(getVioletDirtCake());
		return list;
	}
	public ItemStack getCandyCorn() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R1);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Candy Corn");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 1");
		lore.add("§7Recipe From: §c §c2017 Halloween Event");
		lore.add("§7Effects: §9+125 DEX (20s)§7, §aSpeed II (20s)");
		lore.add("§7Hunger: §e1 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6120 Seconds");
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
		lore.add("§7Recipe From: §c §c2017 Halloween Event");
		lore.add("§7Effects: §9+40 INT (180s)§7, §9+40 SPR (180s)");
		lore.add("§7Hunger: §e5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6300 Seconds");
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
		lore.add("§7Recipe From: §c §c2017 Halloween Event");
		lore.add("§7Effects: §9+150 INT (20s)");
		lore.add("§7Hunger: §e3.5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §690 Seconds");
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
		lore.add("§7Recipe From: §c2017 Christmas Event");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e1 Food Bars");
		lore.add("§7Restores §c200 HP§7, §b50 MP");
		lore.add("§7Cooldown: §650 Seconds");
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
		lore.add("§7Recipe From: §c2017 Christmas Event");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e2 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b200 MP");
		lore.add("§7Cooldown: §650 Seconds");
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
		lore.add("§7Recipe From: §c2017 Christmas Event");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e1.5 Food Bars");
		lore.add("§7Restores §c1200 HP (50/5s)§7, §b120 MP (5/5s)");
		lore.add("§7Cooldown: §6300 Seconds");
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
		lore.add("§7Recipe From: §c2017 Christmas Event");
		lore.add("§7Effects: §9+45 STR (150s)§7, §9+80 END (150s)");
		lore.add("§7Hunger: §e2.5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6240 Seconds");
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
		lore.add("§7Recipe From: §cVote Calendar Reward");
		lore.add("§7Effects: §9+20 STR (120s)§7, §9+20 DEX (120s)§7,");
		lore.add("§9+20 INT (120s)§7, §9+20 SPR (120s)§7, §9+75 VIT (120s)");
		lore.add("§7Hunger: §e4 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6240 Seconds");
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
		lore.add("§7Recipe From: §c2018 MLMC Olympics");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e2 Food Bars");
		lore.add("§7Restores §c150 HP§7, §b0 MP");
		lore.add("§7Cooldown: §630 Seconds");
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
		lore.add("§7Recipe From: §c2018 MLMC Olympics");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e6 Food Bars");
		lore.add("§7Restores §c1890 HP (45/5s)§7, §b0 MP");
		lore.add("§7Cooldown: §6360 Seconds");
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
		lore.add("§7Recipe From: §c2018 MLMC Prom");
		lore.add("§7Effects: §9+110 STR (20s)");
		lore.add("§7Hunger: §e4 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6120 Seconds");
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
		lore.add("§7Recipe From: §c2018 World Cup Event");
		lore.add("§7Effects: §9+45 DEX (180s)");
		lore.add("§7Hunger: §e7 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6300 Seconds");
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
		lore.add("§7Recipe From: §c2018 World Cup Event");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e4 Food Bars");
		lore.add("§7Restores §c1500 HP (125/10s)§7, §b0 MP");
		lore.add("§7Cooldown: §6300 Seconds");
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
		lore.add("§7Recipe From: §c2018 World Cup Event");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e7 Food Bars");
		lore.add("§7Restores §c350 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6120 Seconds");
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
		lore.add("§7Recipe From: §c2018 World Cup Event");
		lore.add("§7Effects: §9+120 INT (20s)§7, §9+160 SPR (20s)");
		lore.add("§7Hunger: §e5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6150 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getVioletDirtCake() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R16);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Violet's Dirt Pie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 16");
		lore.add("§7Recipe From: §cToken Shop § 2020 Recipe Event");
		lore.add("§7Effects: §9+45 PRC (180s)");
		lore.add("§7Hunger: §e6 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6300 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getAvaBruschetta() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R17);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Ava's Bruschetta");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 17");
		lore.add("§7Recipe From: §c2020 Recipe Event");
		lore.add("§7Effects: §9+110 STR (15s)§7, §9+110 DEX (15s)§7,");
		lore.add("§9+110 INT (15s)§7, §9+110 SPR (15s)");
		lore.add("§7Hunger: §e5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6180 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getAralicMuscadineWine() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R18);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Aralic's Muscadine Wine");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 18");
		lore.add("§7Recipe From: §c2020 Recipe Event");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e2 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b500 MP (20/5s)");
		lore.add("§7Cooldown: §6300 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getMonaLemonBar() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R19);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Mona's Lemon Bars");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 19");
		lore.add("§7Recipe From: §c2020 Recipe Event");
		lore.add("§7Effects: §aSpeed III (10s)§7, §aHaste II (10s)§7,");
		lore.add("§aCleanses all Negative Effects");
		lore.add("§7Hunger: §e3.5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §660 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getNeoEggsBenedict() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R20);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Neo's Eggs Benedict");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 20");
		lore.add("§7Recipe From: §c2020 Recipe Event");
		lore.add("§7Effects: §9+350 VIT (240s)§7, §9+80 END (240s)");
		lore.add("§7Hunger: §e7 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6480 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getMattBeefFriedRice() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R21);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Matt's Beef Fried Rice");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 21");
		lore.add("§7Recipe From: §c2020 Recipe Event");
		lore.add("§7Effects: §9+30 STR (150s)§7, §9+30 DEX (150s)§7,");
		lore.add("§9+30 INT (150s)§7, §9+30 SPR (150s)");
		lore.add("§7Hunger: §e6 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6360 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getConversationHeart() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R22);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Conversation Heart");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 22");
		lore.add("§7Recipe From: §c2020 Valentine's Event");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e2 Food Bars");
		lore.add("§7Restores §c600 HP (100/5s)§7, §b0 MP");
		lore.add("§7Cooldown: §6120 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getChocolateEgg() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R23);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Chocolate Egg");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 23");
		lore.add("§7Recipe From: §c2020 Easter Egg Hunt");
		lore.add("§7Effects: §aSpeed II (5s)");
		lore.add("§7Hunger: §e2 Food Bars");
		lore.add("§7Restores §c100 HP§7, §b0 MP");
		lore.add("§7Cooldown: §620 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}

	public ItemStack getPlayerPie() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R24);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Player's Pie");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6Limited Edition Recipe 24");
		lore.add("§7Recipe From: §c2020 Player Appreciation Event");
		lore.add("§7Effects: §9+125 PRC (20s)");
		lore.add("§7Hunger: §e6 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6150 Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getGummySpider() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R25);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Gummy Spider");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Limited Edition Recipe 25");
		lore.add("§7Recipe From: §c2020 Halloween Event");
		lore.add("§7Effects: §9xxx");
		lore.add("§7Hunger: §exx Food Bars");
		lore.add("§7Restores §cxx HP§7, §bxx MP");
		lore.add("§7Cooldown: §6xxx Seconds");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getBaklava() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R26);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9Baklava");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§7Limited Edition Recipe 26");
		lore.add("§7Recipe From: §c2020 Nation Olympics");
		lore.add("§7Effects: §aSpeed II (5s)");
		lore.add("§7Hunger: §e4 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b100 MP");
		lore.add("§7Cooldown: §630 Seconds");
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
	
	public ArrayList<ItemStack> getVioletDirtCakeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.DIRT, 3));
		recipe.add(new ItemStack(Material.PUMPKIN_PIE, 2));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(util.setAmount(ingr.getHoney(), 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getAvaBruschettaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getToast());
		recipe.add(ingr.getTomato());
		recipe.add(ingr.getOil());
		recipe.add(ingr.getSalt());
		return recipe;
	}
	
	public ArrayList<ItemStack> getAralicMuscadineWineRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getGrapes(), 3));
		recipe.add(ingr.getYeast());
		recipe.add(new ItemStack(Material.SWEET_BERRIES));
		recipe.add(new ItemStack(Material.APPLE));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMonaLemonbarRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(ingr.getLemon(), 3));
		recipe.add(ingr.getButter());
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getNeoEggBenedictRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(new ItemStack(Material.EGG), 3));
		recipe.add(util.setAmount(ingr.getGreens(), 2));
		recipe.add(util.setAmount(ingr.getToast(), 3));
		recipe.add(new ItemStack(Material.COOKED_PORKCHOP));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMattBeefFriedRiceRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(ingr.getRice());
		recipe.add(ingr.getOil());
		recipe.add(ingr.getExoticGreens());
		recipe.add(new ItemStack(Material.COOKED_BEEF));
		recipe.add(new ItemStack(Material.CARROT));
		return recipe;
	}
	
	public ArrayList<ItemStack> getConversationHeartRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(new ItemStack(Material.SUGAR), 6));
		recipe.add(util.setAmount(ingr.getGrapes(), 3));
		return recipe;
	}
	
	public ArrayList<ItemStack> getChocolateEggRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(new ItemStack(Material.SUGAR), 4));
		recipe.add(util.setAmount(new ItemStack(Material.COCOA_BEANS), 4));
		return recipe;
	}
	
	public ArrayList<ItemStack> getPlayerPieRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(util.setAmount(new ItemStack(Material.LIME_DYE), 3));
		recipe.add(util.setAmount(new ItemStack(Material.SUGAR), 2));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		recipe.add(util.setAmount(new ItemStack(Material.BREAD), 3));
		return recipe;
	}
	
	public ArrayList <ItemStack> getGummySpiderRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SPIDER_EYE));
		recipe.add(new ItemStack(Material.SUGAR));
		recipe.add(new ItemStack(Material.PINK_DYE));
		recipe.add(t1.getCuredFlesh());
		return recipe;
	}
	
	public ArrayList <ItemStack> getBaklavaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.WHEAT));
		recipe.add(ingr.getHoney());
		recipe.add(ingr.getNuts());
		recipe.add(ingr.getOil());
		return recipe;
	}
}
