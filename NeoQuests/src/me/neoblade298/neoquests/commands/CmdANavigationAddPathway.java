package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationAddPathway implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("start"), new CommandArgument("end")));

	@Override
	public String getDescription() {
		return "Adds an existing pathway to your path";
	}

	@Override
	public String getKey() {
		return "editor";
	}

	@Override
	public String getPermission() {
		return "neoquests.admin";
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.PLAYER_ONLY;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		try {
			NavigationManager.startPathwayEditor(p);
		} catch (NeoIOException e) {
			NeoQuests.showWarning("Failed to start pathway editor", e);
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
