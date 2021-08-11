package me.neoblade298.neobossinstances;

import java.util.ArrayList;

import org.bukkit.Location;

public class Boss {
	private Location coords = null;
	private String cmd = null;
	private int cooldown = 0;
	private String displayName = null;
	private boolean isRaid = false;
	private int timeLimit = 0;
	private String permission = null;
	private ArrayList<RaidBoss> raidBosses = null;
	private String placeholder = null;

	public Boss(Location coords, String cmd, int cooldown, String displayName, String permission, String placeholder) {
		this.coords = coords;
		this.cmd = cmd;
		this.cooldown = cooldown;
		this.displayName = displayName;
		this.setPermission(permission);
		this.setPlaceholder(placeholder);
	}

	public Boss(Location coords, String cmd, int cooldown, String displayName, boolean isRaid, int timeLimit,
			String permission, String placeholder) {
		this.coords = coords;
		this.cmd = cmd;
		this.cooldown = cooldown;
		this.displayName = displayName;
		this.isRaid = isRaid;
		this.timeLimit = timeLimit;
		this.setPermission(permission);
		this.raidBosses = new ArrayList<RaidBoss>();
		this.setPlaceholder(placeholder.replaceAll("&", "§").replaceAll("@", "&"));
	}

	public boolean isRaid() {
		return isRaid;
	}

	public void setRaid(boolean isRaid) {
		this.isRaid = isRaid;
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
}
