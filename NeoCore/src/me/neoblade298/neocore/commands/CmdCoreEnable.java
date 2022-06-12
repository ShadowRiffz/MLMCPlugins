package me.neoblade298.neocore.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.listeners.IOListener;
import me.neoblade298.neocore.util.Util;

public class CmdCoreEnable implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("save/preload/load/cleanup", false)));

	@Override
	public String getPermission() {
		return "neocore.admin";
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public String getKey() {
		return "enable";
	}

	@Override
	public String getDescription() {
		return "Enables an IO action: save, preload, load, cleanup";
	}

	@Override
	public void run(CommandSender s, String[] args) {
		if (args.length == 0) {
			Util.msg(s, "&7Valid IO actions: save, preload, load, cleanup");
		}
		else {
			switch (args[0].toLowerCase()) { 
			case "save": IOListener.setCanSave(true); break;
			case "load": IOListener.setCanLoad(true); break;
			case "preload": IOListener.setCanPreload(true); break;
			case "cleanup": IOListener.setCanCleanup(true); break;
			}
			Util.msg(s, "Successfully set " + args[0].toLowerCase() + " to enabled.");
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
