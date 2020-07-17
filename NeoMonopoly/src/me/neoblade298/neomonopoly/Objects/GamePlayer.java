package me.neoblade298.neomonopoly.Objects;

import java.util.ArrayList;

public class GamePlayer {
	private int numJailFree;
	private int numRailroads;
	private int money;
	private int position;

	public int getNumJailFree() {
		return numJailFree;
	}

	public void setNumJailFree(int numJailFree) {
		this.numJailFree = numJailFree;
	}

	public int getNumRailroads() {
		return numRailroads;
	}

	public void setNumRailroads(int numRailroads) {
		this.numRailroads = numRailroads;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<Property> properties) {
		this.properties = properties;
	}

	private boolean isJailed;
	private ArrayList<Property> properties;

	public boolean isJailed() {
		return isJailed;
	}

	public void setJailed(boolean isJailed) {
		this.isJailed = isJailed;
	}
}
