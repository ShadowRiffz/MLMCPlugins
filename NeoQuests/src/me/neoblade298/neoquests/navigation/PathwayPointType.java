package me.neoblade298.neoquests.navigation;

import org.bukkit.Color;
import org.bukkit.Particle.DustOptions;

public enum PathwayPointType {
	
	POINT(new DustOptions(Color.ORANGE, 2.0F)),
	PORTAL(new DustOptions(Color.BLUE, 2.0F));

	private DustOptions options;
	private PathwayPointType(DustOptions options) {
		this.options = options;
	}
	
	public DustOptions getDustOptions() {
		return options;
	}
}
