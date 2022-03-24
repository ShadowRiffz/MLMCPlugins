package me.Neoblade298.NeoProfessions.Gardens;

import me.Neoblade298.NeoProfessions.Minigames.MinigameParameters;
import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class Fertilizer {
	int id;
	MinigameParameters params;
	double timeMultiplier;
	public Fertilizer(int id, MinigameParameters params, double timeMultiplier) {
		this.id = id;
		this.params = params;
		this.timeMultiplier = timeMultiplier;
	}
	public StoredItem getSi() {
		return si;
	}
	public MinigameParameters getParams() {
		return params;
	}
	public double getTimeMultiplier() {
		return timeMultiplier;
	}
}
