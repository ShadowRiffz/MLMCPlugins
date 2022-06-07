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
	private String name;
	private LinkedList<PathwayPoint> points = new LinkedList<PathwayPoint>();
	private PathwayPoint selected;
	private File pathwayFile, endpointFile;

	private static final DustOptions PARTICLE_POINT_OPTIONS = new DustOptions(Color.YELLOW, 2.0F);
	private static final DustOptions PARTICLE_OPTIONS = new DustOptions(Color.YELLOW, 1.0F);
	private static final int PARTICLES_PER_POINT = 20;
	private static final double PARTICLE_OFFSET = 0.1;
	private static final int PARTICLE_SPEED = 0;
	
	private PathwayPoint endpointEditor;
	
	public PathwayEditor(Player p, String name) throws NeoIOException {
		this.p = p;
		this.name = name;
		
		pathwayFile = new File(NavigationManager.getDataFolder(), "pathways/New Pathways.yml");
		endpointFile = new File(NavigationManager.getDataFolder(), "endpoints/New Endpoints.yml");
	}
	
	public String getName() {
		return name;
	}
	
	public void show() {
		NavigationManager.showNearbyPoints(p);
		Pathway.showLines(p, points, false);
		showSelectedPoint();
	}
	
	public boolean isSelected(PathwayPoint point) {
		return selected != null && selected.getLocation().equals(point.getLocation());
	}
	
	public void deselect() {
		Util.msg(p, "Successfully deselected point!");
		selected = null;
	}
	
	public void selectOrConnectPoints(PathwayPoint point) {
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
				selected.addConnection(this.name);
			}
			points.add(point);
			point.addConnection(this.name);
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
			PathwayPoint point = points.removeLast();
			point.removeConnection(this.name);
			points.getLast().removeConnection(this.name);
			Util.msg(p, "Successfully undid last connection!");
		}
		else if (points.size() == 2) {
			for (PathwayPoint point : points) {
				point.removeConnection(this.name);
			}
			points.clear();
			Util.msg(p, "Successfully undid last connection!");
		}
		else {
			Util.msg(p, "Nothing to undo!");
		}
	}
	
	public boolean save() throws NeoIOException {
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
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(pathwayFile);
			ConfigurationSection sec = cfg.createSection(name);
			sec.set("world", points.getFirst().getLocation().getWorld().getName());
			ArrayList<String> serialized = new ArrayList<String>(points.size());
			for (PathwayPoint point : points) {
				serialized.add(point.serializeAsPath());
			}
			sec.set("points", serialized);
			sec.set("bidirectional", true);
			cfg.save(pathwayFile);
			NavigationManager.addPathway(new Pathway(sec, pathwayFile));
			NavigationManager.savePoints();
			Util.msg(p, "Successfully saved new pathway §6" + name + "§7!");
			NavigationManager.exitPathwayEditor(p);
			saveEndpoints();
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new NeoIOException("Failed to save pathway editor " + name);
		}
	}
	
	public void deletePoint(Location loc) {
		PathwayPoint point = NavigationManager.getPoint(loc);
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
	
	public void editEndpoint(PathwayPoint point) {
		this.endpointEditor = point;
	}
	
	public void stopEditingEndpoint() {
		this.endpointEditor = null;
	}
	
	public boolean editingEndpoint() {
		return endpointEditor != null;
	}
	
	public PathwayPoint getEditingEndpoint() {
		return endpointEditor;
	}
	
	private void saveEndpoints() throws NeoIOException {
		YamlConfiguration cfg = YamlConfiguration.loadConfiguration(endpointFile);
		for (PathwayPoint point : new PathwayPoint[] { points.getFirst(), points.getLast() }) {
			ConfigurationSection sec = cfg.createSection(point.getEndpointKey());
			sec.set("display", point.getDisplay());
			sec.set("location", point.serializeLocation());
			sec.set("world", point.getLocation().getWorld().getName());
		}
		try {
			cfg.save(endpointFile);
		} catch (IOException e) {
			e.printStackTrace();
			throw new NeoIOException("Failed to save endpoints for new pathway");
		}
	}
	
	public File getEndpointFile() {
		return endpointFile;
	}
	
	public File getPathwayFile() {
		return pathwayFile;
	}
}
