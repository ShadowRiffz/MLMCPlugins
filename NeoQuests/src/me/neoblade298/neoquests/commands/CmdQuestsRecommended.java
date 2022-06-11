package me.neoblade298.neoquests.commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;

import io.lumine.mythic.bukkit.utils.lib.lang3.StringUtils;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.util.PaginatedList;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.quests.CompletedQuest;
import me.neoblade298.neoquests.quests.QuestRecommendation;
import me.neoblade298.neoquests.quests.Quester;
import me.neoblade298.neoquests.quests.QuestsManager;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class CmdQuestsRecommended implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("page", false)));

	@Override
	public String getDescription() {
		return "Lists recommended quests for you to take";
	}

	@Override
	public String getKey() {
		return "recommended";
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
		run(s, args, false);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}

	public static void run(CommandSender s, String[] args, boolean challenges) {
		Player p = (Player) s;
		
		if (args.length == 1 && !StringUtils.isNumeric(args[0])) {
			Util.msg(s, "&cInvalid argument, must be a page number!");
			return;
		}
		
		Quester quester = QuestsManager.getQuester(p);
		int page = Integer.parseInt(args[0]) - 1;
		ArrayList<QuestRecommendation> recs = challenges ? QuestsManager.getChallenges() : QuestsManager.getRecommendations();
		PaginatedList<QuestRecommendation> pages = new PaginatedList<QuestRecommendation>();
		for (QuestRecommendation rec : recs) {
			int min = rec.getMin(), max = rec.getMax(), level = SkillAPI.getPlayerData(p).getMainClass().getLevel();
			if (min == -1 || min > level) return;
			if (max == -1 || max < level) return;
			if (quester.getCompletedQuest(rec.getQuest().getKey()) != null) return;

			pages.add(rec);
		}
		
		if (page < 0 || page > pages.pages() - 1) {
			Util.msg(s, "&cPage out of bounds!");
			return;
		}
		
		for (QuestRecommendation rec : pages.get(page)) {
			ComponentBuilder text = new ComponentBuilder("§7- §6" + rec.getQuest().getDisplay());
			ComponentBuilder nav = new ComponentBuilder("§7§o[Click for GPS]")
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nav to " + rec.getEndpoint()));
			p.spigot().sendMessage(text.append(nav.create()).create());
		}
	}
}
