package me.Neoblade298.NeoProfessions.Minigames;

import java.util.ArrayList;

public class MinigameDroptable {
	ArrayList<MinigameDrops> droptable;
	int totalWeight;
	public ArrayList<MinigameDrops> getDropTable() {
		return droptable;
	}
	public int getTotalWeight() {
		return totalWeight;
	}
	public MinigameDroptable(ArrayList<MinigameDrops> droptable) {
		this.droptable = droptable;
		
		this.totalWeight = 0;
		for (MinigameDrops drops : droptable) {
			this.totalWeight += drops.getWeight();
		}
	}
	
}
