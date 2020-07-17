package me.neoblade298.neomonopoly.Objects;

public class Railroad extends Property {
	private GamePlayer owner;
	private boolean isMortgaged;
	private int[] rent;

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
}
