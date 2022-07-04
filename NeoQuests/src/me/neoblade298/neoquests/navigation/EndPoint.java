package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import me.neoblade298.neocore.exceptions.NeoIOException;

public class EndPoint {
	private Point point;
	private String key, display;
	private ConfigurationSection cfg;
	private File file;
	private HashMap<EndPoint, LinkedList<Point>> startPoints = new HashMap<EndPoint, LinkedList<Point>>();
	private HashMap<EndPoint, LinkedList<Point>> destinations = new HashMap<EndPoint, LinkedList<Point>>();
	
	public EndPoint() {}
	
	public EndPoint(File file, ConfigurationSection cfg) throws NeoIOException {
		this.file = file;
		this.key = cfg.getName();
		this.display = cfg.getString("display");
		this.cfg = cfg;
	}
	
	public void loadPathways() throws NeoIOException {
		ConfigurationSection sec = cfg.getConfigurationSection("to");
		if (sec != null) {
			for (String key : sec.getKeys(false)) {
				ConfigurationSection path = sec.getConfigurationSection(key);
				EndPoint end = NavigationManager.getEndpoint(key);
				if (end == null) {
					throw new NeoIOException("Failed to load path from " + this.key + " to " + key + ", " + key + " doesn't exist or isn't an endpoint.");
				}
				LinkedList<Point> points = parsePoints(path.getStringList("points"));
				destinations.put(end, points);
				end.addStartPoint(this, points);
				
				if (path.getBoolean("bidirectional", false)) {
					LinkedList<Point> reversed = NavigationManager.createReversed(points);
					startPoints.put(end, reversed);
					end.addDestination(this, reversed);
				}
			}
		}
		cfg = null; // Remove cfg from memory after it's used
	}
	
	// Notably, when an endpoint is first made it has no file variable (must be set and/or loaded via cfg)
	public EndPoint(String key, String display) {
		this.key = key;
		this.display = display;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public String getKey() {
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
	
	public void addStartPoint(EndPoint point, LinkedList<Point> pw) {
		startPoints.put(point, pw);
		getStartPoints();
	}
	
	public void addDestination(EndPoint point, LinkedList<Point> pw) {
		destinations.put(point, pw);
		getDestinations();
	}
	
	public HashMap<EndPoint, LinkedList<Point>> getStartPoints() {
		return startPoints;
	}
	
	public HashMap<EndPoint, LinkedList<Point>> getDestinations() {
		return destinations;
	}
	
	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof EndPoint;
	}
	
	private LinkedList<Point> parsePoints(List<String> list) throws NeoIOException {
		if (list.size() <= 1) {
			throw new NeoIOException("Pathway " + this.key + " has <= 1 points, invalid!");
		}
		
		LinkedList<Point> points = new LinkedList<Point>();
		for (String line : list) {
			String args[] = line.split(" ");
			double x = Double.parseDouble(args[0]);
			double y = Double.parseDouble(args[1]);
			double z = Double.parseDouble(args[2]);
			Point point = NavigationManager.getPoint(new Location(this.point.getWorld(), x, y, z));
			if (point == null) {
				throw new NeoIOException("Pathway " + this.key + " failed to load point " + line);
			}
			points.add(point);
		}
		
		// Passed validation
		setupPoints(points);
		return points;
	}
	
	private void setupPoints(LinkedList<Point> points) {
		ListIterator<Point> iter = points.listIterator();
		Point l1 = null;
		Point l2 = iter.next();
		while (iter.hasNext()) {
			l1 = l2;
			l2 = iter.next();
			l1.addConnection(this.key);
			l2.addConnection(this.key);
		}
	}
	
	public World getWorld() {
		return point.getWorld();
	}
	
	public void setPoint(Point point) {
		this.point = point;
	}
	
	@Override
	public String toString() {
		return "EP: " + this.point.toString();
	}
}
