package me.neoblade298.neosettings;

import java.io.File;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neocore.listeners.IOListener;
import me.neoblade298.neosettings.objects.Settings;

public class NeoSettings extends JavaPlugin implements Listener, IOComponent {
	private HashMap<String, Settings> settings;
	// SQL
	public String url, user, pass;
	public boolean debug;
	public HashMap<UUID, Long> lastSave;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoSettings Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("settings").setExecutor(new Commands(this));
	    this.debug = false;
	    
	    IOListener.register(this, this);
	    loadConfig();
	    loadBuiltinSettings();
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoSettings Disabled");
	    super.onDisable();
	}
	
	public void loadConfig() {
	    this.settings = new HashMap<String, Settings>();
	    this.lastSave = new HashMap<UUID, Long>();

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
		// Settings testSettings = createSettings("Test", this, true);
		// testSettings.addSetting("StringTest", "Default");
		// testSettings.addSetting("BooleanTest", false);
		// testSettings.addSetting("IntegerTest", 5);
	}
	
	public Settings createSettings(String key, Plugin plugin, boolean hidden) {
		if (settings.containsKey(key)) {
			Bukkit.getLogger().log(Level.INFO, "[NeoSettings] Setting " + key + " for plugin " + plugin.getName() + " already exists. Returning existing setting.");
			return settings.get(key);
		}
		Bukkit.getLogger().log(Level.INFO, "[NeoSettings] Created setting of " + key + " for plugin " + plugin.getName() + ", hidden: " + hidden + ".");
		Settings newSettings = new Settings(this, key, hidden);
		settings.put(key, newSettings);
		return newSettings;
	}
	
	public boolean changeSetting(String setting, String subsetting, String value, UUID uuid, boolean canAccessHidden) {
		if (!this.settings.containsKey(setting)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + setting + "." + subsetting + " for " + uuid + ". Setting doesn't exist.");
			return false;
		}
		if (settings.get(setting).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + setting + ". Setting is hidden.");
			return false;
		}
		return this.settings.get(setting).changeSetting(subsetting, value, uuid);
	}
	
	public boolean addToSetting(String setting, String subsetting, int value, UUID uuid, boolean canAccessHidden) {
		if (!this.settings.containsKey(setting)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to change setting of " + setting + "." + subsetting + " for " + uuid + ". Setting doesn't exist.");
			return false;
		}
		if (settings.get(setting).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + setting + ". Setting is hidden.");
			return false;
		}
		return this.settings.get(setting).addToSetting(subsetting, value, uuid);
	}
	
	public boolean resetSetting(String setting, String subsetting, UUID uuid, boolean canAccessHidden) {
		if (this.settings.containsKey(setting)) {
			return this.settings.get(setting).resetSetting(subsetting, uuid);
		}
		if (settings.get(setting).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + setting + ". Setting is hidden.");
			return false;
		}
		Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to reset setting of " + setting + "." + subsetting + " for " + uuid + ". Setting doesn't exist.");
		return false;
	}
	
	public Settings getSettings(String key, boolean canAccessHidden) {
		if (!settings.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + key + ". Setting doesn't exist.");
			return null;
		}
		if (settings.get(key).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + key + ". Setting is hidden.");
			return null;
		}
		return settings.get(key);
	}
	
	public boolean exists(String key, String subkey, UUID uuid, boolean canAccessHidden) {
		if (!settings.containsKey(key)) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + key + ". Setting doesn't exist.");
			return false;
		}
		if (settings.get(key).isHidden() && !canAccessHidden) {
			Bukkit.getLogger().log(Level.WARNING, "[NeoSettings] Failed to get setting of " + key + ". Setting is hidden.");
			return false;
		}
		return settings.get(key).exists(subkey, uuid);
	}
	
	public HashMap<String, Settings> getAllSettings() {
		return settings;
	}

	@Override
	public void loadPlayer(OfflinePlayer p, Statement stmt) {
		settings.get(key)
		for (String key : settings.keySet()) {
			settings.get(key).load(stmt, p.getUniqueId());
		}
	}
	
	@Override
	public void savePlayer(Player p, Statement stmt) {
		for (String key : settings.keySet()) {
			settings.get(key).save(stmt, p.getUniqueId());
		}
	}

	@Override
	public void cleanup(Statement stmt) {
		for (Player p : Bukkit.getOnlinePlayers()) {
			savePlayer(p, stmt);
		}
	}

	@Override
	public String getKey() {
		return "SettingsManager";
	}
}
