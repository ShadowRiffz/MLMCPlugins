package me.neoblade298.neouno.Objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neouno.Uno;
import me.neoblade298.neouno.Cards.*;

public class Game {
	public String name;
	public ArrayList<GamePlayer> spectators;
	public ArrayList<GamePlayer> gameplayers;
	public ArrayList<Card> drawDeck;
	private ArrayList<Card> copyDeck;
	public Card topCard;
	public HashMap<Player, GamePlayer> players;
	private HashMap<GamePlayer, Integer> points;
	public Uno main;
	public GamePlayer curr;
	public ArrayList<GamePlayer> turns;
	private int pointsToWin;
	public int drawNum;
	public String requiredAction;
	public boolean isBusy;
	
	public Game(Uno main, String name, ArrayList<Player> players, int pointsToWin) {
		this.name = name;
		this.main = main;
		this.pointsToWin = pointsToWin;

		// Initialize data structures
		this.spectators = new ArrayList<GamePlayer>();
		this.gameplayers = new ArrayList<GamePlayer>();
		this.copyDeck = new ArrayList<Card>();
		this.drawDeck = new ArrayList<Card>();
		this.players = new HashMap<Player, GamePlayer>();
		this.points = new HashMap<GamePlayer, Integer>();
		this.turns = new ArrayList<GamePlayer>();
		this.requiredAction = null;
		this.drawNum = 0;
		this.isBusy = false;
		
		for (Player p : players) {
			GamePlayer gp = new GamePlayer(this, p);
			this.turns.add(gp);
			this.gameplayers.add(gp);
			this.players.put(p, gp);
			this.points.put(gp, 0);
		}

		// Set up copy deck
		ArrayList<ChatColor> colors = new ArrayList<ChatColor>();
		colors.add(ChatColor.DARK_GREEN);
		colors.add(ChatColor.RED);
		colors.add(ChatColor.BLUE);
		colors.add(ChatColor.YELLOW);
		
		// Initialize copy deck
		for (ChatColor color : colors) {
			for (int i = 0; i <= 9; i++) {
				copyDeck.add(new NumberCard(this, i, color));
				if (i != 0) {
					copyDeck.add(new NumberCard(this, i, color));
				}
			}
			for (int i = 0; i < 2; i++ ) {
				copyDeck.add(new SkipCard(this, color));
				copyDeck.add(new ReverseCard(this, color));
				copyDeck.add(new DrawTwoCard(this, color));
			}
		}
		
		for (int i = 0; i < 4; i++) {
			copyDeck.add(new Wildcard(this));
			copyDeck.add(new WildDrawFour(this));
		}
		
		onRoundStart();
	}
	
	public void drawCard(GamePlayer gp, int num) {
		if (drawDeck.size() < num || drawDeck.size() < drawNum) {
			drawDeck = new ArrayList<Card>(copyDeck);
			Collections.shuffle(drawDeck);
		}
		if (drawNum == 0 && num == 1) {
			gp.getCards().add(drawDeck.remove(0));
			broadcast("&f" + gp + "&7 draws a card.");
			turns.add(curr);
			curr = turns.remove(0);
			nextTurn();
		}
		else if (drawNum > 0 && num == 1) {
			for (int i = 0; i < drawNum; i++) {
				gp.getCards().add(drawDeck.remove(0));
			}
			broadcast("&f" + gp + "&7 draws &f" + drawNum + " &7cards.");
			drawNum = 0;
			turns.add(curr);
			curr = turns.remove(0);
			nextTurn();
		}
		// Only used when uno is challenged
		else if (num > 1) {
			for (int i = 0; i < num; i++) {
				gp.getCards().add(drawDeck.remove(0));
			}
			broadcast("&f" + gp + "&7 draws &f" + num + " &7cards.");
		}
	}
	
	private void onRoundStart() {
		Game game = this;
		broadcast("&7Starting new round...");
		new BukkitRunnable() { public void run() {
			drawDeck = new ArrayList<Card>(copyDeck);
			Collections.shuffle(drawDeck);
			for (GamePlayer gp : turns) {
				for (int i = 0; i < 7; i++) {
					gp.getCards().add(drawDeck.remove(0));
				}
			}
			
			Collections.shuffle(turns);
			String msg = "A round has started! The order is: ";
			for (int i = 0; i < turns.size(); i++) {
				GamePlayer gp = turns.get(i);
				msg += "&f" + gp;
				if (i != turns.size() - 1) {
					msg += "&7, ";
				}
			}
			curr = turns.remove(0);
			topCard = drawDeck.remove(0);
			broadcast(msg);
			showHands();
			game.isBusy = false;
		}}.runTaskLater(main, 20L);
	}
	
	private void onRoundEnd(GamePlayer winner) {
		this.isBusy = true;
		broadcast("&a&l" + winner + " won the round!");
		this.turns.add(this.curr);
		this.curr = null;
		Game game = this;

		if (game.pointsToWin > 0) {
			new BukkitRunnable() { public void run() {
				int points = 0;
				for (GamePlayer gp : game.gameplayers) {
					points += calculatePoints(gp.getCards());
					gp.getCards().clear();
				}
				game.points.put(winner, game.points.get(winner) + points);
				
				// Display points
				broadcast("Point scores:");
				for (GamePlayer gp : game.gameplayers) {
					broadcast("&f" + gp + "&7 - " + game.points.get(gp));
				}
			}}.runTaskLater(main, 20L);
		
			new BukkitRunnable() { public void run() {
				int winnerPoints = game.points.get(winner);
				if (winnerPoints > game.pointsToWin) {
					broadcast("&a&l" + winner + " won the game with " + winnerPoints + " points!");
					for (GamePlayer gp : game.gameplayers) {
						game.points.put(gp, 0);
					}
				}
			}}.runTaskLater(main, 60L);
		}
		else {
			onRoundStart();
		}
	}
	
	public void nextTurn() {
		this.isBusy = true;
		Game game = this;
		new BukkitRunnable() { public void run() {
			curr.setCalledUno(false);
			showHands();
			game.isBusy = false;
		}}.runTaskLater(main, 20L);
	}
	
	public void broadcast(String msg) {
		for (GamePlayer gp : gameplayers) {
			gp.message(msg);
		}
		for (GamePlayer gp : spectators) {
			gp.message(msg);
		}
	}
	
	public void showHands() {
		for (GamePlayer gp : gameplayers) {
			gp.showHand();
		}
	}
	
	public void playCard(GamePlayer gp, Card card) {
		int number = topCard.getNumber();
		ChatColor color = topCard.getColor();
		boolean topAdd = topCard instanceof WildDrawFour || topCard instanceof DrawTwoCard;
		boolean playAdd = card instanceof WildDrawFour || card instanceof DrawTwoCard;
		
		if (number != card.getNumber() && !color.equals(ChatColor.WHITE) &&
			!color.equals(card.getColor()) && !card.getColor().equals(ChatColor.WHITE)) {
			gp.message("&cThis card is not the same type or color!");
			return;
		}
		if (topAdd && !playAdd && drawNum > 0) {
			gp.message("&cYou must either use /uno draw or play a card that makes the next player draw more cards!");
			return;
		}
		
		gp.getCards().remove(card);
		card.onPlay();
		topCard = card;
		broadcast("&f" + gp + " &7played a " + card.getDisplay() + "&7 and has &f" + gp.getCards().size() + "&7 cards left!");
		if (drawNum > 0) { 
			broadcast("&7Next person to draw must draw &f" + drawNum + "&7 cards!");
		}
		if (gp.getCards().size() == 0) {
			onRoundEnd(gp);
			return;
		}
		if (card.getColor().equals(ChatColor.WHITE)) {
			broadcast("&f" + gp + " &7must choose the new color with &c/uno color [r/g/b/y]&7!");
		}
		if (requiredAction == null) {
			nextTurn();
		}
	}
	
	private int calculatePoints(ArrayList<Card> list) {
		int points = 0;
		for (Card card : list) {
			if (card.getNumber() >= 0 && card.getNumber() <= 9) {
				points += card.getNumber();
			}
			else {
				points += 10;
			}
		}
		return points;
	}
	
	public void forceEndGame() {
		broadcast("&4&lThe game was forcibly ended by an admin!");
		endGame();
	}
	
	public void playerLeave(boolean forced, GamePlayer gp) {
		if (forced) broadcast("&f" + gp + "&7 was kicked by an admin!");
		else broadcast("&f" + gp + " &7left the game!");
		
		main.ingame.remove(gp.getPlayer());
		gameplayers.remove(gp);
		players.remove(gp.getPlayer());
		if (turns.contains(gp)) {
			turns.remove(gp);
		}
		if (curr.equals(gp)) {
			nextTurn();
		}
		if (turns.size() <= 1) {
			endGame();
		}
	}
	
	public void endGame() {
		if (this.pointsToWin > 0) {
			broadcast("Point scores:");
			for (GamePlayer gp : gameplayers) {
				broadcast("&f" + gp + "&7 - " + points.get(gp));
				main.ingame.remove(gp.getPlayer());
			}
		}
		main.games.remove(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
