package me.neoblade298.neoquests.navigation;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import me.neoblade298.neocore.util.Util;


public class PathwayInstance {
	private Player p;
	private Pathway pathway;
	private BukkitTask task;
	public PathwayInstance(Player p, Pathway pathway) {
		this.p = p;
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
	
	public void stop() {
		task.cancel();
		Util.msg(p, "§7Navigation from §6" + pathway.getStartPoint() + " §7to §6" + pathway.getEndPoint() + " §7was successful!");
	}
	
	public void cancel(String reason) {
		task.cancel();
		Util.msg(p, "§cNavigation from §6" + pathway.getStartPoint() + " §cto §6" + pathway.getEndPoint() + " §cwas cancelled, " + reason);
	}
}

