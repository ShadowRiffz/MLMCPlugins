package me.neoblade298.neocore.commands.builtin;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.messaging.MessagingManager;
import me.neoblade298.neocore.util.Util;

public class CmdCoreMessage implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(
			new CommandArgument("player", false), new CommandArgument("message")));

	@Override
	public String getPermission() {
		return "mycommand.staff";
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public String getKey() {
		return "sendmsg";
	}

	@Override
	public String getDescription() {
		return "Plays a message";
	}

	@Override
	public void run(CommandSender s, String[] args) {
		if (args.length == 1) {
			MessagingManager.sendMessage(s, args[0]);
		}
		else if (args.length == 2) {
			Player p = Bukkit.getPlayer(args[1]);

			MessagingManager.sendMessage(p, args[1]);
			Util.msg(s, "&7Successfully sent message");
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
