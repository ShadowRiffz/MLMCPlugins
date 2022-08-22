package me.neoblade298.neomonopoly.Objects;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class Lobby {
	private ArrayList<Player> players;
	private ArrayList<Player> invited;
	private Player host;
	private String name;
	private int money;
	
	public Lobby(Player p, String name) {
		host = p;
		players = new ArrayList<Player>();
		players.add(p);
		this.name = name;
		this.invited = new ArrayList<Player>();
		this.money = 6000;
	}
	
	public String getPlayerList() {
		String msg = new String();
		for (int i = 0; i < players.size(); i++) {
			msg += "§e" + players.get(i).getName();
			if (i != players.size() - 1) msg += "§7, ";
		}
		return msg;
	}
	
	public void broadcast(String msg) {
		for (Player p : players) {
			String message = "&4[&c&lMLMC&4] &7" + msg;
			p.sendMessage(message.replaceAll("&", "§"));
		}
	}

	public ArrayList<Player> getInvited() {
		return invited;
	}

	public void setInvited(ArrayList<Player> invited) {
		this.invited = invited;
	}

	public int getStartingMoney() {
		return money;
	}

	public void setStartingMoney(int money) {
		this.money = money;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<Player> players) {
		this.players = players;
	}

	public Player getHost() {
		return host;
	}

	public void setHost(Player host) {
		this.host = host;
	}
}
