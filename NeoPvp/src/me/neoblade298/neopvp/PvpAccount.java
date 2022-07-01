package me.neoblade298.neopvp;

import java.util.HashSet;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PvpAccount {
	private UUID uuid;
	private HashSet<UUID> uniqueKills;
	private double pvpBalance;
	private int elo, killstreak;
	
	public void addElo(int amount) {
		elo += amount;
	}
	
	public void takeElo(int amount) {
		addElo(-amount);
	}
	
	public int getElo() {
		return elo;
	}
	
	public double getBalance() {
		return pvpBalance;
	}
	
	public void setBalance(double balance) {
		pvpBalance = balance;
	}
	
	public void addBalance(double balance) {
		pvpBalance += balance;
	}
	
	public int getNumUniqueKills() {
		return uniqueKills.size();
	}
	
	public void addUniqueKill(Player p) {
		uniqueKills.add(p.getUniqueId());
	}
	
	public void incrementKillstreak() {
		killstreak++;
	}
	
	public int getKillstreak() {
		return killstreak;
	}
	
	public void clearKillstreak() {
		killstreak = 0;
	}
}
