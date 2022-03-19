package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;

public class StoredItemResult implements RecipeResult {
	int id;
	int amount;

	public StoredItemResult(String key, String[] lineArgs) {
		this.id = 0;
		this.amount = 1;
		
		for (String arg : lineArgs) {
			if (arg.startsWith("id")) {
				this.id = Integer.parseInt(arg.substring(arg.indexOf(':') + 1));
			}
			else if (arg.startsWith("amount")) {
				this.amount = Integer.parseInt(arg.substring(arg.indexOf(':') + 1));
			}
		}
		
		// Add to source
		StorageManager.getItemDefinitions().get(id).addSource("§7Crafted by player", false);
		StorageManager.getItemDefinitions().get(id).addRelevantRecipe(key);
	}

	@Override
	public void giveResult(Player p) {
		StorageManager.givePlayer(p, id, amount);
	}
}
