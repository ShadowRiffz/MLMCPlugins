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
import me.neoblade298.neomonopoly.SpaceCards.Space;

public class Game {
	String name;
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
	private ArrayList<Space> board;
	public HashMap<ChatColor, ArrayList<BuildableProperty>> colors;
	public boolean isBusy;
	public boolean decidingOrder;
	public Monopoly main;
	
	public HashMap<GamePlayer, ArrayList<String>> requiredActions;
	public ArrayList<GamePlayer> currentTurn;

	public Game(String name, int money, ArrayList<Player> players, Monopoly main) throws Exception {
		this.name = name;
		this.numHotels = 32;
		this.numHotels = 12;
		this.main = main;
		this.isBusy = false;
		this.decidingOrder = true;
		
		// RNG cards and board
		main.loadBoard(board, this);
		unusedChest = new ArrayList<RNGCard>(main.communitychest);
		unusedChance = new ArrayList<RNGCard>(main.chance);
		usedChest = new ArrayList<RNGCard>();
		usedChance = new ArrayList<RNGCard>();
		Collections.shuffle(unusedChest);
		Collections.shuffle(unusedChance);
		orderDecider = new HashMap<GamePlayer, Integer>();
		colors = new HashMap<ChatColor, ArrayList<BuildableProperty>>(main.colors);

		// Initialize players
		this.gameplayers = new ArrayList<GamePlayer>();
		for (Player p : players) {
			GamePlayer gp = new GamePlayer(p, money, this);
			this.gameplayers.add(gp);
			this.players.put(p, gp);
			this.requiredActions.put(gp, new ArrayList<String>(Arrays.asList("ROLL_ORDER")));
			this.orderDecider.put(gp, 0);
		}
		currentTurn = new ArrayList<GamePlayer>(gameplayers);
		
		broadcast("The game has been started! Roll the dice to decide the player order!");
		startTurn(false);
	}
	
	public void handleDiceRoll(GamePlayer p, int dice, boolean isDoubles) {
		String action = requiredActions.get(p).get(0);
		
		switch (action) {
		case "ROLL_MOVE":
			if (isDoubles) {
				this.numDoubles++;
				if (numDoubles >= 3) {
					broadcast("&e" + p + " &crolled doubles 3 times in a row!");
					sendToJail(p);
					requiredActions.get(p).clear();
					return;
				}
				broadcast("&e" + p + " &7rolled doubles! They can roll again!");
				requiredActions.get(p).add(0, "ROLL_MOVE");
			}
			new BukkitRunnable() { public void run () {
				boolean passedGo = ((p.getPosition() + dice) / 40) > 0;
				p.move(dice);
				if (passedGo) giveMoney(200, p, "&e" + p + " &7passed go and received &a$200&7!");
				broadcast("&e" + p + " &7has landed on the space " + board.get(p.getPosition()).getShorthand(p));
				board.get(p.getPosition()).onLand(p, dice);
				isBusy = false;
			}}.runTaskLater(main, 40L);
			break;
		case "ROLL_PAY":
			break;
		case "ROLL_ORDER":
			orderDecider.put(p, dice);
			requiredActions.get(p).clear();
			endTurnOrder(p);
			break;
		}
	}
	
	public void endTurn(GamePlayer p) {
		if (!p.equals(currentTurn.get(0))) {
			p.message("&cIt's not your turn!");
			return;
		}
		if (requiredActions.get(p).size() != 0) {
			messageRequiredAction(p);
			return;
		}
		
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
				startTurn(true);
			}}.runTaskLater(main, 40L);
		}
		else {
			isBusy = false;
			startTurn(true);
		}
	}
	
	public void startTurn(boolean nextPlayer) {
		if (nextPlayer) currentTurn.add(currentTurn.remove(0));
		GamePlayer curr = currentTurn.get(0);
		broadcast("It is now &e" + curr + "'s &7turn! Roll the dice with &c/mono roll&7!");
		board.get(curr.getPosition()).onStart(curr);
	}

	public void broadcast(String msg) {
		for (GamePlayer gp : gameplayers) {
			String message = new String("&4[&c&lMLMC&4] &7" + msg).replaceAll("§", "&");
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
			gp.message("&cYou must first get enough money to pay off your bill of &e" + gp.getBills() + "&c!");
			break;
		case "IN_JAIL":
			gp.message("&cYou must first either roll doubles, pay &e$50 &cwith /mono payjail, or use a jail free with /mono jailfree!");
			break;
		case "UNOWNED_SPACE":
			gp.message("&cYou must first choose what to do with the space you landed on!");
			break;
		}
	}
	
	public void sendToJail(GamePlayer gp) {
		broadcast("&e" + gp + " &cis sent to jail!");
		requiredActions.get(gp).clear();
		gp.setJailed(true);
		gp.setPosition(10);
	}
	
	public void billPlayer(GamePlayer payer, int amt, GamePlayer paid) {
		if (payer.getMoney() < amt) {
			payer.setBills(amt);
			payer.setBilltaker(paid);
			requiredActions.get(payer).add(0, "PAY_BILLS");
			if (paid == null) {
				broadcast("&e" + payer + " &ccould not pay the bank &a$" + amt + "&c! They only have &a$" + payer.getMoney() + "&c. "
						+ "Destroy houses or mortgage properties to get money and type /mono paybills, or type /mono bankrupt to give up!");
			}
			else {
				broadcast("&e" + payer + " &ccould not pay &e" + paid + " &a$" + amt + "&c! They only have &a$" + payer.getMoney() + "&c. "
						+ "Destroy houses or mortgage properties to get money and type /mono paybills, or type /mono bankrupt to give up!");
			}
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
		}
	}
	
	public void giveMoney(int amt, GamePlayer p, String msg) {
		broadcast(msg + " &7They now have &a$" + p.getMoney() + "&7.");
	}
}
