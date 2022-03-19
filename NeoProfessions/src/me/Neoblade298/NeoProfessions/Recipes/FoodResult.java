package me.Neoblade298.NeoProfessions.Recipes;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.Neoblade298.NeoConsumables.Consumables;

public class FoodResult implements RecipeResult {
	String key;
	int amount;

	public FoodResult(String[] lineArgs) {
		this.key = "default";
		this.amount = 1;
		
		for (String arg : lineArgs) {
			if (arg.startsWith("key")) {
				this.key = arg.substring(arg.indexOf(':') + 1);
			}
			else if (arg.startsWith("amount")) {
				this.amount = Integer.parseInt(arg.substring(arg.indexOf(':') + 1));
			}
		}
	}

	@Override
	public void giveResult(Player p) {
		HashMap<Integer, ItemStack> failed = p.getInventory().addItem(Consumables.food.get(this.key).getItem(this.amount));
		for (Integer i : failed.keySet()) {
			p.getWorld().dropItem(p.getLocation(), failed.get(i));
		}
	}
	
	@Override
	public ItemStack getResultItem() {
		return Consumables.food.get(this.key).getItem(this.amount);
	}
}
