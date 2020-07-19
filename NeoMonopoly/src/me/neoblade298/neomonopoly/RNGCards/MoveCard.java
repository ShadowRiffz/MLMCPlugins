package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class MoveCard implements RNGCard{
	private Game game;
	private int position;
	
	public MoveCard(Game game, int position) {
		this.game = game;
		this.position = position;
	}
	
	@Override
	public void onDraw(GamePlayer gp) {
		
	}
}
