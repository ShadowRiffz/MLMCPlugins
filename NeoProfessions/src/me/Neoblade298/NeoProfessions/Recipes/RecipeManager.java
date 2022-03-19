package me.Neoblade298.NeoProfessions.Recipes;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
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
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;
import me.Neoblade298.NeoProfessions.Storage.StoredItemInstance;

public class RecipeManager {
	Professions main;
	public static HashMap<UUID, Long> lastSave = new HashMap<UUID, Long>();
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
					recipes.put(key, new Recipe(key, display, exp, reqs, components, result));
				}
			}
		}
	}
	public static void loadPlayer(OfflinePlayer p) {
		// Check if player exists already
		if (knowledge.containsKey(p.getUniqueId())) {
			return;
		}

		HashSet<String> keys = new HashSet<String>();
		knowledge.put(p.getUniqueId(), keys);
		
		// Check if player exists on SQL
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Professions.connection, Professions.properties);
			Statement stmt = con.createStatement();
			ResultSet rs;
			rs = stmt.executeQuery("SELECT * FROM professions_knowledge WHERE UUID = '" + p.getUniqueId() + "';");
			while (rs.next()) {
				keys.add(rs.getString(2));
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to load or init storage for user " + p.getName());
			e.printStackTrace();
		}
	}

	public static void savePlayer(Player p, Connection con, Statement stmt, boolean savingMultiple) {
		UUID uuid = p.getUniqueId();
		if (lastSave.getOrDefault(uuid, 0L) + 10000 >= System.currentTimeMillis()) {
			// If saved less than 10 seconds ago, don't save again
			return;
		}
		lastSave.put(uuid, System.currentTimeMillis());
		
		try {
			for (String key : knowledge.get(uuid)) {
				stmt.addBatch("REPLACE INTO professions_accounts "
						+ "VALUES ('" + uuid + "', '" + key + "');");
			}
			
			// Set to true if you're saving several accounts at once
			if (!savingMultiple) {
					stmt.executeBatch();
			}
		}
		catch (Exception e) {
			Bukkit.getLogger().log(Level.WARNING, "Professions failed to save storage for user " + p.getName());
			e.printStackTrace();
		}
	}

	public static void saveAll() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
			Statement stmt = con.createStatement();
			for (Player p : Bukkit.getOnlinePlayers()) {
				savePlayer(p, con, stmt, true);
			}
			stmt.executeBatch();
			con.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void handleLeave(Player p) {
		UUID uuid = p.getUniqueId();

		BukkitRunnable save = new BukkitRunnable() {
			public void run() {
				if (knowledge.containsKey(uuid)) {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(Professions.connection, Professions.sqlUser, Professions.sqlPass);
						Statement stmt = con.createStatement();

						// Save account
						savePlayer(p, con, stmt, false);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		};
		save.runTaskAsynchronously(main);
	}
	
	public static HashSet<String> getKnowledge(Player p) {
		return knowledge.get(p.getUniqueId());
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent e) {
		handleLeave(e.getPlayer());
	}
	
	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleLeave(e.getPlayer());
	}

	@EventHandler
	public void onJoin(AsyncPlayerPreLoginEvent e) {
		OfflinePlayer p = Bukkit.getOfflinePlayer(e.getUniqueId());
		loadPlayer(p);
	}
}
