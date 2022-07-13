package me.neoblade298.neopvp.commands;

import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neopvp.wars.WarManager;

public class CmdAWarDisplay implements Subcommand {

	@Override
	public String getDescription() {
		return "Sets display name of the war";
	}

	@Override
	public String getKey() {
		return "display";
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
		String display = args[0];
		for (int i = 1; i < args.length; i++) {
			display += " " + args[i];
		}
		WarManager.getWarCreator(s).setDisplay(display);
		WarManager.displayWarCreation(s);
	}
	
	@Override
	public String getArgOverride() {
		return "[display]";
	}

}
