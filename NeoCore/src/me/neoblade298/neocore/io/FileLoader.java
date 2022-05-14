package me.neoblade298.neocore.io;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public interface FileLoader {
	public void load(YamlConfiguration cfg);
}
