package me.Neoblade298.NeoProfessions.Gardens;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Managers.GardenManager;
import me.Neoblade298.NeoProfessions.Managers.MinigameManager;
import me.Neoblade298.NeoProfessions.Minigames.Minigame;

public class Garden {
	int size;
	HashMap<Integer, GardenSlot> slots;
	private static final long DEFAULT_GROW_TIME = 1800000; // 30 minutes
	private static final int BASE_SIZE = 3;
	
	public Garden() {
		this.size = BASE_SIZE;
		this.slots = new HashMap<Integer, GardenSlot>();
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}
	
	public HashMap<Integer, GardenSlot> getSlots() {
		return slots;
	}
	
	public void plantSeed(int slot, int plantId, int fertilizerId) {
		Fertilizer fert = null;
		long growTime = DEFAULT_GROW_TIME;
		if (fertilizerId != -1) {
			fert = GardenManager.getFertilizer(fertilizerId);
			growTime *= fert.getTimeMultiplier();
		}
		this.slots.put(slot, new GardenSlot(plantId, fert, System.currentTimeMillis() + growTime));
	}
	
	public void harvestSeed(Player p, int slot) {
		GardenSlot gs = slots.get(slot);
		if (gs != null && gs.getEndTime() <= System.currentTimeMillis()) {
			slots.remove(slot);
			MinigameManager.startMinigame(p, gs.getFertilizer() != null ? gs.getFertilizer().getParams() : Minigame.defaultParams);
		}
	}
}
