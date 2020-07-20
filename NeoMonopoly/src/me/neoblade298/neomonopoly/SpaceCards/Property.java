package me.neoblade298.neomonopoly.SpaceCards;

import me.neoblade298.neomonopoly.Objects.GamePlayer;

public interface Property extends Space {
	
	public void onLand(GamePlayer lander, int dice);
	public void onOwned(GamePlayer owner);
	public void onUnowned(GamePlayer formerOwner);
	public void onBankrupt(GamePlayer formerOwner);
	public void onStart(GamePlayer starter);
	public GamePlayer getOwner();
	public void setOwner(GamePlayer owner);
	public int getPrice();
	public void setPrice(int price);
	public boolean canMortgage();
	public boolean isMortgaged();
	public void setMortgaged(boolean isMortgaged);
	public int[] getRent();
	public void setRent(int[] rent);
	public String getName();
	public void setName(String name);
	int calculateRent(int dice);
	public String listComponent();
	public String getColoredName();
}
