package me.neoblade298.neoquests.io;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

public interface FileLoader {
	public void load(CommandSender s, YamlConfiguration cfg);
}
