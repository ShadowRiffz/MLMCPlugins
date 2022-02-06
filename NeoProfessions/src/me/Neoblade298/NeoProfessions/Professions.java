package me.Neoblade298.NeoProfessions;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
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
import me.Neoblade298.NeoProfessions.Recipes.CulinarianRecipes;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import me.neoblade298.neogear.Gear;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Professions extends JavaPlugin implements Listener {
	public boolean debug = false;
	public boolean isInstance = false;

	private Economy econ;
	private Permission perms;
	private Chat chat;
	private YamlConfiguration cfg;

	static String sqlUser = "neoblade298";
	static String sqlPass = "7H56480g09&Z01pz";
	static String connection = "jdbc:mysql://66.70.180.136:3306/MLMC?useSSL=false";
	static Properties properties = new Properties();

	public ProfessionsMethods professionsMethods;
	public CulinarianRecipes culinarianRecipes;
	public MasonUtils masonUtils;

	public GeneralListeners generalListeners;
	
	public CurrencyManager cManager;
	
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
		this.cfg = YamlConfiguration.loadConfiguration(file);
		isInstance = cfg.getBoolean("is-instance");

		// Set up required listeners
		getServer().getPluginManager().registerEvents(new InventoryListeners(this), this);
		getServer().getPluginManager().registerEvents(new AugmentManager(), this);
		getServer().getPluginManager().registerEvents(new PartyListeners(this), this);
		if (!isInstance) {
			// Currency
			cManager = new CurrencyManager(this);
		}
			
		if (!isInstance) {
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


		// Setup charm timer
		Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
			public void run() {
				for (World w : Bukkit.getWorlds()) {
					if (w.getName().equalsIgnoreCase("Argyll") || w.getName().equalsIgnoreCase("ClassPVP")) {
						for (Player p : w.getPlayers()) {
							// First check what charms the player has
							ItemStack item = p.getInventory().getItemInMainHand();
							// Then make sure it's not a literal hunger charm
							if (item.getType() != Material.PRISMARINE_CRYSTALS) {
								String charmLine = null;
								if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
									for (String line : item.getItemMeta().getLore()) {
										if (line.contains("Hunger Charm")) {
											charmLine = line;
											break;
										}
									}
								}
								if (charmLine != null) {
									if (p.getFoodLevel() != 19) {
										FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, 19);
										Bukkit.getPluginManager().callEvent(event);
										
										// Override event cancellation if we're decreasing hunger so that
										// it's not cancelled by NeoSAPIAddons
										if (event.isCancelled() && p.getFoodLevel() <= 19) {
											return;
										}
										p.setFoodLevel(19);
									}
								}
							}
						}
					}
				}
			}
		}, 0, 20L);
		
		// SQL
		properties.setProperty("useSSL", "false");
		properties.setProperty("user", sqlUser);
		properties.setProperty("password", sqlPass);
		properties.setProperty("useSSL", "false");
	}

	public void onDisable() {
		super.onDisable();
		try {
			cManager.cleanup();
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
}