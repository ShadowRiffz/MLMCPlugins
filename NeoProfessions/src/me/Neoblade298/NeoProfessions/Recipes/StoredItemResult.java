package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.Neoblade298.NeoProfessions.Managers.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class StoredItemResult implements RecipeResult {
	StoredItem item;
	int amount;

	public StoredItemResult(String key, String[] lineArgs) {
		this.amount = 1;
		
		for (String arg : lineArgs) {
			if (arg.startsWith("id")) {
				int id = Integer.parseInt(arg.substring(arg.indexOf(':') + 1));
				item = StorageManager.getItemDefinitions().get(id);
			}
			else if (arg.startsWith("amount")) {
				this.amount = Integer.parseInt(arg.substring(arg.indexOf(':') + 1));
			}
		}
		
		// Add to source
		item.addSource("§7Crafted", false);
		item.addRelevantRecipe(key);
	}

	@Override
	public void giveResult(Player p) {
		StorageManager.givePlayer(p, item.getId(), amount);
	}
	
	@Override
	public ItemStack getResultItem(Player p, boolean canCraft) {
		ItemStack item = this.item.getStorageView(amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName((canCraft ? "§a" : "§c") + ChatColor.stripColor(meta.getDisplayName()));
		item.setItemMeta(meta);
		return item;
	}
}
