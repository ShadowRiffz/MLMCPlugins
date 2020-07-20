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
	private GamePlayer host;
	
	public Auction(Game game, Property property, GamePlayer host) {
		this.game = game;
		this.property = property;
		this.participants = new ArrayList<GamePlayer>(game.gameplayers);
		this.bid = 0;
		this.topBidder = host;
		this.host = host;

		game.broadcast("&7An auction for &e" + property.getName() + "&7 has begun! &e30 &7seconds remaining!");
		BukkitRunnable endAuction = new BukkitRunnable() {
			public void run() {
				if (game.auction != null) {
					endAuction();
				}
			}
		};
		BukkitRunnable warn3 = new BukkitRunnable() {
			public void run() {
				if (game.auction != null) {
					game.broadcast("&e3 &7seconds remaining!");
					endAuction.runTaskLater(game.main, 60L);
				}
			}
		};
		BukkitRunnable warn5 = new BukkitRunnable() {
			public void run() {
				if (game.auction != null) {
					game.broadcast("&e5 &7seconds remaining!");
					warn3.runTaskLater(game.main, 40L);
				}
			}
		};
		BukkitRunnable warn10 = new BukkitRunnable() {
			public void run() {
				if (game.auction != null) {
					game.broadcast("&e10 &7seconds remaining!");
					warn5.runTaskLater(game.main, 100L);
				}
			}
		};
		warn10.runTaskLater(game.main, 400L);
	}
	
	public void bid(GamePlayer gp, int amount) {
		if (!participants.contains(gp)) {
			gp.message("&cYou already left the auction!");
		}
		else if (amount > bid) {
			bid = amount;
			topBidder = gp;
			game.broadcast("&e" + gp + " &7bid &a$" + bid + " &7!");
		}
		else if (amount <= bid) {
			gp.message("&cYou must bid higher than &a$" + bid + "&c!");
		}
	}
	
	public void endAuction() {
		game.broadcast("&7The winner of the auction for &e" + property.getName() + " &7is &e" + topBidder + " with &a$" + bid + "&7!");
		property.setOwner(topBidder);
		topBidder.getProperties().add(property);
		property.onOwned(topBidder);
		game.requiredActions.get(host).remove(0);
		game.checkEndTurn(host);
	}
	
	public void display(GamePlayer gp) {
		gp.message("&7Auctioning: " + property.getShorthand(gp));
		gp.message("&7Top bid: &a$" + bid + " &7 by &e" + topBidder);
	}
	
	public void dropPlayer(GamePlayer gp) {
		if (topBidder.equals(gp)) {
			gp.message("&cYou can't leave when you're the top bidder!");
		}
		else if (!participants.contains(gp)) {
			gp.message("&cYou already left the auction!");
		}
		else {
			game.broadcast("&e" + gp + " &7has left the auction!");
			if (participants.size() == 1) {
				endAuction();
			}
		}
	}
}
