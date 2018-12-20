package me.Neoblade298.NeoProfessions;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import me.Neoblade298.NeoProfessions.Commands.BlacksmithCommands;
import me.Neoblade298.NeoProfessions.Commands.CulinarianCommands;
import me.Neoblade298.NeoProfessions.Commands.MasonCommands;
import me.Neoblade298.NeoProfessions.Commands.StonecutterCommands;

public class Main extends JavaPlugin implements Listener {
	public boolean debug = false;
  
  public void onEnable()
  {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoProfessions Enabled");
    getServer().getPluginManager().registerEvents(this, this);
    
    // Command listeners for all classes
    this.getCommand("blacksmith").setExecutor(new BlacksmithCommands(this));
    this.getCommand("mason").setExecutor(new MasonCommands(this));
    this.getCommand("stonecutter").setExecutor(new StonecutterCommands(this));
    this.getCommand("culinarian").setExecutor(new CulinarianCommands(this));
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoProfessions Disabled");
    super.onDisable();
  }
}