package me.neoblade298.neoquests.io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conversations.Conversation;

public class FileReader {
	public static <A, B> void load(String name, HashMap<A, B> map, FileLoader<A, B> loader) {
		for (File file : new File(NeoQuests.inst().getDataFolder(), name).listFiles()) {
			if (file.isDirectory()) {
				loadDirectory(file, map, loader);
			}
			else {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				loader.load(cfg, map);
			}
		}
	}
	
	private static <A, B> void loadDirectory(File dir, HashMap<A, B> map, FileLoader<A, B> loader) {
		for (File file : dir.listFiles()) {
			if (file.isDirectory()) {
				loadDirectory(file, map, loader);
			}
			else {
				YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
				loader.load(cfg, map);
			}
		}
	}
}
