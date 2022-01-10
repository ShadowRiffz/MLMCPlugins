package me.neoblade298.neouno.Cards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import me.neoblade298.neouno.Objects.Game;

public class DrawTwoCard implements Card{
	private Game game;
	private ChatColor color;
	private int number;
	private String display;
	private String alias;
	
	public DrawTwoCard(Game game, ChatColor color) {
		this.game = game;
		this.color = color;
		this.number = 12;
		this.display = color + "+2";
		this.alias = "+2" + game.main.colorToString.get(color).substring(0, 1);
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
		if (Bukkit.getPlayer(game.turns.get(0).getPlayer()) == null) {
			game.broadcast("&cThe next player, &e" + game.turns.get(0).getPlayer() + "&c, is offline. Kick them to continue, or wait for them to log back on!");
			return;
		}
		game.turns.add(game.curr);
		game.curr = game.turns.remove(0);
		game.drawNum += 2;
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
