package me.neoblade298.neocore;

import java.io.File;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.events.NeoCoreInitEvent;
import me.neoblade298.neocore.listeners.IOListener;

public class NeoCore extends JavaPlugin implements org.bukkit.event.Listener {
	private static NeoCore inst;
	public static String sqlUser, sqlPass, connection;
	public static Properties properties = new Properties();
	
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("NeoCore Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new IOListener(), this);
		
		// SQL
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "config.yml"));
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		connection = "jdbc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		sqlUser = sql.getString("username");
		sqlPass = sql.getString("password");
		properties.setProperty("useSSL", "false");
		properties.setProperty("user", sqlUser);
		properties.setProperty("password", sqlPass);
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.getPluginManager().callEvent(new NeoCoreInitEvent());
			}
		}.runTask(this);
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoCore Disabled");
	    super.onDisable();
	}
	
	public static NeoCore inst() {
		return inst;
	}
	
}
