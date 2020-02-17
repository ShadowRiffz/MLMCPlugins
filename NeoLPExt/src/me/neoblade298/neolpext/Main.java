package me.neoblade298.neolpext;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.luckperms.api.LuckPerms;

public class Main extends JavaPlugin implements org.bukkit.event.Listener {
	protected LuckPerms api;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoLPExt Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    
	    RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
	    if (provider != null) {
	        api = provider.getProvider();
	    }

	    this.getCommand("lpext").setExecutor(new Commands(this, api));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoLPExt Disabled");
	    super.onDisable();
	}
	
}
