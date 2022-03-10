package me.Neoblade298.NeoProfessions.Minigames;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Inventories.HarvestingMinigame;
import me.Neoblade298.NeoProfessions.Inventories.LoggingMinigame;
import me.Neoblade298.NeoProfessions.Inventories.StonecuttingMinigame;

public class Minigame {
	private String type;
	private ArrayList<MinigameDrops> droptable;
	int numDrops;
	
	public Minigame(String type, ArrayList<MinigameDrops> droptable, int numDrops) {
		this.type = type;
		this.droptable = droptable;
		this.numDrops = numDrops;
	}
	
	private ArrayList<MinigameDrop> generateDrops() {
		ArrayList<MinigameDrop> drops = new ArrayList<MinigameDrop>();
		for (int i = 0; i < numDrops; i++) {
			int rand = Professions.gen.nextInt(numDrops);
			MinigameDrops mdrops;
			int index = 0, min = mdrops.getMinAmt(), max = mdrops.getMaxAmt();
			do {
				mdrops = droptable.get(index);
				rand -= mdrops.getWeight();
				index++;
			}
			while (rand >= 0);
			drops.add(new MinigameDrop(mdrops.getItem(),
					Professions.gen.nextInt(max + 1 - min) + min));
		}
		return drops;
	}
	
	public void startMinigame(Player p) {
		if (type.equalsIgnoreCase("stonecutting")) {
			new StonecuttingMinigame();
		}
		else if (type.equalsIgnoreCase("harvesting")) {
			new HarvestingMinigame();
		}
		else {
			new LoggingMinigame();
		}
	}
}
