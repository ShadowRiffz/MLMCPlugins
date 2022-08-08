package me.neoblade298.neopvp.generators;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.bungee.BungeeAPI;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.interfaces.Manager;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neocore.scheduler.ScheduleInterval;
import me.neoblade298.neocore.scheduler.SchedulerAPI;
import me.neoblade298.neopvp.NeoPvp;

public class GeneratorManager implements Manager {
	private static HashMap<String, Generator> generators = new HashMap<String, Generator>();
	private static FileLoader generatorLoader;
	
	static {
		generatorLoader = (cfg, file) -> {
			for (String key : cfg.getKeys(false)) {
				if (generators.containsKey(key)) {
					Bukkit.getLogger().warning("[NeoPvp] Duplicate generator " + key + " tried to load from file " +
							file.getPath() + ", original in " + generators.get(key).getPath());
				}
				try {
					generators.put(key.toUpperCase(), new Generator(cfg.getConfigurationSection(key), file)); 
				}
				catch (Exception e) {
					Bukkit.getLogger().warning("[NeoPvp] Failed to load generator " + key + " in file " + file.getPath());
					e.printStackTrace();
				}
			}
		};
	}
	
	public static Generator getGenerator(String key) {
		return generators.get(key);
	}
	
	public GeneratorManager() {
		SchedulerAPI.scheduleRepeating("neopvp-generators", ScheduleInterval.HALF_HOUR, new Runnable() {
			public void run() {
				int amount = 0;
				for (Generator gen : generators.values()) {
					amount = gen.spawnItem();
				}
				BungeeAPI.broadcast("&4[&c&lMLMC&4] &7The diamond generators have spawned " + amount + " diamonds each!");
			}
		});
		
		// 5 minute warning
		SchedulerAPI.scheduleRepeating("neopvp-generators-5m-warn", ScheduleInterval.HALF_HOUR, 1500, new Runnable() {
			public void run() {
				BungeeAPI.broadcast("&4[&c&lMLMC&4] &e5 &7minutes until the diamond generators activate near &c/warp resource&7!");
				BungeeAPI.broadcast("&4[&c&lMLMC&4] &7Fight for the diamonds in the pvp areas!");
			}
		});
		
		// 1 minute warning
		SchedulerAPI.scheduleRepeating("neopvp-generators-1m-warn", ScheduleInterval.HALF_HOUR, 1740, new Runnable() {
			public void run() {
				BungeeAPI.broadcast("&4[&c&lMLMC&4] &e1 &7minute until the diamond generators activate near &c/warp resource&7!");
			}
		});
		
		reload();
	}

	@Override
	public void cleanup() {}

	@Override
	public String getKey() {
		return "NeoPvp-GeneratorManager";
	}

	@Override
	public void reload() {
		try {
			NeoCore.loadFiles(new File(NeoPvp.inst().getDataFolder(), "generators.yml"), generatorLoader);
		} catch (NeoIOException e) {
			e.printStackTrace();
		}
	}
}
