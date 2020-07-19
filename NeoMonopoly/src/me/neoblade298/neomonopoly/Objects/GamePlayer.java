package me.neoblade298.neomonopoly.Objects;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.neoblade298.neomonopoly.SpaceCards.BuildableProperty;
import me.neoblade298.neomonopoly.SpaceCards.Property;
import me.neoblade298.neomonopoly.SpaceCards.Railroad;
import me.neoblade298.neomonopoly.SpaceCards.Utility;

public class GamePlayer {
	public int getNumUtilities() {
		return numUtilities;
	}

	public void setNumUtilities(int numUtilities) {
		this.numUtilities = numUtilities;
	}

	private int numJailFree;
	private int numRailroads;
	private int numUtilities;
	private int money;
	private int position;
	private Player player;
	private int bills;
	private GamePlayer billtaker;
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

	private Game game;
	
	public int getBills() {
		return bills;
	}

	public void setBills(int bills) {
		this.bills = bills;
	}

	public GamePlayer(Player player, int money, Game game) {
		this.money = money;
		this.position = 0;
		this.numRailroads = 0;
		this.numUtilities = 0;
		this.numJailFree = 0;
		this.bills = 0;
		this.player = player;
		this.game = game;
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

	private boolean isJailed;
	private ArrayList<Property> properties;

	public boolean isJailed() {
		return isJailed;
	}

	public void setJailed(boolean isJailed) {
		this.isJailed = isJailed;
	}
	
	public void message(String msg) {
		String message = new String("&4[&c&lMLMC&4] &7" + msg).replaceAll("§", "&");
		player.sendMessage(message);
	}
	
	public boolean equals(GamePlayer gp) {
		return this.player.equals(gp.getPlayer());
	}
}
