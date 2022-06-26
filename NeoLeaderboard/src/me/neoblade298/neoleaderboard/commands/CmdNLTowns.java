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
import me.neoblade298.neoleaderboard.points.PointsManager;
import me.neoblade298.neoleaderboard.points.TownEntry;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CmdNLTowns implements Subcommand {
	private static final CommandArguments args = new CommandArguments(new CommandArgument("nation"));

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getKey() {
		return "nation";
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
		TreeSet<TownEntry> sorted = new TreeSet<TownEntry>(PointsManager.getTownEntriesFromNation(n));
		Iterator<TownEntry> iter = sorted.iterator();
		
		ComponentBuilder builder = new ComponentBuilder("§6§l>§8§m--------§c§l» §6Point Contribution: §e" + n.getName() + " §c§l«§8§m--------§6§l<");
		int i = 1;
		while (iter.hasNext() && i <= 5) {
			TownEntry e = iter.next();
			String name = e.getTown().getName();
			String hovertext = "Click for details: §e/nl residents " + name;
			hovertext += "\n";
			builder.append("\n§6§l" + i + ". §e" + name + " §7- §f" + e.getTotalPoints(), FormatRetention.NONE)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hovertext)))
			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nl towns " + name));
		}
		s.spigot().sendMessage(builder.create());
	}
}
