package me.neoblade298.neoleaderboard.commands;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Formatter;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoleaderboard.points.NationEntry;
import me.neoblade298.neoleaderboard.points.PointsManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CmdNations implements Subcommand {
	private static final CommandArguments args = new CommandArguments();
	private static final Formatter fmt = new Formatter();
	private static final String month;
	
	static {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		month = fmt.format("%tB %tb %tm", c, c, c).toString();
	}

	@Override
	public String getDescription() {
		return null;
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
		String temp = "testNation";
		TreeSet<NationEntry> sorted = new TreeSet<NationEntry>(PointsManager.getNationEntries());
		Iterator<NationEntry> iter = sorted.iterator();
		
		ComponentBuilder builder = new ComponentBuilder("&c&l» §6§Top Nation of " + month + ": §e" + temp + "«&8&m--------&6&l<\\n")
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/nations previous")))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nations previous"));
		builder.append("§6§l>§8§m--------&c&l» Nation Leaderboard «&8&m--------&6&l<\\n", FormatRetention.NONE);
		int i = 1;
		while (iter.hasNext() && i <= 5) {
			NationEntry e = iter.next();
			String name = e.getNation().getName();
			builder.append("§6§l" + i + ". §e" + name + "\\n", FormatRetention.NONE)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/nl towns " + name)))
			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nations towns " + name));
		}
		s.spigot().sendMessage(builder.create());
		
		/* Maybe not needed if I can send spigot messages to console?
		if (false) {
			s.sendMessage("&c&l» §6§Top Nation of " + month + ": §e" + temp + "«&8&m--------&6&l<");
			s.sendMessage("§6§l>§8§m--------&c&l» Nation Leaderboard «&8&m--------&6&l<\\n");
			while (iter.hasNext() && i <= 5) {
				NationEntry e = iter.next();
				String name = e.getNation().getName();
				builder.append("§6§l" + i + ". §e" + name + "\\n", FormatRetention.NONE)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("/nl towns " + name)))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nations towns " + name));
			}
		}
		*/
	}
}
