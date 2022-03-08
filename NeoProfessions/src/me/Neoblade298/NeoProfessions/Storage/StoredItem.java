package me.Neoblade298.NeoProfessions.Storage;

import java.util.ArrayList;

public class StoredItem {
	private String name;
	private ArrayList<String> lore;
	
	public StoredItem(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getLore() {
		return lore;
	}

	public void setLore(ArrayList<String> lore) {
		this.lore = lore;
	}
	
}
