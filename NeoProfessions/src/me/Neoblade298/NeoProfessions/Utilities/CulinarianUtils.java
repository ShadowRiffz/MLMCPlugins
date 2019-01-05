package me.Neoblade298.NeoProfessions.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.milkbowl.vault.economy.Economy;

public class CulinarianUtils {
	
	static final int CRAFT_COST = 50;

	public static int getFoodLevel(ItemStack item) {
		if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
			String line = item.getItemMeta().getLore().get(0);
			if(line.contains("Tier 1")) {
				return 1;
			}
			else if(line.contains("Tier 2")) {
				return 2;
			}
			else if(line.contains("Tier 3") || line.contains("Limited Edition")) {
				return 3;
			}
			else if(line.contains("Legendary")) {
				return 4;
			}
		}
		return -1;
	}
	
	public static int getMaxCraftable(Player p, ArrayList<ItemStack> items, boolean isSmelted) {
		int count = 0;
		PlayerInventory inv = p.getInventory();
		boolean canCraft = true;
		if(isSmelted) {
			int fuel = 0;
			HashMap<Material, Integer> fuels = new HashMap<Material, Integer>();
			fuels.put(Material.COAL, 0);
			while(canCraft) {
				for(ItemStack item : items) {
					if(!(inv.containsAtLeast(item, item.getAmount() * count))) {
						canCraft = false;
						break;
					}
				}
				if(canCraft && fuel < count + 1) {
					if(inv.containsAtLeast(new ItemStack(Material.COAL), fuels.get(Material.COAL) + 1)) {
						fuels.put(Material.COAL, fuels.get(Material.COAL) + 1);
						fuel += 8;
					}
					else {
						canCraft = false;
						break;
					}
				}
				if(canCraft) {
					count++;
				}
				else {
					count--;
				}
			}
		}
		else {
			while(canCraft) {
				for(ItemStack item : items) {
					if(!(inv.containsAtLeast(item, item.getAmount() * count))) {
						canCraft = false;
						break;
					}
				}
				if(canCraft) {
					count++;
				}
				else {
					count--;
				}
			}
		}
		return count;
	}
	
	public static boolean canCraft(Player p, ArrayList<ItemStack> items, boolean isSmelted, int amount) {
		PlayerInventory inv = p.getInventory();
		for(ItemStack item : items) {
			if(!(inv.containsAtLeast(item, item.getAmount() * amount))) {
				return false;
			}
		}
		if(isSmelted) {
			if(!(inv.containsAtLeast(new ItemStack(Material.COAL), (int) Math.ceil(amount / 8)))) {
				return false;
			}
		}
		return true;
	}
	
	public static void craftRecipeMax(Player p, Economy econ, ArrayList<ItemStack> recipe, ItemStack result, boolean isSmelted, String name) {
		PlayerInventory inv = p.getInventory();
		int amount = CulinarianUtils.getMaxCraftable(p, recipe, true);
		if(econ.has(p, CRAFT_COST * amount)) {
			if(amount > 0) {
				for(ItemStack item : recipe) {
					inv.removeItem(Util.setAmount(item, item.getAmount() * amount));
					if(item.equals(new ItemStack(Material.WATER_BUCKET))) {
						inv.addItem(new ItemStack(Material.BUCKET, amount));
					}
					if(item.equals(new ItemStack(Material.MILK_BUCKET))) {
						inv.addItem(new ItemStack(Material.BUCKET, amount));
					}
				}
				if(isSmelted) {
					inv.removeItem(Util.setAmount(new ItemStack(Material.COAL), (int) Math.ceil(amount / 8)));
				}
				econ.withdrawPlayer(p, CRAFT_COST * amount);
				inv.addItem(Util.setAmount(result, amount));
				Util.sendMessage(p, "&7Successfully crafted &e" + amount + " " + name);
			}
			else {
				Util.sendMessage(p, "&cYou lack the ingredients to craft any of this recipe!");
			}
		}
		else {
			Util.sendMessage(p, "&cYou lack the gold to craft " + amount + " of this recipe!");
		}
	}
	
	public static void craftRecipe(Player p, Economy econ, int amount, ArrayList<ItemStack> recipe, ItemStack result, boolean isSmelted, String name) {
		PlayerInventory inv = p.getInventory();
		if(CulinarianUtils.canCraft(p, recipe, true, amount)) {
			if(econ.has(p, CRAFT_COST * amount)) {
				for(ItemStack item : recipe) {
					inv.removeItem(Util.setAmount(item, item.getAmount() * amount));
					if(item.equals(new ItemStack(Material.WATER_BUCKET))) {
						inv.addItem(new ItemStack(Material.BUCKET, amount));
					}
					if(item.equals(new ItemStack(Material.MILK_BUCKET))) {
						inv.addItem(new ItemStack(Material.BUCKET, amount));
					}
				}
				if(isSmelted) {
					inv.removeItem(Util.setAmount(new ItemStack(Material.COAL), (int) Math.ceil(amount)));
				}
				econ.withdrawPlayer(p, CRAFT_COST * amount);
				inv.addItem(Util.setAmount(result, amount));
				Util.sendMessage(p, "&7Successfully crafted &e" + amount + " " + name);
			}
			else {
				Util.sendMessage(p, "&cYou lack the gold to craft " + amount + " of this recipe!");
			}
		}
		else {
			Util.sendMessage(p, "&cYou lack the materials to craft " + amount + " of this recipe!");
		}
	}
}
