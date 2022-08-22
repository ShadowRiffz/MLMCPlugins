package me.neoblade298.neoinfinitewater;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class InfiniteWater extends JavaPlugin implements org.bukkit.event.Listener {
	
	public HashSet<UUID> notUsing;
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoInfiniteWater Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("water").setExecutor(new Commands(this));
	    notUsing = new HashSet<UUID>();
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoInfiniteWater Disabled");
	    super.onDisable();
	}
	
	public void toggle(Player p) {
		UUID uuid = p.getUniqueId();
		if (notUsing.contains(uuid)) {
			notUsing.remove(uuid);
			p.sendMessage("§4[§c§lMLMC§4] §7You toggled infinite water buckets §eon§7!");
		}
		else {
			notUsing.add(uuid);
			p.sendMessage("§4[§c§lMLMC§4] §7You toggled infinite water buckets §eoff§7!");
		}
	}

    @EventHandler
    public void OnPour(PlayerBucketEmptyEvent event) {
        final Material bucket = event.getBucket();
        final Player p = event.getPlayer();
        if (p.hasPermission("neoinfinitewater.use") && !notUsing.contains(p.getUniqueId())) {
        	new BukkitRunnable() {
        		public void run() {
        			try {
                        if (bucket == Material.WATER_BUCKET) {
                        	p.getInventory().removeItem(event.getItemStack());
                        	p.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
                        }
        			}
        			catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
        	}.runTask(this);
        }
    }
}
