package me.neoblade298.neoquests.quests;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neoquests.objectives.ObjectiveInstance;
import me.neoblade298.neoquests.objectives.ObjectiveSetInstance;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class Quester {
	private UUID uuid;
	private HashMap<String, CompletedQuest> completedQuests = new HashMap<String, CompletedQuest>();
	private HashMap<String, QuestInstance> activeQuests = new HashMap<String, QuestInstance>();;
	
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
		getPlayer().sendTitle("§fQuest Completed", "§6" + qi.getQuest().getName(), 10, 70, 10);
		getPlayer().sendMessage("§4[§c§lMLMC§4] §7You completed quest: §6" + qi.getQuest().getName() + "§7!");
	}
	
	public void cancelQuest(String name) {
		if (activeQuests.containsKey(name.toUpperCase())) {
			QuestInstance qi = activeQuests.remove(name.toUpperCase());
			qi.cleanup();
			getPlayer().sendTitle("§fQuest Cancelled", "§6" + qi.getQuest().getName(), 10, 70, 10);
		}
	}
	
	public void startQuest(Quest q) {
		getPlayer().sendTitle("§fQuest Started", "§6" + q.getName(), 10, 70, 10);
		getPlayer().sendMessage("§4[§c§lMLMC§4] §7You started quest: §6" + q.getName() + "§7! Type §c/quest§7!");
		QuestInstance qi = new QuestInstance(this, q);
		activeQuests.put(q.getKey(), qi);
		qi.initialize();
	}
	
	public void displayQuests(CommandSender s) {
		for (QuestInstance qi : activeQuests.values()) {
			ComponentBuilder builder = new ComponentBuilder("§6-[" + qi.getQuest().getName() + "]- ");
			ComponentBuilder quitquest = new ComponentBuilder("§e<Click to Quit Quest>")
					.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests quit " + qi.getQuest().getName()));
			s.spigot().sendMessage(builder.append(quitquest.create()).create());
			for (ObjectiveSetInstance osi : qi.getObjectiveSetInstances().values()) {
				s.sendMessage("§e" + osi.getSet().getDisplay() + ":");
				for (ObjectiveInstance oi : osi.getObjectives()) {
					s.sendMessage("§7- " + oi.getObjective().getDisplay() + "§f: " + oi.getCount() + " / " + oi.getObjective().getNeeded());
				}
			}
		}
		ComponentBuilder builder = new ComponentBuilder("§e<Click for recommended quest!>")
				.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/quests guide"));
		s.spigot().sendMessage(builder.create());
	}
}
