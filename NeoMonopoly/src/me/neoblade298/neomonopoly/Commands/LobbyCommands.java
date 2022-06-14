package me.neoblade298.neomonopoly.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neoblade298.neomonopoly.Monopoly;
import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
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
		sender.sendMessage("§4[§c§lMLMC§4] §7Successfully created lobby §e" + lobby.getName() + "§7!");
	}

	public void joinLobby(String name, Player sender) {
		if (!main.lobbies.containsKey(name)) {
			sender.sendMessage("§4[§c§lMLMC§4] §cThat lobby doesn't exist!");
			return;
		}

		Lobby lobby = main.lobbies.get(name);
		ArrayList<Player> invited = lobby.getInvited();
		if (invited.contains(sender)) {
			if (lobby.getPlayers().size() <= 7) {
				sender.sendMessage("§4[§c§lMLMC§4] §7Successfully joined lobby §e" + lobby.getName() + "§7!");
				lobby.broadcast("&e" + sender.getName() + " &7has joined the lobby!");
				setStartingMoney(lobby.getHost().getPlayer(), "" + (6000 / (lobby.getPlayers().size() + 1)));
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
			lobby.broadcast("&7Lobby disbanded by host!");
			for (Player p : lobby.getPlayers()) {
				main.inlobby.remove(p);
			}
			main.lobbies.remove(lobby.getName());
		}
		else {
			lobby.getPlayers().remove(sender);
			main.inlobby.remove(sender);
			sender.sendMessage("§4[§c§lMLMC§4] §7Successfully left lobby!");
			lobby.broadcast("&e" + sender.getName() + " &7has left the lobby!");
		}
	}

	public void kickPlayer(Player sender, String name) {
		Lobby lobby = main.inlobby.get(sender);
		Player toKick = Bukkit.getPlayer(name);
		if (toKick == null) {
			return;
		}
		if (lobby.getHost().equals(sender)) {
			lobby.getPlayers().remove(Bukkit.getPlayer(name));
			Bukkit.getPlayer(name).sendMessage("§7You were kicked from the lobby!");
			lobby.broadcast("&e" + Bukkit.getPlayer(name).getName() + "&7 has been kicked by the host!");
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cOnly hosts can kick from lobby!");
		}
	}

	public void invitePlayer(Player sender, String name) {
		Lobby lobby = main.inlobby.get(sender);
		Player invited = Bukkit.getPlayer(name);
		if (invited == null) {
			return;
		}
		if (lobby.getHost().equals(sender)) {
			lobby.getInvited().add(invited);
			lobby.broadcast("&7Successfully invited &e" + invited.getName() + "&7!");
			invited.sendMessage("§4[§c§lMLMC§4] §7You were invited to monopoly lobby §e" + lobby.getName() + "§7! Join with §c/mono join " + lobby.getName() + "§7.");
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
			lobby.broadcast("&7Successfully set starting money to &e" + amt + "!");
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
	
	public void spectateGame(Player sender, String name) {
		if (!main.ingame.containsKey(sender)) {
			if (main.games.containsKey(name)) {
				Game game = main.games.get(name);
				GamePlayer gp = new GamePlayer(sender, 0, game, '-');
				main.ingame.put(sender, game);
				gp.setPosition(-1);
				game.gameplayers.add(gp);
				gp.message("&7You're now spectating! Leave any time with &c/mono quit&7.");
				game.broadcast("&e" + gp + " &7is now spectating!");
			}
			else {
				sender.sendMessage("§4[§c§lMLMC§4] §cThat game doesn't exist");
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're already in a game!");
		}
	}
}
