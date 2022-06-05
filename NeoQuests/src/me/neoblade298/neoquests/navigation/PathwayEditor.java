package me.neoblade298.neoquests.navigation;

import java.io.File;
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
	private File file;
	private String name;
	private LinkedList<PathwayPoint> points = new LinkedList<PathwayPoint>();
	private PathwayPoint selected;

	private static final DustOptions PARTICLE_POINT_OPTIONS = new DustOptions(Color.YELLOW, 2.0F);
	private static final DustOptions PARTICLE_OPTIONS = new DustOptions(Color.YELLOW, 1.0F);
	private static final int PARTICLES_PER_POINT = 20;
	private static final double PARTICLE_OFFSET = 0.1;
	private static final int PARTICLE_SPEED = 0;
	
	public PathwayEditor(Player p, String name, File file) throws NeoIOException {
		this.p = p;
		this.name = name;
		this.file = file;
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
		
		try {
			this.file.createNewFile();
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			ConfigurationSection sec = cfg.createSection(name);
			sec.set("start", "Start Placeholder");
			sec.set("end", "End Placeholder");
			sec.set("world", points.getFirst().getLocation().getWorld().getName());
			ArrayList<String> serialized = new ArrayList<String>(points.size());
			for (PathwayPoint point : points) {
				serialized.add(point.serializeAsPath());
			}
			sec.set("points", serialized);
			cfg.save(file);
			NavigationManager.addPathway(new Pathway(cfg, file));
			Util.msg(p, "Successfully saved new pathway §6" + name + "§7!");
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new NeoIOException("Failed to save pathway editor " + name + ", " + e.getMessage());
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
}
