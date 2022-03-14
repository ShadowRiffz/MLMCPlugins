package me.Neoblade298.NeoProfessions.Recipes;

import org.bukkit.entity.Player;

import me.neoblade298.neoresearch.Research;

public class ResearchRequirement implements RecipeRequirement {
	String key;
	
	public ResearchRequirement(String[] lineArgs) {
		this.key = "default";
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("key")) {
				this.key = args[1];
			}
		}
	}
	
	public boolean passesReq(Player p) {
		return Research.getPlayerStats(p).getCompletedResearchItems().containsKey(key);
	}

	public String failMessage(Player p) {
		String name = Research.getPlayerStats(p).getCompletedResearchItems().get(key).getName();
		return "�4[�c�lMLMC�4] �cYou don't have the research goal " + name + "completed!";
	}
}