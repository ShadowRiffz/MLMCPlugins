package me.neoblade298.neosessions.sessions;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.info.BossInfo;
import me.neoblade298.neocore.util.Util;

public class BossSessionInfo extends SessionInfo {
	private BossInfo bi;
	private long cooldown;
	private Location bossSpawn;
	
	public BossSessionInfo(ConfigurationSection cfg) {
		super(cfg.getName(), Util.stringToLoc(cfg.getString("player-spawn"))); // TODO: Location
		this.bossSpawn = Util.stringToLoc(cfg.getString("boss-spawn"));
	}

	@Override
	public Session createSession(String from, int numPlayers, int multiplier) {
		return new BossSession(this, from, numPlayers, multiplier);
	}

	@Override
	public void cleanupSession() {
		
	}
}
