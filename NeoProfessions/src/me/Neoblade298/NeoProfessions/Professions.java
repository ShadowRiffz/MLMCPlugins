package me.Neoblade298.NeoProfessions;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Neoblade298.NeoProfessions.Augments.AugmentManager;
import me.Neoblade298.NeoProfessions.Commands.NeoprofessionsCommands;
import me.Neoblade298.NeoProfessions.Commands.ValueCommand;
import me.Neoblade298.NeoProfessions.Inventories.ProfessionInventory;
import me.Neoblade298.NeoProfessions.Listeners.GeneralListeners;
import me.Neoblade298.NeoProfessions.Listeners.InventoryListeners;
import me.Neoblade298.NeoProfessions.Listeners.PartyListeners;
import me.Neoblade298.NeoProfessions.Methods.ProfessionsMethods;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionManager;
import me.Neoblade298.NeoProfessions.Recipes.CulinarianRecipes;
import me.Neoblade298.NeoProfessions.Storage.StorageManager;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.neoblade298.neogear.Gear;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Professions extends JavaPlugin implements Listener {
	public boolean debug = false;
	public boolean isInstance = false;

	public static Economy econ;
	public static Permission perms;
	public static Chat chat;
	public static YamlConfiguration cfg;
	
	public static String lvlupMsg;

	public static String sqlUser;
	public static String sqlPass;
	public static String connection;
	public static Properties properties = new Properties();

	public ProfessionsMethods professionsMethods;
	public CulinarianRecipes culinarianRecipes;
	public MasonUtils masonUtils;

	public GeneralListeners generalListeners;
	
	public static CurrencyManager cm;
	public static ProfessionManager pm;
	public static StorageManager sm;
	
	public me.neoblade298.neogear.Gear neogear;
	
	public HashMap<Player, ProfessionInventory> viewingInventory = new HashMap<Player, ProfessionInventory>();

	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("NeoProfessions Enabled");

		// Setup vault
		if (!setupEconomy()) {
			this.getLogger().severe("Disabled due to no Vault dependency found!");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		this.setupPermissions();
		this.setupChat();
		
		neogear = (Gear) Bukkit.getPluginManager().getPlugin("NeoGear");
		
		// Configuration// Save config if doesn't exist
		File file = new File(getDataFolder(), "config.yml");
		if (!file.exists()) {
			saveResource("config.yml", false);
		}
		cfg = YamlConfiguration.loadConfiguration(file);

		// SQL
		ConfigurationSection sql = cfg.getConfigurationSection("sql");
		connection = "jdbc:mysql://" + sql.getString("host") + ":" + sql.getString("port") + "/" + 
				sql.getString("db") + sql.getString("flags");
		sqlUser = sql.getString("username");
		sqlPass = sql.getString("password");

		loadConfig();
		if (new File(getDataFolder(), "instance").exists()) {
			isInstance = true;
		}

		// Set up required listeners
		getServer().getPluginManager().registerEvents(new InventoryListeners(this), this);
		getServer().getPluginManager().registerEvents(new AugmentManager(this), this);
		if (Bukkit.getPluginManager().isPluginEnabled("mcMMO")) {
			getServer().getPluginManager().registerEvents(new PartyListeners(this), this);
		}
			
		if (!isInstance) {
			// Currency
			cm = new CurrencyManager(this);
			pm = new ProfessionManager(this);
			sm = new StorageManager(this);
			
			// NeoGear
			neogear = (Gear) Bukkit.getServer().getPluginManager().getPlugin("NeoGear");
	
			masonUtils = new MasonUtils();
	
			// Connect method classes to main
			professionsMethods = new ProfessionsMethods(this);
	
			// Command listeners for all classes
			this.getCommand("value").setExecutor(new ValueCommand(this));
			this.getCommand("prof").setExecutor(new NeoprofessionsCommands(this));
			
			
			// Setup Event Listeners
			getServer().getPluginManager().registerEvents(new GeneralListeners(this), this);
	
			// Setup recipes (make sure the recipes haven't been added before)
			culinarianRecipes = new CulinarianRecipes(this);
			Iterator<Recipe> iter = getServer().recipeIterator();
			boolean canAdd = true;
			while (iter.hasNext()) {
				Recipe r = iter.next();
				if (r instanceof ShapedRecipe && ((ShapedRecipe) r).getKey().getKey().equalsIgnoreCase("Vodka")) {
					canAdd = false;
				}
			}
			if (canAdd) {
				for (Recipe recipe : culinarianRecipes.getRecipes()) {
					Bukkit.addRecipe(recipe);
				}
			}
		}
		
		// SQL
		properties.setProperty("useSSL", "false");
		properties.setProperty("user", sqlUser);
		properties.setProperty("password", sqlPass);
		properties.setProperty("useSSL", "false");
	}
	
	private void loadConfig() {
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
		try {
			if (cm != null) {
				cm.cleanup();
			}
			if (pm != null) {
				ProfessionManager.saveAll();
			}
		} catch (Exception e) {
			e.printStackTrace();
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

	private boolean setupChat() {
		RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
		chat = rsp.getProvider();
		return chat != null;
	}

	private boolean setupPermissions() {
		RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
		perms = rsp.getProvider();
		return perms != null;
	}

	public Economy getEconomy() {
		return econ;
	}

	public Permission getPermissions() {
		return perms;
	}

	public Chat getChat() {
		return chat;
	}
	
	public static CurrencyManager getCurrencyManager() {
		return cm;
	}
}