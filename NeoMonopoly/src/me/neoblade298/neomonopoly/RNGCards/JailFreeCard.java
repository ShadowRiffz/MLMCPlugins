package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class JailFreeCard extends RNGCard{
	private Game game;
	
	public JailFreeCard(Game game, String name) {
		super(game, name);
		this.game = game;
	}
	
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		gp.setNumJailFree(gp.getNumJailFree() + 1);
		game.broadcast("&e" + gp + " &7got a &fget out of jail free &7card! They now have " + gp.getNumJailFree() + "&7.");
		game.isBusy = false;
	}
}
