package me.Neoblade298.NeoProfessions.Recipes;

public class GearResult implements RecipeResult {
	String type;
	String rarity;
	int level;

	public GearResult(String[] lineArgs) {
		this.type = "sword";
		this.rarity = "common";
		this.level = 5;
		
		for (String lineArg : lineArgs) {
			String[] args = lineArg.split(":");
			if (args[0].equalsIgnoreCase("type")) {
				this.type = args[1];
			}
			else if (args[0].equalsIgnoreCase("rarity")) {
				this.rarity = args[1];
			}
			else if (args[0].equalsIgnoreCase("level")) {
				this.level = Integer.parseInt(args[1]);
			}
		}
	}
}
