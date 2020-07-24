package me.neoblade298.neouno.Cards;

import org.bukkit.ChatColor;

import me.neoblade298.neouno.Objects.Game;
import me.neoblade298.neouno.Objects.GamePlayer;

public class SkipCard implements Card{
	private Game game;
	private ChatColor color;
	private int number;
	private String display;
	private String alias;
	
	public SkipCard(Game game, ChatColor color) {
		this.game = game;
		this.color = color;
		this.number = 10;
		this.display = this.color + "Skip";
		this.alias = "skip" + game.main.colorToString.get(color).substring(0, 1);
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
		GamePlayer gp = game.turns.remove(0);
		game.broadcast("&f" + gp + "'s &7turn was skipped!");
		game.turns.add(gp);
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
