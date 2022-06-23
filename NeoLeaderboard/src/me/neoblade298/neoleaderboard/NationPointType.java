package me.neoblade298.neoleaderboard;

public enum NationPointType {
	RAID("Town raids"),
	WAR("Wars"),
	EVENT("Events");
	

	private final String display;
	private NationPointType(final String display) {
		this.display = display;
	}
	public String getDisplay() {
		return display;
	}
}
