package me.neoblade298.neogear;

public class Rarity {
	public String colorCode;
	public String displayName;
	
	public Rarity(String colorCode, String displayName) {
		this.colorCode = colorCode.replaceAll("&", "§");
		this.displayName = displayName.replaceAll("&", "§");
	}
}
