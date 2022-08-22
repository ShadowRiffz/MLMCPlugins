package me.neoblade298.neouno.Objects;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Lobby {
	private ArrayList<String> players;
	private ArrayList<String> invited;
	private String host;
	private String name;
	private int pointsToWin;
	
	public Lobby(String pname, String name) {
		host = pname;
		players = new ArrayList<String>();
		players.add(pname);
		this.name = name;
		this.invited = new ArrayList<String>();
		this.pointsToWin = -1;
	}
	
	public String getPlayerList() {
		String msg = new String();
		for (int i = 0; i < players.size(); i++) {
			msg += "§e" + Bukkit.getPlayer(players.get(i)).getName();
			if (i != players.size() - 1) msg += "§7, ";
		}
		return msg;
	}
	
	public void broadcast(String msg) {
		for (String name : players) {
			Player p = Bukkit.getPlayer(name);
			String message = "&4[&c&lMLMC&4] &7" + msg;
			p.sendMessage(message.replaceAll("&", "§"));
		}
	}

	public ArrayList<String> getInvited() {
		return invited;
	}

	public void setInvited(ArrayList<String> invited) {
		this.invited = invited;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<String> players) {
		this.players = players;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setPointsToWin(int pointsToWin) {
		this.pointsToWin = pointsToWin;
	}

	public int getPointsToWin() {
		return pointsToWin;
	}
}
