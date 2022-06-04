package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsQuit implements Subcommand {

	@Override
	public String getDescription() {
		return "Quits the specified quest";
	}

	@Override
	public String getKey() {
		return "quit";
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
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		Quester q = QuestsManager.getQuester(p);
		// /quests quit
		if (args.length == 1) {
			if (q.getActiveQuests().size() > 1) {
				NeoQuests.getCommands().get("quest").runCommand("", s, new String[0]);
			}
			else if (q.getActiveQuests().size() == 1) {
				q.cancelQuest(args[1]);
			}
			else {
				Util.sendMessage(p, "§cYou don't have any active quests!");
			}
		}
		else {
			q.cancelQuest(args[1]);
		}
	}

	@Override
	public String getArgs() {
		return null;
	}

}
