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

public class CmdPvpBase implements Subcommand {
	private static final CommandArguments args = new CommandArguments(new CommandArgument("player", false));

	@Override
	public String getDescription() {
		return "Views player's pvp account stats";
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
			PvpManager.getAccount((Player) s).displayAccount(s);
		}
		else {
			PvpAccount acct = PvpManager.getAccount(Bukkit.getPlayer(args[0]));
			if (acct != null) {
				acct.displayAccount(s);
			}
			else {
				Util.msg(s, "&cCould not find player! Maybe they're not online?");
			}
		}
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
