package me.neoblade298.neoquests.commands;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.navigation.EndPoint;
import me.neoblade298.neoquests.navigation.NavigationManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CmdANavigationFrom implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("endpoint"),
			new CommandArgument("player", false)));

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
		if (args.length == 1) {
			p = (Player) s;
		}
		else {
			p = (Bukkit.getPlayer(args[1]));
		}
		
		if (p == null) {
			Util.msg(s, "&cPlayer is not online!");
		}
		
		EndPoint point = NavigationManager.getEndpoint(args[0]);
		if (point.getDestinations().size() > 0) {
			Util.msg(p, "Setting start point to &6" + point.getDisplay() + "&7. Choose a destination:");
			for (EndPoint start : point.getDestinations().keySet()) {
				ComponentBuilder entry = new ComponentBuilder("§7- §6" + start.getDisplay())
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nav start " + start.getKey() + " " + point.getKey()))
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to start from " + start.getDisplay())));
				p.spigot().sendMessage(entry.create());
			}
		}
		else {
			Util.msg(p, "&cThis start point is not connected to any destinations!");
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
