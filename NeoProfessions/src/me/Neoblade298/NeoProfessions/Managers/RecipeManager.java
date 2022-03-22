package me.Neoblade298.NeoProfessions.Managers;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Objects.IOComponent;
import me.Neoblade298.NeoProfessions.Recipes.FoodResult;
import me.Neoblade298.NeoProfessions.Recipes.GearResult;
import me.Neoblade298.NeoProfessions.Recipes.KnowledgeRequirement;
import me.Neoblade298.NeoProfessions.Recipes.LevelRequirement;
import me.Neoblade298.NeoProfessions.Recipes.Recipe;
import me.Neoblade298.NeoProfessions.Recipes.RecipeRequirement;
import me.Neoblade298.NeoProfessions.Recipes.RecipeResult;
import me.Neoblade298.NeoProfessions.Recipes.ResearchRequirement;
import me.Neoblade298.NeoProfessions.Recipes.StoredItemResult;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;

public class RecipeManager implements IOComponent {
	Professions main;
	public static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
	public static HashMap<UUID, HashSet<String>> knowledge = new HashMap<UUID, HashSet<String>>();
	public static HashMap<String, Recipe> recipes = new HashMap<String, Recipe>();
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
					ArrayList<String> storedItemLines = (ArrayList<String>) sec.getStringList("components");
					ArrayList<StoredItemInstance> components = new ArrayList<StoredItemInstance>();
					for (String line : storedItemLines) {
						String[] args = line.split(" ");
						int id = 0;
						int amount = 1;
						for (String arg : args) {
							if (arg.startsWith("id")) {
								id = Integer.parseInt(arg.substring(arg.indexOf(':') + 1));
							}
							else if (arg.startsWith("amount")) {
								amount = Integer.parseInt(arg.substring(arg.indexOf(':') + 1));
							}
						}
						StoredItemInstance si = new StoredItemInstance(StorageManager.getItemDefinitions().get(id), amount);
						si.getItem().addRelevantRecipe(key);
						components.add(si);
					}
					
					// Requirements
					ArrayList<String> stringReqs = (ArrayList<String>) sec.getStringList("requirements");
					ArrayList<RecipeRequirement> reqs = new ArrayList<RecipeRequirement>();
					for (String line : stringReqs) {
						String[] args = line.split(" ");
						if (args[0].equalsIgnoreCase("recipe")) {
							reqs.add(new KnowledgeRequirement(args));
						}
						else if (args[0].equalsIgnoreCase("rgoal")) {
							reqs.add(new ResearchRequirement(args));
						}
						else if (args[0].equalsIgnoreCase("level")) {
							reqs.add(new LevelRequirement(args));
						}
					}
					
					// Results
					String[] resultArgs = sec.getString("result").split(" ");
					RecipeResult result = null;
					if (resultArgs[0].startsWith("gear")) {
						result = new GearResult(resultArgs);
					}
					else if (resultArgs[0].startsWith("food")) {
						result = new FoodResult(resultArgs);
					}
					else if (resultArgs[0].startsWith("storeditem")) {
						result = new StoredItemResult(key, resultArgs);
					}
					
					String display = sec.getString("display");
					int exp = sec.getInt("exp");
					boolean canMulticraft = sec.getBoolean("can-multicraft");
					recipes.put(key, new Recipe(key, display, exp, reqs, components, result, canMulticraft));
				}
			}
		}
	}
	
	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		// Check if player exists already
		if (knowledge.containsKey(p.getUniqueId())) {
			return;
		}

		HashSet<String> keys = new HashSet<String>();
		knowledge.put(p.getUniqueId(), keys);
		
		// Check if player exists on SQL
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM professions_knowledge WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				keys.add(rs.getString(2));
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init storage for user " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void savePlayer(Player p, Statement stmt) {
		UUID uuid = p.getUniqueId();
		try {
			for (String key : knowledge.get(uuid)) {
				stmt.addBatch("REPLACE INTO professions_accounts "
						+ "VALUES ('" + uuid + "', '" + key + "');");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save storage for user " + p.getName());
			e.printStackTrace();
		}
	}
	
	public static HashSet<String> getKnowledge(Player p) {
		return knowledge.get(p.getUniqueId());
	}
}
