package me.Neoblade298.NeoProfessions.Gardens;

public class GardenSlot {
	int id;
	Fertilizer fertilizer;
	long endTime;
	
	public GardenSlot(int id, Fertilizer fertilizer, long endTime) {
		this.id = id;
		this.fertilizer = fertilizer;
		this.endTime = endTime;
	}

	public int getId() {
		return id;
	}

	public Fertilizer getFertilizer() {
		return fertilizer;
	}

	public long getEndTime() {
		return endTime;
	}
}
