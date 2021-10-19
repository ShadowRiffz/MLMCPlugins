package me.Neoblade298.NeoConsumables.runnables;

import org.bukkit.scheduler.BukkitTask;

import me.Neoblade298.NeoConsumables.objects.Attributes;

public class AttributeTask {
	BukkitTask task;
	Attributes attr;
	public AttributeTask(BukkitTask task, Attributes attr) {
		this.task = task;
		this.attr = attr;
	}
	public BukkitTask getTask() {
		return task;
	}
	public void setTask(BukkitTask task) {
		this.task = task;
	}
	public Attributes getAttr() {
		return attr;
	}
	public void setAttr(Attributes attr) {
		this.attr = attr;
	}
}
