package me.neoblade298.neosessions.sessions;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import io.lumine.mythic.api.mobs.MythicMob;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.neoblade298.neocore.info.BossInfo;
import me.neoblade298.neocore.util.Util;

public class BossSessionInfo extends SessionInfo {
	private BossInfo bi;
	private long cooldown;
	private Location bossSpawn;
	private MythicMob mob;
	
	public BossSessionInfo(ConfigurationSection cfg) {
		super(cfg.getName(), Util.stringToLoc(cfg.getString("player-spawn"))); // TODO: Location
		this.bossSpawn = Util.stringToLoc(cfg.getString("boss-spawn"));
		this.mob = MythicBukkit.inst().getMobManager().getMythicMob(cfg.getString("boss-mob")).get();
	}

	@Override
	public Session createSession(String from, int numPlayers, int multiplier) {
		return new BossSession(this, from, numPlayers, multiplier);
	}
	
	public BossInfo getBossInfo() {
		return bi;
	}
	
	public MythicMob getBossMob() {
		return mob;
	}
	
	public Location getBossSpawn() {
		return bossSpawn;
	}
}
