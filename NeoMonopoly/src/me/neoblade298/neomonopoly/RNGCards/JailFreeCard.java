package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class JailFreeCard implements RNGCard{
	private Game game;
	
	public JailFreeCard(Game game) {
		this.game = game;
	}

	@Override
	public void onDraw(GamePlayer gp) {
		gp.setNumJailFree(gp.getNumJailFree() + 1);
	}
}
