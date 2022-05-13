package me.neoblade298.neoquests.conversations;

import java.util.ArrayList;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import me.neoblade298.neoquests.actions.ActionSequence;
import me.neoblade298.neoquests.actions.EndConversationAction;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionResult;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class ConversationResponse {
	private String text;
	private ArrayList<Condition> conditions;
	private ActionSequence startActions;
	int next = -1;
	
	public ConversationResponse(ConfigurationSection cfg) {
		this.text = cfg.getString("text").replaceAll("&", "§");
		this.startActions = new ActionSequence(cfg.getStringList("actions"));
		this.conditions = Condition.parseConditions(cfg.getStringList("conditions"));
		this.next = cfg.getInt("next");
	}
	
	public ConversationResponse() {
		this.text = "§7[End Conversation]";
		this.startActions = new ActionSequence(new EndConversationAction());
	}
	
	// True if number should be incremented
	public boolean showResponse(Player p, int num) {
		ArrayList<Condition> failed = Condition.getFailedConditions(p, conditions); // Pos 0 is blocking condition
		if (!failed.isEmpty()) {
			if (failed.get(0).getResult().equals(ConditionResult.UNCLICKABLE)) { // Unclickable
				StringBuilder failHover = new StringBuilder("§c§oCannot be selected:");
				for (Condition failedCond : failed) {
					failHover.append("\n- " + failedCond.getExplanation(p));
				}
				ComponentBuilder builder = new ComponentBuilder("§c§o[" + num + "] " + text)
				.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(failHover.toString())));
				p.spigot().sendMessage(builder.create());
				return true;
			}
			else { // Invisible
				return false;
			}
		}
		else { // Visible and passes conditions
			ComponentBuilder builder = new ComponentBuilder("§c§o[" + num + "] §7" + text)
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§oClick to select this response!")))
			.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, Integer.toString(num)));
			p.spigot().sendMessage(builder.create());
			return true;
		}
	}
	
	// Returns next stage
	public boolean tryResponse(Player p) {
		ArrayList<Condition> failed = Condition.getFailedConditions(p, conditions); // Pos 0 is blocking condition
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
		return startActions;
	}
	
	public static ArrayList<ConversationResponse> parseResponses(ConfigurationSection cfg) {
		ArrayList<ConversationResponse> responses = new ArrayList<ConversationResponse>();
		for (String key : cfg.getKeys(false)) {
			responses.add(new ConversationResponse(cfg.getConfigurationSection(key)));
		}
		return responses;
	}
}
