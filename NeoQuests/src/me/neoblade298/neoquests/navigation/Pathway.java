package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neocore.util.Util;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.ParticleUtils;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;

public class Pathway {
	private String key, fileLocation;
	private World w;
	private LinkedList<PathwayPoint> points = new LinkedList<PathwayPoint>();
	private ArrayList<Condition> conditions;
	private boolean bidirectional;
	
	private static final int PARTICLES_PER_POINT = 20;
	private static final double PARTICLE_OFFSET = 0.1;
	private static final int PARTICLE_SPEED = 0;
	private static final int END_RADIUS_SQ = 100;
	
	private static final double DISTANCE_SHOWABLE = 5000;
	private static final DustOptions PARTICLE_DATA = new DustOptions(Color.RED, 1.0F);
	
	public Pathway() {}
	
	public Pathway(ConfigurationSection cfg, File file) throws NeoIOException {
		key = cfg.getName().toUpperCase();
		fileLocation = file.getPath();
		this.w = Bukkit.getWorld(cfg.getString("world", "Argyll"));
		this.conditions = ConditionManager.parseConditions(cfg.getStringList("conditions"));
		this.bidirectional = cfg.getBoolean("bidirectional", false);
		parsePoints(cfg.getStringList("points"));
	}
	
	public Pathway(Pathway pw) {
		this.key = "REVERSE_" + pw.getKey();
		this.fileLocation = pw.getFileLocation();
		this.w = pw.getWorld();
		this.conditions = pw.getConditions();
		this.bidirectional = true;
		this.points = reversePoints(pw.getPoints());
		setupPoints();
	}
	
	private void parsePoints(List<String> list) throws NeoIOException {
		if (list.size() <= 1) {
			throw new NeoIOException("Pathway " + this.key + " has <= 1 points, invalid!");
		}
		
		for (String line : list) {
			String args[] = line.split(" ");
			double x = Double.parseDouble(args[0]);
			double y = Double.parseDouble(args[1]);
			double z = Double.parseDouble(args[2]);
			PathwayPoint point = NavigationManager.getPoint(new Location(w, x, y, z));
			if (point == null) {
				throw new NeoIOException("Pathway " + this.key + " failed to load point " + line);
			}
			points.add(point);
		}

		if (!points.getFirst().isEndpoint() || !points.getLast().isEndpoint()) {
			throw new NeoIOException("Pathway " + this.key + " does not have a start or finish endpoint");
		}
		
		// Passed validation
		setupPoints();
	}
	
	private LinkedList<PathwayPoint> reversePoints(LinkedList<PathwayPoint> original) {
		LinkedList<PathwayPoint> list = new LinkedList<PathwayPoint>();
		Iterator<PathwayPoint> iter = original.descendingIterator();
		while (iter.hasNext()) {
			list.add(iter.next());
		}
		return list;
	}
	
	private void setupPoints() {
		points.getFirst().addEndpointTo(points.getLast(), this);
		points.getLast().addEndpointFrom(points.getFirst(), this);
		ListIterator<PathwayPoint> iter = points.listIterator();
		PathwayPoint l1 = null;
		PathwayPoint l2 = iter.next();
		while (iter.hasNext()) {
			l1 = l2;
			l2 = iter.next();
			l1.addConnection(this.key);
			l2.addConnection(this.key);
		}
	}
	
	public String getStartPoint() {
		return points.getFirst().getDisplay();
	}
	
	public String getEndPoint() {
		return points.getLast().getDisplay();
	}
	
	public PathwayInstance start(Player p) {
		Condition c = ConditionManager.getBlockingCondition(p, conditions);
		if (c != null) {
			Util.msg(p, "§cCould not start navigation from §6" + getStartPoint() + " to §6" + getEndPoint() + "§c, " + c.getExplanation(p));
			return null;
		}

		PathwayInstance pwi = new PathwayInstance(p, this);
		BukkitTask task = new BukkitRunnable() {
			public void run() {
				if (p == null) {
					this.cancel();
					return;
				}
				
				// Check if in different world
				if (!p.getWorld().equals(w)) {
					pwi.cancel("no longer in same world.");
					return;
				}
				
				// Check if location reached
				if (p.getLocation().distanceSquared(getEndLocation()) <= END_RADIUS_SQ) {
					pwi.stop();
					return;
				}
				
				show(p);
			}
		}.runTaskTimer(NeoQuests.inst(), 0L, 20L);

		pwi.setTask(task);
		Util.msg(p, "§7Started navigation from &6" + getStartPoint() + "&7 to &6" + getEndPoint() + "§7!");
		return pwi;
	}
	
	public void show(Player p) {
		showLines(p, this.points, true);
	}
	
	public static void showLines(Player p, LinkedList<PathwayPoint> points, boolean useDisplayLocation) {
		if (points.size() < 2) {
			return;
		}
		ListIterator<PathwayPoint> iter = points.listIterator();
		PathwayPoint l1 = null;
		PathwayPoint l2 = iter.next();
		if (l2.getLocation().distanceSquared(p.getLocation()) < DISTANCE_SHOWABLE) {
			l2.spawnParticle(p, useDisplayLocation, false);
		}
		while (iter.hasNext()) {
			l1 = l2;
			l2 = iter.next();
			boolean dist1 = l1.getLocation().distanceSquared(p.getLocation()) < DISTANCE_SHOWABLE;
			boolean dist2 = l2.getLocation().distanceSquared(p.getLocation()) < DISTANCE_SHOWABLE;
			if (dist2) {
				l2.spawnParticle(p, useDisplayLocation, !iter.hasNext()); // Spawn end particle at last point only
			}
			
			if (dist1 || dist2) {
				if (l1.getType() != PathwayPointType.PORTAL || l2.getType() != PathwayPointType.PORTAL) {
					ParticleUtils.drawLine(p, useDisplayLocation ? l1.getDisplayLocation() : l1.getGroundLocation(),
							useDisplayLocation ? l2.getDisplayLocation() : l2.getGroundLocation(), PARTICLES_PER_POINT, PARTICLE_OFFSET, PARTICLE_SPEED, PARTICLE_DATA);
				}
			}
		}
	}
	
	public Location getEndLocation() {
		return points.get(points.size() - 1).getLocation();
	}
	
	public String getFileLocation() {
		return fileLocation;
	}
	
	public String getKey() {
		return key;
	}
	
	public boolean isBidirectional() {
		return bidirectional;
	}
	
	public World getWorld() {
		return w;
	}
	
	public ArrayList<Condition> getConditions() {
		return conditions;
	}
	
	public LinkedList<PathwayPoint> getPoints() {
		return points;
	}
}
