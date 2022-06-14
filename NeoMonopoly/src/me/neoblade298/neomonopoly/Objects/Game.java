package me.neoblade298.neomonopoly.Objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neomonopoly.Monopoly;
import me.neoblade298.neomonopoly.RNGCards.RNGCard;
import me.neoblade298.neomonopoly.SpaceCards.BuildableProperty;
import me.neoblade298.neomonopoly.SpaceCards.Property;
import me.neoblade298.neomonopoly.SpaceCards.Space;
import me.neoblade298.neomonopoly.SpaceCards.Utility;

public class Game {
	private String name;
	public ArrayList<GamePlayer> gameplayers;
	public HashMap<Player, GamePlayer> players;
	private HashMap<GamePlayer, Integer> orderDecider;
	private int numHouses;
	private int numHotels;
	private int numDoubles;
	private ArrayList<RNGCard> unusedChest;
	private ArrayList<RNGCard> usedChest;
	private ArrayList<RNGCard> unusedChance;
	private ArrayList<RNGCard> usedChance;
	public ArrayList<Space> board;
	public HashMap<ChatColor, ArrayList<BuildableProperty>> colors;
	public boolean isBusy;
	public boolean decidingOrder;
	public Monopoly main;
	public Auction auction;
	public Trade trade;
	
	public HashMap<GamePlayer, ArrayList<String>> requiredActions;
	public ArrayList<GamePlayer> currentTurn;

	public Game(String name, int money, ArrayList<Player> players, Monopoly main) throws Exception {
		this.name = name;
		this.numHouses = 32;
		this.numHotels = 12;
		this.main = main;
		this.isBusy = false;
		this.decidingOrder = true;
		
		// Data structure initialization
		board = new ArrayList<Space>();
		unusedChest = new ArrayList<RNGCard>();
		unusedChance = new ArrayList<RNGCard>();
		usedChest = new ArrayList<RNGCard>();
		usedChance = new ArrayList<RNGCard>();
		orderDecider = new HashMap<GamePlayer, Integer>();
		colors = new HashMap<ChatColor, ArrayList<BuildableProperty>>();
		requiredActions = new HashMap<GamePlayer, ArrayList<String>>();
		currentTurn = new ArrayList<GamePlayer>();
		
		// Load in data
		main.loadBoard(board, this, colors);
		main.loadRNGCards(unusedChest, this, "communitychest");
		main.loadRNGCards(unusedChance, this, "chance");
		Collections.shuffle(unusedChest);
		Collections.shuffle(unusedChance);

		// Initialize players
		this.gameplayers = new ArrayList<GamePlayer>();
		this.players = new HashMap<Player, GamePlayer>();
		char mapChar = 'a';
		for (Player p : players) {
			GamePlayer gp = new GamePlayer(p, money, this, mapChar);
			this.gameplayers.add(gp);
			this.players.put(p, gp);
			this.requiredActions.put(gp, new ArrayList<String>(Arrays.asList("ROLL_ORDER")));
			mapChar++;
		}
		currentTurn = new ArrayList<GamePlayer>(gameplayers);
		
		broadcast("The game has been started! Roll the dice to decide the player order!");
		startTurn(false);
	}
	
	public void handleDiceRoll(GamePlayer p, int dice, boolean isDoubles) {
		String action = requiredActions.get(p).get(0);
		
		switch (action) {
		case "ROLL_MOVE":
			requiredActions.get(p).remove(0);
			if (isDoubles) {
				this.numDoubles++;
				if (numDoubles >= 3) {
					broadcast("&e" + p + " &crolled doubles 3 times in a row!");
					sendToJail(p);
					requiredActions.get(p).clear();
					return;
				}
				broadcast("&e" + p + " &7rolled doubles! They can roll again!");
				requiredActions.get(p).add("ROLL_MOVE");
			}
			movePlayer(p, dice, true, true);
			break;
		case "ROLL_PAY":
			requiredActions.get(p).remove(0);
			Utility space = (Utility) board.get(p.getPosition());
			billPlayer(p, 10 * dice, space.getOwner());
			break;
		case "ROLL_ORDER":
			orderDecider.put(p, dice);
			requiredActions.get(p).clear();
			endTurnOrder(p);
			break;
		case "JAIL_ACTION":
			requiredActions.get(p).remove(0);
			if (isDoubles) {
				broadcast("&e" + p + " &7rolled doubles! They can now leave jail!");
				p.setJailed(false);
				p.resetJailTime();
				movePlayer(p, dice, true, true);
				return;
			}
			broadcast("&e" + p + " &7failed to roll doubles!");
			p.addJailTime();
			if (p.getJailTime() >= 3) {
				broadcast("&e" + p + " &7must pay 2x the fine to leave jail!");
				billPlayer(p, 100, null);
				p.setJailed(false);
				p.resetJailTime();
			}
			this.isBusy = false;
			break;
		}
	}
	
	public void endTurn(GamePlayer p) {
		broadcast("&e" + p + " &7ends their turn!");
		startTurn(true);
	}
	
	public void endTurnOrder(GamePlayer p) {
		broadcast("&e" + p + " &7ends their turn!");
		
		if (orderDecider.size() == gameplayers.size()) {
			ArrayList<GamePlayer> playerOrder = new ArrayList<GamePlayer>();
			ArrayList<Integer> playerNumbers = new ArrayList<Integer>();
			while (!orderDecider.isEmpty()) {
				int max = 0;
				GamePlayer maxPlayer = null;
				for (Entry<GamePlayer, Integer> entry : orderDecider.entrySet()) {
					if (entry.getValue() > max) {
						max = entry.getValue();
						maxPlayer = entry.getKey();
					}
				}
				playerOrder.add(maxPlayer);
				playerNumbers.add(max);
				orderDecider.remove(maxPlayer);
			}
			currentTurn = new ArrayList<GamePlayer>(playerOrder);
			for (int i = 0; i < playerOrder.size(); i++) {
				broadcast("&f" + (i+1) + ". &e" + playerOrder.get(i) + " &7- &a" + playerNumbers.get(i));
			}
			broadcast("&e" + playerOrder.get(0) + " &7starts first!");
			Game game = this;
			new BukkitRunnable() { public void run() {
				game.isBusy = false;
				startTurn(false);
			}}.runTaskLater(main, 40L);
		}
		else {
			isBusy = false;
			startTurn(true);
		}
	}
	
	public void startTurn(boolean nextPlayer) {
		this.numDoubles = 0;
		if (nextPlayer) {
			currentTurn.add(currentTurn.remove(0));
		}
		GamePlayer curr = currentTurn.get(0);
		String msg = "It is now &e" + curr + "'s &7turn! ";
		board.get(curr.getPosition()).onStart(curr);
		String action = requiredActions.get(curr).get(0);
		if (action.equalsIgnoreCase("JAIL_ACTION")) {
			msg += "&7They're jailed and must either type &c/mono payjail&7 to pay &a$50&7, &c/mono jailfree&7 to use a get out of "
					+ "jail free card, or &c/mono roll &7to try to roll doubles to get out of jail.";
		}
		else if (action.equalsIgnoreCase("ROLL_MOVE")) {
			msg += "&7Type &c/mono roll &7to roll the dice!";
		}
		else if (action.equalsIgnoreCase("ROLL_ORDER")) {
			msg += "&7Type &c/mono roll &7to roll the dice!";
		}
		broadcast(msg);
	}

	public void broadcast(String msg) {
		for (GamePlayer gp : gameplayers) {
			String message = new String("&4[&c&lMLMC&4] &7" + msg).replaceAll("&", "§");
			gp.getPlayer().sendMessage(message);
		}
	}
	
	public void messageRequiredAction(GamePlayer gp) {
		String action = requiredActions.get(gp).get(0);
		switch (action) {
		case "ROLL_MOVE":
			gp.message("&cYou must first roll to move your piece!");
			break;
		case "ROLL_PAY":
			gp.message("&cYou must first roll to see how much you'll be paying!");
			break;
		case "ROLL_ORDER":
			gp.message("&cYou must first roll to get the order players will be playing!");
			break;
		case "PAY_BILLS":
			gp.message("&cYou must first get enough money to pay off your bill of &e" + gp.getBills() + "&c! Use /mono paybills!");
			break;
		case "JAIL_ACTION":
			gp.message("&cYou must first either roll doubles, pay &e$50 &cwith /mono payjail, or use a jail free with /mono jailfree!");
			break;
		case "UNOWNED_SPACE":
			gp.message("&cYou must first choose what to do with the space you landed on!");
			break;
		case "WAIT_PLAYERBILLS":
			gp.message("&cSome players owe you money that must first be paid!");
			break;
		}
	}
	
	public void movePlayer(GamePlayer p, int spaces, boolean passGo, boolean normalLand) {
		Game game = this;
		boolean passedGo = p.getPosition() + spaces >= 40;
		p.move(spaces);
		if (passedGo) giveMoney(200, p, "&e" + p + " &7passed go and received &a+$200&7!", true);
		broadcast("&e" + p + " &7has landed on " + board.get(p.getPosition()).getShorthand(p));
		if (normalLand) {
			new BukkitRunnable() { public void run() {
				board.get(p.getPosition()).onLand(p, spaces);
				game.checkEndTurn(p);
			}}.runTaskLater(main, 20L);
		}
	}
	
	public void movePlayerAbsolute(GamePlayer p, int position, boolean passGo, boolean normalLand) {
		Game game = this;
		boolean passedGo = p.getPosition() >= position;
		p.moveAbsolute(position);
		if (passedGo) giveMoney(200, p, "&e" + p + " &7passed go and received &a+$200&7!", true);
		broadcast("&e" + p + " &7has landed on " + board.get(p.getPosition()).getShorthand(p));
		if (normalLand) {
			new BukkitRunnable() { public void run() {
				board.get(p.getPosition()).onLand(p, 0);
				game.checkEndTurn(p);
			}}.runTaskLater(main, 20L);
		}
	}
	
	public void sendToJail(GamePlayer gp) {
		broadcast("&e" + gp + " &cis sent to jail!");
		requiredActions.get(gp).clear();
		endTurn(gp);
		gp.setJailed(true);
		gp.setPosition(10);
		isBusy = false;
	}
	
	public void payBill(GamePlayer payer) {
		if (payer.getBills() > payer.getMoney()) {
			payer.message("&cYou do not have enough money to pay your bills! You need &a$" + payer.getBills() + " &cbut only have &a$" + payer.getMoney() + "&c.");
			return;
		}
		
		int amt = payer.getBills();
		payer.setMoney(payer.getMoney() - payer.getBills());
		GamePlayer paid = payer.getBilltaker();
		if (paid == null) {
			broadcast("&e" + payer + " &7paid the bank &a$" + amt + "&7! They now have &a$" + payer.getMoney() + "&7.");
		}
		else {
			paid.setMoney(paid.getMoney() + amt);
			broadcast("&e" + payer + " &7paid &e" + paid + " &a$" + amt + "&7! They now have &a$" + payer.getMoney() + "&7.");
		}
		requiredActions.get(payer).remove("PAY_BILLS");
		
		
		// Check for edge case where everyone owes the current player money due to the chance card
		ArrayList<String> currentActions = requiredActions.get(currentTurn.get(0));
		boolean allPaid = true;
		if (currentActions.size() > 0 && currentActions.get(0).equals("WAIT_PLAYERBILLS")) {
			for (GamePlayer p : requiredActions.keySet()) {
				if (!p.equals(currentTurn.get(0))) {
					if (requiredActions.get(p).size() != 0) allPaid = false;
				}
			}
			if (allPaid) currentActions.remove("WAIT_PLAYERBILLS");
		}
	}
	
	public boolean billPlayer(GamePlayer payer, int amt, GamePlayer paid) {
		if (payer.getMoney() < amt) {
			int remaining = amt - payer.getMoney();
			payer.setMoney(0);
			payer.setBills(remaining);
			payer.setBilltaker(paid);
			requiredActions.get(payer).add(0, "PAY_BILLS");
			if (paid == null) {
				broadcast("&e" + payer + " &ccould not pay the bank &a$" + amt + "&c! They need &a$" + remaining + "&c more. "
						+ "Destroy houses or mortgage properties to get money and type /mono paybills, or type /mono bankrupt to give up!");
			}
			else {
				paid.setMoney(paid.getMoney() + amt - remaining);
				broadcast("&e" + payer + " &ccould not pay &e" + paid + " &a$" + amt + "&c! They need &a$" + remaining + "&c more. "
						+ "Destroy houses or mortgage properties to get money and type /mono paybills, or type /mono bankrupt to give up!");
			}
			return false;
		}
		else {
			payer.setMoney(payer.getMoney() - amt);
			if (paid == null) {
				broadcast("&e" + payer + " &7paid the bank &a$" + amt + "&7! They now have &a$" + payer.getMoney() + "&7.");
			}
			else {
				paid.setMoney(paid.getMoney() + amt);
				broadcast("&e" + payer + " &7paid &e" + paid + " &a$" + amt + "&7! They now have &a$" + payer.getMoney() + "&7.");
			}
			return true;
		}
	}
	
	public void drawChance(GamePlayer gp) {
		RNGCard card = unusedChance.remove(0);
		card.onDraw(gp, "Chance");
		if (unusedChance.size() == 0) {
			unusedChance = new ArrayList<RNGCard>(usedChance);
			Collections.shuffle(unusedChance);
			usedChance.clear();
		}
		else {
			usedChance.add(card);
		}
	}
	
	public void drawChest(GamePlayer gp) {
		RNGCard card = unusedChest.remove(0);
		card.onDraw(gp, "Community Chest");
		if (unusedChest.size() == 0) {
			unusedChest = new ArrayList<RNGCard>(usedChest);
			Collections.shuffle(unusedChest);
			usedChest.clear();
		}
		else {
			usedChest.add(card);
		}
	}
	
	public void giveMoney(int amt, GamePlayer p, String msg, boolean showExtra) {
		p.setMoney(p.getMoney() + amt);
		if (showExtra) broadcast(msg + " &7They now have &a$" + p.getMoney() + "&7.");
		else broadcast(msg);
	}
	
	public void takeMoney(int amt, GamePlayer p, String msg, boolean showExtra) {
		p.setMoney(p.getMoney() - amt);
		if (!msg.equalsIgnoreCase("") && showExtra) msg += " ";
		if (showExtra) broadcast(msg + "&7They now have &a$" + p.getMoney() + "&7.");
		else broadcast(msg);
	}
	
	public void checkEndTurn(GamePlayer gp) {
		if (requiredActions.get(gp).size() == 0) {
			gp.message("&cNo actions remaining! Manage properties or end your turn with /mono end!");
		}
	}
	
	public int getHouses() {
		 return numHouses;
	}
	
	public int getHotels() {
		return numHotels;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addHouses(int num) {
		numHouses += num;
	}
	
	public void addHotels(int num) {
		numHotels += num;
	}
	
	public void buyProperty(GamePlayer gp, Property prop) {
		prop.setOwner(gp);
		takeMoney(prop.getPrice(), gp, "&e" + gp + " &7has purchased " + prop.getShorthand(gp) + "&7 for &c-$" + prop.getPrice() + "&7.", true);
		prop.onOwned(gp);
		checkEndTurn(gp);
	}
	
	public void onBankrupt() {
		if (currentTurn.size() == 1) {
			GamePlayer winner = currentTurn.get(0);
			broadcast("&a&l" + winner + " is the winner! They have $" + winner.getMoney() + "!");
			for (GamePlayer gp : gameplayers) {
				main.ingame.remove(gp.getPlayer());
			}
			main.games.remove(this.name);
		}
	}
	
	public void startAuction(GamePlayer gp, Property prop) {
		this.auction = new Auction(this, prop, gp);
	}
	
	public void startTrade(GamePlayer trader, GamePlayer tradee) {
		this.trade = new Trade(this, trader, tradee);
		broadcast("A trade has begun between &e" + trader + " &7and &e" + tradee + "&7! &c/mono trade view&7!");
	}
	
	public void forceEndGame() {
		int topMoney = 0;
		GamePlayer topPlayer = null;
		for (GamePlayer gp : gameplayers) {
			if (gp.getMoney() > topMoney) {
				topPlayer = gp;
				topMoney = gp.getMoney();
			}
		}
		broadcast("&7The game has been ended by force by an admin! The default winner is &e" + topPlayer + " &7with &a$" + topMoney + "&7!");
		for (GamePlayer gp : gameplayers) {
			main.ingame.remove(gp.getPlayer());
		}
		main.games.remove(this.name);
	}
}
