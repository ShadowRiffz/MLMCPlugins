package me.neoblade298.neoleaderboard.points;

public enum PlayerPointType implements PointType {
	KILL_PLAYER("Player kills (5 / kill)"),
	PLAYTIME("Time online (0.1 / minute)"),
	KILL_BOSS("Boss kills (0.05 * boss level / kill)"),
	NORMAL_CRAFT("8 Diamonds + 1 Netherite Ingot crafted together (10 / craft)"),
	EDIT_BLOCK("Blocks placed/broken (0.002 / block)"),
	PLAYER_EVENT("Player event points");
	

	private final String display;
	private PlayerPointType(final String display) {
		this.display = display;
	}
	public String getDisplay() {
		return display;
	}
}
