package me.neoblade298.neocore.commands.builtin;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import me.neoblade298.neocore.bungee.BungeeAPI;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class CmdBCoreSend implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("player"), new CommandArgument("server")));

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
		return "send";
	}

	@Override
	public String getDescription() {
		return "Broadcasts a message on all servers";
	}

	@Override
	public String getArgOverride() {
		return "[msg]";
	}

	@Override
	public void run(CommandSender s, String[] args) {
		BungeeAPI.sendPlayer(Bukkit.getPlayer(args[0]), args[1]);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
