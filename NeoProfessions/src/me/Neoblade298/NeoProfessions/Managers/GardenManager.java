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
import me.Neoblade298.NeoProfessions.Recipes.AugmentResult;
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

public class GardenManager implements IOComponent {
	Professions main;
	public static HashMap<UUID, HashSet<String>> gardens = new HashMap<UUID, HashSet<String>>();
	public static HashMap<String, Recipe> recipes = new HashMap<String, Recipe>();
	public GardenManager(Professions main) {
		this.main = main;
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
