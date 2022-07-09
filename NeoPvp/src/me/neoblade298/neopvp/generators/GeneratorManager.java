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
import me.neoblade298.neocore.util.TimeUtil;
import me.neoblade298.neopvp.NeoPvp;

public class GeneratorManager implements Manager {
	private static HashMap<String, Generator> generators = new HashMap<String, Generator>();
	private static FileLoader generatorLoader;
	private static BukkitTask tasks[] = new BukkitTask[3];
	private static final int MINUTES_PER_GENERATION = 30;
	
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
		tasks[0] = new BukkitRunnable() {
			public void run() {
				for (Generator gen : generators.values()) {
					gen.spawnItem();
				}
			}
		}.runTaskTimer(NeoPvp.inst(), TimeUtil.getTicksToNextSegment(MINUTES_PER_GENERATION), 1000 * 60 * MINUTES_PER_GENERATION);
		

		tasks[0] = new BukkitRunnable() {
			public void run() {
				for (Generator gen : generators.values()) {
					gen.spawnItem();
				}
			}
		}.runTaskTimer(NeoPvp.inst(), TimeUtil.getTicksToNextSegment(MINUTES_PER_GENERATION), 1000 * 60 * MINUTES_PER_GENERATION);
		
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
