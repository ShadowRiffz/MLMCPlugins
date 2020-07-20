package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class JailCard extends RNGCard{
	private Game game;
	
	public JailCard(Game game, String name) {
		super(game, name);
		this.game = game;
	}
	
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		game.sendToJail(gp);
		game.isBusy = false;
	}
}
