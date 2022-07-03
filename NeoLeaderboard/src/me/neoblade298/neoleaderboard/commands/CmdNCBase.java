package me.neoblade298.neoleaderboard.commands;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Iterator;
import java.util.TreeSet;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoleaderboard.NeoLeaderboard;
import me.neoblade298.neoleaderboard.points.NationEntry;
import me.neoblade298.neoleaderboard.points.NationPointType;
import me.neoblade298.neoleaderboard.points.PlayerPointType;
import me.neoblade298.neoleaderboard.points.PointsManager;
import me.neoblade298.neoleaderboard.points.TownEntry;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CmdNCBase implements Subcommand {
	private static final CommandArguments args = new CommandArguments();

	@Override
	public String getDescription() {
		return "Displays clickable list of categories";
	}

	@Override
	public String getKey() {
		return "";
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
	public CommandArguments getArgs() {
		return args;
	}

	@Override
	public void run(CommandSender s, String[] args) {
			ComponentBuilder builder = new ComponentBuilder("§6§l>§8§m--------§c§l» Nation Categories «§8§m--------§6§l<");
			for (NationPointType type : NationPointType.values()) {
				builder.append("\n §6§l» §e" + type.getDisplay(), FormatRetention.NONE)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/nc " + type)))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nc " + type));
			}
			builder.append("\n§6§l>§8§m--------§c§l» Player Categories «§8§m--------§6§l<", FormatRetention.NONE);
			for (PlayerPointType type : PlayerPointType.values()) {
				builder.append("\n §6§l» §e" + type.getDisplay(), FormatRetention.NONE)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/nc " + type)))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nc " + type));
			}
			s.spigot().sendMessage(builder.create());
	}
}
