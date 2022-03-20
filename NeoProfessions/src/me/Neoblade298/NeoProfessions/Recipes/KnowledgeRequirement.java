package me.Neoblade298.NeoProfessions.Recipes;

import java.util.HashSet;

import org.bukkit.entity.Player;

public class KnowledgeRequirement implements RecipeRequirement {
	String key;
	
	public KnowledgeRequirement(String[] lineArgs) {
		this.key = "default";
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("key")) {
				this.key = args[1];
			}
		}
	}
	
	public boolean passesReq(Player p) {
		HashSet<String> knowledge = RecipeManager.knowledge.get(p.getUniqueId());
		return knowledge != null && knowledge.contains(this.key);
	}

	public String failMessage(Player p) {
		return "§4[§c§lMLMC§4] §cYou lack the recipe!";
	}
	
	public String getLoreString(Player p) {
		String msg = passesReq(p) ? "§a" : "§c";
		msg += "- Recipe knowledge";
		return msg;
	}
}
