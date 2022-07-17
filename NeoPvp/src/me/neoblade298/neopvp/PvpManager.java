package me.neoblade298.neopvp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.io.IOComponent;

public class PvpManager implements IOComponent {
	private static HashMap<UUID, PvpAccount> accounts = new HashMap<UUID, PvpAccount>();
	private static final double MAX_ELO_GAIN = 24;
	private static final double DEFAULT_KILL_GOLD = 500;
	private static final int MAX_UNIQUE_KILLS = 100;
	private static final double PCT_GOLD_KEPT = 0.9;
	
	
	// Account only exists when player is online, but ALWAYS exists if player is online
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
		
		// Wins and losses
		killerAcc.incrementWins();
		killedAcc.incrementLosses();
		
		// TODO
		handleWarKill();
	}
	
	private static void handleWarKill() {
		// Check for if they're involved in a war
	}

	@Override
	public void cleanup(Statement arg0, Statement arg1) {	}

	@Override
	public String getKey() {
		return "PvpManager";
	}

	@Override
	public void loadPlayer(Player p, Statement stmt) {
		accounts.get(p.getUniqueId()).loadPlayer();
	}

	@Override
	public void preloadPlayer(OfflinePlayer p, Statement stmt) {
		try {
			ResultSet rs = stmt.executeQuery("SELECT * FROM neopvp_accounts WHERE uuid = '" + p.getUniqueId() + "';");
			if (rs.next()) {
				accounts.put(p.getUniqueId(), new PvpAccount(p.getUniqueId(), rs));
			}
			else {
				accounts.put(p.getUniqueId(), new PvpAccount(p.getUniqueId()));
			}
		} catch (SQLException e) {
			Bukkit.getLogger().warning("[NeoPvp] Failed to load pvp account for player " + p.getName());
			e.printStackTrace();
		}
	}

	@Override
	public void savePlayer(Player p, Statement insert, Statement delete) {
		PvpAccount acct = getAccount(p);
		try {
			insert.addBatch("INSERT INTO neopvp_accounts VALUES ('" +
					p.getUniqueId() + "'," + acct.getKillstreak() + "," + acct.getWins() + "," + acct.getLosses() + "," +
					acct.getElo() + "," + acct.getBalance() + "," + acct.getProtectionExpiration() + ");");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
