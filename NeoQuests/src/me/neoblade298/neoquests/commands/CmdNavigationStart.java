package me.neoblade298.neoquests.commands;

import net.md_5.bungee.api.ChatColor;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdNavigationStart implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("pathway"),
			new CommandArgument("player", false)));

	@Override
	public String getDescription() {
		return "Starts navigation";
	}

	@Override
	public String getKey() {
		return "start";
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
		Player p = (Player) s;
		NavigationManager.startNavigation(p, args[0]);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}
