package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfig;
import me.neoblade298.neocore.io.LineConfigParser;

public class PathwayPoint implements LineConfigParser<PathwayPoint> {
	private static final int PARTICLES_PER_POINT = 20;
	private static final double PARTICLE_OFFSET = 0.1;
	private static final int PARTICLE_SPEED = 0;
	private static final int PARTICLES_PER_ENDPOINT = 20;
	private static final double ENDPOINT_SPEED = 0.05;
	private Chunk chunk;
	private Location loc, groundLoc, displayLoc;
	private PathwayPointType type;
	private LineConfig cfg;
	private HashSet<String> connections = new HashSet<String>();
	
	// Endpoint specific
	private String key, display;
	private File file;
	private boolean isEndpoint;
	private HashMap<PathwayPoint, Pathway> endpointsFrom = new HashMap<PathwayPoint, Pathway>(), endpointsTo = new HashMap<PathwayPoint, Pathway>();
	
	public PathwayPoint() {}
	
	public PathwayPoint(Location loc, PathwayPointType type, LineConfig cfg) {
		this(loc, type);
		this.cfg = cfg;
	}
	
	public PathwayPoint(Location loc, PathwayPointType type) {
		this.loc = loc;
		this.groundLoc = loc.clone().add(0, 1, 0);
		this.displayLoc = loc.clone().add(0, 2, 0);
		this.type = type;
	}

	public void spawnParticle(Player p, boolean useDisplayLocation) {
		if (useDisplayLocation) {
			if (isEndpoint) {
			    p.spawnParticle(Particle.END_ROD, displayLoc, PARTICLES_PER_ENDPOINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET, ENDPOINT_SPEED);
			}
		    p.spawnParticle(Particle.REDSTONE, displayLoc, PARTICLES_PER_POINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_SPEED, type.getDustOptions());
		}
		else {
			if (isEndpoint) {
			    p.spawnParticle(Particle.END_ROD, groundLoc, PARTICLES_PER_ENDPOINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET, ENDPOINT_SPEED);
			}
		    p.spawnParticle(Particle.REDSTONE, groundLoc, PARTICLES_PER_POINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_SPEED, type.getDustOptions());
		}
	}

	public Location getLocation() {
		return loc;
	}

	public Location getDisplayLocation() {
		return displayLoc;
	}

	public Location getGroundLocation() {
		return displayLoc;
	}

	public String serializeAsPoint() {
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
	
	public String serializeAsPath() {
		return loc.getX() + " " + loc.getY() + " " + loc.getZ();
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
	
	public void addConnection(String key) {
		connections.add(key);
	}
	
	public void removeConnection(String key) {
		connections.remove(key);
	}
	
	public boolean isConnected() {
		return connections.size() > 0;
	}

	public HashSet<String> getPathwaysUsing() {
		return connections;
	}
	
	public void setChunk(Chunk chunk) {
		this.chunk = chunk;
	}
	
	public Chunk getChunk() {
		return chunk;
	}
	
	public void setIsEndpoint(boolean isEndpoint) {
		this.isEndpoint = isEndpoint;
	}
	
	public boolean isEndpoint() {
		return this.isEndpoint;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getEndpointKey() {
		return this.key;
	}
	
	public String getDisplay() {
		return this.display;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return this.file;
	}
	
	public void addEndpointFrom(PathwayPoint point, Pathway pw) {
		if (!point.isEndpoint) {
			Bukkit.getLogger().warning("[NeoQuests] Failed to add from-endpoint to " + this.key + ", point " + point.getEndpointKey() + " is not an endpoint");
		}
		endpointsFrom.put(point, pw);
	}
	
	public void addEndpointTo(PathwayPoint point, Pathway pw) {
		if (!point.isEndpoint) {
			Bukkit.getLogger().warning("[NeoQuests] Failed to add to-endpoint to " + this.key + ", point " + point.getEndpointKey() + " is not an endpoint");
		}
		endpointsTo.put(point, pw);
	}
	
	public HashMap<PathwayPoint, Pathway> getFromEndpoints() {
		return endpointsFrom;
	}
	
	public HashMap<PathwayPoint, Pathway> getToEndpoints() {
		return endpointsTo;
	}
	
	public void setEndpointFields(String key, String display, File file) {
		isEndpoint = true;
		this.key = key;
		this.display = display;
		this.file = file;
	}
	
	public String serializeLocation() {
		return loc.getX() + " " + loc.getY() + " "+ loc.getZ();
	}
}
