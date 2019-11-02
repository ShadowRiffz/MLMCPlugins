package me.Neoblade298.NeoProfessions.Utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.milkbowl.vault.economy.Economy;

public class CulinarianUtils {
	
	static final int CRAFT_COST = 1;
	static final int NUM_PER_CRAFT = 4;
	static Random gen = new Random();
	Util util;
	
	public CulinarianUtils() {
		util = new Util();
	}
	
	public void checkAlcoholUp(Player p, int amount, HashMap<Player, Integer> drunkness) {
		if(amount >= 30 && amount < 50) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 30*20, 0), true);
			util.sendMessage(p, "&7You are now &esomewhat drunk&7 (" + amount + "/100).");
		}
		else if(amount >= 50 && amount < 70) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 0), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60*20, 0), true);
			util.sendMessage(p, "&7You are now &6very drunk&7 (" + amount + "/100).");
		}
		else if(amount >= 70 && amount < 100) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 10*20, 0), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 20*20, 0), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60*20, 0), true);
			util.sendMessage(p, "&7You are now &4dangerously drunk&7. (" + amount + "/100).");
		}
		else if(amount > 100) {
			double chance = gen.nextDouble();
			if(chance >= 0.5) {
				util.sendMessage(p, "&7You throw up from being too drunk. Your drunkness is reduced to &6very drunk&7 (50/100).");
				drunkness.put(p, 50);
			}
			else {
				util.sendMessage(p, "&7You become too drunk and lose 80% of your current health.");
				p.damage(1);
				p.setHealth(p.getHealth() * 0.2);
				drunkness.put(p, 100);
			}
		}
		else {
			util.sendMessage(p, "&7You drink&7 (" + amount + "/100).");
		}
	}
	
	public void checkAlcoholDown(Player p, int amount) {
		if(amount == 0) {
			util.sendMessage(p, "&7You are completely sober&7 (0/100).");
		}
		else if(amount == 29) {
			util.sendMessage(p, "&7Your drunkness is reduced to &fsober&7 (29/100).");
		}
		else if(amount == 49) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 30*20, 0), true);
			util.sendMessage(p, "&7Your drunkness is reduced to &esomewhat drunk&7 (49/100).");
		}
		else if(amount == 69) {
			p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 10*20, 0), true);
			p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 60*20, 0), true);
			util.sendMessage(p, "&7Your drunkness is reduced to &6very drunk&7 (69/100).");
		}
	}
	
	public int getRecipeLevel(ItemStack item) {
		if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
			String line = item.getItemMeta().getLore().get(0);
			if(line.contains("Tier 1") || line.contains("Ingredient")) {
				return 1;
			}
			else if(line.contains("Tier 2")) {
				return 2;
			}
			else if(line.contains("Tier 3") || line.contains("Drink")) {
				return 3;
			}
			else if(line.contains("Limited Edition") || line.contains("Legendary")) {
				return 4;
			}
		}
		return -1;
	}
	
	public int getTotalLeaves(ItemStack[] contents) {
		int total = 0;
		for(ItemStack content : contents) {
			if(content != null) {
				if(content.isSimilar(new ItemStack(Material.OAK_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.SPRUCE_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.BIRCH_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.JUNGLE_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.ACACIA_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.DARK_OAK_LEAVES))) {
					total += content.getAmount();
				}
			}
		}
		return total;
	}
	
	public void removeLeaves(PlayerInventory inv, int amount) {
		ItemStack[] contents = inv.getStorageContents();
		int slotNum = 0;
		for(ItemStack content : contents) {
			if(amount <= 0) {
				return;
			}
			if(content != null) {
				if(content.isSimilar(new ItemStack(Material.OAK_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.SPRUCE_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.BIRCH_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.ACACIA_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.DARK_OAK_LEAVES)) ||
						content.isSimilar(new ItemStack(Material.JUNGLE_LEAVES))) {
					if(content.getAmount() > amount) {
						content.setAmount(content.getAmount() - amount);
						inv.setItem(slotNum, content);
						amount = 0;
					}
					else {
						amount -= content.getAmount();
						inv.setItem(slotNum, null);
					}
				}
			}
			slotNum++;
		}
	}
	
	public void removeMushrooms(PlayerInventory inv, int amount) {
		ItemStack[] contents = inv.getStorageContents();
		int slotNum = 0;
		for(ItemStack content : contents) {
			if(amount <= 0) {
				return;
			}
			if(content != null) {
				if(content.isSimilar(new ItemStack(Material.RED_MUSHROOM)) ||
						content.isSimilar(new ItemStack(Material.BROWN_MUSHROOM))) {
					if(content.getAmount() > amount) {
						amount = 0;
						content.setAmount(content.getAmount() - amount);
					}
					else {
						amount -= content.getAmount();
						inv.setItem(slotNum, null);
					}
				}
			}
			slotNum++;
		}
	}
	
	public int getTotalMushrooms(ItemStack[] contents) {
		int total = 0;
		for(ItemStack content : contents) {
			if(content != null) {
				if(content.isSimilar(new ItemStack(Material.RED_MUSHROOM)) ||
						content.isSimilar(new ItemStack(Material.BROWN_MUSHROOM))) {
					total += content.getAmount();
				}
			}
		}
		return total;
	}
	
	public int getMaxCraftable(Player p, ArrayList<ItemStack> items, boolean isSmelted) {
		int count = -1;
		PlayerInventory inv = p.getInventory();
		ItemStack[] contents = inv.getStorageContents();
		for(ItemStack item : items) {
			int total = 0;
			if(item.isSimilar(new ItemStack(Material.OAK_LEAVES))) {
				total = getTotalLeaves(contents);
			}
			else if(item.isSimilar(new ItemStack(Material.RED_MUSHROOM))) {
				total = getTotalMushrooms(contents);
			}
			else {
				for(ItemStack content : contents) {
					if(content != null && content.isSimilar(item)) {
						total += content.getAmount();
					}
				}
			}
			int canCraft = total / item.getAmount();
			if(canCraft < count || count == -1) {
				count = canCraft;
			}
		}
		
		if(isSmelted) {
			int total = 0;
			HashMap<Integer, ? extends ItemStack> all = inv.all(Material.COAL);
			for(Integer i : all.keySet()) {
				total += all.get(i).getAmount();
			}
			int canCraft = total * 8;
			if(canCraft < count || count == -1) {
				count = canCraft;
			}
		}
		
		return count;
	}
	
	public boolean canCraft(Player p, ArrayList<ItemStack> items, boolean isSmelted, int amount) {
		PlayerInventory inv = p.getInventory();
		for(ItemStack item : items) {
			if(item.isSimilar(new ItemStack(Material.OAK_LEAVES))) {
				if(getTotalLeaves(inv.getStorageContents()) < item.getAmount() * amount) {
					return false;
				}
			}
			else if(item.isSimilar(new ItemStack(Material.RED_MUSHROOM))) {
				if(getTotalMushrooms(inv.getStorageContents()) < item.getAmount() * amount) {
					return false;
				}
			}
			else {
				if(!(inv.containsAtLeast(item, item.getAmount() * amount))) {
					return false;
				}
			}
		}
		if(isSmelted) {
			if(!(inv.containsAtLeast(new ItemStack(Material.COAL), (int) Math.ceil(amount / 8)))) {
				return false;
			}
		}
		return true;
	}
	
	public void craftRecipeMax(Player p, Economy econ, ArrayList<ItemStack> recipe, ItemStack result, boolean isSmelted, String name) {
		String[] id = result.getItemMeta().getLore().get(0).split(" ");
		String perm = "";
		if(id[0].contains("Ingredient")) {
			perm = "recipes.Ingr" + id[1];
		}
		else if(id[0].contains("Tier")) {
			perm = "recipes.t" + id[1] + "r" + id[3];
		}
		else if(id[0].contains("Limited")) {
			perm = "recipes.tler" + id[3];
		}
		else if(id[0].contains("Legendary")) {
			perm = "recipes.tlegend" + id[2];
		}
		if(p.hasPermission(perm)) {
			PlayerInventory inv = p.getInventory();
			int amount = getMaxCraftable(p, recipe, isSmelted);
			if(econ.has(p, CRAFT_COST * amount)) {
				if(amount > 0) {
					for(ItemStack item : recipe) {
						// Edge case for OAK_LEAVES
						if(item.isSimilar(new ItemStack(Material.OAK_LEAVES))) {
							removeLeaves(inv, item.getAmount() * amount);
						}
						// Edge case for mushrooms
						else if(item.isSimilar(new ItemStack(Material.RED_MUSHROOM))) {
							removeMushrooms(inv, item.getAmount() * amount);
						}
						// Normal case
						else {
							inv.removeItem(util.setAmount(item, item.getAmount() * amount));
						}
						
						// Return buckets
						if(item.isSimilar(new ItemStack(Material.WATER_BUCKET))) {
							inv.addItem(new ItemStack(Material.BUCKET, amount));
						}
						if(item.isSimilar(new ItemStack(Material.MILK_BUCKET))) {
							inv.addItem(new ItemStack(Material.BUCKET, amount));
						}
						if(item.isSimilar(new ItemStack(Material.LAVA_BUCKET))) {
							inv.addItem(new ItemStack(Material.BUCKET, amount));
						}
					}
					if(isSmelted) {
						inv.removeItem(util.setAmount(new ItemStack(Material.COAL), (int) Math.ceil((double)(amount / 8))));
					}
					econ.withdrawPlayer(p, CRAFT_COST * amount);
					inv.addItem(util.setAmount(result, amount * NUM_PER_CRAFT));
					util.sendMessage(p, "&7Successfully crafted &e" + amount * NUM_PER_CRAFT + " " + name);
				}
				else {
					util.sendMessage(p, "&cYou lack the ingredients to craft any of this recipe!");
				}
			}
			else {
				util.sendMessage(p, "&cYou lack the gold to craft " + amount + " of this recipe!");
			}
		}
		else {
			util.sendMessage(p, "&cYou have not yet unlocked this recipe!");
		}
	}
	
	public void craftRecipe(Player p, Economy econ, int amount, ArrayList<ItemStack> recipe, ItemStack result, boolean isSmelted, String name) {
		String[] id = result.getItemMeta().getLore().get(0).split(" ");
		String perm = "";
		if(id[0].contains("Ingredient")) {
			perm = "recipes.Ingr" + id[1];
		}
		else if(id[0].contains("Tier")) {
			perm = "recipes.t" + id[1] + "r" + id[3];
		}
		else if(id[0].contains("Limited")) {
			perm = "recipes.tler" + id[3];
		}
		else if(id[0].contains("Drink")) {
			perm = "recipes.drink" + id[3];
		}
		else if(id[0].contains("Legendary")) {
			perm = "recipes.tlegend" + id[2];
		}
		if(p.hasPermission(perm)) {
			PlayerInventory inv = p.getInventory();
			if(canCraft(p, recipe, isSmelted, amount)) {
				if(econ.has(p, CRAFT_COST * amount)) {
					for(ItemStack item : recipe) {
						// Edge case for OAK_LEAVES
						if(item.isSimilar(new ItemStack(Material.OAK_LEAVES))) {
							removeLeaves(inv, item.getAmount() * amount);
						}
						// Edge case for mushrooms
						else if(item.isSimilar(new ItemStack(Material.RED_MUSHROOM))) {
							removeMushrooms(inv, item.getAmount() * amount);
						}
						// Normal case
						else {
							inv.removeItem(util.setAmount(item, item.getAmount() * amount));
						}
						
						// Return buckets
						if(item.equals(new ItemStack(Material.WATER_BUCKET))) {
							inv.addItem(new ItemStack(Material.BUCKET, amount));
						}
						if(item.equals(new ItemStack(Material.MILK_BUCKET))) {
							inv.addItem(new ItemStack(Material.BUCKET, amount));
						}
					}
					if(isSmelted) {
						inv.removeItem(util.setAmount(new ItemStack(Material.COAL), (int) Math.ceil((double)(amount / 8))));
					}
					econ.withdrawPlayer(p, CRAFT_COST * amount);
					inv.addItem(util.setAmount(result, amount * NUM_PER_CRAFT));
					util.sendMessage(p, "&7Successfully crafted &e" + amount * NUM_PER_CRAFT + " " + name);
				}
				else {
					util.sendMessage(p, "&cYou lack the gold to craft " + amount + " of this recipe!");
				}
			}
			else {
				util.sendMessage(p, "&cYou lack the materials to craft " + amount + " of this recipe!");
			}
		}
		else {
			util.sendMessage(p, "&cYou have not yet unlocked this recipe!");
		}
	}
}
