package me.neoblade298.neocore.io;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import me.neoblade298.neocore.exceptions.NeoIOException;

public class FileReader {
	public static  void loadRecursive(File dir, FileLoader loader) throws NeoIOException {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadRecursive(file, loader);
			}
			else {
				try {
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					loader.load(cfg);
				}
				catch (Exception e) {
					Bukkit.getLogger().warning(e.getMessage());
					throw new NeoIOException("Failed to parse yaml for file " + file.getParent() + "/" + file.getName());
				}
			}
		}
	}
}
