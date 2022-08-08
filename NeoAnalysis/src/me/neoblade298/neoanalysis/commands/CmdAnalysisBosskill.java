package me.neoblade298.neoanalysis.commands;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerClass;

import me.neoblade298.neoanalysis.NeoAnalysis;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class CmdAnalysisBosskill implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("Player"),
			new CommandArgument("Boss")));

	@Override
	public String getDescription() {
		return "Adds a sql line for when a player killed a boss";
	}

	@Override
	public String getKey() {
		return "bosskill";
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
		Player p = (Player) s;
		UUID uuid = p.getUniqueId();
		PlayerClass pclass = SkillAPI.getPlayerData(p).getMainClass();
		Statement stmt = NeoCore.getStatement();
		
		new BukkitRunnable() {
			public void run() {
				try {
					stmt.executeUpdate("REPLACE INTO analysis_bosskills VALUES ('" + uuid + "','" + p.getName() + "','" + pclass.getData().getName() +
							"','" + args[1] + "'," + pclass.getLevel() + ");");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.runTaskAsynchronously(NeoAnalysis.inst());
	}
}
