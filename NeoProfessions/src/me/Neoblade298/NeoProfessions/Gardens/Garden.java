package me.Neoblade298.NeoProfessions.Gardens;

import java.util.HashMap;

public class Garden {
	int size;
	HashMap<Integer, GardenSlot> slots;
	
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
}
