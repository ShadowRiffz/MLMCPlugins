package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class GoToJail implements Space {
	private Game game;
	
	public GoToJail(Game game) {
		this.game = game;
	}

	@Override
	public void onLand(GamePlayer lander, int dice) {
		game.sendToJail(lander);
		game.isBusy = false;
	}
	
	public char getMapChar() {
		return '/';
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
		gp.message("&7[&cGo to Jail&7]");
	}
	
	@Override
	public String getShorthand(GamePlayer gp) {
		return "&7[&cGo to Jail&7]";
	}
}
