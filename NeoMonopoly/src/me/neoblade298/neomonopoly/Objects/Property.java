package me.neoblade298.neomonopoly.Objects;

public abstract class Property implements Space {
	private GamePlayer owner;
	private boolean isMortgaged;
	private int[] rent;
	
	public void onLand(GamePlayer lander, int dice) {
		
	}
	
	public void onStart() {
		
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
}
