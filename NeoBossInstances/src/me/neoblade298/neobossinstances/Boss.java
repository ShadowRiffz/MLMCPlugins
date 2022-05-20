package me.neoblade298.neobossinstances;

import java.util.ArrayList;
import java.util.HashSet;

import org.bukkit.Location;

import io.lumine.mythic.core.spawning.spawners.MythicSpawner;

public class Boss {
	private Location coords = null;
	private String name = null;
	private String cmd = null;
	private int cooldown = 0;
	private String displayName = null;
	private BossType type = BossType.BOSS;
	private int timeLimit = 0;
	private String permission = null;
	private ArrayList<RaidBoss> raidBosses = null;
	private String placeholder = null;
	private ArrayList<String> mythicmobs = null;
	private ArrayList<SpawnerSet> spawnersets = null;
	private HashSet<String> spawnersAlive = null;
	private int totalSpawners = 0;

	public Boss(String name, Location coords, String cmd, int cooldown, String displayName, String permission, String placeholder, ArrayList<String> mythicmobs) {
		this.name = name;
		this.coords = coords;
		this.cmd = cmd;
		this.cooldown = cooldown;
		this.displayName = displayName;
		this.permission = permission;
		this.placeholder = placeholder.replaceAll("&", "§").replaceAll("@", "&");
		this.mythicmobs = mythicmobs;
	}

	public Boss(String name, Location coords, String cmd, int cooldown, String displayName, BossType type, int timeLimit,
			String permission, String placeholder, ArrayList<String> mythicmobs) {
		this.name = name;
		this.coords = coords;
		this.cmd = cmd;
		this.cooldown = cooldown;
		this.displayName = displayName;
		this.type = type;
		this.timeLimit = timeLimit;
		this.permission = permission;
		this.raidBosses = new ArrayList<RaidBoss>();
		this.placeholder = placeholder.replaceAll("&", "§").replaceAll("@", "&");
		this.mythicmobs = mythicmobs;
	}

	public Boss(String name, Location coords, String cmd, int cooldown, String displayName, BossType type, int timeLimit,
			String permission, String placeholder, ArrayList<String> mythicmobs, ArrayList<SpawnerSet> spawners) {
		this.name = name;
		this.coords = coords;
		this.cmd = cmd;
		this.cooldown = cooldown;
		this.displayName = displayName;
		this.type = type;
		this.timeLimit = timeLimit;
		this.permission = permission;
		this.raidBosses = new ArrayList<RaidBoss>();
		this.placeholder = placeholder.replaceAll("&", "§").replaceAll("@", "&");
		this.spawnersets = spawners;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BossType getBossType() {
		return type;
	}

	public int getTimeLimit() {
		return timeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.timeLimit = timeLimit;
	}

	public Location getCoords() {
		return coords;
	}

	public void setCoords(Location coords) {
		this.coords = coords;
	}

	public String getCmd() {
		return cmd;
	}

	public void setCmd(String cmd) {
		this.cmd = cmd;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public ArrayList<RaidBoss> getRaidBosses() {
		return raidBosses;
	}

	public void setRaidBosses(ArrayList<RaidBoss> raidBosses) {
		this.raidBosses = raidBosses;
	}

	public String getPlaceholder() {
		return placeholder;
	}

	public void setPlaceholder(String placeholder) {
		this.placeholder = placeholder;
	}
	
	public ArrayList<String> getMythicMobs() {
		return mythicmobs;
	}
	
	public void setMythicMobs(ArrayList<String> mythicmobs) {
		this.mythicmobs = mythicmobs;
	}
	
	public void setSpawners(ArrayList<SpawnerSet> spawnersets) {
		this.spawnersets = spawnersets;
	}
	
	public ArrayList<SpawnerSet> getSpawners() {
		return spawnersets;
	}
	
	public HashSet<String> getSpawnersAlive() {
		return spawnersAlive;
	}
	
	public void setTotalSpawners(int total) {
		this.totalSpawners = total;
	}
	
	public int getTotalSpawners() {
		return totalSpawners;
	}
	
	public void checkSpawnerKill(MythicSpawner spawner) {
		spawnersAlive.remove(spawner.getInternalName());
	}
	
	public void setSpawnersAlive(HashSet<String> spawnersAlive) {
		this.spawnersAlive = spawnersAlive;
	}
	
	public int getSpawnersKilled() {
		return totalSpawners - spawnersAlive.size();
	}
}
