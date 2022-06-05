package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdNavigationCreate implements Subcommand {

	@Override
	public String getDescription() {
		return "Creates a new pathway editor";
	}

	@Override
	public String getKey() {
		return "create";
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
			NavigationManager.startPathwayEditor(p, args[1]);
		} catch (NeoIOException e) {
			NeoQuests.showWarning("Failed to start pathway editor", e);
		}
	}

	@Override
	public String getArgs() {
		return "[name]";
	}

}
