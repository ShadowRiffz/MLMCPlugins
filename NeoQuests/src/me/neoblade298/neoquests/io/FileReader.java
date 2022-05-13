package me.neoblade298.neoquests.io;

import java.io.File;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import me.neoblade298.neoquests.NeoQuests;

public class FileReader {
	public static  void load(String name, FileLoader loader, CommandSender s) {
		for (File file : new File(NeoQuests.inst().getDataFolder(), name).listFiles()) {
			if (file.isDirectory()) {
				loadDirectory(file, loader, s);
			}
			else {
				try {
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					loader.load(s, cfg);
				}
				catch (Exception e) {
					NeoQuests.showWarning(s, "Failed to parse yaml for file " + file.getParent() + "/" + file.getName(), e);
				}
			}
		}
	}
	
	private static void loadDirectory(File dir, FileLoader loader, CommandSender s) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadDirectory(file, loader, s);
			}
			else {
				try {
					YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
					loader.load(s, cfg);
				}
				catch (Exception e) {
					NeoQuests.showWarning(s, "Failed to parse yaml for file " + file.getParent() + "/" + file.getName(), e);
				}
			}
		}
	}
}
