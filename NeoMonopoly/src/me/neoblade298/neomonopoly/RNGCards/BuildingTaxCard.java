package me.neoblade298.neomonopoly.RNGCards;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class BuildingTaxCard implements RNGCard {
	private Game game;
	private int house;
	private int hotel;
	
	public BuildingTaxCard(Game game, int house, int hotel) {
		this.game = game;
		this.house = house;
		this.hotel = hotel;
	}
	
	@Override
	public void onDraw(GamePlayer gp) {
		
	}

}
