package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class NearestRailroadCard implements RNGCard {
	private Game game;
	
	public NearestRailroadCard(Game game) {
		this.game = game;
	}
	
	@Override
	public void onDraw(GamePlayer gp) {
		
	}

}
