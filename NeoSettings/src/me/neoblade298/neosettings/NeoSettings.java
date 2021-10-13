package me.neoblade298.neosettings;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neosettings.objects.Settings;

public class NeoSettings extends JavaPlugin implements org.bukkit.event.Listener {
	private HashMap<String, Settings> settings;
	// SQL
	public String url, user, pass;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoSettings Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("settings").setExecutor(new Commands(this));
	    
	    loadConfig();
	    loadBuiltinSettings();
	}
	
	public void onDisable() {
	    saveAll();
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoSettings Disabled");
	    super.onDisable();
	}
	
	public void loadConfig() {
	    this.settings = new HashMap<String, Settings>();

		// Save config if doesn't exist
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		ConfigurationSection cfg = YamlConfiguration.loadConfiguration(file);

		// SQL
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		url = "jdbc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		user = sql.getString("username");
		pass = sql.getString("password");
	}
	
	private void loadBuiltinSettings() {
		// Settings testSettings = createSettings("Test", this);
		// testSettings.addSetting("StringTest", "Default");
		// testSettings.addSetting("BooleanTest", false);
		// testSettings.addSetting("IntegerTest", 5);
	}
	
	public Settings createSettings(String key, Plugin plugin) {
		Bukkit.getLogger().log(Level.INFO, "[NeoSettings] Created setting of " + key + " for plugin " + plugin.getName() + ".");
		Settings newSettings = new Settings(this, key);
		settings.put(key, newSettings);
		return newSettings;
	}
	
	public boolean changeSetting(String setting, String subsetting, String value, UUID uuid) {
		if (this.settings.containsKey(setting)) {
			return this.settings.get(setting).changeSetting(subsetting, value, uuid);
		}
		Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + setting + "." + subsetting + " for " + uuid + ". Setting doesn't exist.");
		return false;
	}
	
	public boolean resetSetting(String setting, String subsetting, UUID uuid) {
		if (this.settings.containsKey(setting)) {
			return this.settings.get(setting).resetSetting(subsetting, uuid);
		}
		Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to reset setting of " + setting + "." + subsetting + " for " + uuid + ". Setting doesn't exist.");
		return false;
	}
	
	public Settings getSettings(String key) {
		if (!settings.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + key + ". Setting doesn't exist.");
			return null;
		}
		return settings.get(key);
	}
	
	public HashMap<String, Settings> getAllSettings() {
		return settings;
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		BukkitRunnable load = new BukkitRunnable() {
			public void run() {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection(url, user, pass);
					for (String key : settings.keySet()) {
						settings.get(key).load(con, e.getPlayer().getUniqueId());
					}
					con.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		};
		load.runTaskLaterAsynchronously(this, 60L);
	}
	
	
	public void handleLeave(UUID uuid) {
		BukkitRunnable save = new BukkitRunnable() {
			public void run() {
				try {
					Class.forName("com.mysql.jdbc.Driver");
					Connection con = DriverManager.getConnection(url, user, pass);
					Statement stmt = con.createStatement();
					for (String key : settings.keySet()) {
						settings.get(key).save(con, stmt, uuid);
					}
					stmt.executeBatch();
					con.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		save.runTaskAsynchronously(this);
	}
	
	private void saveAll() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DriverManager.getConnection(url, user, pass);
			Statement stmt = con.createStatement();
			for (Player p : Bukkit.getOnlinePlayers()) {
				for (String key : settings.keySet()) {
					settings.get(key).save(con, stmt, p.getUniqueId());
				}
			}
			stmt.executeBatch();
			con.close();
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		handleLeave(e.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onKick(PlayerKickEvent e) {
		handleLeave(e.getPlayer().getUniqueId());
	}
}
