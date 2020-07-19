package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class TakeMoneyCard implements RNGCard {
	private Game game;
	private int amount;
	
	public TakeMoneyCard(Game game, int amount) {
		this.game = game;
		this.amount = amount;
	}
	
	@Override
	public void onDraw(GamePlayer gp) {
		
	}

}
