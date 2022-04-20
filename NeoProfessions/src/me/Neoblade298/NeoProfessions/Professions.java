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

import me.Neoblade298.NeoProfessions.Commands.*;
import me.Neoblade298.NeoProfessions.Inventories.ProfessionInventory;
import me.Neoblade298.NeoProfessions.Listeners.IOListeners;
import me.Neoblade298.NeoProfessions.Listeners.InventoryListeners;
import me.Neoblade298.NeoProfessions.Listeners.PartyListeners;
import me.Neoblade298.NeoProfessions.Managers.*;
import me.Neoblade298.NeoProfessions.Objects.Manager;
import me.Neoblade298.NeoProfessions.Objects.Rarity;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;
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
	public static GardenManager gm;
	public static StorageManager sm;
	public static MinigameManager mim;
	public static RecipeManager rm;
	public static IOListeners io;
	
	public me.neoblade298.neogear.Gear neogear;
	
	public static HashMap<Player, ProfessionInventory> viewingInventory = new HashMap<Player, ProfessionInventory>();
	public static ArrayList<Manager> managers = new ArrayList<Manager>();
	private static HashMap<Rarity, Double> rarityExpMults = new HashMap<Rarity, Double>();
	private static HashMap<ProfessionType, Double> profExpMults = new HashMap<ProfessionType, Double>();
	private static HashMap<Rarity, Integer> defaultWeights = new HashMap<Rarity, Integer>();

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
			gm = new GardenManager(this);
			
			managers.add(cm);
			managers.add(pm);
			managers.add(sm);
			managers.add(rm);
			managers.add(mim);
			managers.add(gm);
			io = new IOListeners(this);
			getServer().getPluginManager().registerEvents(cm, this);
			getServer().getPluginManager().registerEvents(sm, this);
			getServer().getPluginManager().registerEvents(rm, this);
			getServer().getPluginManager().registerEvents(mim, this);
			getServer().getPluginManager().registerEvents(io, this);
			IOListeners.addComponent(cm);
			IOListeners.addComponent(pm);
			IOListeners.addComponent(sm);
			IOListeners.addComponent(rm);
			IOListeners.addComponent(gm);
	
			// Command listeners for all classes
			this.getCommand("value").setExecutor(new ValueCommand(this));
			this.getCommand("ebal").setExecutor(new EssenceBalanceCommand(this));
			this.getCommand("epay").setExecutor(new EssencePayCommand(this));
			this.getCommand("evouch").setExecutor(new EssenceVouchCommand(this));
			this.getCommand("egive").setExecutor(new EssenceGiveCommand(this));
			this.getCommand("etake").setExecutor(new EssenceTakeCommand(this));
			this.getCommand("eset").setExecutor(new EssenceSetCommand(this));
			this.getCommand("prof").setExecutor(new ProfCommand(this));
			this.getCommand("profs").setExecutor(new ProfsCommand(this));
			this.getCommand("gardens").setExecutor(new GardensCommand(this));
			this.getCommand("craft").setExecutor(new CraftCommand(this));
			this.getCommand("inv").setExecutor(new InvCommand(this));
		}
		
		// SQL
		properties.setProperty("useSSL", "false");
		properties.setProperty("user", sqlUser);
		properties.setProperty("password", sqlPass);
		properties.setProperty("useSSL", "false");
	}
	
	public void loadConfig() {
		if (new File(getDataFolder(), "instance").exists()) {
			isInstance = true;
		}
		
		// sql
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		connection = "jdbc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		sqlUser = sql.getString("username");
		sqlPass = sql.getString("password");
		
		// general
		lvlupMsg = cfg.getString("levelup").replaceAll("&", "§");
		
		// exp multipliers
		rarityExpMults.clear();
		ConfigurationSection expcfg = cfg.getConfigurationSection("rarity-exp-multipliers");
		for (String key : expcfg.getKeys(false)) {
			Rarity rarity = Rarity.valueOf(key.toUpperCase());
			rarityExpMults.put(rarity, expcfg.getDouble(key));
		}
		
		// profession exp multipliers
		profExpMults.clear();
		expcfg = cfg.getConfigurationSection("profession-exp-multipliers");
		for (String key : expcfg.getKeys(false)) {
			ProfessionType type = ProfessionType.valueOf(key.toUpperCase());
			profExpMults.put(type, expcfg.getDouble(key));
		}
		
		// default weights
		defaultWeights.clear();
		ConfigurationSection wtcfg = cfg.getConfigurationSection("default-weights");
		for (String key : wtcfg.getKeys(false)) {
			Rarity rarity = Rarity.valueOf(key.toUpperCase());
			defaultWeights.put(rarity, wtcfg.getInt(key));
		}
	}

	public void onDisable() {
		super.onDisable();
		if (!isInstance && io != null) {
			io.handleDisable();
		}
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
	
	public static double getExpMultiplier(Rarity rarity) {
		return rarityExpMults.getOrDefault(rarity, 1.0);
	}
	
	public static double getExpMultiplier(ProfessionType prof) {
		return profExpMults.getOrDefault(prof, 1.0);
	}
	
	public static int getDefaultWeight(Rarity rarity) {
		return defaultWeights.getOrDefault(rarity, 1);
	}
}