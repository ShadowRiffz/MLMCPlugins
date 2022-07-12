package me.neoblade298.neopvp.commands;

import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;

import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neopvp.wars.War;
import me.neoblade298.neopvp.wars.WarManager;
import me.neoblade298.neopvp.wars.WarTeam;

public class CmdAWarTeam2 implements Subcommand {
	private 

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
		return "mycommand.staff";
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
