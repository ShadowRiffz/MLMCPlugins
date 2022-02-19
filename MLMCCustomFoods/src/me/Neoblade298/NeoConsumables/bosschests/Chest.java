package me.Neoblade298.NeoConsumables.bosschests;

import java.util.LinkedList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import me.Neoblade298.NeoConsumables.Consumables;

public class Chest {
	private static Random gen = new Random();
	private LinkedList<ChestStage> stages;
	private Consumables main;
	
	public Chest(Consumables main, String nbt, int level, LinkedList<ChestStage> stages, String display) {
		this.main = main;
		this.stages = stages;
	}
	
	public void useChest(Player p) {
		long delay = 0;
		for (ChestStage stage : stages) {
			// Calculate if it will happen
			if (stage.getChance() < gen.nextDouble()) {
				continue;
			}

			delay += 20;
			BukkitRunnable runStage = new BukkitRunnable() {
				public void run() {
					p.playSound(p.getLocation(), stage.getSound(), 1.0F, stage.getPitch());
					// TODO: Add effect
					ChestReward reward = stage.chooseReward();
					reward.giveReward(p);
					reward.sendMessage(p);
					if (stage.getEffect() != null) {
						Chest.runAnimation(p, stage.getEffect());
					}
				}
			};
			runStage.runTaskLater(main, delay);
		}
		
		// Extra 20 ticks after last item, then 10 ticks to close the animation
		final long fDelay = delay + 30;
		
		// Add chest animation
		BukkitRunnable chestAnimation = new BukkitRunnable() {
			int tick = 0;
			public void run() {
				if (tick >= fDelay - 10) {
					this.cancel();
				}
				tick += 2;
				Location loc = p.getLocation();
				loc.setY(loc.getY() + 1);
		        Vector dir = loc.getDirection().setY(0).normalize().multiply(4);
				p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.add(dir), 5, 0.1, 0.1, 0.1, 0); 
			}
		};
		chestAnimation.runTaskTimer(main, 0, 2);
		
		BukkitRunnable endSound = new BukkitRunnable() {
			public void run() {
				p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_CLOSE, 1.0F, 1.0F);
			}
		};
		endSound.runTaskLater(main, fDelay - 10);
		
		BukkitRunnable endAnimation = new BukkitRunnable() {
			double distance = 4;
			public void run() {
				if (distance <= 0) {
					this.cancel();
				}
				else {
					distance -= 0.8;
					Location loc = p.getLocation();
					loc.setY(loc.getY() + 1);
			        Vector dir = loc.getDirection().setY(0).normalize().multiply(distance);
					p.getWorld().spawnParticle(Particle.FIREWORKS_SPARK, loc.add(dir), 5, 0.1, 0.1, 0.1, 0);
				}
			}
		};
		endAnimation.runTaskTimer(main, fDelay - 10, 2);
		
		BukkitRunnable endParticle = new BukkitRunnable() {
			public void run() {
				Location loc = p.getLocation();
				loc.setY(loc.getY() + 1);
		        Vector dir = loc.getDirection().setY(0).normalize().multiply(0.5);
				p.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, loc.add(dir), 100, 0.6, 0.6, 0.6, 0);
				p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
			}
		};
		endParticle.runTaskLater(main, fDelay);
	}
	
	private static void runAnimation(Player p, String type) {
		Particle part = Particle.CRIT_MAGIC;
		int amount = 50;
		if (type.equalsIgnoreCase("endrod")) {
			part = Particle.END_ROD;
			amount = 100;
		}
		else if (type.equalsIgnoreCase("fire")) {
			part = Particle.FLAME;
			amount = 200;
		}
		else if (type.equalsIgnoreCase("witch")) {
			part = Particle.SPELL_WITCH;
			amount = 500;
		}
		
		Location loc = p.getLocation();
		loc.setY(loc.getY() + 1);
        Vector dir = loc.getDirection().setY(0).normalize().multiply(4);
		p.getWorld().spawnParticle(part, loc.add(dir), amount, 0.4, 0.4, 0.4, 0.2);
	}
}
