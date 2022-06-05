package me.neoblade298.neoquests.navigation;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.Neoblade298.NeoProfessions.Utilities.Util;

public class PathwayInstance {
	private Player p;
	private Pathway pathway;
	private BukkitTask task;
	public PathwayInstance(Pathway pathway) {
		this.pathway = pathway;
	}
	public Pathway getPathway() {
		return pathway;
	}
	public void setTask(BukkitTask task) {
		this.task = task;
	}
	public BukkitTask getTask() {
		return task;
	}
	
	public void stop(boolean success) {
		if (success) {
			task.cancel();
			Util.sendMessage(p, "§7Navigation from §6" + pathway.getStartDisplay() + " §7to §6" + pathway.getEndDisplay() + " §7was successful!");
		}
		else {
			task.cancel();
			Util.sendMessage(p, "§cNavigation from §6" + pathway.getStartDisplay() + " §cto §6" + pathway.getEndDisplay() + " §cwas cancelled.");
		}
	}
}

