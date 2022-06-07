package me.neoblade298.neoquests.commands;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CmdANavigationFrom implements Subcommand {

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
		if (point.getToEndpoints().size() > 0) {
			Util.msg(p, "Setting start point to &6" + point.getDisplay() + "&7. Choose a destination:");
			for (Entry<PathwayPoint, Pathway> ent : point.getToEndpoints().entrySet()) {
				ComponentBuilder entry = new ComponentBuilder("§7- §6" + ent.getKey().getDisplay())
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nav start " + ent.getValue().getKey()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to start from " + ent.getKey().getDisplay())));
				p.spigot().sendMessage(entry.create());
			}
		}
		else {
			Util.msg(p, "&cThis start point is not connected to any destinations!");
		}
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
