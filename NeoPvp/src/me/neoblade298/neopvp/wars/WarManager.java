package me.neoblade298.neopvp.wars;

import java.util.HashMap;

public class WarManager {
	private static HashMap<String, War> wars = new HashMap<String, War>();
	
	public void createWar(War war) {
		wars.put(war.getName(), war);
	}
}
