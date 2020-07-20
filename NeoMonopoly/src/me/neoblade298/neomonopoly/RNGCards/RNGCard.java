package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public abstract class RNGCard {
	Game game;
	String name;
	public void onDraw(GamePlayer gp, String src) {
		game.broadcast("&f" + src + ": &7" + name);
	}
}
