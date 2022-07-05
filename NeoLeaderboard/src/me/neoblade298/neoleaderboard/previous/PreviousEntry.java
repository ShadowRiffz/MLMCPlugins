package me.neoblade298.neoleaderboard.previous;

import java.util.UUID;

import org.bukkit.Bukkit;

public class PreviousEntry {
	private UUID uuid;
	private String name, category;
	private double points;
	
	public PreviousEntry(UUID uuid, String name, String category, double points) {
		this.uuid = uuid;
		this.name = name;
		this.category = category;
		this.points = points;
	}

	public PreviousEntry(UUID uuid, String category, double points) {
		this.uuid = uuid;
		this.name = Bukkit.getOfflinePlayer(uuid).getName();
		this.category = category;
		this.points = points;
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public String getCategory() {
		return category;
	}

	public double getPoints() {
		return points;
	}
}
