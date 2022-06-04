package me.neoblade298.neoquests.navigation;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import me.neoblade298.neocore.exceptions.NeoIOException;
import me.neoblade298.neoquests.conditions.Condition;
import me.neoblade298.neoquests.conditions.ConditionManager;

public class Pathway {
	private String key;
	private LinkedList<Location> points = new LinkedList<Location>();
	private ArrayList<Condition> conditions;
	
	private static final int BLOCKS_PER_PARTICLE = 2;
	private static final int PARTICLES_PER_POINT = 20;
	private static final int PARTICLE_OFFSET = 1;
	
	private static final double DISTANCE_SHOWABLE = 1024;
	
	public Pathway(ConfigurationSection cfg) throws NeoIOException {
		key = cfg.getName().toUpperCase();
		this.conditions = ConditionManager.parseConditions(cfg.getStringList("conditions"));
		parsePoints(cfg.getStringList("points"));
	}
	
	private void parsePoints(List<String> list) throws NeoIOException {
		for (String line : list) {
			String args[] = line.split(" ");
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int z = Integer.parseInt(args[3]);
			points.add(new Location(Bukkit.getWorld(args[0]), x, y, z));
		}
		
		if (points.size() <= 1) {
			throw new NeoIOException("Pathway " + this.key + " has <= 1 points, invalid!");
		}
	}
	
	public void start(Player p) {
		
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
}
