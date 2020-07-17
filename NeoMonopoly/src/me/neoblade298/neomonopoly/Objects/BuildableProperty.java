package me.neoblade298.neomonopoly.Objects;

public class BuildableProperty extends Property {
	private GamePlayer owner;
	private int numHouses;
	private int numHotels;
	private boolean isMonopoly;
	private boolean isMortgaged;
	private int[] rent;
	
	public BuildableProperty() {
		
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

}
