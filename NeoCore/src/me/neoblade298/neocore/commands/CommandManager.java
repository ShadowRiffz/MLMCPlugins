package me.neoblade298.neocore.commands;

import java.util.TreeMap;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor {
	private TreeMap<String, Subcommand> handlers = new TreeMap<String, Subcommand>();
	private String base;
	
	public CommandManager(String base) {
		this.base = base;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0) {
			if (handlers.containsKey("") ) {
				runCommand("", sender, args);
				return true;
			}
			else {
				return false;
			}
		}
		else if (StringUtils.isNumeric(args[0]) && handlers.get("") != null && handlers.get("") instanceof CmdList) {
			runCommand("", sender, args);
			return true;
		}
		else if (handlers.containsKey(args[0].toUpperCase())) {
			runCommand(args[0], sender, args);
			return true;
		}
		return false;
	}
	
	public void runCommand(String key, CommandSender s, String[] args) {
		runCommand(key, s, args, false);
	}
	
	public void runCommand(String key, CommandSender s, String[] args, boolean shouldCheck) {
		Subcommand sc = handlers.get(key.toUpperCase());
		if (!shouldCheck || check(sc, s)) {
			sc.run(s, args);
		}
	}
	
	private boolean check(Subcommand cmd, CommandSender s) {
		if ((cmd.getPermission() != null && !s.hasPermission(cmd.getPermission())) && !s.isOp()) {
			s.sendMessage("§cYou're missing the permission: " + cmd.getPermission());
			return false;
		}

		if ((cmd.getRunner() == SubcommandRunner.PLAYER_ONLY && !(s instanceof Player)) ||
				(cmd.getRunner() == SubcommandRunner.CONSOLE_ONLY && !(s instanceof ConsoleCommandSender))) {
			s.sendMessage("§cYou are the wrong type of user for this command!");
			return false;
		}
		
		return true;
	}
	
	public void register(Subcommand sc) {
		handlers.put(sc.getKey().toUpperCase(), sc);
	}
	
	public void registerCommandList(String key, ChatColor color) {
		handlers.put(key.toUpperCase(), new CmdList(key, base, handlers, color));
	}
	
	public void registerCommandList(String key) {
		handlers.put(key.toUpperCase(), new CmdList(key, base, handlers));
	}
	
	public Subcommand getCommand(String key) {
		return handlers.get(key.toUpperCase());
	}
}
