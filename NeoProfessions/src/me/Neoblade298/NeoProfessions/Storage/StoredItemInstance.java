package me.Neoblade298.NeoProfessions.Storage;

public class StoredItemInstance {
	StoredItem item;
	int amt;
	
	public StoredItemInstance(StoredItem item, int amt) {
		this.item = item;
		this.amt = amt;
	}

	public StoredItem getItem() {
		return item;
	}

	public int getAmount() {
		return amt;
	}
	
	public String toString() {
		return item.getDisplay();
	}
}
