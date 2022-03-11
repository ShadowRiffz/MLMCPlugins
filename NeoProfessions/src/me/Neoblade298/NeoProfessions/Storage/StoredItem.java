package me.Neoblade298.NeoProfessions.Storage;

import java.util.ArrayList;

import me.Neoblade298.NeoProfessions.Objects.Rarity;

public class StoredItem {
	private String display;
	private String name;
	private int id;
	private int level;
	private Rarity rarity;
	private ArrayList<String> lore;

	public StoredItem(int id, String name, int level, String rarity, ArrayList<String> lore) {
		this.id = id;
		switch (rarity) {
		case "uncommon":
			this.rarity = Rarity.UNCOMMON;
			break;
		case "rare":
			this.rarity = Rarity.RARE;
			break;
		case "epic":
			this.rarity = Rarity.EPIC;
			break;
		case "legendary":
			this.rarity = Rarity.LEGENDARY;
			break;
		default:
			this.rarity = Rarity.COMMON;
			break;
		}
		this.lore = new ArrayList<String>();
		this.name = name;
		this.display = "§6[Lv " + level + "] " + this.rarity.getCode() + name;
		this.lore.add("§7Rarity: " + this.rarity.getDisplay());
		if (lore != null) {
			for (String line : lore) {
				this.lore.add("§7§o" + line);
			}
		}
	}
	
	public int getID() {
		return this.id;
	}

	public String getDisplay() {
		return display;
	}
	
	public String getName() {
		return this.name;
	}

	public ArrayList<String> getLore() {
		return lore;
	}

	public Rarity getRarity() {
		return this.rarity;
	}

	public int getLevel() {
		return this.level;
	}
}
