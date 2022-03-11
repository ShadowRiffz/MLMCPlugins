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
	private String display;
	int numDrops;
	int difficulty;
	
	public Minigame(String display, String type, ArrayList<MinigameDrops> droptable, int numDrops, int difficulty) {
		this.type = type;
		this.droptable = droptable;
		this.numDrops = numDrops;
		this.display = display;
		this.difficulty = difficulty;
	}
	
	private ArrayList<MinigameDrop> generateDrops() {
		ArrayList<MinigameDrop> drops = new ArrayList<MinigameDrop>();
		for (int i = 0; i < numDrops; i++) {
			int rand = Professions.gen.nextInt(numDrops);
			MinigameDrops mdrops = droptable.get(0);
			int index = 0;
			do {
				mdrops = droptable.get(index);
				rand -= mdrops.getWeight();
				index++;
			}
			while (rand >= 0);
			int min = mdrops.getMinAmt(), max = mdrops.getMaxAmt();
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
			new HarvestingMinigame(MinigameManager.main, p, generateDrops(), display, numDrops, difficulty);
		}
		else {
			new LoggingMinigame();
		}
	}
}
