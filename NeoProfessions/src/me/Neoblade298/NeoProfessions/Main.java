package me.Neoblade298.NeoProfessions;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.Neoblade298.NeoProfessions.Commands.BlacksmithCommands;
import me.Neoblade298.NeoProfessions.Commands.CulinarianCommands;
import me.Neoblade298.NeoProfessions.Commands.MasonCommands;
import me.Neoblade298.NeoProfessions.Commands.StonecutterCommands;
import me.Neoblade298.NeoProfessions.Methods.BlacksmithMethods;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public class Main extends JavaPlugin implements Listener {
	public boolean debug = false;

  private Economy econ;
  private Permission perms;
  private Chat chat;
  
  public BlacksmithMethods blacksmithMethods;
  
  public void onEnable()
  {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoProfessions Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    // Setup vault
    if (!setupEconomy()) {
      this.getLogger().severe("Disabled due to no Vault dependency found!");
      Bukkit.getPluginManager().disablePlugin(this);
      return;
    }
	  this.setupPermissions();
	  this.setupChat();
	  
	  
	  // Connect method classes to main
	  blacksmithMethods = new BlacksmithMethods(this);
	  
	  // Command listeners for all classes
	  this.getCommand("blacksmith").setExecutor(new BlacksmithCommands(this));
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoProfessions Disabled");
    super.onDisable();
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