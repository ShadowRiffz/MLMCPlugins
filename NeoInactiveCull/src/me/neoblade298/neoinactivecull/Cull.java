package me.neoblade298.neoinactivecull;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class Cull extends JavaPlugin implements org.bukkit.event.Listener {
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoInactiveCull starting culling...");
		
		new BukkitRunnable() {
			public void run() {
				File irMain = new File("/home/MLMC/ServerTowny/plugins/InventoryRollback/saves");
				Long start = System.currentTimeMillis();
				for (File directory : irMain.listFiles()) {
					if (directory.isDirectory()) {
						for (File save : directory.listFiles()) {
							YamlConfiguration yml = YamlConfiguration.loadConfiguration(save);
							
							ConfigurationSection data = yml.getConfigurationSection("data");
							for (String key : data.getKeys(false)) {
								long time = Long.parseLong(key);
								if (time + 2678400000L < System.currentTimeMillis()) {
									data.set(key, null);
								}
							}
							int numSaves = data.getKeys(false).size();
							if (numSaves > 0) {
								yml.set("saves", data.getKeys(false).size());
								try {
									yml.save(save);
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							else {
								save.delete();
							}
						}
					}
				}
				Bukkit.getServer().getLogger().info("NeoInactiveCull culling complete! Took " + (System.currentTimeMillis() - start) + "ms");
			}
		}.runTaskAsynchronously(this);
	}
	
	public void onDisable() {
	    org.bukkit.Bukkit.getServer().getLogger().info("NeoInactiveCull disabled");
	    super.onDisable();
	}
	
}
