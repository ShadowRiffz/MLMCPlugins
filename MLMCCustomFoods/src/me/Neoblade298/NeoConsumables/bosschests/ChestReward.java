package me.Neoblade298.NeoConsumables.bosschests;

import org.bukkit.entity.Player;

public abstract class ChestReward {
	private int weight;
	
	public abstract void giveReward(Player p);
	public abstract void sendMessage(Player p);
	public void setWeight(int weight) {
		this.weight = weight;
	}
	public int getWeight() {
		return weight;
	}
}
