package me.neoblade298.neoquests.commands;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestAdminReset implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("player"),
			new CommandArgument("account #", false))));

	@Override
	public String getDescription() {
		return "Resets a player's completed quests";
	}

	@Override
	public String getKey() {
		return "reset";
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
		Player p = Bukkit.getPlayer(args[0]);
		
		// Reset entire player
		Statement stmt = NeoCore.getStatement();
		try {
			if (args.length == 1) {
				stmt.executeQuery("DELETE FROM quests_completed WHERE uuid = '" + p.getUniqueId() + "';");
				for (Quester quester : QuestsManager.getAllAccounts(p)) {
					quester.getCompletedQuests().clear();
				}
			}
			else {
				if (!StringUtils.isNumeric(args[1])) {
					Util.msg(s, "&cAccount must be a number!");
				}
				int acct = Integer.parseInt(args[1]);
				Quester quester = QuestsManager.getQuester(p, acct);
				if (quester == null) {
					Util.msg(s, "&cThis account doesn't exist!");
				}
				stmt.executeQuery("DELETE FROM quests_completed WHERE uuid = '" + p.getUniqueId() + "' AND account = " + args[1] + ";");
				quester.getCompletedQuests().clear();
			}
		}
		catch (Exception e) {
			Util.msg(s, "&cCommand failed! Stack trace in console.");
			e.printStackTrace();
		} 
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
