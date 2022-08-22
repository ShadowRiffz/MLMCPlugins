package me.neoblade298.neopvp.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neopvp.wars.WarManager;

public class CmdAWarLimit implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("number")));

	@Override
	public String getDescription() {
		return "Sets max players in a team at once";
	}

	@Override
	public String getKey() {
		return "limit";
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
		WarManager.getWarCreator(s).setMaxPlayers(Integer.parseInt(args[0]));
		WarManager.displayWarCreation(s);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
