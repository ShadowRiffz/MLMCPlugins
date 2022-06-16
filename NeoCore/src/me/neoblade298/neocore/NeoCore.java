package me.neoblade298.neocore;

import java.io.File;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.commands.*;
import me.neoblade298.neocore.events.NeoCoreInitEvent;
import me.neoblade298.neocore.events.NeoPluginLoadEvent;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.io.IOComponent;
import me.neoblade298.neocore.listeners.IOListener;
import me.neoblade298.neocore.player.*;
import net.milkbowl.vault.economy.Economy;

public class NeoCore extends JavaPlugin implements org.bukkit.event.Listener {
	private static HashMap<String, PlayerFields> fields = new HashMap<String, PlayerFields>();
	private static HashMap<String, PlayerTags> tags = new HashMap<String, PlayerTags>();
	private static NeoCore inst;
	private static HashMap<String, ArrayList<Dependant>> dependants = new HashMap<String, ArrayList<Dependant>>();
	private static String instName = null;
	private static Economy econ;
	private static boolean debug;
	
	
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
		
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        econ = rsp.getProvider();
        
        initCommands();
		
		new BukkitRunnable() {
			public void run() {
				Bukkit.getPluginManager().callEvent(new NeoCoreInitEvent());
			}
		}.runTask(this);
	}

	private void initCommands() {
		String cmd = "core";
		CommandManager core = new CommandManager(cmd);
		core.registerCommandList("");
		core.register(new CmdCoreEnable());
		core.register(new CmdCoreDisable());
		this.getCommand(cmd).setExecutor(core);
	}
	
	public void onDisable() {
		IOListener.handleDisable();
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoCore Disabled");
	    super.onDisable();
	}
	
	public static NeoCore inst() {
		return inst;
	}
	
	public static Dependant addDependant(String msg, Dependant d) {
		ArrayList<Dependant> list = dependants.getOrDefault(msg, new ArrayList<Dependant>());
		list.add(d);
		dependants.put(msg, list);
		return d;
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
	
	public static IOComponent registerIOComponent(JavaPlugin plugin, IOComponent component) {
		IOListener.register(plugin, component);
		return component;
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
				e.printStackTrace();
				throw new NeoIOException("Failed to parse yaml for file " + load.getParent() + "/" + load.getName());
			}
		}
	}
	
	public static Economy getEconomy() {
		return econ;
	}
	
	public PlayerFields createPlayerFields(String key, Plugin plugin, boolean hidden) {
		if (fields.containsKey(key)) {
			Bukkit.getLogger().log(Level.INFO, "[NeoCore] Player fields " + key + " for plugin " + plugin.getName() + " already exists. Returning existing keyed player data.");
			return fields.get(key);
		}
		Bukkit.getLogger().log(Level.INFO, "[NeoCore] Created player fields of " + key + " for plugin " + plugin.getName() + ", hidden: " + hidden + ".");
		PlayerFields newkp = new PlayerFields(key, hidden);
		fields.put(key, newkp);
		return newkp;
	}
	
	public PlayerTags createPlayerTags(String key, Plugin plugin, boolean hidden) {
		if (tags.containsKey(key)) {
			Bukkit.getLogger().log(Level.INFO, "[NeoCore] Player tags " + key + " for plugin " + plugin.getName() + " already exists. Returning existing keyed player data.");
			return tags.get(key);
		}
		Bukkit.getLogger().log(Level.INFO, "[NeoCore] Created player tags of " + key + " for plugin " + plugin.getName() + ", hidden: " + hidden + ".");
		PlayerTags newkp = new PlayerTags(key, hidden);
		tags.put(key, newkp);
		return newkp;
	}
	
	public static boolean isDebug() {
		return debug;
	}
}
