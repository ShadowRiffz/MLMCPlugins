package me.Neoblade298.NeoProfessions.Recipes;

import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Managers.AugmentManager;
import net.md_5.bungee.api.ChatColor;

public class AugmentResult implements RecipeResult {
	String type;
	int level;

	public AugmentResult(String[] lineArgs) {
		this.type = "default";
		this.level = 5;
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("type")) {
				this.type = args[1];
			}
			else if (args[0].equalsIgnoreCase("level")) {
				this.level = Integer.parseInt(args[1]);
			}
		}
	}

	@Override
	public void giveResult(Player p) {
		HashMap<Integer, ItemStack> failed = p.getInventory().addItem(AugmentManager.augmentMap.get(type).get(level).getItem(p));
		for (Integer i : failed.keySet()) {
			p.getWorld().dropItem(p.getLocation(), failed.get(i));
		}
	}
	
	@Override
	public ItemStack getResultItem(Player p, boolean canCraft) {
		ItemStack item = AugmentManager.augmentMap.get(type).get(level).getItem(p);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName((canCraft ? "§a" : "§c") + ChatColor.stripColor(meta.getDisplayName()));
		item.setItemMeta(meta);
		return item;
	}
}
