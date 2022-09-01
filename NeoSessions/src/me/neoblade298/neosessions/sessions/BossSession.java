package me.neoblade298.neosessions.sessions;

public class BossSession extends Session {
	private BossSessionInfo info;
	public BossSession(BossSessionInfo info, String from, int numPlayers, int multiplier) {
		super(info, from, numPlayers, multiplier);
		this.info = info;
	}
	

	@Override
	public void start() {
		super.start();
	}

	@Override
	public void end() {
		super.end();
		
	}

	@Override
	public BossSessionInfo getSessionInfo() {
		return info;
	}
}
