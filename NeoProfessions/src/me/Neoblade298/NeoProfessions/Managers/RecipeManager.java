package me.Neoblade298.NeoProfessions.Managers;

import java.io.File;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Objects.IOComponent;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
import me.Neoblade298.NeoProfessions.Recipes.AugmentResult;
import me.Neoblade298.NeoProfessions.Recipes.FoodResult;
import me.Neoblade298.NeoProfessions.Recipes.GardenSizeRequirement;
import me.Neoblade298.NeoProfessions.Recipes.GardenUpgradeResult;
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
	private static HashMap<UUID, HashSet<String>> knowledge = new HashMap<UUID, HashSet<String>>();
	private static HashMap<String, List<String>> recipeLists = new HashMap<String, List<String>>();
	private static HashMap<String, Recipe> recipes = new HashMap<String, Recipe>();
	
	public RecipeManager(Professions main) {
		this.main = main;
		
		loadRecipes(new File(main.getDataFolder(), "recipes"));
		
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "recipe-lists.yml"));
		for (String key : cfg.getKeys(false)) {
			List<String> rlist = cfg.getStringList(key);
			ArrayList<String> files = new ArrayList<String>();
			Iterator<String> iter = rlist.iterator();
			while (iter.hasNext()) {
				String recipe = iter.next();
				if (recipe.startsWith("file:")) {
					iter.remove();
					files.add(recipe.substring(recipe.indexOf(':') + 1));
				}
			}
			for (String file : files) {
				for (String recipe : YamlConfiguration.loadConfiguration(new File (main.getDataFolder(), "recipes/" + file)).getKeys(false)) {
					rlist.add(recipe);
				}
			}
			recipeLists.put(key, rlist);
		}
		
		// Add a recipe list for all recipes
		List<String> allList = new ArrayList<String>();
		for (String recipe : recipes.keySet()) {
			allList.add(recipe);
		}
		recipeLists.put("all", allList);
	}
	
	private void loadRecipes(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadRecipes(file);
			}
			else {
				YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
				for (String key : yaml.getKeys(false)) {
					try {
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
							StoredItemInstance si = new StoredItemInstance(StorageManager.getItem(id), amount);
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
							else if (args[0].equalsIgnoreCase("garden-size")) {
								reqs.add(new GardenSizeRequirement(args));
							}
						}
						
						int level = sec.getInt("level");
						if (level != 0) {
							reqs.add(new LevelRequirement(ProfessionType.CRAFTER, level));
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
						else if (resultArgs[0].startsWith("augment")) {
							result = new AugmentResult(resultArgs);
						}
						else if (resultArgs[0].startsWith("garden-upgrade")) {
							result = new GardenUpgradeResult(resultArgs);
						}
						
						String display = sec.getString("display");
						int exp = sec.getInt("exp", -1);
						boolean canMulticraft = sec.getBoolean("can-multicraft");
						recipes.put(key, new Recipe(key, display, exp, level, reqs, components, result, canMulticraft));
					}
					catch (Exception e) {
						Bukkit.getLogger().log(Level.WARNING, "[NeoProfessions] Failed to load Recipe: " + key);
						e.printStackTrace();
					}
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
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init recipes for user " + p.getName());
			e.printStackTrace();
		}
	}
	
	public static void convertPlayer(UUID uuid, HashSet<String> knowledge, Statement stmt) {
		try {
			for (String key : knowledge) {
				stmt.addBatch("REPLACE INTO professions_knowledge "
						+ "VALUES ('" + uuid + "', '" + key + "');");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save recipes for user " + uuid);
			e.printStackTrace();
		}
	}

	@Override
	public void savePlayer(Player p, Statement stmt) {
		UUID uuid = p.getUniqueId();
		try {
			for (String key : knowledge.get(uuid)) {
				stmt.addBatch("REPLACE INTO professions_knowledge "
						+ "VALUES ('" + uuid + "', '" + key + "');");
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save recipes for user " + p.getName());
			e.printStackTrace();
		}
	}
	
	public static HashSet<String> getKnowledge(Player p) {
		return knowledge.get(p.getUniqueId());
	}
	
	@Override
	public String getComponentName() {
		return "RecipeManager";
	}
	
	public static List<String> getRecipeList(String key) {
		return recipeLists.get(key);
	}
	
	public static boolean hasKnowledge(Player p, String key) {
		if (knowledge.containsKey(p.getUniqueId())) {
			return knowledge.get(p.getUniqueId()).contains(key);
		}
		return false;
	}
	
	public static Recipe getRecipe(String key) {
		return recipes.get(key);
	}
	
	public static void giveKnowledge(Player p, String key) {
		giveKnowledge(p.getUniqueId(), key);
	}
	
	public static void giveKnowledge(UUID uuid, String key) {
		knowledge.get(uuid).add(key);
	}
}
