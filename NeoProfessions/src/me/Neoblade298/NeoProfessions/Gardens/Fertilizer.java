package me.Neoblade298.NeoProfessions.Gardens;

import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;

public class Fertilizer {
	int id;
	MinigameParameters params;
	double timeMultiplier;
	public Fertilizer(int id, MinigameParameters params, double timeMultiplier) {
		this.id = id;
		this.params = params;
		this.timeMultiplier = timeMultiplier;
	}
	public int getId() {
		return id;
	}
	public MinigameParameters getParams() {
		return params;
	}
	public double getTimeMultiplier() {
		return timeMultiplier;
	}
}
