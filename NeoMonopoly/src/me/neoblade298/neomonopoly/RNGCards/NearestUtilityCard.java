package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class NearestUtilityCard implements RNGCard{
	private Game game;
	
	public NearestUtilityCard(Game game) {
		this.game = game;
	}
	
	@Override
	public void onDraw(GamePlayer gp) {
		
	}

}
