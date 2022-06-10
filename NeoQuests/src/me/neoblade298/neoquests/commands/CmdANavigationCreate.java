package me.neoblade298.neoquests.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationCreate implements Subcommand {

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
			NavigationManager.startPathwayEditor(p, args[0]);
		} catch (NeoIOException e) {
			NeoQuests.showWarning("Failed to start pathway editor", e);
		}
	}
	
	@Override
	public int getMinArgs() {
		return 1;
	}
	
	@Override
	public int getMaxArgs() {
		return 1;
	}

	@Override
	public String getArgs() {
		return "[name]";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}
}
