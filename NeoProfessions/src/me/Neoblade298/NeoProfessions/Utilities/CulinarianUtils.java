package me.Neoblade298.NeoProfessions.Utilities;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class CulinarianUtils {

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
			while(canCraft) {
				for(ItemStack item : items) {
					if(!(inv.containsAtLeast(item, item.getAmount() * count))) {
						canCraft = false;
						break;
					}
				}
				if(fuel < count + 1) {
					if(inv.containsAtLeast(new ItemStack(Material.COAL), fuels.get(Material.COAL) + 1)) {
						fuels.put(Material.COAL, fuels.get(Material.COAL) + 1);
						fuel += 8;
					}
					else {
						canCraft = false;
						break;
					}
				}
				count++;
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
				count++;
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
}
