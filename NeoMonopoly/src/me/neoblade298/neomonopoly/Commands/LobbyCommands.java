package me.neoblade298.neomonopoly.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neoblade298.neomonopoly.Monopoly;
import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.Lobby;

public class LobbyCommands {
	Monopoly main;

	public LobbyCommands(Monopoly main) {
		this.main = main;
	}

	public void createLobby(String name, Player sender) {

		// Check if the name exists already, or player is already in a game
		if (main.inlobby.containsKey(sender)) {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're already in a lobby!");
			return;
		}
		else if (main.ingame.containsKey(sender)) {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're already in a game!");
			return;
		}
		else if (main.lobbies.containsKey(name) || main.games.containsKey(name)) {
			sender.sendMessage("§4[§c§lMLMC§4] §cThat game name is taken!");
			return;
		}

		Lobby lobby = new Lobby(sender, name);
		main.inlobby.put(sender, lobby);
		main.lobbies.put(name, lobby);
		sender.sendMessage("§4[§c§lMLMC§4] §7Successfully created lobby &e" + lobby.getName() + "&7!");
	}

	public void joinLobby(String name, Player sender) {
		if (!main.lobbies.containsKey(name)) {
			sender.sendMessage("§4[§c§lMLMC§4] §cThat lobby doesn't exist!");
			return;
		}

		Lobby lobby = main.lobbies.get(name);
		ArrayList<Player> invited = lobby.getInvited();
		if (invited.contains(sender)) {
			if (lobby.getPlayers().size() <= 3) {
				sender.sendMessage("§4[§c§lMLMC§4] §7Successfully joined lobby &e" + lobby.getName() + "&7!");
				lobby.getPlayers().add(sender);
				lobby.getInvited().remove(sender);
				main.inlobby.put(sender, lobby);
			}
			else {
				sender.sendMessage("§4[§c§lMLMC§4] §cThat lobby is full!");
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou aren't invited to that lobby!");
		}
	}

	public void leaveLobby(Player sender) {
		Lobby lobby = main.inlobby.get(sender);
		if (lobby.getHost().equals(sender)) {
			for (Player p : lobby.getPlayers()) {
				main.inlobby.remove(p);
			}
			main.lobbies.remove(lobby.getName());
			sender.sendMessage("§4[§c§lMLMC§4] §7Lobby disbanded!");
		}
		else {
			lobby.getPlayers().remove(sender);
			main.inlobby.remove(sender);
			sender.sendMessage("§4[§c§lMLMC§4] §7Successfully left lobby!");
		}
	}

	public void kickPlayer(Player sender, String name) {
		Lobby lobby = main.inlobby.get(sender);
		if (lobby.getHost().equals(sender)) {
			lobby.getPlayers().remove(Bukkit.getPlayer(name));
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cOnly hosts can kick from lobby!");
		}
	}

	public void invitePlayer(Player sender, String name) {
		Lobby lobby = main.inlobby.get(sender);
		if (lobby.getHost().equals(sender)) {
			lobby.getInvited().add(Bukkit.getPlayer(name));
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cOnly hosts can invite to lobby!");
		}
	}

	public void setStartingMoney(Player sender, String amt) {
		Lobby lobby = main.inlobby.get(sender);
		int amount = 0;
		try {
			amount = Integer.parseInt(amt);
		} catch (NumberFormatException e) {
			sender.sendMessage("§4[§c§lMLMC§4] §cInvalid number format!");
			return;
		}
		
		if (amount <= 500 || amount >= 100000) {
			sender.sendMessage("§4[§c§lMLMC§4] §cAmount must be between 500 and 100000!");
			return;
		}

		if (lobby.getHost().equals(sender)) {
			main.inlobby.get(sender).setStartingMoney(amount);
			sender.sendMessage("§4[§c§lMLMC§4] §7Successfully set starting money to §e" + amt + "!");
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cOnly hosts can change starting money!");
		}
	}

	public void startGame(Player sender) {
		Lobby lobby = main.inlobby.get(sender);
		if (lobby.getPlayers().size() >= 2) {
			if (lobby.getHost().equals(sender)) {
				try {
					Game game = new Game(lobby.getName(), lobby.getStartingMoney(), lobby.getPlayers(), main);
					main.games.put(lobby.getName(), game);
					for (Player p : lobby.getPlayers()) {
						main.inlobby.remove(p);
						main.ingame.put(p, game);
					}
					main.lobbies.remove(lobby.getName());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else {
				sender.sendMessage("§4[§c§lMLMC§4] §cOnly hosts can start the game!");
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cThere must be at least 2 players to start!");
		}
	}
}
