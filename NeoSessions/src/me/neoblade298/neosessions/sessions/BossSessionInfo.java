package me.neoblade298.neosessions.sessions;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.info.BossInfo;

public class BossSessionInfo extends SessionInfo {
	private BossInfo bi;
	private long cooldown;
	
	public BossSessionInfo(ConfigurationSection cfg) {
		super(cfg.getName(), new Location(null, 1, 1, 1)); // TODO: Location
	}

	@Override
	public Session createSession(String from, int numPlayers, int multiplier) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cleanupSession() {
		// TODO Auto-generated method stub
		
	}
}
