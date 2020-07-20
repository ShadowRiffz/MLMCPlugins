package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class Tax implements Space {
	private Game game;
	private int amount;
	private String type;
	
	public Tax(Game game, int amount, String type) {
		this.game = game;
		this.amount = amount;
		this.type = type;
	}

	@Override
	public void onLand(GamePlayer lander, int dice) {
		game.billPlayer(lander, this.amount, null);
		game.isBusy = false;
	}

	@Override
	public void onStart(GamePlayer starter) {
		game.requiredActions.get(starter).add("ROLL_MOVE");
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.WHITE;
	}

	@Override
	public void setColor(ChatColor color) {
		return;
	}

	@Override
	public Game getGame() {
		return this.game;
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}
	
	@Override
	public void displayProperty(GamePlayer gp) {
		gp.message("&7[&f" + type + " Tax&7 (&e" + amount + "&7]");
	}
	
	@Override
	public String getShorthand(GamePlayer gp) {
		return "&7[&f" + type + " Tax&7 (&e" + amount + "&7]";
	}

}
