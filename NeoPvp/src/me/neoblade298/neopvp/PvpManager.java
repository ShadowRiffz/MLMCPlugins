package me.neoblade298.neopvp;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.NeoCore;

public class PvpManager {
	private static HashMap<UUID, PvpAccount> accounts;
	private static final double MAX_ELO_GAIN = 24;
	private static final double DEFAULT_KILL_GOLD = 500;
	private static final int MAX_UNIQUE_KILLS = 100;
	private static final double PCT_GOLD_KEPT = 0.9;
	
	public static PvpAccount getAccount(Player p) {
		return accounts.get(p.getUniqueId());
	}
	
	public static PvpAccount getAccount(UUID uuid) {
		return accounts.get(uuid);
	}
	
	public static void handleKill(Player killer, Player killed) {
		PvpAccount killerAcc = getAccount(killer);
		PvpAccount killedAcc = getAccount(killed);

		// Calculate elo
		double killerElo = killerAcc.getElo();
		double killedElo = killedAcc.getElo();
		double killerExpected = 1/(1+Math.pow(10, (killedElo - killerElo) / 400));
		int change = (int) (MAX_ELO_GAIN * (1 - killerExpected));
		killerAcc.addElo(change);
		killedAcc.takeElo(change);
		
		// Take money
		double toTake = DEFAULT_KILL_GOLD * (1 + (Math.max(killerAcc.getNumUniqueKills(), MAX_UNIQUE_KILLS) * 0.05));
		if (NeoCore.getEconomy().has(killed, toTake)) {
			killerAcc.addUniqueKill(killed);
		}
		else {
			toTake = NeoCore.getEconomy().getBalance(killed);
		}
		NeoCore.getEconomy().withdrawPlayer(killed, toTake);
		killerAcc.addBalance(toTake * PCT_GOLD_KEPT);
		
		// Transfer pvp balance
		killedAcc.setBalance(0);
		killerAcc.addBalance(killedAcc.getBalance());
		
		// Killstreak
		killerAcc.incrementKillstreak();
		killedAcc.clearKillstreak();
		
		// TODO
		handleWarKill();
	}
	
	private static void handleWarKill() {
		
	}
}
