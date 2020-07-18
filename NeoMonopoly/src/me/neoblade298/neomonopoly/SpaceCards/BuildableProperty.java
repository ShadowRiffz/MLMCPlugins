package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class BuildableProperty extends Property {
	private GamePlayer owner;
	private int numHouses;
	private int numHotels;
	private boolean isMonopoly;
	private boolean isMortgaged;
	private int[] rent;

	private int price;
	private int houseprice;
	private String name;
	private ChatColor color;
	
	public BuildableProperty(String name, int[] rent, int price, int houseprice, ChatColor color) {
		numHouses = 0;
		numHotels = 0;
		isMonopoly = false;
		isMortgaged = false;
		owner = null;
		this.name = name;
		this.rent = rent;
		this.price = price;
		this.setHouseprice(houseprice);
		this.color = color;
	}

	@Override
	public void onLand(GamePlayer lander, int dice) {
		
	}

	@Override
	public void onStart() {
		return;
	}

	public GamePlayer getOwner() {
		return owner;
	}

	public void setOwner(GamePlayer owner) {
		this.owner = owner;
	}

	public boolean isMortgaged() {
		return isMortgaged;
	}

	public void setMortgaged(boolean isMortgaged) {
		this.isMortgaged = isMortgaged;
	}

	public int[] getRent() {
		return rent;
	}

	public void setRent(int[] rent) {
		this.rent = rent;
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

	public boolean isMonopoly() {
		return isMonopoly;
	}

	public void setMonopoly(boolean isMonopoly) {
		this.isMonopoly = isMonopoly;
	}

	public int getHouseprice() {
		return houseprice;
	}

	public void setHouseprice(int houseprice) {
		this.houseprice = houseprice;
	}
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

}
