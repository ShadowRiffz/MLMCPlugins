package me.neoblade298.neopvp.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neopvp.PvpManager;

public class CmdAPvpAddProtection implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("player"), new CommandArgument("time (seconds)")));

	@Override
	public String getDescription() {
		return "Adds protection time to a player";
	}

	@Override
	public String getKey() {
		return "addprotection";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		PvpManager.getAccount(Bukkit.getPlayer(args[0])).addProtection(Integer.parseInt(args[1]) * 1000);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
