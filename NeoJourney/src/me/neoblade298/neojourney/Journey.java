package me.neoblade298.neojourney;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemMendEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Journey extends JavaPlugin implements org.bukkit.event.Listener {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoJourney Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("journey").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoJourney Disabled");
	    super.onDisable();
	}
	
	@EventHandler
	public void onMend(PlayerItemMendEvent e) {
		e.setRepairAmount(e.getRepairAmount() / 2);
	}
	
}
