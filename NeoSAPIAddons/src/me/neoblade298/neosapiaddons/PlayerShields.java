package me.neoblade298.neosapiaddons;

import java.util.LinkedList;
import java.util.Queue;

import org.bukkit.entity.Player;

public class PlayerShields {
	private Queue<Shield> shields = new LinkedList<Shield>();
	private Player p;
	private double max;
	private double amount;

	public PlayerShields(Player p) {
		this.p = p;
		this.max = 0;
		this.amount = 0;
	}
	
	public void addShield(Shield shield) {
		shields.add(shield);
		if (amount <= 0) {
			max = shield.getTotal();
			amount = shield.getTotal();
		}
		else if (amount + shield.getTotal() > max) {
			max = amount + shield.getTotal();
			amount += shield.getTotal();
		}
		else {
			amount += shield.getTotal();
		}
		ShieldManager.updatePlayerShields(p);
	}
	
	public double useShields(double damage) {
		while (!shields.isEmpty() && damage > 0) {
			Shield curr = shields.peek();
			damage = curr.useShield(damage);
			if (!curr.isUsable()) {
				shields.poll();
			}
		}
		return damage;
	}
	
	public double getMax() {
		return max;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public void subtractShields(double difference) {
		this.amount -= difference;
	}
}
