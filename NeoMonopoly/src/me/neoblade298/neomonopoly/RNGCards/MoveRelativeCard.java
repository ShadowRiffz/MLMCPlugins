package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class MoveRelativeCard implements RNGCard {
	private Game game;
	private int move;
	
	public MoveRelativeCard(Game game, int move) {
		this.game = game;
		this.move = move;
	}
	
	@Override
	public void onDraw(GamePlayer gp) {
		
	}
	
}
