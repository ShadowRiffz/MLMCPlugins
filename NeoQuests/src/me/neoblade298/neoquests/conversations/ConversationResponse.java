package me.neoblade298.neoquests.conversations;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neoblade298.neoquests.actions.ActionSequence;
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
	private int nextStage;
	
	public boolean showResponse(Player p, int num) {
		ArrayList<Condition> failed = Condition.getFailedConditions(p, conditions); // Pos 0 is blocking condition
		if (!failed.isEmpty()) {
			if (failed.get(0).getResult().equals(ConditionResult.UNCLICKABLE)) { // Unclickable
				StringBuilder failHover = new StringBuilder("§c§oCannot be selected:");
				for (Condition failedCond : failed) {
					failHover.append(failedCond.getExplanation(p));
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
			ComponentBuilder builder = new ComponentBuilder("§c§o[" + num + "] §7" + text);
			.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("§oClick to select this response!")));
			return true;
		}
	}
	
	public void chooseResponse(Player p) {
		// Needs to check conditions first to be sure it's not greyed out
	}
}
