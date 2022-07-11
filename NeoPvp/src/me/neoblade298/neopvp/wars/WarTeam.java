package me.neoblade298.neopvp.wars;

import java.util.HashSet;

import org.bukkit.Location;

import com.palmergames.bukkit.towny.object.Nation;

import io.lumine.mythic.core.mobs.ActiveMob;

public class WarTeam {
	private String name;
	private int points;
	private ActiveMob baseMob;
	private Location spawn, mascotSpawn;
	private HashSet<Nation> nations = new HashSet<Nation>();
	
	public WarTeam(String name) {
		this.name = name;
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

	public String getName() {
		return name;
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
