package me.neoblade298.neojourney;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityResurrectEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Journey extends JavaPlugin implements org.bukkit.event.Listener {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoJourney Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("njourney").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoJourney Disabled");
	    super.onDisable();
	}
	
	//@EventHandler
	//public void onMend(PlayerItemMendEvent e) {
		// Currently broken, literally can't use this or the event will repeat
		// forever, may want to manually do this later on
		// e.setRepairAmount((e.getRepairAmount() + 1) / 2);
	//}
	
	@EventHandler
	public void onDurability(PlayerItemDamageEvent e) {
		if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasCustomModelData()) {
			e.setDamage((e.getDamage() + 1) / 2);
		}
	}
	
	@EventHandler(ignoreCancelled = true)
	public void onRevive(EntityResurrectEvent e) {
		if (e.getEntity() instanceof Player) {
			String w = e.getEntity().getWorld().getName();
			if (w.equalsIgnoreCase("Argyll") || w.equalsIgnoreCase("ClassPVP")) {
				e.setCancelled(true);
			}
		}
	}
	
}
