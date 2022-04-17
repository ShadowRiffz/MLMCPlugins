package me.Neoblade298.NeoConsumables.objects;

import com.sucy.skill.api.util.BuffType;

public class BuffAction {
	BuffType type;
	int duration;
	double value;
	boolean percent;
	String category;
	
	public BuffAction(BuffType type, double value, int duration, boolean percent) {
		this.type = type;
		this.duration = duration;
		this.value = value;
		this.percent = percent;
		this.category = null;
	}

	public BuffType getType() {
		return type;
	}

	public int getDuration() {
		return duration;
	}

	public double getValue() {
		return value;
	}

	public boolean isPercent() {
		return percent;
	}
	
	public String getCategory() {
		return this.category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
}
