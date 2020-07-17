package me.neoblade298.neomonopoly.Objects;

import me.neoblade298.neomonopoly.Monopoly;

public class Chance implements Space{
	Monopoly main;
	
	public Chance(Monopoly main) {
		this.main = main;
	}

	@Override
	public void onLand(GamePlayer lander, int dice) {
		return;
	}

	@Override
	public void onStart() {
		return;
	}
}
