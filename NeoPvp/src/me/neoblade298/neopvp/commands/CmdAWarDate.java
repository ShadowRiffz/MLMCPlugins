package me.neoblade298.neopvp.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neopvp.wars.WarManager;

public class CmdAWarDate implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("mm/dd/yyyy"), new CommandArgument("hour", false)));

	@Override
	public String getDescription() {
		return "Sets date";
	}

	@Override
	public String getKey() {
		return "date";
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
		if (args.length == 1) {
			WarManager.getWarCreator(s).setDate(args[0]);
		}
		else {
			WarManager.getWarCreator(s).setDate(args[0], Integer.parseInt(args[1]));
		}
		WarManager.displayWarCreation(s);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
