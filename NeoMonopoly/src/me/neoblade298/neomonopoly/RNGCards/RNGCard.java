package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public abstract class RNGCard {
	String name;
	Game game;
	public RNGCard(Game game, String name) {
		this.name = name;
		this.game = game;
	}
	public void onDraw(GamePlayer gp, String src) {
		game.broadcast("&f" + src + ": &7" + name);
	}
}
