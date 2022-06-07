package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.navigation.NavigationManager;

public class CmdANavigationStart implements Subcommand {

	@Override
	public String getDescription() {
		return "Starts navigation for a player";
	}

	@Override
	public String getKey() {
		return "start";
	}

	@Override
	public String getPermission() {
		return "neoquests.admin";
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = null;
		if (args.length == 2) {
			p = (Player) s;
		}
		else {
			p = (Bukkit.getPlayer(args[2]));
		}
		
		if (p == null) {
			Util.msg(s, "&cPlayer is not online!");
		}
		NavigationManager.startNavigation(p, args[1]);
	}

	@Override
	public String getArgs() {
		return "[pathway] {player}";
	}
	
	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

}
