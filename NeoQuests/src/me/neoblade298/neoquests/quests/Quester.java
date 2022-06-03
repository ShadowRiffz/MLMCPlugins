package me.neoblade298.neoquests.quests;

import java.util.Collection;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neoquests.conversations.ConversationManager;
import me.neoblade298.neoquests.objectives.ObjectiveInstance;
import me.neoblade298.neoquests.objectives.ObjectiveSetInstance;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class Quester {
	private UUID uuid;
	private HashMap<String, CompletedQuest> completedQuests = new HashMap<String, CompletedQuest>();
	private HashMap<String, QuestInstance> activeQuests = new HashMap<String, QuestInstance>();;
	private TreeMap<String, Questline> activeQuestlines = new TreeMap<String, Questline>();
	
	public Quester(UUID uuid) {
		this.uuid = uuid;
	}
	
	public Player getPlayer() {
		return Bukkit.getPlayer(uuid);
	}
	
	public void completeQuest(QuestInstance qi, int stage, boolean success) {
		qi.cleanup();
		activeQuests.remove(qi.getQuest().getKey());
		completedQuests.put(qi.getQuest().getKey(), new CompletedQuest(qi.getQuest(), stage, success));
		Questline ql = qi.getQuest().getQuestline();
		if (ql != null && ql.getLastQuest().equals(qi.getQuest().getKey())) activeQuestlines.remove(ql.getKey());
		getPlayer().sendTitle("�fQuest Completed", "�6" + qi.getQuest().getName(), 10, 70, 10);
		getPlayer().sendMessage("�4[�c�lMLMC�4] �7You completed quest: �6" + qi.getQuest().getName() + "�7!");
		ConversationManager.startConversation(getPlayer(), ql.getNextQuest(getPlayer()).getStartConversation(), false);
	}
	
	public void cancelQuest(String name) {
		if (activeQuests.containsKey(name.toUpperCase())) {
			QuestInstance qi = activeQuests.remove(name.toUpperCase());
			qi.cleanup();
			getPlayer().sendTitle("�fQuest Cancelled", "�6" + qi.getQuest().getName(), 10, 70, 10);
		}
	}
	
	public void startQuest(Quest q) {
		getPlayer().sendTitle("�fQuest Started", "�6" + q.getName(), 10, 70, 10);
		getPlayer().sendMessage("�4[�c�lMLMC�4] �7You started quest: �6" + q.getName() + "�7! Type �c/quest�7!");
		QuestInstance qi = new QuestInstance(this, q);
		activeQuests.put(q.getKey(), qi);
		if (q.getQuestline() != null) activeQuestlines.add(q.getQuestline());
		qi.initialize();
	}
	
	public void displayQuests(CommandSender s) {
		for (QuestInstance qi : activeQuests.values()) {
			ComponentBuilder builder = new ComponentBuilder("�6-[" + qi.getQuest().getName() + "]- ");
			ComponentBuilder quitquest = new ComponentBuilder("�e<Click to Quit Quest>")
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests quit " + qi.getQuest().getKey()));
			s.spigot().sendMessage(builder.append(quitquest.create()).create());
			for (ObjectiveSetInstance osi : qi.getObjectiveSetInstances()) {
				s.sendMessage("�e" + osi.getSet().getDisplay() + ":");
				for (ObjectiveInstance oi : osi.getObjectives()) {
					s.sendMessage("�7- " + oi.getObjective().getDisplay() + "�f: " + oi.getCount() + " / " + oi.getObjective().getNeeded());
				}
			}
		}
		ComponentBuilder builder = new ComponentBuilder("�e<Click for other quests you can take!>")
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests guide"));
		s.spigot().sendMessage(builder.create());
	}
	
	public void displayGuide(CommandSender s) {
		for (QuestInstance qi : activeQuests.values()) {
			ComponentBuilder builder = new ComponentBuilder("�6-[" + qi.getQuest().getName() + "]- ");
			ComponentBuilder quitquest = new ComponentBuilder("�e<Click to Quit Quest>")
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests quit " + qi.getQuest().getName()));
			s.spigot().sendMessage(builder.append(quitquest.create()).create());
			for (ObjectiveSetInstance osi : qi.getObjectiveSetInstances()) {
				s.sendMessage("�e" + osi.getSet().getDisplay() + ":");
				for (ObjectiveInstance oi : osi.getObjectives()) {
					s.sendMessage("�7- " + oi.getObjective().getDisplay() + "�f: " + oi.getCount() + " / " + oi.getObjective().getNeeded());
				}
			}
		}
		if (activeQuestlines.size() > 0) {
			s.sendMessage("�eActive Questlines:");
			for (Questline ql : activeQuestlines.values()) {
				Quest next = ql.getNextQuest(getPlayer());
				ComponentBuilder builder = new ComponentBuilder("�7- �6" + ql.getDisplay() + " �7(�e" + next.getName() + "�7) ");
				ComponentBuilder takequest = new ComponentBuilder("�e<Click to Accept Quest>")
						.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests take " + next.getKey()));
				s.spigot().sendMessage(builder.append(takequest.create()).create());
			}
		}
		ComponentBuilder rec = new ComponentBuilder("�e<Click to show other recommended quests!>")
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests recommended"));
		ComponentBuilder side = new ComponentBuilder("�e<Click to show challenging sidequests!>")
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
}
