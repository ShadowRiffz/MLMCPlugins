package me.neoblade298.neoquests.quests;

import java.util.Collection;
import java.util.HashMap;
import java.util.TreeMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;
import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.listeners.ObjectiveListener;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Quester {
	private Player p;
	private int acct;
	private HashMap<String, CompletedQuest> completedQuests = new HashMap<String, CompletedQuest>();
	private HashMap<String, QuestInstance> activeQuests = new HashMap<String, QuestInstance>();
	private TreeMap<String, Questline> activeQuestlines = new TreeMap<String, Questline>();
	private Location loc;
	
	public Quester(Player p, int acct) {
		this.p = p;
		this.acct = acct;
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
		if (ql != null) {
			if (ql.getLastQuest().equals(qi.getQuest().getKey())) {
				activeQuestlines.remove(ql.getKey());
			}
			else {
				ConversationManager.startConversation(p, ql.getNextQuest(p).getStartConversation(), false);
			}
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

			p.sendMessage("§4[§c§lMLMC§4] §7You cancelled quest: §6" + qi.getQuest().getDisplay() + "§7!");
			p.sendTitle("§fQuest Cancelled", "§6" + qi.getQuest().getDisplay(), 10, 70, 10);
		}
	}
	
	private QuestInstance cleanupQuest(String key) {
		QuestInstance qi = activeQuests.remove(key.toUpperCase());
		qi.stopListening();
		return qi;
	}
	
	public void startQuest(Quest q) {
		startQuest(q, false);
	}
	
	public void startQuest(Quest q, boolean ignoreConditions) {
		if (!ignoreConditions) {
			Condition c = ConditionManager.getBlockingCondition(p, q.getConditions());
			if (c != null) {
				p.sendMessage("§4[§c§lMLMC§4] §cCould not start quest: §6" + q.getDisplay() + "§c: " + c.getExplanation(p));
				return;
			}
		}
		p.sendTitle("§fQuest Started", "§6" + q.getDisplay(), 10, 70, 10);
		p.sendMessage("§4[§c§lMLMC§4] §7You started quest: §6" + q.getDisplay() + "§7! Type §c/quest§7!");
		QuestInstance qi = new QuestInstance(this, q);
		activeQuests.put(q.getKey(), qi);
		if (q.getQuestline() != null) activeQuestlines.put(q.getKey(), q.getQuestline());
		qi.setupInstances(true);
		qi.displayObjectives(p);
	}
	
	public void displayQuests(CommandSender s) {
		if (activeQuests.size() > 0) {
			for (QuestInstance qi : activeQuests.values()) {
				ComponentBuilder builder = new ComponentBuilder("§6§l-[" + qi.getQuest().getDisplay() + "]- ");
				ComponentBuilder quitquest = new ComponentBuilder("§7§o[Click to Quit]")
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests quit " + qi.getQuest().getKey()));
				s.spigot().sendMessage(builder.append(quitquest.create()).create());
				qi.displayObjectives(s);
			}
			s.sendMessage("§7=====");
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
		if (activeQuestlines.size() > 0) {
			s.sendMessage("§eActive Questlines:");
			for (Questline ql : activeQuestlines.values()) {
				Quest next = ql.getNextQuest(p);
				if (next == null) {
					Bukkit.getLogger().warning("[NeoQuests] Player questline " + ql.getDisplay() + " returned null for next, this should never happen "
							+ "as questlines are removed once the last quest is completed.");
					continue;
				}
				ComponentBuilder builder = new ComponentBuilder("§7- §6" + ql.getDisplay() + " §7(§e" + next.getDisplay() + "§7) ");
				ComponentBuilder takequest = new ComponentBuilder("§7§o[Click to Take]")
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
	
	// Call when switching to another quest account
	public void stopListening() {
		ObjectiveListener.stopListening(p);
	}
	
	public void startListening() {
		for (QuestInstance qi : activeQuests.values()) {
			qi.startListening();
		}
	}
	
	public Collection<QuestInstance> getActiveQuests() {
		return activeQuests.values();
	}
	
	public void addActiveQuest(QuestInstance qi) {
		activeQuests.put(qi.getQuest().getKey(), qi);
	}
	
	public CompletedQuest getCompletedQuest(String quest) {
		return completedQuests.get(quest.toUpperCase());
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
	
	public int getAccount() {
		return acct;
	}
	
	public void reset() {
		this.loc = null;
		this.activeQuestlines.clear();
		for (QuestInstance qi : activeQuests.values()) {
			qi.cleanupInstances();
		}
		this.activeQuests.clear();
		this.completedQuests.clear();
	}
}
