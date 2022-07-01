package me.neoblade298.neopvp;

import java.util.HashSet;
import java.util.UUID;

public class PvpAccount {
	private UUID uuid;
	private HashSet<UUID> uniqueKills;
	private double pvpBalance;
	private int elo;
	
	public void addElo(int amount) {
		elo += amount;
	}
	
	public void takeElo(int amount) {
		addElo(-amount);
	}
	
	public int getElo() {
		return elo;
	}
}
