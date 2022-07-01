package me.neoblade298.neopvp;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.entity.Player;

public class PvpManager {
	private static HashMap<UUID, PvpAccount> accounts;
	private static final double MAX_ELO_GAIN = 24;
	
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
	}
}
