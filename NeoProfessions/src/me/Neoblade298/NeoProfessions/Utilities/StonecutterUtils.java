package me.Neoblade298.NeoProfessions.Utilities;

public class StonecutterUtils {
	
	public static boolean isWeaponAttribute(String attr) {
		return (attr.equalsIgnoreCase("Strength") ||
				attr.equalsIgnoreCase("Dexterity") ||
				attr.equalsIgnoreCase("Intelligence") ||
				attr.equalsIgnoreCase("Spirit") ||
				attr.equalsIgnoreCase("Perception"));
	}

	public static boolean isArmorAttribute(String attr) {
		return (attr.equalsIgnoreCase("Strength") ||
				attr.equalsIgnoreCase("Dexterity") ||
				attr.equalsIgnoreCase("Intelligence") ||
				attr.equalsIgnoreCase("Spirit") ||
				attr.equalsIgnoreCase("Perception") ||
				attr.equalsIgnoreCase("Endurance") ||
				attr.equalsIgnoreCase("Vitality"));
	}
}
