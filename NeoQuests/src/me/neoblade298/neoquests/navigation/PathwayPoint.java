package me.neoblade298.neoquests.navigation;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigParser;

public class PathwayPoint implements LineConfigParser<PathwayPoint> {
	private static final DustOptions PARTICLE_POINT_DATA = new DustOptions(Color.ORANGE, 2.0F);
	private static final int PARTICLES_PER_POINT = 20;
	private static final double PARTICLE_OFFSET = 0.2;
	private static final int PARTICLE_SPEED = 0;
	private Location loc;
	
	public PathwayPoint() {}
	
	public PathwayPoint(Location loc) {
		this.loc = loc;
	}

	public void spawnParticle(Player p) {
	    p.spawnParticle(Particle.REDSTONE, loc, PARTICLES_PER_POINT, PARTICLE_OFFSET * 2, PARTICLE_OFFSET * 2, PARTICLE_OFFSET * 2, PARTICLE_SPEED, PARTICLE_POINT_DATA);
	}

	public Location getLocation() {
		return loc;
	}

	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PathwayPoint create(LineConfig arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKey() {
		return "point";
	}

}
