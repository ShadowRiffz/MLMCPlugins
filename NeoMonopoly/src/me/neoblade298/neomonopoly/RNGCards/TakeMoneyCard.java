package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class TakeMoneyCard extends RNGCard {
	private Game game;
	private int amount;
	
	public TakeMoneyCard(Game game, String name, int amount) {
		super(game, name);
		this.game = game;
		this.amount = amount;
	}
	
	@Override
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		game.isBusy = false;
		boolean cantPay = false;
		for (GamePlayer p : game.currentTurn) {
			if (!p.equals(gp)) {
				if(!(game.billPlayer(p, amount, gp))) cantPay = true;
			}
		}
		if (cantPay) {
			game.requiredActions.get(gp).add(0, "WAIT_PLAYERBILLS");
		}
		game.isBusy = false;
	}

}
