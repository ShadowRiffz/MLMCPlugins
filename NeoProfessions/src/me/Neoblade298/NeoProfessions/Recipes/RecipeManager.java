package me.Neoblade298.NeoProfessions.Recipes;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import me.Neoblade298.NeoProfessions.Professions;

public class RecipeManager {
	Professions main;
	public static HashMap<UUID, HashSet<String>> knowledge = new HashMap<UUID, HashSet<String>>();
	public static HashMap<String, Recipe> recipes;
	public RecipeManager(Professions main) {
		this.main = main;
		
		loadRecipes(new File(main.getDataFolder(), "recipes"));
	}
	
	private void loadRecipes(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadRecipes(file);
			}
			else {
				YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
				for (String key : yaml.getKeys(false)) {
					ConfigurationSection sec = yaml.getConfigurationSection(key);
					
					// Components
					
					
					// Requirements
					ArrayList<String> stringReqs = (ArrayList<String>) sec.getStringList("requirements");
					ArrayList<RecipeRequirement> reqs = new ArrayList<RecipeRequirement>();
					for (String line : stringReqs) {
						String[] args = line.split(" ");
						if (args[0].equalsIgnoreCase("recipe")) {
							reqs.add(new KnowledgeRequirement(args));
						}
					}
				}
			}
		}
	}
}
