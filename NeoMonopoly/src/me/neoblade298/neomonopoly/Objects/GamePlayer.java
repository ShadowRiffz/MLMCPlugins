package me.neoblade298.neomonopoly.Objects;

import java.util.ArrayList;

import org.bukkit.entity.Player;

import me.neoblade298.neomonopoly.SpaceCards.Property;

public class GamePlayer {

	private int numJailFree;
	private int numRailroads;
	private int numUtilities;
	private int money;
	private int position;
	private Player player;
	private int bills;
	private GamePlayer billtaker;
	private boolean isBankrupt;
	private Game game;
	private int jailTime;
	private boolean isJailed;
	private ArrayList<Property> properties;
	public char mapChar;
	private String name;

	public GamePlayer(Player player, int money, Game game, char mapChar) {
		this.numRailroads = 0;
		this.numUtilities = 0;
		this.numJailFree = 0;
		this.money = money;
		this.position = 0;
		this.player = player;
		this.bills = 0;
		this.isBankrupt = false;
		this.game = game;
		this.jailTime = 0;
		this.isJailed = false;
		this.properties = new ArrayList<Property>();
		this.mapChar = mapChar;
		this.name = player.getName();
	}
	
	
	public int getNumUtilities() {
		return numUtilities;
	}

	public void setNumUtilities(int numUtilities) {
		this.numUtilities = numUtilities;
	}

	public boolean isBankrupt() {
		return isBankrupt;
	}

	public void setBankrupt(boolean isBankrupt) {
		this.isBankrupt = isBankrupt;
	}

	public GamePlayer getBilltaker() {
		return billtaker;
	}

	public void setBilltaker(GamePlayer billtaker) {
		this.billtaker = billtaker;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}

	public int getBills() {
		return bills;
	}

	public void setBills(int bills) {
		this.bills = bills;
	}

	public String toString() {
		return player.getName();
	}

	public void move(int spaces) {
		this.position = (this.position += spaces) % 40;
	}

	public void moveAbsolute(int space) {
		this.position = space;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getNumJailFree() {
		return numJailFree;
	}

	public void setNumJailFree(int numJailFree) {
		this.numJailFree = numJailFree;
	}

	public int getNumRailroads() {
		return numRailroads;
	}

	public void setNumRailroads(int numRailroads) {
		this.numRailroads = numRailroads;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	public void setProperties(ArrayList<Property> properties) {
		this.properties = properties;
	}


	public boolean isJailed() {
		return isJailed;
	}
	
	public int getJailTime() {
		return jailTime;
	}
	
	public void resetJailTime() {
		jailTime = 0;
	}
	
	public void addJailTime() {
		jailTime++;
	}

	public void setJailed(boolean isJailed) {
		this.isJailed = isJailed;
	}

	public void message(String msg) {
		String message = new String("&4[&c&lMLMC&4] &7" + msg).replaceAll("&", "§");
		player.sendMessage(message);
	}

	public boolean equals(GamePlayer gp) {
		return this.player.equals(gp.getPlayer());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
