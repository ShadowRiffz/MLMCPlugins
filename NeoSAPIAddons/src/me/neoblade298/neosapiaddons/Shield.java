package me.neoblade298.neosapiaddons;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Shield {
	private double amount;
	private double total;
	private PlayerShields pshields;
	public Shield(Player p, double amt, boolean decayPercent, double decayDelay, double decayAmount, double decayPeriod, int decayRepetitions) {
		this.total = amt;
		this.amount = amt;
		this.pshields = ShieldManager.getPlayerShields(p);
		new BukkitRunnable() {
			int reps = decayRepetitions;
			double total = amt;
			public void run() {
				if (!isUsable()) {
					return;
				}
				boolean outOfShield = false;
				if (decayPercent) {
					outOfShield = decayAmount(total * decayAmount * 0.01);
				}
				else {
					outOfShield = decayAmount(decayAmount);
				}
				if (outOfShield || --reps <= 0) {
					this.cancel();
				}
			}
		}.runTaskTimer(SAPIAddons.inst, (long) (decayDelay * 20), (long) (decayPeriod * 20));
	}
	
	// Returns leftover damage
	public double useShield(double damage) {
		if (this.amount <= 0) {
			return damage;
		}
		double original = this.amount;
		this.amount = Math.max(0, this.amount - damage);
		pshields.subtractShields(original - amount);
		return this.amount > 0 ? 0 : damage - original;
	}
	
	public boolean isUsable() {
		return this.amount > 0;
	}
	
	public double getTotal() {
		return total;
	}
	
	// Needs its own method for bukkittask
	public boolean decayAmount(double amount) {
		double original = this.amount;
		this.amount = Math.max(this.amount - amount, 0);
		pshields.subtractShields(original - this.amount);
		return this.amount <= 0;
	}
}
