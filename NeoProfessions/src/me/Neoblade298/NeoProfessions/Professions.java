package me.Neoblade298.NeoProfessions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Neoblade298.NeoProfessions.Commands.EssencePayCommand;
import me.Neoblade298.NeoProfessions.Commands.EssenceVouchCommand;
import me.Neoblade298.NeoProfessions.Commands.NeoprofessionsCommands;
import me.Neoblade298.NeoProfessions.Commands.ValueCommand;
import me.Neoblade298.NeoProfessions.Inventories.ProfessionInventory;
import me.Neoblade298.NeoProfessions.Listeners.IOListeners;
import me.Neoblade298.NeoProfessions.Listeners.InventoryListeners;
import me.Neoblade298.NeoProfessions.Listeners.PartyListeners;
import me.Neoblade298.NeoProfessions.Managers.*;
import net.milkbowl.vault.economy.Economy;

public class Professions extends JavaPlugin implements Listener {
	public boolean debug = false;
	public static Random gen = new Random();
	public boolean isInstance = false;

	public static Economy econ;
	public static YamlConfiguration cfg;
	
	public static String lvlupMsg;

	public static String sqlUser;
	public static String sqlPass;
	public static String connection;
	public static Properties properties = new Properties();
	
	public static CurrencyManager cm;
	public static ProfessionManager pm;
	public static StorageManager sm;
	public static MinigameManager mim;
	public static RecipeManager rm;
	public static IOListeners io;
	
	public me.neoblade298.neogear.Gear neogear;
	
	public static HashMap<Player, ProfessionInventory> viewingInventory = new HashMap<Player, ProfessionInventory>();

	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("NeoProfessions Enabled");

		// Setup vault
		if (!setupEconomy()) {
			this.getLogger().severe("Disabled due to no Vault dependency found!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		
		// Configuration// Save config if doesn't exist
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		cfg = YamlConfiguration.loadConfiguration(file);

		loadConfig();
		if (new File(getDataFolder(), "instance").exists()) {
			isInstance = true;
		}

		// Set up required listeners
		getServer().getPluginManager().registerEvents(new InventoryListeners(this), this); // Repairs
		getServer().getPluginManager().registerEvents(new AugmentManager(this), this); // Working augments
		if (Bukkit.getPluginManager().isPluginEnabled("mcMMO")) {
			getServer().getPluginManager().registerEvents(new PartyListeners(this), this);
		}
			
		if (!isInstance) {
			// Managers and listeners
			cm = new CurrencyManager(this);
			pm = new ProfessionManager(this);
			sm = new StorageManager(this);
			rm = new RecipeManager(this);
			mim = new MinigameManager(this);
			io = new IOListeners(this);
			getServer().getPluginManager().registerEvents(io, this);
			getServer().getPluginManager().registerEvents(cm, this);
			getServer().getPluginManager().registerEvents(sm, this);
			IOListeners.addComponent(cm);
			IOListeners.addComponent(pm);
			IOListeners.addComponent(sm);
			IOListeners.addComponent(rm);
	
			// Command listeners for all classes
			this.getCommand("value").setExecutor(new ValueCommand(this));
			this.getCommand("epay").setExecutor(new EssencePayCommand(this));
			this.getCommand("evouch").setExecutor(new EssenceVouchCommand(this));
			this.getCommand("prof").setExecutor(new NeoprofessionsCommands(this));
		}
		
		// SQL
		properties.setProperty("useSSL", "false");
		properties.setProperty("user", sqlUser);
		properties.setProperty("password", sqlPass);
		properties.setProperty("useSSL", "false");
	}
	
	private void loadConfig() {
		// sql
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		connection = "jdbc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		sqlUser = sql.getString("username");
		sqlPass = sql.getString("password");
		
		// general
		lvlupMsg = cfg.getString("levelup");
		
		// droptables
		loadDroptables(new File(getDataFolder(), "droptables"));
	}
	
	private void loadDroptables(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadDroptables(dir);
			}
			else {
				YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
				for (String table : yml.getKeys(false)) {
					AugmentManager.droptables.put(table, (ArrayList<String>) yml.getStringList(table));
				}
			}
		}
	}

	public void onDisable() {
		super.onDisable();
		io.handleDisable();
		Bukkit.getServer().getLogger().info("NeoProfessions Disabled");
	}

	private boolean setupEconomy() {
		if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
			return false;
		}

		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public Economy getEconomy() {
		return econ;
	}
	
	public static CurrencyManager getCurrencyManager() {
		return cm;
	}
}