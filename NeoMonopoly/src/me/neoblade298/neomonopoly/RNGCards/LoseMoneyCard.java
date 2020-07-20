package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class LoseMoneyCard extends RNGCard {
	private Game game;
	private String name;
	private int amount;
	
	public LoseMoneyCard(Game game, String name, int amount) {
		this.game = game;
		this.amount = amount;
		this.name = name;
	}

	@Override
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		game.billPlayer(gp, amount, null);
		game.isBusy = false;
	}

}
