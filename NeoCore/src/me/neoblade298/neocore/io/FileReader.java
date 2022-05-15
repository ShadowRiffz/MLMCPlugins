package me.neoblade298.neocore.io;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import me.neoblade298.neocore.exceptions.NeoIOException;

public class FileReader {
	public static  void load(File dir, FileLoader loader) throws NeoIOException {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				load(file, loader);
			}
			else {
				try {
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					loader.load(cfg);
				}
				catch (Exception e) {
					throw new NeoIOException("Failed to parse yaml for file " + file.getParent() + "/" + file.getName());
				}
			}
		}
	}
}
