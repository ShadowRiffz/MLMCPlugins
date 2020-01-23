package me.Neoblade298.NeoProfessions;

import java.util.HashMap;
import java.util.UUID;

public class CurrencyManager {
	// UUID, essence/oretype, amount
	public HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>> currencies;
	
	public CurrencyManager(Main main) {
		currencies = new HashMap<UUID, HashMap<String, HashMap<Integer, Integer>>>();
		
		// Put in every type of currency
		for (int i = 0; i <= 60; i += 5) {
		}
	}
}
