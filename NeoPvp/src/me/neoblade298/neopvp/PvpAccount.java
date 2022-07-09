package me.neoblade298.neopvp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.util.Util;

public class PvpAccount {
	private UUID uuid;
	private Player p;
	private HashSet<UUID> uniqueKills = new HashSet<UUID>();
	private double pvpBalance;
	private int elo, killstreak, wins, losses;
	private long protectionExpires = -1;
	
	public PvpAccount(UUID uuid) {
		this.uuid = uuid;
		this.p = Bukkit.getPlayer(uuid);
	}
	
	public PvpAccount(UUID uuid, ResultSet rs) throws SQLException {
		this.uuid = uuid;
		killstreak = rs.getInt(2);
		wins = rs.getInt(3);
		losses = rs.getInt(4);
		elo = rs.getInt(5);
		pvpBalance = rs.getDouble(6);
		protectionExpires = rs.getLong(7);
		this.p = Bukkit.getPlayer(uuid);
	}
	
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
	
	public void incrementWins() {
		wins++;
	}
	
	public void incrementLosses() {
		losses++;
	}
	
	public void setUniqueKills(HashSet<UUID> uniqueKills) {
		this.uniqueKills = uniqueKills;
	}
	
	public int getWins() {
		return wins;
	}
	
	public int getLosses() {
		return losses;
	}
	
	public boolean isProtected() {
		return protectionExpires > System.currentTimeMillis();
	}
	
	public void displayAccount(CommandSender s) {
		Util.msg(s, "&c===[&6" + p.getName() + "&6]===");
		Util.msg(s, " &6Rating: &e" + elo);
		Util.msg(s, " &6Pvp Balance: &e" + pvpBalance);
		Util.msg(s, " &6Killstreak: &e" + killstreak);
		Util.msg(s, " &6Wins: &e" + wins);
		Util.msg(s, " &6Losses: &e" + losses);
	}
	
	public UUID getUuid() {
		return uuid;
	}
	
	public Player getPlayer() {
		return this.p;
	}
	
	public long getProtectionExpiration() {
		return protectionExpires;
	}
}
