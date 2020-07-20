package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class TakeMoneyCard extends RNGCard {
	private Game game;
	private String name;
	private int amount;
	
	public TakeMoneyCard(Game game, String name, int amount) {
		this.game = game;
		this.amount = amount;
		this.name = name;
	}
	
	@Override
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		game.isBusy = false;
		boolean cantPay = false;
		for (GamePlayer p : game.gameplayers) {
			if (!p.equals(gp)) {
				if(!(game.billPlayer(p, (game.gameplayers.size() - 1) * amount, gp))) cantPay = true;
			}
		}
		if (cantPay) {
			game.requiredActions.get(gp).add(0, "WAIT_PLAYERBILLS");
		}
		game.isBusy = false;
	}

}
