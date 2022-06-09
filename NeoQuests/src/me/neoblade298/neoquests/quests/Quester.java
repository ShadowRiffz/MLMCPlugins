package me.neoblade298.neoquests.quests;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;
import me.neoblade298.neoquests.objectives.ObjectiveSetInstance;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Quester {
	private Player p;
	private HashMap<String, CompletedQuest> completedQuests = new HashMap<String, CompletedQuest>();
	private HashMap<String, QuestInstance> activeQuests = new HashMap<String, QuestInstance>();
	private TreeMap<String, Questline> activeQuestlines = new TreeMap<String, Questline>();
	private Location loc;
	
	public Quester(Player p) {
		this.p = p;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public void completeQuest(QuestInstance qi, int stage, boolean success) {
		cleanupQuest(qi.getQuest().getKey());
		completedQuests.put(qi.getQuest().getKey(), new CompletedQuest(qi.getQuest(), stage, success));
		Questline ql = qi.getQuest().getQuestline();
		p.sendTitle("§fQuest Completed", "§6" + qi.getQuest().getDisplay(), 10, 70, 10);
		p.sendMessage("§4[§c§lMLMC§4] §7You completed quest: §6" + qi.getQuest().getDisplay() + "§7!");
		if (ql != null && ql.getLastQuest().equals(qi.getQuest().getKey())) {
			activeQuestlines.remove(ql.getKey());
			ConversationManager.startConversation(p, ql.getNextQuest(p).getStartConversation(), false);
		}
	}
	
	public void cancelQuest(String key) {
		if (activeQuests.containsKey(key.toUpperCase())) {
			QuestInstance qi = cleanupQuest(key.toUpperCase());
			
			// If cancelling the first quest in a questline, that questline is no longer active
			if (qi.getQuest().getQuestline() != null) {
				Questline ql = qi.getQuest().getQuestline();
				if (ql.getFirstQuest().getKey().equals(qi.getQuest().getKey())) {
					activeQuestlines.remove(ql.getKey());
				}
			}
			
			p.sendTitle("§fQuest Cancelled", "§6" + qi.getQuest().getDisplay(), 10, 70, 10);
		}
	}
	
	private QuestInstance cleanupQuest(String key) {
		QuestInstance qi = activeQuests.remove(key.toUpperCase());
		qi.cleanup();
		return qi;
	}
	
	public void startQuest(Quest q) {
		Condition c = ConditionManager.getBlockingCondition(p, q.getConditions());
		if (c != null) {
			p.sendMessage("§4[§c§lMLMC§4] §cCould not start quest: §6" + q.getDisplay() + "§c, " + c.getExplanation(p));
		}
		p.sendTitle("§fQuest Started", "§6" + q.getDisplay(), 10, 70, 10);
		p.sendMessage("§4[§c§lMLMC§4] §7You started quest: §6" + q.getDisplay() + "§7! Type §c/quest§7!");
		QuestInstance qi = new QuestInstance(this, q);
		activeQuests.put(q.getKey(), qi);
		if (q.getQuestline() != null) activeQuestlines.put(q.getKey(), q.getQuestline());
		qi.initialize();
	}
	
	public void displayQuests(CommandSender s) {
		if (activeQuests.size() > 0) {
			for (QuestInstance qi : activeQuests.values()) {
				ComponentBuilder builder = new ComponentBuilder("§6-[" + qi.getQuest().getDisplay() + "]- ");
				ComponentBuilder quitquest = new ComponentBuilder("§e<Click to Quit Quest>")
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests quit " + qi.getQuest().getKey()));
				s.spigot().sendMessage(builder.append(quitquest.create()).create());
				for (ObjectiveSetInstance osi : qi.getObjectiveSetInstances()) {
					s.sendMessage("§e" + osi.getSet().getDisplay() + ":");
					for (ObjectiveInstance oi : osi.getObjectives()) {
						String msg = "§7- " + oi.getObjective().getDisplay() + "§f: " + oi.getCount() + " / " + oi.getObjective().getNeeded();
						if (oi.getObjective().getEndpoint() != null) {
							builder = new ComponentBuilder(msg);
							ComponentBuilder nav = new ComponentBuilder(" §e<Click for Navigation>")
									.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nav to " + oi.getObjective().getEndpoint()));
							s.spigot().sendMessage(builder.append(nav.create()).create());
						}
						else {
							s.sendMessage(msg);
						}
					}
				}
			}
		}
		else {
			s.sendMessage("§7You have no active quests!");
		}
		ComponentBuilder builder = new ComponentBuilder("§e<Click for other quests you can take!>")
				.event(new HoverEvent(Action.SHOW_TEXT, new Text("/quests guide")))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests guide"));
		s.spigot().sendMessage(builder.create());
	}
	
	public void displayGuide(CommandSender s) {
		for (QuestInstance qi : activeQuests.values()) {
			ComponentBuilder builder = new ComponentBuilder("§6-[" + qi.getQuest().getDisplay() + "]- ");
			ComponentBuilder quitquest = new ComponentBuilder("§e<Click to Quit Quest>")
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests quit " + qi.getQuest().getKey()))
					.event(new HoverEvent(Action.SHOW_TEXT, new Text("/quests quit")));
			s.spigot().sendMessage(builder.append(quitquest.create()).create());
			for (ObjectiveSetInstance osi : qi.getObjectiveSetInstances()) {
				s.sendMessage("§e" + osi.getSet().getDisplay() + ":");
				for (ObjectiveInstance oi : osi.getObjectives()) {
					s.sendMessage("§7- " + oi.getObjective().getDisplay() + "§f: " + oi.getCount() + " / " + oi.getObjective().getNeeded());
				}
			}
		}
		if (activeQuestlines.size() > 0) {
			s.sendMessage("§eActive Questlines:");
			for (Questline ql : activeQuestlines.values()) {
				Quest next = ql.getNextQuest(p);
				ComponentBuilder builder = new ComponentBuilder("§7- §6" + ql.getDisplay() + " §7(§e" + next.getDisplay() + "§7) ");
				ComponentBuilder takequest = new ComponentBuilder("§e<Click to Accept Quest>")
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests take " + next.getKey()))
						.event(new HoverEvent(Action.SHOW_TEXT, new Text("/quests take")));
				s.spigot().sendMessage(builder.append(takequest.create()).create());
			}
		}
		ComponentBuilder rec = new ComponentBuilder("§e<Click to show other recommended quests!>")
				.event(new HoverEvent(Action.SHOW_TEXT, new Text("/quests recommended")))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests recommended"));
		ComponentBuilder side = new ComponentBuilder("§e<Click to show challenging sidequests!>")
				.event(new HoverEvent(Action.SHOW_TEXT, new Text("/quests challenges")))
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests challenges"));
		s.spigot().sendMessage(rec.create());
		s.spigot().sendMessage(side.create());
	}
	
	public Collection<QuestInstance> getActiveQuests() {
		return activeQuests.values();
	}
	
	public void resumeQuest(QuestInstance qi) {
		activeQuests.put(qi.getQuest().getKey(), qi);
	}
	
	public CompletedQuest getCompletedQuest(String quest) {
		return completedQuests.get(quest);
	}
	
	public Collection<CompletedQuest> getCompletedQuests() {
		return completedQuests.values();
	}
	
	public void addQuestline(Questline ql) {
		this.activeQuestlines.put(ql.getKey(), ql);
	}
	
	public void addCompletedQuest(CompletedQuest cq) {
		this.completedQuests.put(cq.getQuest().getKey(), cq);
	}
	
	public Collection<Questline> getActiveQuestlines() {
		return activeQuestlines.values();
	}
	
	public void setActiveQuests(HashMap<String, QuestInstance> activeQuests) {
		this.activeQuests = activeQuests;
	}
	
	public HashMap<String, QuestInstance> getActiveQuestsHashMap() {
		return activeQuests;
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public void setLocation(Location loc) {
		this.loc = loc;
	}
}
