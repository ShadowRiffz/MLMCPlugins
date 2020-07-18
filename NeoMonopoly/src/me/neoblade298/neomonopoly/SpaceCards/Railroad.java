package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class Railroad extends Property {
	private String name;
	private GamePlayer owner;
	private boolean isMortgaged;
	private int[] rent;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private ChatColor color;
	
	public Railroad (String name, int[] rent, ChatColor color) {
		this.owner = null;
		this.isMortgaged = false;
		this.name = name;
		this.rent = rent;
		this.setColor(color);
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

	@Override
	public ChatColor getColor() {
		return color;
	}

	@Override
	public void setColor(ChatColor color) {
		this.color = color;
	}

}
