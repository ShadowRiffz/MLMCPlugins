package me.neoblade298.neoleaderboard.commands;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Iterator;
import java.util.TreeSet;

import org.bukkit.command.CommandSender;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;

import me.neoblade298.neocore.commands.CommandArgument;
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

public class CmdNLTowns implements Subcommand {
	private static final CommandArguments args = new CommandArguments(new CommandArgument("nation"));
	private static final Formatter fmt = new Formatter();
	private static final String month;
	
	static {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		month = fmt.format("%tB", c).toString();
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
		Nation n = TownyUniverse.getInstance().getNation(args[0]);
		TreeSet<NationEntry> sorted = new TreeSet<NationEntry>(PointsManager.getNationEntries());
		Iterator<NationEntry> iter = sorted.iterator();
		
		ComponentBuilder builder = new ComponentBuilder("§6§l>§8§m--------§c§l» §6Point Contribution: §e" + n.getName() + " §c§l«§8§m--------§6§l<");
		int i = 1;
		while (iter.hasNext() && i <= 5) {
			NationEntry e = iter.next();
			String name = e.getNation().getName();
			String hovertext = "Click for details: §e/nl towns " + name;
			hovertext += "\n";
			builder.append("\n§6§l" + i + ". §e" + name + " §7- §f" + e.getEffectivePoints(), FormatRetention.NONE)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hovertext)))
			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nl towns " + name));
		}
		s.spigot().sendMessage(builder.create());
	}
}
