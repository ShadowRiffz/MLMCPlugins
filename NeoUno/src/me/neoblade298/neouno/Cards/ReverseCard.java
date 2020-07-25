package me.neoblade298.neouno.Cards;

import java.util.Collections;

import org.bukkit.ChatColor;

import me.neoblade298.neouno.Objects.Game;

public class ReverseCard implements Card{
	private Game game;
	private ChatColor color;
	private int number;
	private String display;
	private String alias;
	
	public ReverseCard(Game game, ChatColor color) {
		this.game = game;
		this.color = color;
		this.number = 11;
		this.display = this.color + "Reverse";
		this.alias = "reverse" + game.main.colorToString.get(color).substring(0, 1);
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
	public void setDisplay(String display) {
		this.display = display;
	}

	@Override
	public void onPlay() {
		Collections.reverse(game.turns);
		game.turns.add(game.curr);
		game.curr = game.turns.remove(0);
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
