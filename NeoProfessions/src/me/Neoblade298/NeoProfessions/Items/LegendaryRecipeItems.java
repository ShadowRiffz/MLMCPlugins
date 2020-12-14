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
	String BASE64_R8 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNWRhNTU4ZjEzNTMzZmYyOWExODU0MDhhZWY0OWM1YTBhOTc2NDg3M2NkZDc5NzZiNGQ0ZjVjNDRhYWFjZSJ9fX0=";
	String BASE64_R9 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTMxZjlkYTg5NzJkMTJlMDM1NGRjNzQ2MjZmZmViNWMyNTI0NTUwM2UyOTQzMjg3OTA5Mjg1YTc5MTYxMTcifX19";
	String BASE64_R10 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvOGQ2YzFlNmJjNDI0ZDM3MGIxNjA4OWM2MWMxODJjMmI3NTZkOGQyYjdhMGRkYjUyN2M4N2VkYWNkMTkwYzAwYSJ9fX0=";

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
		lore.add("§7Effects: §9+150 STR (20s)§7, §9+150 DEX (20s)§7,");
		lore.add("§9+150 INT (20s)§7, §9+150 SPR (20s)");
		lore.add("§7Hunger: §e7.5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §dYour attacks heal you for 20%");
		lore.add("§dof damage dealt. (20s)");
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
		lore.add("§7Effects: §9+150 END (240s)§7, §9 +200 VIT (240s)");
		lore.add("§7Hunger: §e10 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §d1.15x damage buff. (240s)");
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
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e10 Food Bars");
		lore.add("§7Restores §c250 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §dMovement speed reduced by 50%,");
		lore.add("§dbut you take 75% reduced damage. (20s)");
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
		lore.add("§7Effects: §9+150 STR (20s)§7, §9+150 DEX (20s)§7,");
		lore.add("§9+150 INT (20s)§7, §9+150 SPR (20s)");
		lore.add("§7Hunger: §e8 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §dReduces damage taken by 40%. (20s)");
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
		lore.add("§7Effects: §9+40 STR (180s)§7, §9+40 DEX (180s)§7,");
		lore.add("§9+40 INT (180s)§7, §9+40 SPR (180s)");
		lore.add("§7Hunger: §e6 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §dReduces ability cooldowns by 10%. (180s)");
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
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e7 Food Bars");
		lore.add("§7Restores §c2700 HP (150/10s)§7, §b1800 MP (100/10s)");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §dTaking damage heals all allies in a");
		lore.add("§dradius of 8 by 0.3% of their health. (60s)");
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
		lore.add("§7Effects: §9+150 STR (20s)§7, §9+150 DEX (20s)§7,");
		lore.add("§9+150 INT (20s)§7, §9+150 SPR (20s)");
		lore.add("§7Hunger: §e8 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §dSpeed increases by 2x. (20s)");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getCommunityCake() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R8);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lCommunity Cake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 8");
		lore.add("§7Effects: §fNone");
		lore.add("§7Hunger: §e10 Food Bars");
		lore.add("§7Restores §c1500 HP§7, §b500 MP");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §dAllies (10 blocks) also get HP/MP effects.");
		lore.add("§dAdditionally, they heal for 0.5% of damage dealt");
		lore.add("§dfor 10 seconds.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getMonaBobaTea() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R9);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lMona's Boba Tea");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 9");
		lore.add("§7Effects: §9+45 STR (180s)§7, §9+45 DEX (180s)§7,");
		lore.add("§9+45 INT (180s)§7, §9+45 SPR (180s)");
		lore.add("§7Hunger: §e8 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §dReduce cooldowns by 1 second");
		lore.add("§dper second for 10 seconds.");
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
	public ItemStack getRandumCheesecakeRecipe() {
		ItemStack item = SkullCreator.itemFromBase64(BASE64_R10);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§9§lRandum's Cheesecake");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add("§6§lLegendary Recipe 10");
		lore.add("§7Effects: §9+150 STR (20s)§7, §9+150 DEX (20s)§7,");
		lore.add("§9+150 INT (20s)§7, §9+150 SPR (20s)");
		lore.add("§7Hunger: §e5 Food Bars");
		lore.add("§7Restores §c0 HP§7, §b0 MP");
		lore.add("§7Cooldown: §6600 Seconds");
		lore.add("§7Special: §dSacrifice 30% of your max health");
		lore.add("§dand Cheesecake will grant a 30%");
		lore.add("§ddamage boost for 20 seconds.");
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
		recipe.add(util.setAmount(ingr.getExoticGreens(), 2));
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
		recipe.add(util.setAmount(t1.getHotChocolate(), 3));
		recipe.add(t1.getIceCream());
		return recipe;
	}
	
	public ArrayList<ItemStack> getCommunityCakeRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.SUGAR, 3));
		recipe.add(new ItemStack(Material.DIAMOND, 3));
		recipe.add(new ItemStack(Material.DIAMOND_BLOCK));
		recipe.add(new ItemStack(Material.EMERALD, 2));
		return recipe;
	}
	
	public ArrayList<ItemStack> getMonaBobaTeaRecipe() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(t1.getGreenTea());
		recipe.add(t2.getExoticTea());
		recipe.add(t1.getBarleyTea());
		recipe.add(util.setAmount(ingr.getHoney(), 3));
		recipe.add(new ItemStack(Material.DIAMOND, 2));
		recipe.add(new ItemStack(Material.MILK_BUCKET));
		return recipe;
	}
	
	public ArrayList<ItemStack> getRandumCheesecake() {
		ArrayList<ItemStack> recipe = new ArrayList<ItemStack>();
		recipe.add(new ItemStack(Material.DIAMOND, 2));
		recipe.add(ingr.getLemon());
		recipe.add(ingr.getCheese());
		recipe.add(ingr.getSalt());
		recipe.add(util.setAmount(t3.getSpongeCake(), 3));
		return recipe;
	}
}
