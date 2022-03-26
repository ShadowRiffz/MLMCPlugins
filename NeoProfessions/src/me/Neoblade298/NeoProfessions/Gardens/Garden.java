package me.Neoblade298.NeoProfessions.Gardens;

import java.util.HashMap;

import org.bukkit.entity.Player;

import me.Neoblade298.NeoProfessions.Managers.GardenManager;
import me.Neoblade298.NeoProfessions.Managers.MinigameManager;

public class Garden {
	int size;
	HashMap<Integer, GardenSlot> slots;
	private static final long DEFAULT_GROW_TIME = 1800000; // 30 minutes
	
	public void setSize(int size) {
		this.size = size;
		this.slots = new HashMap<Integer, GardenSlot>();
	}
	
	public int getSize() {
		return size;
	}
	
	public HashMap<Integer, GardenSlot> getSlots() {
		return slots;
	}
	
	public void plantSeed(int slot, int plantId, int fertilizerId) {
		Fertilizer fert = null;
		if (fertilizerId != -1) {
			fert = GardenManager.getFertilizer(fertilizerId);
		}
		this.slots.put(slot, new GardenSlot(plantId, fert, System.currentTimeMillis() + DEFAULT_GROW_TIME));
	}
	
	public void harvestSeed(Player p, int slot) {
		GardenSlot gs = slots.get(slot);
		if (gs != null && gs.getEndTime() <= System.currentTimeMillis()) {
			slots.remove(slot);
			MinigameManager.startMinigame(p, gs.getFertilizer().getParams());
		}
	}
}
