package me.neoblade298.neoquests;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class ParticleUtils {
	private static final int BLOCKS_PER_PARTICLE = 2;
	public static void drawLine(Player p, Location l1, Location l2, int perPoint, double offset, double speed, DustOptions options) {
		Location start = l1.clone();
		Location end = l2.clone();
	    
		Vector v = end.subtract(start).toVector();
		int iterations = (int) (v.length() / BLOCKS_PER_PARTICLE);
		for (int i = 1; i < iterations; i++) {
		    v.normalize();
		    v.multiply(i * BLOCKS_PER_PARTICLE);
		    start.add(v);
		    p.spawnParticle(Particle.REDSTONE, start, perPoint, offset, offset, offset, speed, options);
			start.subtract(v);
		}
	}
}
