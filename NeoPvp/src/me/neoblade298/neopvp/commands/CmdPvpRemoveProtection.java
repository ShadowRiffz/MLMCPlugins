package me.neoblade298.neopvp.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neopvp.PvpManager;

public class CmdPvpRemoveProtection implements Subcommand {
	private static final CommandArguments args = new CommandArguments();

	@Override
	public String getDescription() {
		return "Removes your protection";
	}

	@Override
	public String getKey() {
		return "removeprotection";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.PLAYER_ONLY;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		PvpManager.getAccount((Player) s).removeProtection();
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
