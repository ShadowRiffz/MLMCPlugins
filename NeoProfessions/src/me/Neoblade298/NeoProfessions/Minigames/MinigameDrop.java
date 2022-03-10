package me.Neoblade298.NeoProfessions.Minigames;

import me.Neoblade298.NeoProfessions.Storage.StoredItem;

public class MinigameDrop {
	StoredItem item;
	int amt;
	
	public MinigameDrop(StoredItem item, int amt) {
		this.item = item;
		this.amt = amt;
	}

	public StoredItem getItem() {
		return item;
	}

	public int getAmt() {
		return amt;
	}
}
