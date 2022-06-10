package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsGuide implements Subcommand {
	private static final CommandArguments args = new CommandArguments();

	@Override
	public String getDescription() {
		return "Lists recommended quests for the player's level";
	}

	@Override
	public String getKey() {
		return "guide";
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
		Player p = (Player) s;
		Quester q = QuestsManager.getQuester(p);
		q.displayGuide(s);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
