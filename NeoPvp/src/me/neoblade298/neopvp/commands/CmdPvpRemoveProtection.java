package me.neoblade298.neopvp.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neopvp.PvpAccount;
import me.neoblade298.neopvp.PvpManager;

public class CmdPvpRemoveProtection implements Subcommand {
	private static final CommandArguments args = new CommandArguments(new CommandArgument("player", false));

	@Override
	public String getDescription() {
		return "Removes a player's protection";
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
	public void run(CommandSender s, String[] args) {
		if (args.length == 0) {
			PvpManager.getAccount((Player) s).removeProtection();
		}
		else {
			if (s.hasPermission("mycommand.staff")) {
				PvpManager.getAccount(Bukkit.getPlayer(args[0])).removeProtection();
				Util.msg(s, "&7Successfully removed player's protection.");
			}
			else {
				Util.msg(s, "&cYou can't use this command!");
			}
		}
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
