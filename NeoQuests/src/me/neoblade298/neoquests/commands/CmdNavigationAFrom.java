package me.neoblade298.neoquests.commands;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.navigation.NavigationManager;
import me.neoblade298.neoquests.navigation.Pathway;
import me.neoblade298.neoquests.navigation.PathwayPoint;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class CmdNavigationAFrom implements Subcommand {

	@Override
	public String getDescription() {
		return "Starts navigation for a player from an endpoint";
	}

	@Override
	public String getKey() {
		return "from";
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
		
		PathwayPoint point = NavigationManager.getEndpoint(args[1]);
		Util.msg(p, "Starting navigation to &6" + point.getDisplay() + ". Choose a starting point:");
		for (Entry<PathwayPoint, Pathway> ent : point.getToEndpoints().entrySet()) {
			ComponentBuilder entry = new ComponentBuilder("§7- §6" + ent.getKey().getDisplay())
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nav start " + ent.getValue().getKey()));
			p.spigot().sendMessage(entry.create());
		}
		NavigationManager.startNavigation(p, args[1]);
	}

	@Override
	public String getArgs() {
		return "[endpoint] {player}";
	}
	
	@Override
	public ChatColor getColor() {
		return ChatColor.DARK_RED;
	}

}
