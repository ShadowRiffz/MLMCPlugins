package me.neoblade298.neopvp.wars;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import org.bukkit.Location;

import com.palmergames.bukkit.towny.object.Nation;

import io.lumine.mythic.core.mobs.ActiveMob;
import me.neoblade298.neocore.util.Util;

public class WarTeam {
	private String display;
	private int points;
	private ActiveMob baseMob;
	private Location spawn, mascotSpawn;
	private HashSet<Nation> nations = new HashSet<Nation>();
	
	public WarTeam(ResultSet team) throws SQLException {
		this.display = team.getString(2);
		this.spawn = Util.stringToLoc(team.getString(3));
		this.mascotSpawn = Util.stringToLoc(team.getString(4));
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

	public ActiveMob getBaseMob() {
		return baseMob;
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
}
