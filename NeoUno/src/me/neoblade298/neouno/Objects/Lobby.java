package me.neoblade298.neouno.Objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Lobby {
	private ArrayList<UUID> players;
	private ArrayList<UUID> invited;
	private UUID host;
	private String name;
	private int pointsToWin;
	
	public Lobby(UUID uuid, String name) {
		host = uuid;
		players = new ArrayList<UUID>();
		players.add(uuid);
		this.name = name;
		this.invited = new ArrayList<UUID>();
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
		for (UUID uuid : players) {
			Player p = Bukkit.getPlayer(uuid);
			String message = "&4[&c&lMLMC&4] &7" + msg;
			p.sendMessage(message.replaceAll("&", "§"));
		}
	}

	public ArrayList<UUID> getInvited() {
		return invited;
	}

	public void setInvited(ArrayList<UUID> invited) {
		this.invited = invited;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<UUID> getPlayers() {
		return players;
	}

	public void setPlayers(ArrayList<UUID> players) {
		this.players = players;
	}

	public UUID getHost() {
		return host;
	}

	public void setHost(UUID host) {
		this.host = host;
	}

	public void setPointsToWin(int pointsToWin) {
		this.pointsToWin = pointsToWin;
	}

	public int getPointsToWin() {
		return pointsToWin;
	}
}
