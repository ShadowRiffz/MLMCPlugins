package me.neoblade298.neopvp.generators;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.interfaces.Manager;
import me.neoblade298.neocore.io.FileLoader;
import me.neoblade298.neopvp.NeoPvp;

public class GeneratorManager implements Manager {
	private static HashMap<String, Generator> generators = new HashMap<String, Generator>();
	private static FileLoader generatorLoader;
	private static BukkitTask task;
	
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
		task = new BukkitRunnable() {
			public void run() {
				for (String key)
			}
		}
		
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
	
	public static long getNextHour() {
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusHours(1).truncatedTo(ChronoUnit.HOURS);
		return Duration.between(start, end).toMillis();
	}
	
	public static long getNextHalfHour() {
		LocalDateTime start = LocalDateTime.now();
		LocalDateTime end = start.plusHours(1).truncatedTo(ChronoUnit.HOURS);
		return Duration.between(start, end).toMillis();
	}
}
