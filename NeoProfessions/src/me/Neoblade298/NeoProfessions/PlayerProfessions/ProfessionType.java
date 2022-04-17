package me.Neoblade298.NeoProfessions.PlayerProfessions;

public enum ProfessionType {
	STONECUTTER("Stonecutter"),
	LOGGER("Logger"),
	HARVESTER("Harvester"),
	CRAFTER("Crafter");
	
	private final String display;
	private ProfessionType(final String display) {
		this.display = display;
	}
	
	public String getDisplay() {
		return display;
	}
}
