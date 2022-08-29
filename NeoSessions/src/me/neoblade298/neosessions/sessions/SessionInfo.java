package me.neoblade298.neosessions.sessions;

public interface SessionInfo {
	public Session createSession(SessionPlayer first);
	public void cleanupSession();
}
