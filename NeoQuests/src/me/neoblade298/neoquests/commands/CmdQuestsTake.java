package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neoquests.conversations.Conversation;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsTake implements Subcommand {

	@Override
	public String getDescription() {
		return "Starts the specified quest";
	}

	@Override
	public String getKey() {
		return "take";
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
		Conversation conv = QuestsManager.getQuest(args[0]).getStartConversation();
		ConversationManager.startConversation(p, conv);
	}

	@Override
	public String getArgs() {
		return "[key]";
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}
