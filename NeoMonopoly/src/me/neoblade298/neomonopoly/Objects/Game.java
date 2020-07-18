package me.neoblade298.neomonopoly.Objects;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.neoblade298.neomonopoly.RNGCards.RNGCard;

public class Game {
	String name;
	private ArrayList<GamePlayer> players;
	private int numHouses;
	private int numHotels;
	private ArrayList<RNGCard> unusedChest;
	private ArrayList<RNGCard> usedChest;
	private ArrayList<RNGCard> unusedChance;
	private ArrayList<RNGCard> usedChance;

	public Game(String name, int money, ArrayList<Player> players) {
		
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<GamePlayer> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<GamePlayer> players) {
		this.players = players;
	}

	public int getNumHouses() {
		return numHouses;
	}

	public void setNumHouses(int numHouses) {
		this.numHouses = numHouses;
	}

	public int getNumHotels() {
		return numHotels;
	}

	public void setNumHotels(int numHotels) {
		this.numHotels = numHotels;
	}

	public ArrayList<RNGCard> getUnusedChest() {
		return unusedChest;
	}

	public void setUnusedChest(ArrayList<RNGCard> unusedChest) {
		this.unusedChest = unusedChest;
	}

	public ArrayList<RNGCard> getUsedChest() {
		return usedChest;
	}

	public void setUsedChest(ArrayList<RNGCard> usedChest) {
		this.usedChest = usedChest;
	}

	public ArrayList<RNGCard> getUnusedChance() {
		return unusedChance;
	}

	public void setUnusedChance(ArrayList<RNGCard> unusedChance) {
		this.unusedChance = unusedChance;
	}

	public ArrayList<RNGCard> getUsedChance() {
		return usedChance;
	}

	public void setUsedChance(ArrayList<RNGCard> usedChance) {
		this.usedChance = usedChance;
	}

}
