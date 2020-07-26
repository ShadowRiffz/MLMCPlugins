package me.neoblade298.neomonopoly.Objects;

import java.util.ArrayList;

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
		this.participants = new ArrayList<GamePlayer>(game.currentTurn);
		this.bid = 0;
		this.topBidder = host;
		this.host = host;

		game.broadcast("&7An auction for &e" + property.getName() + "&7 has begun! It ends when only 1 bidder remains! Drop out with &c/mono auction leave&7!");
	}
	
	public void bid(GamePlayer gp, int amount) {
		if (!participants.contains(gp)) {
			gp.message("&cYou already left the auction!");
		}
		else if (amount > gp.getMoney()) {
			gp.message("&cYou don't have enough money!");
		}
		else if (amount <= bid) {
			gp.message("&cYou must bid higher than &a$" + bid + "&c!");
		}
		else if (amount > bid) {
			bid = amount;
			topBidder = gp;
			game.broadcast("&e" + gp + " &7bid &a$" + bid + "&7!");
		}
	}
	
	public void endAuction() {
		game.broadcast("&7The winner of the auction for &e" + property.getColoredName() + " &7is &e" + topBidder + " with &a$" + bid + "&7!");
		game.takeMoney(bid, topBidder, "", true);
		property.setOwner(topBidder);
		topBidder.getProperties().add(property);
		property.onOwned(topBidder);
		game.requiredActions.get(host).remove(0);
		game.checkEndTurn(host);
		game.auction = null;
	}
	
	public void display(GamePlayer gp) {
		gp.message("&7Auctioning: " + property.getShorthand(gp));
		gp.message("&7Top bid: &a$" + bid + "&7 by &e" + topBidder);
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
			participants.remove(gp);
			if (participants.size() == 1) {
				endAuction();
			}
		}
	}

	public ArrayList<GamePlayer> getParticipants() {
		return participants;
	}

	public void setParticipants(ArrayList<GamePlayer> participants) {
		this.participants = participants;
	}

	public GamePlayer getTopBidder() {
		return topBidder;
	}

	public void setTopBidder(GamePlayer topBidder) {
		this.topBidder = topBidder;
	}

	public GamePlayer getHost() {
		return host;
	}

	public void setHost(GamePlayer host) {
		this.host = host;
	}
}
