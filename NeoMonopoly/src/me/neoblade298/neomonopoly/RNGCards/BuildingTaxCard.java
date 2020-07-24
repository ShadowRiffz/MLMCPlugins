package me.neoblade298.neomonopoly.RNGCards;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
import me.neoblade298.neomonopoly.SpaceCards.BuildableProperty;
import me.neoblade298.neomonopoly.SpaceCards.Property;

public class BuildingTaxCard extends RNGCard {
	private Game game;
	private int house;
	private int hotel;
	
	public BuildingTaxCard(Game game, String name, int house, int hotel) {
		super(game, name);
		this.game = game;
		this.house = house;
		this.hotel = hotel;
	}
	
	public void onDraw(GamePlayer gp, String src) {
		super.onDraw(gp, src);
		
		// Calculate all hotels and houses
		new BukkitRunnable() { public void run() {
			int numHouses = 0, numHotels = 0;
			for (Property prop : gp.getProperties()) {
				if (prop instanceof BuildableProperty) {
					BuildableProperty bprop = (BuildableProperty) prop;
					numHouses += bprop.getNumHouses();
					numHotels += bprop.getNumHotels();
				}
			}
			int pay = (numHouses * house) + (numHotels * hotel);
			game.broadcast("&e" + gp + " &7has &e" + numHouses + " &7houses and &e" + numHotels + 
					" &7hotels so they pay &a$" + pay + "&7.");
			game.billPlayer(gp, pay, null);
			game.isBusy = false;
		}}.runTaskLater(game.main, 40L);
	}

}
