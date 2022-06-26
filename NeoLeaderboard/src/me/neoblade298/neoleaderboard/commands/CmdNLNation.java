package me.neoblade298.neoleaderboard.commands;

import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.command.CommandSender;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.object.Nation;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoleaderboard.points.NationEntry;
import me.neoblade298.neoleaderboard.points.PlayerEntry;
import me.neoblade298.neoleaderboard.points.PointsManager;
import me.neoblade298.neoleaderboard.points.TownEntry;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.hover.content.Text;

public class CmdNLNation implements Subcommand {
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
		NationEntry ne = PointsManager.getNationEntry(n.getUUID());
		Iterator<UUID> iter = ne.getTopTownOrder().descendingIterator();
		HashMap<UUID, TownEntry> towns = ne.getAllTownPoints();
		
		ComponentBuilder builder = new ComponentBuilder("§6§l>§8§m--------§c§l» §6Point Contribution: §e" + n.getName() + " §c§l«§8§m--------§6§l<");
		int i = 1;
		while (iter.hasNext() && i <= 10) {
			TownEntry e = towns.get(iter.next());
			String name = e.getTown().getName();
			builder.append("\n§6§l" + i + ". §e" + name + " §7- §f" + e.getTotalPoints(), FormatRetention.NONE)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(buildTownHover(e))))
			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nl towns " + name));
		}
		s.spigot().sendMessage(builder.create());
	}
	
	private String buildTownHover(TownEntry e) {
		String hovertext = "Click for details: §e/nl town " + e.getNation().getName() + "\n";
		hovertext += "§6Top town contributors:";
		
		TreeSet<UUID> playerOrder = e.getTopPlayers()
		HashMap<UUID, PlayerEntry> players = e.getAllPlayerPoints();
		Iterator<UUID> iter = playerOrder.descendingIterator();
		for (int i = 1; i <= 10 && iter.hasNext(); i++) {
			UUID uuid = iter.next();
			PlayerEntry pe = players.get(uuid);
			hovertext += "\n§6§l" + i + ". §e" + PointsManager.calculateEffectivePoints(
					PointsManager.getNationEntry(e.getNation().getUUID()), pe.getContributed());
		}
		return hovertext;
	}
}
