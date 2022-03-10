package me.Neoblade298.NeoProfessions.Minigames;

import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class MinigameDrops {
	StoredItem item;
	int minAmt, maxAmt;
	int weight;

	public MinigameDrops(StoredItem item, int minAmt, int maxAmt, int weight) {
		this.item = item;
		this.minAmt = minAmt;
		this.maxAmt = maxAmt;
		this.weight = weight;
	}

	public StoredItem getItem() {
		return item;
	}

	public int getMinAmt() {
		return minAmt;
	}

	public int getMaxAmt() {
		return maxAmt;
	}

	public int getWeight() {
		return weight;
	}
}
