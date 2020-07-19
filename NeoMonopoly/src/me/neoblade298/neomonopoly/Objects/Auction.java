package me.neoblade298.neomonopoly.Objects;

import java.util.ArrayList;

import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neomonopoly.SpaceCards.Property;

public class Auction {
	private Game game;
	private ArrayList<GamePlayer> participants;
	private int bid;
	private Property property;
	private GamePlayer topBidder;
	
	public Auction(Game game, Property property, GamePlayer host) {
		this.game = game;
		this.property = property;
		this.participants = new ArrayList<GamePlayer>(game.gameplayers);
		this.bid = 0;
		this.topBidder = host;

		game.broadcast("&7An auction for &e" + property.getName() + "&7 has begun! &e30 &7seconds remaining!");
		BukkitRunnable endAuction = new BukkitRunnable() {
			public void run() {
				endAuction();
			}
		};
		BukkitRunnable warn3 = new BukkitRunnable() {
			public void run() {
				game.broadcast("&e3 &7seconds remaining!");
				endAuction.runTaskLater(game.main, 60L);
				
			}
		};
		BukkitRunnable warn5 = new BukkitRunnable() {
			public void run() {
				game.broadcast("&e5 &7seconds remaining!");
				warn3.runTaskLater(game.main, 40L);
			}
		};
		BukkitRunnable warn10 = new BukkitRunnable() {
			public void run() {
				game.broadcast("&e10 &7seconds remaining!");
				warn5.runTaskLater(game.main, 100L);
			}
		};
		warn10.runTaskLater(game.main, 400L);
	}
	
	public void bid(GamePlayer player, int amount) {
		if (amount > bid) {
			bid = amount;
			topBidder = player;
		}
	}
	
	public void endAuction() {
		game.broadcast("&7The winner of the auction for &e" + property.getName() + "&7is &e" + topBidder + " with &a$" + bid + "&7!");
		property.setOwner(topBidder);
		property.onOwned(topBidder);
	}
}
