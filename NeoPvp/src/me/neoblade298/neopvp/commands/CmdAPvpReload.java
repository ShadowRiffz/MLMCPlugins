package me.neoblade298.neopvp.commands;

import org.bukkit.command.CommandSender;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neopvp.NeoPvp;

public class CmdAPvpReload implements Subcommand {
	private static final CommandArguments args = new CommandArguments();

	@Override
	public String getDescription() {
		return "Reloads the pvp plugin";
	}

	@Override
	public String getKey() {
		return "reload";
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
		NeoPvp.reload();
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
