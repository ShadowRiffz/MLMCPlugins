package me.neoblade298.neouno.Cards;

import org.bukkit.ChatColor;

import me.neoblade298.neouno.Objects.Game;

public class NumberCard implements Card {
	private Game game;
	private int number;
	private ChatColor color;
	private String display;
	private String alias;
	public NumberCard(Game game, int number, ChatColor color) {
		this.game = game;
		this.number = number;
		this.color = color;
		this.display = this.color + "" + this.number;
		this.alias = this.number + game.main.colorToString.get(color).substring(0, 1);
	}
	
	@Override
	public ChatColor getColor() {
		return color;
	}
	
	@Override
	public void setColor(ChatColor color) {
		this.color = color;
	}
	
	@Override
	public int getNumber() {
		return this.number;
	}

	@Override
	public void setNumber(int number) {
		this.number = number;
	}
	
	@Override
	public String getDisplay() {
		return this.display;
	}
	
	@Override
	public void onPlay() {
		game.turns.add(game.curr);
		game.curr = game.turns.remove(0);
	}
	
	@Override
	public void onDraw(boolean normalDraw) {
		return;
	}
	
	@Override
	public String getAlias() {
		return this.alias;
	}

}
