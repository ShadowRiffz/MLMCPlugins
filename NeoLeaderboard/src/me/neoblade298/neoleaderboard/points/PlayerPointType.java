package me.neoblade298.neoleaderboard.points;

public enum PlayerPointType implements PointType {
	KILL_PLAYER("Player kills"),
	PLAYTIME("Time online"),
	KILL_BOSS("Boss kills"),
	PROFESSION_CRAFT("Profession items crafted"),
	NORMAL_CRAFT("Point items crafted"),
	EDIT_BLOCK("Blocks placed/broken"),
	PLAYER_EVENT("Player event points");
	

	private final String display;
	private PlayerPointType(final String display) {
		this.display = display;
	}
	public String getDisplay() {
		return display;
	}
}
