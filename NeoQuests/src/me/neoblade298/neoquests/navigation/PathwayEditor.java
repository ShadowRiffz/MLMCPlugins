package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.ParticleUtils;

public class PathwayEditor {
	private Player p;
	private LinkedList<Point> points = new LinkedList<Point>();
	private Point selected;
	private static final File endpointFile = new File(NavigationManager.getDataFolder(), "endpoints/New Endpoints.yml");

	private static final DustOptions PARTICLE_POINT_OPTIONS = new DustOptions(Color.YELLOW, 2.0F);
	private static final DustOptions PARTICLE_OPTIONS = new DustOptions(Color.YELLOW, 1.0F);
	private static final int PARTICLES_PER_POINT = 20;
	private static final double PARTICLE_OFFSET = 0.1;
	private static final int PARTICLE_SPEED = 0;
	
	private Point pointEditor;
	private EndPoint endpointEditor;
	
	public PathwayEditor(Player p) throws NeoIOException {
		this.p = p;
	}
	
	public void show() {
		NavigationManager.showNearbyPoints(p);
		PathwayInstance.showLines(p, points, false);
		showSelectedPoint();
	}
	
	public boolean isSelected(Point point) {
		return selected != null && selected.getLocation().equals(point.getLocation());
	}
	
	public void deselect() {
		Util.msg(p, "Successfully deselected point!");
		selected = null;
	}
	
	public void selectOrConnectPoints(Point point) {
		if (selected == null) {
			selected = point;
			Util.msg(p, "§7Successfully selected point!");
		}
		else {
			if (!selected.getLocation().getWorld().equals(point.getLocation().getWorld())) {
				Util.msg(p, "§cYou cannot connect points between worlds!");
			}
			
			if (points.size() == 0) {
				points.add(selected);
				selected.addConnection("editor");
			}
			point.addConnection("editor");
			points.add(point);
			selected = point;
			Util.msg(p, "§7Successfully connected points and selected point!");
		}
	}
	
	private void showSelectedPoint() {
		if (selected != null) {
		    p.spawnParticle(Particle.REDSTONE, selected.getGroundLocation(), PARTICLES_PER_POINT, PARTICLE_OFFSET * 2, PARTICLE_OFFSET * 2, PARTICLE_OFFSET * 2, PARTICLE_SPEED, PARTICLE_POINT_OPTIONS);
		    ParticleUtils.drawLine(p, selected.getGroundLocation(), p.getLocation(), PARTICLES_PER_POINT, PARTICLE_OFFSET, PARTICLE_SPEED, PARTICLE_OPTIONS);
		}
	}
	
	public void undoConnection() {
		if (points.size() > 2) {
			points.removeLast().removeConnection("editor");
			Util.msg(p, "Successfully undid last connection!");
		}
		else if (points.size() == 2) {
			points.remove().removeConnection("editor");
			points.remove().removeConnection("editor");
			Util.msg(p, "Successfully undid last connection!");
		}
		else {
			Util.msg(p, "Nothing to undo!");
		}
	}
	
	public boolean save(boolean bidirectional) throws NeoIOException {
		if (points.size() == 0) {
			Util.msg(p, "No points have been connected yet!");
			return false;
		}

		if (!points.getFirst().isEndpoint() || !points.getLast().isEndpoint()) {
			Util.msg(p, "&cEither the start or finish point is not an endpoint! Drop a stick while "
					+ "pointing at the point to make it an endpoint!");
			return false;
		}
		
		try {
			saveNewEndpoints();
			
			EndPoint start = points.getFirst().getEndpoint();
			EndPoint end = points.getLast().getEndpoint();
			File file = start.getFile() == null ? endpointFile : start.getFile();
			YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection sec = yml.getConfigurationSection(start.getKey());
			
			// Get or create "to" section
			if (sec.contains("to")) {
				sec = sec.getConfigurationSection("to");
			}
			else {
				sec = sec.createSection("to");
			}
			
			sec = sec.createSection(end.getKey());
			ArrayList<String> serialized = new ArrayList<String>(points.size());
			for (Point point : points) {
				serialized.add(point.serializeAsPath());
				point.removeConnection("editor");
				point.addConnection(start.getKey() + " -> " + end.getKey());
				if (bidirectional) {
					point.addConnection(end.getKey() + " -> " + start.getKey());
				}
			}
			sec.set("points", serialized);
			// Only add this if it's set to true
			if (bidirectional) sec.set("bidirectional", bidirectional);
			yml.save(file);
			NavigationManager.savePoints();
			
			end.addStartPoint(start, points);
			start.addDestination(end, points);
			if (bidirectional) {
				LinkedList<Point> rev = NavigationManager.createReversed(points);
				end.addDestination(start, rev);
				start.addStartPoint(end, rev);
			}
			
			Util.msg(p, "Successfully saved new pathway!");
			reset();
			return true;
		}
		catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private void reset() {
		// Can't use clear or it'll remove the points from the endpoint pathways!
		points = new LinkedList<Point>();
		endpointEditor = null;
		selected = null;
	}
	
	public void deletePoint(Location loc) {
		Point point = NavigationManager.getPoint(loc);
		if (point == null) {
			return;
		}
		if (point.isConnected()) {
			Util.msg(p, "§cCannot delete point! It is still connected to the following pathways:");
			for (String key : point.getPathwaysUsing()) {
				Util.msg(p, "&7- &6" + key, false);
			}
			return;
		}
		
		if (!NavigationManager.deletePoint(point)) {
			Util.msg(p, "§cFailed to delete point!");
		}
		else {
			if (isSelected(point)) {
				deselect();
			}
			Util.msg(p, "Successfully deleted point");
		}
	}
	
	public void editEndpoint(Point point, EndPoint ep) {
		this.pointEditor = point;
		this.endpointEditor = ep;
	}
	
	public void stopEditingEndpoint() {
		this.pointEditor = null;
		this.endpointEditor = null;
	}
	
	public boolean editingEndpoint() {
		return endpointEditor != null;
	}
	
	public Point getEditingPoint() {
		return pointEditor;
	}
	
	public EndPoint getEditingEndpoint() {
		return endpointEditor;
	}
	
	private void saveNewEndpoints() throws NeoIOException {
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(endpointFile);
		for (Point point : new Point[] { points.getFirst(), points.getLast() }) {
			EndPoint ep = point.getEndpoint();
			// Only save endpoints that haven't been saved yet
			if (ep.getFile() != null) {
				continue;
			}

			ep.setFile(endpointFile);
			ConfigurationSection sec = cfg.createSection(ep.getKey());
			sec.set("display", ep.getDisplay());
			sec.set("location", point.serializeLocation());
			sec.set("world", point.getLocation().getWorld().getName());
			try {
				cfg.save(endpointFile);
			} catch (IOException e) {
				e.printStackTrace();
				throw new NeoIOException("Failed to save endpoints for new pathway");
			}
		}
	}
}
