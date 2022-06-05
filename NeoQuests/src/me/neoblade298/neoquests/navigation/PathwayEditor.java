package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.util.LinkedList;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;

public class PathwayEditor {
	private Player p;
	private File file;
	private YamlConfiguration cfg;
	private String name;
	private LinkedList<PathwayPoint> points = new LinkedList<PathwayPoint>();
	
	public PathwayEditor(Player p, String name, File file) throws NeoIOException {
		this.name = name;
		this.file = file;
		try {
			this.file.createNewFile();
		}
		catch (Exception e) {
			throw new NeoIOException("Failed to start pathway editor " + name + ", " + e.getMessage());
		}
		this.cfg = YamlConfiguration.loadConfiguration(file);
	}
	
	public String getName() {
		return name;
	}
	
	public void show() {
		NavigationManager.showNearbyPoints(p);
		Pathway.showLines(p, points);
	}
	
	public void addPoint(PathwayPoint point) {
		points.add(point);
	}
	
	public PathwayPoint getOrCreatePoint(Location loc) {
		for (PathwayPoint point : points) {
			if (point.getLocation().equals(loc)) {
				return point;
			}
		}
		return null;
	}
}
