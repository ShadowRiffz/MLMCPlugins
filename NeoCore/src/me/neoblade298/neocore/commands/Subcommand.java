package me.neoblade298.neocore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public interface Subcommand {
	public String getPermission();
	public SubcommandRunner getRunner();
	public String getKey();
	public String getDescription();
	public default ChatColor getColor() { return ChatColor.RED; }
	public default CommandArguments getArgs() { return null; }
	public default String getArgOverride() { return null; }
	public default boolean isHidden() { return false; }
	
	public void run(CommandSender s, String[] args);
}
