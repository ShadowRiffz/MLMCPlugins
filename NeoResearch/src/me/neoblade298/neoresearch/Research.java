package me.neoblade298.neoresearch;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMobDeathEvent;

public class Research extends JavaPlugin implements org.bukkit.event.Listener {
	// SQL
	public String url, user, pass;
	public HashMap<String, HashMap<String, Integer>> researchItems;
	public HashMap<String, ArrayList<String>> mobMap;
	public HashMap<String, Integer> researchBookMin;
	public HashMap<UUID, PlayerStats> playerStats;
	public HashMap<Integer, Integer> toNextLvl;

	private String broadcast;
	private String permcmd;

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoResearch Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		this.getCommand("nr").setExecutor(new Commands(this));

		load_config();
	}

	public void onDisable() {
		org.bukkit.Bukkit.getServer().getLogger().info("NeoResearch Disabled");
		super.onDisable();
	}

	public void load_config() {
		File file = new File(getDataFolder(), "config.yml");

		// Save config if doesn't exist
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		ConfigurationSection cfg = YamlConfiguration.loadConfiguration(file);

		// SQL
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		url = "jbdc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		user = sql.getString("username");
		pass = sql.getString("password");

		// General
		ConfigurationSection general = cfg.getConfigurationSection("general");
		broadcast = general.getString("research_complete_broadcast").replaceAll("&", "§");
		permcmd = general.getString("permission_command");

		// Exp
		toNextLvl = new HashMap<Integer, Integer>();
		ConfigurationSection exp = cfg.getConfigurationSection("exp");
		for (String lvl : exp.getKeys(false)) {
			toNextLvl.put(Integer.parseInt(lvl), exp.getInt(lvl));
		}

		// Load research items into mobMap, researchItems,
		researchItems = new HashMap<String, HashMap<String, Integer>>();
		mobMap = new HashMap<String, ArrayList<String>>();
		ConfigurationSection rItems = cfg.getConfigurationSection("research_items");

		for (String rItem : rItems.getKeys(false)) {
			HashMap<String, Integer> obj = new HashMap<String, Integer>();
			ConfigurationSection sec = rItems.getConfigurationSection(rItem);
			for (String mob : rItems.getKeys(false)) {
				obj.put(mob, sec.getInt(mob));

				// Add to mob map and research book min
				if (mobMap.containsKey(mob)) {
					mobMap.get(mob).add(rItem);
					if (researchBookMin.get(mob) < sec.getInt(mob))
						researchBookMin.put(mob, sec.getInt(mob));
				}
				else {
					ArrayList<String> list = new ArrayList<String>();
					list.add(rItem);
					mobMap.put(mob, list);
					researchBookMin.put(mob, sec.getInt(mob));
				}
			}
			researchItems.put(rItem, obj);
		}
	}

	@EventHandler
	public void onMobKill(MythicMobDeathEvent e) {
		if (e.getKiller() instanceof Player) {
			Player p = (Player) e.getKiller();
			String mob = e.getMobType().getInternalName();

			// First update the mob kill stat
			PlayerStats stats = playerStats.get(p.getUniqueId());
			HashMap<String, Integer> mobKills = stats.getMobKills();
			mobKills.put(mob, mobKills.get(mob) + 1);

			// Update research points
			HashMap<String, Integer> researchPoints = stats.getResearchPoints();
			int points = researchPoints.get(mob) + 1;
			researchPoints.put(mob, points);

			// Check for research goals that need it
			TreeSet<String> completedItems = stats.getCompletedResearchItems();
			for (String researchItem : mobMap.get(mob)) { // For each relevant research item
				if (!completedItems.contains(researchItem)) { // If the player hasn't completed it
					// Check if research goal is completed for specific mob
					if (researchItems.get(researchItem).get(mob) <= points) {
						for (String rMob : researchItems.get(researchItem).keySet()) { // Check every objective
							if (researchPoints.get(rMob) <= researchItems.get(researchItem).get(rMob)) {
								return; // Haven't completed every item
							}
						}

						// Completed a research item
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								broadcast.replaceAll("%player%", p.getName()).replaceAll("%item%", researchItem));
						completedItems.add(researchItem);
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
								permcmd.replaceAll("%player%", p.getName()).replaceAll("%item%", researchItem));
					}
				}
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		UUID uuid = p.getUniqueId();
		
		// First check if the account was already loaded
		if (!playerStats.containsKey(uuid)) {
			
			// Asynchronously look up sql and load it in
			BukkitRunnable load = new BukkitRunnable() {
				public void run() {
					try {
						Class.forName("com.mysql.jdbc.Driver");
						Connection con = DriverManager.getConnection(url, user, pass);
						Statement stmt = con.createStatement();
						ResultSet rs = stmt.executeQuery("SELECT * FROM research_accounts WHERE uuid = '" + uuid + "';");
						
						int level = 0, exp = 0;
						TreeSet<String> 
						// Load in account info
						if (rs.next()) {
							level = rs.getInt(2);
							exp = rs.getInt(3);

							rs = stmt.executeQuery("SELECT * FROM research_statistics WHERE uuid = '" + uuid + "';");
							while (rs.next()) {
								
							}

							rs = stmt.executeQuery("SELECT * FROM research_points WHERE uuid = '" + uuid + "';");
							while (rs.next()) {

							}
							
							rs = stmt.executeQuery("SELECT * FROM research_items WHERE uuid = '" + uuid + "';");
							while (rs.next()) {

							}
						}
						
						// No account info, initialize a blank one
						else {
							
						}
					} catch (Exception ex) {
						System.out.println(ex);
					}
				}
			};
			load.runTaskAsynchronously(this);
		}
	}
}
