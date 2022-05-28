package me.neoblade298.neocore.commands;

import java.util.HashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class NeoCommands implements CommandExecutor {
	private HashMap<String, Subcommand> handlers = new HashMap<String, Subcommand>();
	private String base;
	
	public NeoCommands(String base) {
		this.base = base;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0 && handlers.containsKey("")) {
			runCommand("", sender, args);
			return true;
		}
		else if (handlers.containsKey(args[0])) {
			runCommand(args[0], sender, args);
			return true;
		}
		return false;
	}
	
	private void runCommand(String key, CommandSender s, String[] args) {
		Subcommand sc = handlers.get(key);
		if (check(sc, s)) {
			handlers.get("").run(s, args);
		}
	}
	
	private boolean check(Subcommand cmd, CommandSender s) {
		if (cmd.getPermission() == null || s.hasPermission(cmd.getPermission())) {
			s.sendMessage("§cYou're missing the permission: " + cmd.getPermission());
			return false;
		}

		if ((cmd.getRunner() == SubcommandRunner.PLAYER_ONLY && s instanceof Player) ||
				(cmd.getRunner() == SubcommandRunner.CONSOLE_ONLY && s instanceof ConsoleCommandSender) ||
				cmd.getRunner() == SubcommandRunner.BOTH) {
			s.sendMessage("§cYou are the wrong type of user for this command!");
			return false;
		}
		
		return true;
	}
	
	public void register(Subcommand sc) {
		handlers.put(sc.getKey(), sc);
	}
	
	public void registerCommandList(String key) {
		handlers.put(key, new CmdList(key, base, handlers));
	}
}
