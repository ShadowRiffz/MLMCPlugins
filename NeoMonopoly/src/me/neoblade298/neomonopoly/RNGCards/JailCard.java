package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class JailCard extends RNGCard{
	private Game game;
	private String name;
	
	public JailCard(Game game, String name) {
		this.game = game;
		this.name = name;
	}
	
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		game.sendToJail(gp);
		game.isBusy = false;
	}
}
