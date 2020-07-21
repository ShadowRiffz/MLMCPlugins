package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class PayMoneyCard extends RNGCard {
	private Game game;
	private int amount;
	
	public PayMoneyCard(Game game, String name, int amount) {
		super(game, name);
		this.game = game;
		this.amount = amount;
	}
	
	@Override
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		for (GamePlayer p : game.gameplayers) {
			if (!p.equals(gp)) game.giveMoney(amount, p, "&e" + gp + "&7 has given &e" + gp + " &a+$" + amount + "&7!", false);
		}
		game.billPlayer(gp, (game.gameplayers.size() - 1) * amount, null);
		game.isBusy = false;
	}

}
