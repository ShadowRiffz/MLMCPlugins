package me.neoblade298.neosessions.sessions;

import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.info.BossInfo;

public class BossSessionInfo implements SessionInfo {
	private String key;
	private BossInfo bi;
	private long cooldown;
	
	public BossSessionInfo(ConfigurationSection cfg) {
		this.key = cfg.getName();
	}

	@Override
	public Session createSession(SessionPlayer first) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cleanupSession() {
		// TODO Auto-generated method stub
		
	}

}
