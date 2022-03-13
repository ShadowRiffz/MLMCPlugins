package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.CurrencyManager;

public class EssenceRequirement implements RecipeRequirement {
	int amount;
	int level;
	
	public EssenceRequirement(String[] lineArgs) {
		this.amount = 500;
		this.level = 5;
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("amount")) {
				this.amount = Integer.parseInt(args[1]);
			}
			else if (args[0].equalsIgnoreCase("level")) {
				this.level = Integer.parseInt(args[1]);
			}
		}
	}
	
	public boolean passesReq(Player p) {
		return CurrencyManager.hasEnough(p, "essence", level, amount);
	}

	public String failMessage(Player p) {
		return "§4[§c§lMLMC§4] §cYou don't have enough level " + level + " essence!";
	}
	
	public void useReq(Player p) {
		CurrencyManager.subtract(p, "essence", level, amount);
	}
}
