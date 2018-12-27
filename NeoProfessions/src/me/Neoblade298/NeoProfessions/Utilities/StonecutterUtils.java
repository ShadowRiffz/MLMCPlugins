package me.Neoblade298.NeoProfessions.Utilities;

public class StonecutterUtils {

	public static boolean isAttribute(String attr) {
		return (attr.equalsIgnoreCase("Strength") ||
				attr.equalsIgnoreCase("Dexterity") ||
				attr.equalsIgnoreCase("Intelligence") ||
				attr.equalsIgnoreCase("Spirit") ||
				attr.equalsIgnoreCase("Perception") ||
				attr.equalsIgnoreCase("Endurance") ||
				attr.equalsIgnoreCase("Vitality"));
	}
}
