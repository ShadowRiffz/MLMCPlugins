package me.neoblade298.neopvp.wars;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;

import org.bukkit.Location;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;

import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.util.Util;

public class WarTeam {
	private String key, display;
	private int points;
	private ActiveMob mascot;
	private Location spawn, mascotSpawn;
	private HashSet<Nation> nations = new HashSet<Nation>();
	private HashSet<Town> whitelistedTowns = new HashSet<Town>();
	private HashSet<String> whitelistedPlayers = new HashSet<String>();
	
	private static final String MASCOT_NAME = "WarMascot";
	
	public WarTeam(String war, ResultSet team) throws SQLException {
		this.display = team.getString(2);
		for (String nation : team.getString(3).split(",")) {
			nations.add(TownyAPI.getInstance().getNation(nation));
		}
		this.spawn = Util.stringToLoc(team.getString(4));
		this.mascotSpawn = Util.stringToLoc(team.getString(5));

		Statement stmt = NeoCore.getStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM neopvp_warwhitelists WHERE war = '" + war + "' AND team = '" + this.key + "';");
		while (rs.next()) {
			if (rs.getString(3).equals("PLAYER")) {
				whitelistedPlayers.add(rs.getString(4));
			}
			else {
				whitelistedTowns.add(TownyAPI.getInstance().getTown(rs.getString(4)));
			}
		}
	}
	
	public WarTeam(String display) {
		this.display = display;
	}
	
	public void addNation(Nation n) {
		nations.add(n);
	}
	
	public void removeNation(Nation n) {
		nations.remove(n);
	}
	
	public void setSpawn(Location spawn) {
		this.spawn = spawn;
	}
	
	public void setMascotSpawn(Location mascotSpawn) {
		this.mascotSpawn = mascotSpawn;
	}

	public String getDisplay() {
		return display;
	}

	public int getPoints() {
		return points;
	}

	public Location getSpawn() {
		return spawn;
	}

	public Location getMascotSpawn() {
		return mascotSpawn;
	}

	public HashSet<Nation> getNations() {
		return nations;
	}
	
	public void startWar() {
		mascot = MythicBukkit.inst().getMobManager().spawnMob(MASCOT_NAME, mascotSpawn);
	}
	
	public ActiveMob getMascot() {
		return mascot;
	}
}
