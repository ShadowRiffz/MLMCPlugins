package me.neoblade298.neocore;

import java.io.File;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.events.NeoCoreInitEvent;
import me.neoblade298.neocore.events.NeoPluginLoadEvent;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neocore.listeners.IOListener;

public class NeoCore extends JavaPlugin implements org.bukkit.event.Listener {
	private static NeoCore inst;
	private static HashMap<String, ArrayList<Dependant>> dependants = new HashMap<String, ArrayList<Dependant>>();
	private static String instName = null;
	
	public void onEnable() {
		inst = this;
		Bukkit.getServer().getLogger().info("NeoCore Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		
		// SQL
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(new File(this.getDataFolder(), "config.yml"));
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		String connection = "jdbc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		Properties properties = new Properties();
		properties.setProperty("useSSL", "false");
		properties.setProperty("user",  sql.getString("username"));
		properties.setProperty("password", sql.getString("password"));
		getServer().getPluginManager().registerEvents(new IOListener(connection, properties), this);
		
		File instancecfg = new File(this.getDataFolder(), "instance.yml");
		if (instancecfg.exists()) {
			YamlConfiguration icfg = YamlConfiguration.loadConfiguration(instancecfg);
			instName = icfg.getString("name");
		}
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.getPluginManager().callEvent(new NeoCoreInitEvent());
			}
		}.runTask(this);
	}
	
	public void onDisable() {
		IOListener.handleDisable();
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoCore Disabled");
	    super.onDisable();
	}
	
	public static NeoCore inst() {
		return inst;
	}
	
	public static void addDependant(String msg, Dependant d) {
		ArrayList<Dependant> list = dependants.getOrDefault(msg, new ArrayList<Dependant>());
		list.add(d);
		dependants.put(msg, list);
	}
	
	@EventHandler
	public void onDependableLoad(NeoPluginLoadEvent e) {
		String msg = e.getMessage();
		if (dependants.containsKey(msg)) {
			for (Dependant dep : dependants.get(msg)) {
				dep.run();
			}
		}
	}
	
	public static boolean isInstance() {
		return instName != null;
	}
	
	public static String getInstanceName() {
		return instName;
	}
	
	public static void registerIOComponent(JavaPlugin plugin, IOComponent component) {
		IOListener.register(plugin, component);
	}
	
	public static Statement getStatement() {
		return IOListener.getStatement();
	}
	
	public static void loadFiles(File load, FileLoader loader) throws NeoIOException {
		if (!load.exists()) {
			throw new NeoIOException("Failed to load file, doesn't exist: " + load.getParent() + "/" + load.getName());
		}
		
		if (load.isDirectory()) {
			for (File file : load.listFiles()) {
				loadFiles(file, loader);
			}
		}
		else {
			try {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(load);
				loader.load(cfg, load);
			}
			catch (Exception e) {
				Bukkit.getLogger().warning(e.getMessage());
				throw new NeoIOException("Failed to parse yaml for file " + load.getParent() + "/" + load.getName());
			}
		}
	}
}
