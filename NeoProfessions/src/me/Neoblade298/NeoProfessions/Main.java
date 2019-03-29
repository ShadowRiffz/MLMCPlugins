package me.Neoblade298.NeoProfessions;

import java.util.ArrayList;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Neoblade298.NeoProfessions.Commands.BlacksmithCommands;
import me.Neoblade298.NeoProfessions.Commands.CulinarianCommands;
import me.Neoblade298.NeoProfessions.Commands.MasonCommands;
import me.Neoblade298.NeoProfessions.Commands.NeoprofessionsCommands;
import me.Neoblade298.NeoProfessions.Commands.StonecutterCommands;
import me.Neoblade298.NeoProfessions.Listeners.BlacksmithListeners;
import me.Neoblade298.NeoProfessions.Listeners.CulinarianListeners;
import me.Neoblade298.NeoProfessions.Listeners.MasonListeners;
import me.Neoblade298.NeoProfessions.Listeners.SkillapiListeners;
import me.Neoblade298.NeoProfessions.Methods.BlacksmithMethods;
import me.Neoblade298.NeoProfessions.Methods.CulinarianMethods;
import me.Neoblade298.NeoProfessions.Methods.MasonMethods;
import me.Neoblade298.NeoProfessions.Methods.StonecutterMethods;
import me.Neoblade298.NeoProfessions.Recipes.CulinarianRecipes;
import me.Neoblade298.NeoProfessions.Utilities.MasonUtils;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin implements Listener {
	public boolean debug = false;

  private Economy econ;
  private Permission perms;
  private Chat chat;
  
  public BlacksmithMethods blacksmithMethods;
  public MasonMethods masonMethods;
  public StonecutterMethods stonecutterMethods;
  public CulinarianMethods culinarianMethods;
  
  public CulinarianRecipes culinarianRecipes;
  
  public MasonListeners masonListeners;
  
  public void onEnable()
  {
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

	  masonListeners = new MasonListeners(this);
	  
	  // Connect method classes to main
	  blacksmithMethods = new BlacksmithMethods(this);
	  masonMethods = new MasonMethods(this);
	  stonecutterMethods = new StonecutterMethods(this);
	  culinarianMethods = new CulinarianMethods(this);
	  
	  // Command listeners for all classes
	  this.getCommand("blacksmith").setExecutor(new BlacksmithCommands(this));
	  this.getCommand("mason").setExecutor(new MasonCommands(this));
	  this.getCommand("stonecutter").setExecutor(new StonecutterCommands(this));
	  this.getCommand("culinarian").setExecutor(new CulinarianCommands(this));
	  this.getCommand("neoprofessions").setExecutor(new NeoprofessionsCommands(this, blacksmithMethods, stonecutterMethods, culinarianMethods, masonMethods));
	  
    // Setup Event Listeners
    getServer().getPluginManager().registerEvents(new BlacksmithListeners(this), this);
    getServer().getPluginManager().registerEvents(masonListeners, this);
    getServer().getPluginManager().registerEvents(new CulinarianListeners(this), this);
    getServer().getPluginManager().registerEvents(new SkillapiListeners(this), this);
    
    // Setup recipes
    culinarianRecipes = new CulinarianRecipes(this);
    for(Recipe recipe : culinarianRecipes.getRecipes()) {
      Bukkit.addRecipe(recipe);
    }
    // Setup charm timer
	Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
		public void run() {
			for(World w : Bukkit.getWorlds()) {
				if(w.getName().equalsIgnoreCase("Argyll") ||
					w.getName().equalsIgnoreCase("ClassPVP")) {
					for (Player p : w.getPlayers()) {
						ItemStack item = p.getInventory().getItemInMainHand();
						if(MasonUtils.charmLine(item, "Hunger") != null) {
							p.setFoodLevel(18);
						}
					}
				}
			}
		}
	}, 0, 20L);
  }
  
  public void onDisable()
  {
    super.onDisable();
    ArrayList<Recipe> backup = new ArrayList<Recipe>();
    Iterator<Recipe> iter = getServer().recipeIterator();
    while (iter.hasNext()) {
      Recipe r = iter.next();
      boolean add = true;
      if (r.getResult().hasItemMeta()) {
      	for(Recipe recipe : culinarianRecipes.getRecipes()) {
      		if(r.getResult().getItemMeta().hasDisplayName() && r.getResult().getItemMeta().getDisplayName().equals(recipe.getResult().getItemMeta().getDisplayName())) {
      			add = false;
      		}
      	}
      }
      if(add) {
      	backup.add(r);
      }
    }
   Bukkit.clearRecipes();
   for(Recipe recipe : backup) {
  	 Bukkit.addRecipe(recipe);
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