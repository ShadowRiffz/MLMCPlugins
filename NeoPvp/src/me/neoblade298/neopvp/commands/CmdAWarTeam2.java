package me.neoblade298.neopvp.commands;

import org.bukkit.command.CommandSender;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class CmdAWarTeam2 implements Subcommand {

	@Override
	public String getDescription() {
		return "Modify team 2";
	}

	@Override
	public String getKey() {
		return "team2";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.PLAYER_ONLY;
	}
	
	@Override
	public String getArgOverride() {
		return "[create [name]/+nation [nation]/-nation [nation]/setspawn/setmascotspawn]";
	}

	@Override
	public void run(CommandSender s, String[] args) {
		CmdAWarTeam1.runCommand(s, args, 2);
	}

}
