package me.neoblade298.neoquests.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.PaginatedList;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conversations.Conversation;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;

public class CmdQuestsLog implements Subcommand {

	@Override
	public String getDescription() {
		return "Lists all quests you've completed";
	}

	@Override
	public String getKey() {
		return "log";
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
		Quester quester = QuestsManager.initializeOrGetQuester(p);
		if (quester.getCompletedQuests().size() == 0) {
			Util.msg(s, "&cYou don't yet have any completed quests!");
			return;
		}
		
		if (args.length > 1 && StringUtils.isNumeric(args[0])) {
			Util.msg(s, "&cInvalid argument! Must be a page number.");
			return;
		}
		
		PaginatedList<CompletedQuest> list = new PaginatedList<CompletedQuest>(quester.getCompletedQuests());
		int page = args.length == 1 ? Integer.parseInt(args[0]) - 1 : 0;
		if (page < 0 || page >= list.getTotalPages()) {
			Util.msg(s, "&cInvalid page number! Max page is " + list.getTotalPages());
			return;
		}
		
		for (CompletedQuest cq : list.getPage(page)) {
			String msg = "&7- ";
			msg += cq.isSuccess() ? "&a" : "&c";
			msg += cq.getQuest().getDisplay();
			Util.msg(s, msg);
		}
		Util.msg(s, "&7Page &f" + (page + 1) + " &7/ " + list.getTotalPages());
	}

	@Override
	public String getArgs() {
		return "{page}";
	}

	@Override
	public boolean isHidden() {
		return true;
	}
}
