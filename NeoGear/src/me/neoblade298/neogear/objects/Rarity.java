package me.neoblade298.neogear.objects;

public class Rarity {
	public String colorCode;
	public String displayName;
	public double priceModifier;
	public boolean isEnchanted;
	
	public Rarity(String colorCode, String displayName, double priceModifier, boolean isEnchanted) {
		this.colorCode = colorCode.replaceAll("&", "§");
		this.displayName = displayName.replaceAll("&", "§");
		this.priceModifier = priceModifier;
		this.isEnchanted = isEnchanted;
	}
}
