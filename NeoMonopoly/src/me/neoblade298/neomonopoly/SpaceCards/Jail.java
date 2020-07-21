package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class Jail implements Space {
	private Game game;
	
	public Jail(Game game) {
		this.game = game;
	}

	@Override
	public void onLand(GamePlayer lander, int dice) {
		game.isBusy = false;
	}

	@Override
	public void onStart(GamePlayer starter) {
		if (starter.isJailed()) {
			game.requiredActions.get(starter).add("JAIL_ACTION");
		}
		else {
			game.requiredActions.get(starter).add("ROLL_MOVE");
		}
	}
	
	public char getMapChar() {
		return 'J';
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
		String msg = gp.isJailed() ? "&cIn Jail" : "&6Just Visiting Jail";
		gp.message("&7[" + msg + "&7]");
	}

	@Override
	public String getShorthand(GamePlayer gp) {
		String msg = gp.isJailed() ? "&cIn Jail" : "&6Just Visiting Jail";
		return "&7[" + msg + "&7]";
	}

}
