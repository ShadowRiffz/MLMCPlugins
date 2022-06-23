package me.neoblade298.neoleaderboard;

public enum PlayerPointType {
	KILL_PLAYER("Player kills"),
	PLAYTIME("Time online"),
	KILL_BOSS("Boss kills"),
	PROFESSION_CRAFT("Profession items crafted"),
	EDIT_BLOCK("Blocks placed/broken"),
	EVENT("Events");
	

	private final String display;
	private PlayerPointType(final String display) {
		this.display = display;
	}
	public String getDisplay() {
		return display;
	}
}
