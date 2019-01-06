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
		for(ItemStack item : items) {
			int total = 0;
			HashMap<Integer, ? extends ItemStack> all = inv.all(item);
			for(Integer i : all.keySet()) {
				total += all.get(i).getAmount();
			}
			int canCraft = total / item.getAmount();
			if(canCraft < count || count == 0) {
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
			if(canCraft < count || count == 0) {
				count = canCraft;
			}
		}
		
		return count;
	}
	
	public static boolean canCraft(Player p, ArrayList<ItemStack> items, boolean isSmelted, int amount) {
		PlayerInventory inv = p.getInventory();
		for(ItemStack item : items) {
			// Edge case for leaf blocks having all leaf types usable
			if(item.equals(new ItemStack(Material.LEAVES, item.getAmount()))) {
				if(!(inv.containsAtLeast(item, item.getAmount() * amount)) &&
					!(inv.containsAtLeast(Util.setData(new ItemStack(Material.LEAVES), 1), item.getAmount() * amount)) &&
						!(inv.containsAtLeast(Util.setData(new ItemStack(Material.LEAVES), 2), item.getAmount() * amount)) &&
						!(inv.containsAtLeast(Util.setData(new ItemStack(Material.LEAVES), 3), item.getAmount() * amount)) &&
						!(inv.containsAtLeast(new ItemStack(Material.LEAVES_2), item.getAmount() * amount)) &&
						!(inv.containsAtLeast(Util.setData(new ItemStack(Material.LEAVES_2), 1), item.getAmount() * amount))) {
					return false;
				}
			}
			// Edge case for mushrooms having all types usable
			else if(item.equals(new ItemStack(Material.RED_MUSHROOM, item.getAmount()))) {
				if(!(inv.containsAtLeast(item, item.getAmount() * amount)) &&
				!(inv.containsAtLeast(new ItemStack(Material.BROWN_MUSHROOM), item.getAmount() * amount))) {
					return false;
				}
			}
			// Normal case
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
	
	public static void craftRecipeMax(Player p, Economy econ, ArrayList<ItemStack> recipe, ItemStack result, boolean isSmelted, String name) {
		String[] id = result.getItemMeta().getLore().get(0).split(" ");
		String perm = "";
		if(id[0].contains("Ingredient")) {
			perm = "recipe.Ingr" + id[1];
		}
		else if(id[0].contains("Tier")) {
			perm = "recipe.t" + id[1] + "r" + id[3];
		}
		else if(id[0].contains("Limited")) {
			perm = "recipe.tler" + id[3];
		}
		else if(id[0].contains("Legendary")) {
			perm = "recipe.tlegend" + id[2];
		}
		if(p.hasPermission(perm)) {
			PlayerInventory inv = p.getInventory();
			int amount = CulinarianUtils.getMaxCraftable(p, recipe, true);
			if(econ.has(p, CRAFT_COST * amount)) {
				if(amount > 0) {
					for(ItemStack item : recipe) {
						// Edge case for leaves
						if(item.equals(new ItemStack(Material.LEAVES, item.getAmount()))) {
							if(inv.contains(Util.setAmount(item, item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(item, item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 1), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 1), item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 2), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 2), item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 3), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 3), item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(new ItemStack(Material.LEAVES_2), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(new ItemStack(Material.LEAVES_2), item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 1), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 1), item.getAmount() * amount));
							}
						}
						// Edge case for mushrooms
						else if(item.equals(new ItemStack(Material.RED_MUSHROOM, item.getAmount()))) {
							if(inv.contains(Util.setAmount(item, item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(item, item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(new ItemStack(Material.BROWN_MUSHROOM), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(new ItemStack(Material.BROWN_MUSHROOM), item.getAmount() * amount));
							}
						}
						// Normal case
						else {
							inv.removeItem(Util.setAmount(item, item.getAmount() * amount));
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
		else {
			Util.sendMessage(p, "&cYou have not yet unlocked this recipe!");
		}
	}
	
	public static void craftRecipe(Player p, Economy econ, int amount, ArrayList<ItemStack> recipe, ItemStack result, boolean isSmelted, String name) {
		String[] id = result.getItemMeta().getLore().get(0).split(" ");
		String perm = "";
		if(id[0].contains("Ingredient")) {
			perm = "recipe.Ingr" + id[1];
		}
		else if(id[0].contains("Tier")) {
			perm = "recipe.t" + id[1] + "r" + id[3];
		}
		else if(id[0].contains("Limited")) {
			perm = "recipe.tler" + id[3];
		}
		else if(id[0].contains("Legendary")) {
			perm = "recipe.tlegend" + id[2];
		}
		if(p.hasPermission(perm)) {
			PlayerInventory inv = p.getInventory();
			if(CulinarianUtils.canCraft(p, recipe, true, amount)) {
				if(econ.has(p, CRAFT_COST * amount)) {
					for(ItemStack item : recipe) {
						// Edge case for leaves
						if(item.equals(new ItemStack(Material.LEAVES, item.getAmount()))) {
							if(inv.contains(Util.setAmount(item, item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(item, item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 1), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 1), item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 2), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 2), item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 3), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 3), item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(new ItemStack(Material.LEAVES_2), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(new ItemStack(Material.LEAVES_2), item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 1), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(Util.setData(new ItemStack(Material.LEAVES), 1), item.getAmount() * amount));
							}
						}
						// Edge case for mushrooms
						else if(item.equals(new ItemStack(Material.RED_MUSHROOM, item.getAmount()))) {
							if(inv.contains(Util.setAmount(item, item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(item, item.getAmount() * amount));
							}
							else if(inv.contains(Util.setAmount(new ItemStack(Material.BROWN_MUSHROOM), item.getAmount() * amount))) {
								inv.removeItem(Util.setAmount(new ItemStack(Material.BROWN_MUSHROOM), item.getAmount() * amount));
							}
						}
						// Normal case
						else {
							inv.removeItem(Util.setAmount(item, item.getAmount() * amount));
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
		else {
			Util.sendMessage(p, "&cYou have not yet unlocked this recipe!");
		}
	}
}
