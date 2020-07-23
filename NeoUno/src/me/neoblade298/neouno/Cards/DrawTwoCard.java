package me.neoblade298.neouno.Cards;

import org.bukkit.ChatColor;

import me.neoblade298.neouno.Objects.Game;

public class DrawTwoCard implements Card{
	private Game game;
	private ChatColor color;
	private int number;
	private String display;
	
	public DrawTwoCard(Game game, ChatColor color) {
		this.game = game;
		this.color = color;
		this.number = 12;
		this.display = color + "+2";
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
		game.drawNum += 2;
	}

	@Override
	public void onDraw(boolean normalDraw) {
		// TODO Auto-generated method stub
		
	}
}
