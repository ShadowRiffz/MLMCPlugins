package me.neoblade298.neouno.Objects;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.neoblade298.neouno.Cards.Card;

public class GamePlayer {

	private Game game;
	private ArrayList<Card> cards;
	private int points;
	private Player player;
	private boolean calledUno;

	public GamePlayer(Game game, Player player) {
		this.game = game;
		this.cards = new ArrayList<Card>();
		this.player = player;
		this.points = 0;
		this.calledUno = false;
	}

	public void message(String msg) {
		String message = new String("&4[&c&lMLMC&4] &7" + msg).replaceAll("&", "§");
		player.sendMessage(message);
	}
	
	public void showHand() {
		if (game.curr.equals(this)) {
			message("Your turn! Top card is: " + game.topCard.getDisplay());
		}
		else {
			message("&e" + game.curr + "'s &7turn! Top card is: " + game.topCard.getDisplay());
		}
		String msg = new String("&7Hand: ");
		for (Card card : this.cards) {
			msg += card.getColor() + "" + card.getNumber() + " ";
		}
		message(msg);
	}

	public boolean equals(GamePlayer gp) {
		return this.player.equals(gp.getPlayer());
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public String toString() {
		return player.getName();
	}

	public boolean calledUno() {
		return calledUno;
	}

	public void setCalledUno(boolean calledUno) {
		this.calledUno = calledUno;
	}
}
