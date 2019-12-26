package me.neoblade298.neogear.objects;

public class RarityBonuses {
	public Attributes attributes;
	public int duraBonus;
	
	public RarityBonuses(Attributes attributes, int duraBonus) {
		this.attributes = attributes;
		this.duraBonus = duraBonus;
	}
	
	public RarityBonuses() {
		this.attributes = new Attributes();
		this.duraBonus = 0;
	}
}
