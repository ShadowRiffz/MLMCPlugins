package me.neoblade298.neoquests.navigation;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigParser;

public class PathwayPoint implements LineConfigParser<PathwayPoint> {
	private static final int PARTICLES_PER_POINT = 20;
	private static final double PARTICLE_OFFSET = 0.1;
	private static final int PARTICLE_SPEED = 0;
	private Location loc;
	private Location displayLoc;
	private PathwayPointType type;
	private LineConfig cfg;
	private int connections = 0;
	
	public PathwayPoint() {}
	
	public PathwayPoint(Location loc, PathwayPointType type, LineConfig cfg) {
		this(loc, type);
		this.cfg = cfg;
	}
	
	public PathwayPoint(Location loc, PathwayPointType type) {
		this.loc = loc;
		this.displayLoc = loc.clone();
		displayLoc.add(0, 1, 0);
		this.type = type;
	}

	public void spawnParticle(Player p) {
	    p.spawnParticle(Particle.REDSTONE, displayLoc, PARTICLES_PER_POINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_SPEED, type.getDustOptions());
	}

	public Location getLocation() {
		return loc;
	}

	public Location getDisplayLocation() {
		return displayLoc;
	}

	public String serialize() {
		if (cfg != null) {
			return cfg.getFullLine();
		}
		else {
			String serialized = "type:" + type;
			serialized += " x:" + loc.getX();
			serialized += " y:" + loc.getY();
			serialized += " z:" + loc.getZ();
			serialized += " world:" + loc.getWorld().getName();
			return serialized;
		}
	}
	
	public PathwayPointType toggleType() {
		if (type == PathwayPointType.POINT) {
			type = PathwayPointType.PORTAL;
		}
		else {
			type = PathwayPointType.POINT;
		}
		return type;
	}
	
	public PathwayPointType getType() {
		return type;
	}

	@Override
	public PathwayPoint create(LineConfig cfg) {
		this.cfg = cfg;
		double x = cfg.getDouble("x", 0.0);
		double y = cfg.getDouble("y", 0.0);
		double z = cfg.getDouble("z", 0.0);
		World w = Bukkit.getWorld(cfg.getString("world", "Argyll"));
		PathwayPointType type = PathwayPointType.valueOf(cfg.getString("type", "point").toUpperCase());
		return new PathwayPoint(new Location(w, x, y, z), type, cfg);
	}

	@Override
	public String getKey() {
		return ""; // Keyless system
	}
	
	public void addConnection() {
		connections += 1;
	}
	
	public void removeConnection() {
		connections -= 1;
	}
	
	public boolean isConnected() {
		return connections > 0;
	}

}
