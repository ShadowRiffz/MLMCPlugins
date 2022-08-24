package me.neoblade298.neopvp.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neopvp.wars.WarManager;

public class CmdAWarWorld implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("world")));

	@Override
	public String getDescription() {
		return "Sets world";
	}

	@Override
	public String getKey() {
		return "world";
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
		WarManager.getWarCreator(s).setWorld(Bukkit.getWorld(args[0]));
		WarManager.displayWarCreation(s);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
