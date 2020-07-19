package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class JailCard implements RNGCard{
	private Game game;
	
	public JailCard(Game game) {
		this.game = game;
	}
	
	
	@Override
	public void onDraw(GamePlayer gp) {
		game.sendToJail(gp);
	}
}
