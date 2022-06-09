package me.neoblade298.neoquests.conversations;

import java.util.ArrayList;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.conditions.ConditionResult;
import me.neoblade298.neoquests.quests.Quest;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ConversationResponse {
	private String text;
	private ArrayList<Condition> conditions = new ArrayList<Condition>();
	private ActionSequence actions = new ActionSequence();
	int next = -1;

	public ConversationResponse(ConfigurationSection cfg) throws NeoIOException {
		this.text = cfg.getString("text").replaceAll("&", "§");
		this.actions.load(cfg.getStringList("actions"));
		this.conditions = ConditionManager.parseConditions(cfg.getStringList("conditions"));
		this.next = cfg.getInt("next", -3);

		if (actions.getQuest() != null) {
			for (Condition cond : actions.getQuest().getConditions()) {
				this.conditions.add(cond);
			}
		}
	}

	public ConversationResponse() {
		this.text = "§7[End Conversation]";
		this.next = -1;
	}

	// True if number should be incremented
	public boolean showResponse(Player p, int num) {
		ArrayList<Condition> failed = ConditionManager.getFailedConditions(p, conditions); // Pos 0 is blocking
		if (!failed.isEmpty()) {
			if (failed.get(0).getResult().equals(ConditionResult.UNCLICKABLE)) { // Unclickable
				StringBuilder failHover = new StringBuilder("§c§oCannot be selected:");
				for (Condition failedCond : failed) {
					failHover.append("\n- " + failedCond.getExplanation(p));
				}
				ComponentBuilder builder = new ComponentBuilder("§c§l[" + num + "] " + text)
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(failHover.toString())));
				p.spigot().sendMessage(builder.create());
				return true;
			}
			else { // Invisible
				return false;
			}
		}
		else { // Visible and passes conditions
			Quest q = actions.getQuest();
			if (q != null) {
				ComponentBuilder builder = new ComponentBuilder("§c§l[" + num + "] §7" + text + " §6<Starts Quest>")
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
								new Text("§oClick to select " + num + "\n§oThis starts the quest §6" + q.getDisplay())))
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Integer.toString(num)));
				p.spigot().sendMessage(builder.create());
			}
			else {
				ComponentBuilder builder = new ComponentBuilder("§c§l[" + num + "] §7" + text)
						.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§oClick to select " + num)))
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Integer.toString(num)));
				p.spigot().sendMessage(builder.create());
			}
			return true;
		}
	}

	// Returns next stage
	public boolean tryResponse(Player p) {
		ArrayList<Condition> failed = ConditionManager.getFailedConditions(p, conditions); // Pos 0 is blocking
																							// condition
		if (!failed.isEmpty()) {
			StringBuilder failExpl = new StringBuilder("§c§oCannot be selected:");
			for (Condition failedCond : failed) {
				failExpl.append("\n- " + failedCond.getExplanation(p));
			}
			p.sendMessage(failExpl.toString());
			return false;
		}
		else {
			return true;
		}
	}

	public int getNext() {
		return next;
	}

	public ActionSequence getActions() {
		return actions;
	}

	public static ArrayList<ConversationResponse> parseResponses(ConfigurationSection cfg) throws NeoIOException {
		ArrayList<ConversationResponse> responses = new ArrayList<ConversationResponse>();
		for (String key : cfg.getKeys(false)) {
			responses.add(new ConversationResponse(cfg.getConfigurationSection(key)));
		}
		return responses;
	}
}
