package me.Neoblade298.NeoConsumables;

public enum ConsumableType {
	FOOD, CHEST, TOKEN;
	
	public static ConsumableType fromString(String type) {
		if (type == null) {
			return FOOD;
		}
		
		switch (type) {
		case "FOOD":	return FOOD;
		case "CHEST":	return CHEST;
		case "TOKEN":	return TOKEN;
		default:		return FOOD;
		}
	}
}
