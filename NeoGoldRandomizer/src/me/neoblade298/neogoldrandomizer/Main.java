package me.neoblade298.neogoldrandomizer;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	File file;
	FileConfiguration conf;
	static String sqlUser;
	static String sqlPass;
	static String connection;
	static String instanceName;

	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("NeoGoldRandomizer Enabled");
		Bukkit.getServer().getPluginManager().registerEvents(this, this);

		// Save config if doesn't exist
		file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		conf = YamlConfiguration.loadConfiguration(file);

		// Load values from config
		sqlUser = getConfig().getString("sql_user");
		sqlPass = getConfig().getString("sql_pass");
		connection = getConfig().getString("connection");
		connection = getConfig().getString("instance_name");

		getCommand("neogoldrandomizer").setExecutor(new Commands(this));
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoGoldRandomizer Disabled");
		super.onDisable();
	}
}
