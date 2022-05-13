package me.neoblade298.neoquests.io;

import java.util.HashMap;

import org.bukkit.configuration.file.YamlConfiguration;

public interface FileLoader<A, B> {
	public void load(YamlConfiguration cfg, HashMap<A, B> map);
}
