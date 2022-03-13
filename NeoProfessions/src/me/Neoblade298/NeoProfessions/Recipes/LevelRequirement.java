package me.Neoblade298.NeoProfessions.Recipes;

import java.util.HashMap;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.PlayerProfessions.Profession;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionManager;

public class LevelRequirement implements RecipeRequirement {
	String type;
	int min;
	
	public LevelRequirement(String[] lineArgs) {
		this.min = 1;
		this.type = "default";
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("type")) {
				this.type = args[1];
			}
			else if (args[0].equalsIgnoreCase("min")) {
				this.min = Integer.parseInt(args[1]);
			}
		}
	}
	
	public boolean passesReq(Player p) {
		HashMap<String, Profession> profs = ProfessionManager.getAccount(p.getUniqueId());
		return profs != null && profs.get(type).getLevel() >= min;
	}

	public String failMessage(Player p) {
		return "§4[§c§lMLMC§4] §cYou aren't a high enough level §e" + type + "!";
	}
}
