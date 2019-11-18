package me.neoblade298.neobarriers;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {
	HashMap<String, Integer> yvalues = new HashMap<String, Integer>();

	public void onEnable() {
	    Bukkit.getServer().getLogger().info("NeoBarriers Enabled");
		getServer().getPluginManager().registerEvents(this, this);
	    this.getCommand("barriers").setExecutor(new Commands(this));
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoBarriers Disabled");
	}
	
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		Player p = e.getPlayer();
		Block block = e.getBlock();
		if (block.getType().equals(Material.BARRIER)) {
			if (p.getGameMode().equals(GameMode.CREATIVE)) {
				if (yvalues.containsKey(p.getName())) {
					Location loc = block.getLocation();
					for (int i = block.getY() + 1; i <= yvalues.get(p.getName()); i++) {
						loc.add(0, 1, 0);
						Block blockAtLoc = loc.getBlock();
						if (blockAtLoc.getType().equals(Material.AIR)) {
							blockAtLoc.setType(Material.BARRIER);
						}
					}
				}
			}
		}
	}
}
