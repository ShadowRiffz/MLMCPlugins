package me.neoblade298.neoinactivecull;

import java.io.File;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Cull extends JavaPlugin implements org.bukkit.event.Listener {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoInactiveCull starting culling...");
		
		new BukkitRunnable() {
			public void run() {
				File irMain = new File("/home/MLMC/ServerTowny/plugins/InventoryRollbackPlus/backups");
				Long start = System.currentTimeMillis();
				recurseAndDelete(irMain);
				Bukkit.getServer().getLogger().info("NeoInactiveCull culling complete! Took " + (System.currentTimeMillis() - start) + "ms");
			}
		}.runTaskAsynchronously(this);
	}
	
	private void recurseAndDelete(File dir) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				recurseAndDelete(file);
			}
			else {
				String timeStr = file.getName();
				timeStr = timeStr.substring(0, timeStr.length() - 4); // Take out .yml
				long timestamp = Long.parseLong(timeStr);
				if (timestamp + 2678400000L < System.currentTimeMillis()) {
					file.delete();
				}
			}
		}
		if (dir.listFiles().length == 0) {
			dir.delete();
		}
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoInactiveCull disabled");
	    super.onDisable();
	}
	
}
