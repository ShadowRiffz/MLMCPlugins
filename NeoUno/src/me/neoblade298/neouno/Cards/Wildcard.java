package me.neoblade298.neouno.Cards;

import org.bukkit.ChatColor;

import me.neoblade298.neouno.Objects.Game;

public class Wildcard implements Card{
	private Game game;
	private int number;
	private ChatColor color;
	private String display;
	private String alias;
	
	public Wildcard(Game game) {
		this.game = game;
		this.number = -1;
		this.color = ChatColor.WHITE;
		this.display = this.color + "Wild";
		this.alias = "wild";
	}

	@Override
	public ChatColor getColor() {
		return this.color;
	}

	@Override
	public void setColor(ChatColor color) {
		this.color = color;
		this.display = this.color + ChatColor.stripColor(this.display);
	}

	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public void setNumber(int number) {
		this.number = number;
		this.display = this.color + "" + this.number;
	}
	
	@Override
	public String getDisplay() {
		return this.display;
	}

	@Override
	public void onPlay() {
		game.requiredAction = "PICK_COLOR";
		number = game.topCard.getNumber();
		this.display = game.topCard.getDisplay();
	}

	@Override
	public void onDraw(boolean normalDraw) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public String getAlias() {
		return this.alias;
	}
}
