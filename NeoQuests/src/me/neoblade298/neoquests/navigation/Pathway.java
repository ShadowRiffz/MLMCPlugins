package me.neoblade298.neoquests.navigation;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import me.Neoblade298.NeoProfessions.Utilities.Util;
import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.NeoQuests;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;

public class Pathway {
	private String key, startDisplay, endDisplay, fileLocation;
	private World w;
	private LinkedList<Location> points = new LinkedList<Location>();
	private ArrayList<Condition> conditions;
	
	private static final int BLOCKS_PER_PARTICLE = 2;
	private static final int PARTICLES_PER_POINT = 20;
	private static final int PARTICLE_OFFSET = 1;
	private static final int END_RADIUS_SQ = 100;
	
	private static final double DISTANCE_SHOWABLE = 1024;
	
	public Pathway(ConfigurationSection cfg, File file) throws NeoIOException {
		key = cfg.getName().toUpperCase();
		startDisplay = cfg.getString("start");
		endDisplay = cfg.getString("end");
		fileLocation = file.getPath() + "/" + file.getName();
		this.w = Bukkit.getWorld(cfg.getString("world", "Argyll"));
		this.conditions = ConditionManager.parseConditions(cfg.getStringList("conditions"));
		parsePoints(cfg.getStringList("points"));
	}
	
	private void parsePoints(List<String> list) throws NeoIOException {
		if (list.size() <= 1) {
			throw new NeoIOException("Pathway " + this.key + " has <= 1 points, invalid!");
		}
		
		for (String line : list) {
			String args[] = line.split(" ");
			int x = Integer.parseInt(args[0]);
			int y = Integer.parseInt(args[1]);
			int z = Integer.parseInt(args[2]);
			points.add(new Location(w, x, y, z));
		}
	}
	
	public PathwayInstance start(Player p) {
		Condition c = ConditionManager.getBlockingCondition(p, conditions);
		if (c != null) {
			Util.sendMessage(p, "§cCould not start navigation from §6" + startDisplay + " to §6" + endDisplay + "§c, " + c.getExplanation(p));
			return null;
		}

		PathwayInstance pwi = new PathwayInstance(this);
		BukkitTask task = new BukkitRunnable() {
			public void run() {
				if (p == null) {
					this.cancel();
					return;
				}
				
				// Check if in different world
				if (!p.getWorld().equals(w)) {
					pwi.stop(false);
					return;
				}
				
				// Check if location reached
				if (p.getLocation().distanceSquared(getEndLocation()) <= END_RADIUS_SQ) {
					pwi.stop(true);
					return;
				}
				
				show(p);
			}
		}.runTaskTimer(NeoQuests.inst(), 60L, 0L);

		pwi.setTask(task);
		Util.sendMessage(p, "§7started navigation from §6" + startDisplay + " to §6" + endDisplay + "§7!");
		return pwi;
	}
	
	public void show(Player p) {
		ListIterator<Location> iter = points.listIterator();
		Location l1 = iter.next();
		Location l2 = iter.next();
		while (iter.hasNext()) {
			if (l1.distanceSquared(p.getLocation()) < DISTANCE_SHOWABLE) {
				drawLine(p, l1, l2);
			}
			
			l1 = l2;
			l2 = iter.next();
		}
	}
	
	private void drawLine(Player p, Location l1, Location l2) {
		Location start = l1.clone();
		Location end = l2.clone();
		Vector v = end.subtract(start).toVector();
		for (double i = 0; i < v.length() / BLOCKS_PER_PARTICLE; i += BLOCKS_PER_PARTICLE) {
		    v.multiply(i);
		    start.add(v);
		    p.spawnParticle(Particle.REDSTONE, start.getX(), start.getY(), start.getZ(), PARTICLES_PER_POINT, PARTICLE_OFFSET, PARTICLE_OFFSET, PARTICLE_OFFSET);
		    start.subtract(v);
		    v.normalize();
		}
	}
	
	public Location getEndLocation() {
		return points.get(points.size() - 1);
	}
	
	public String getFileLocation() {
		return fileLocation;
	}
	
	public String getStartDisplay() {
		return startDisplay;
	}
	
	public String getEndDisplay() {
		return endDisplay;
	}
}
