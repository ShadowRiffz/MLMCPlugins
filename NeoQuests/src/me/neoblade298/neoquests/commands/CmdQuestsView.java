package me.neoblade298.neoquests.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conversations.Conversation;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsView implements Subcommand {

	@Override
	public String getDescription() {
		return "Checks another user's quests";
	}

	@Override
	public String getKey() {
		return "view";
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
		if (args.length != 1) {
			Util.msg(s, "&cInvalid number of arguments!");
			return;
		}
		Player p = Bukkit.getPlayer(args[1]);
		if (p == null) {
			Util.msg(s, "&cThat player is not online!");
			return;
		}
		
		QuestsManager.getQuester(p).displayQuests(s);
	}

	@Override
	public String getArgs() {
		return "[player]";
	}
}
