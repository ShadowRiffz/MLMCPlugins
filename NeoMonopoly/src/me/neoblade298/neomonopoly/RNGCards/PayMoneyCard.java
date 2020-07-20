package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class PayMoneyCard extends RNGCard {
	private Game game;
	private String name;
	private int amount;
	
	public PayMoneyCard(Game game, String name, int amount) {
		this.game = game;
		this.amount = amount;
		this.name = name;
	}
	
	@Override
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		for (GamePlayer p : game.gameplayers) {
			if (!p.equals(gp)) game.giveMoney(amount, p, "&e" + gp + "&7 has given you &a+$" + amount + "&7!");
		}
		game.billPlayer(gp, (game.gameplayers.size() - 1) * amount, null);
		game.isBusy = false;
	}

}
