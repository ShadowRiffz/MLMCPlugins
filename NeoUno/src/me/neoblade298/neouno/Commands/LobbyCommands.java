package me.neoblade298.neouno.Commands;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.neoblade298.neouno.Uno;
import me.neoblade298.neouno.Objects.Game;
import me.neoblade298.neouno.Objects.GamePlayer;
import me.neoblade298.neouno.Objects.Lobby;

public class LobbyCommands {
	Uno main;

	public LobbyCommands(Uno main) {
		this.main = main;
	}

	public void createLobby(String name, Player sender) {

		String pname = sender.getName();
		// Check if the name exists already, or player is already in a game
		if (main.inlobby.containsKey(pname)) {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're already in a lobby!");
			return;
		}
		else if (main.ingame.containsKey(pname)) {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're already in a game!");
			return;
		}
		else if (main.lobbies.containsKey(name) || main.games.containsKey(name)) {
			sender.sendMessage("§4[§c§lMLMC§4] §cThat game name is taken!");
			return;
		}

		Lobby lobby = new Lobby(pname, name);
		main.inlobby.put(sender.getName(), lobby);
		main.lobbies.put(name, lobby);
		sender.sendMessage("§4[§c§lMLMC§4] §7Successfully created lobby §e" + lobby.getName() + "§7!");
	}

	public void joinLobby(String name, Player sender) {
		if (!main.lobbies.containsKey(name)) {
			sender.sendMessage("§4[§c§lMLMC§4] §cThat lobby doesn't exist!");
			return;
		}

		String pname = sender.getName();
		Lobby lobby = main.lobbies.get(name);
		ArrayList<String> invited = lobby.getInvited();
		if (invited.contains(pname)) {
			if (lobby.getPlayers().size() <= 7) {
				sender.sendMessage("§4[§c§lMLMC§4] §7Successfully joined lobby §e" + lobby.getName() + "§7!");
				lobby.broadcast("&e" + sender.getName() + " &7has joined the lobby!");
				lobby.getPlayers().add(pname);
				lobby.getInvited().remove(pname);
				main.inlobby.put(pname, lobby);
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
		String pname = sender.getName();
		Lobby lobby = main.inlobby.get(pname);
		if (lobby.getHost().equals(pname)) {
			lobby.broadcast("&7Lobby disbanded by host!");
			for (String name : lobby.getPlayers()) {
				main.inlobby.remove(name);
			}
			main.lobbies.remove(lobby.getName());
		}
		else {
			lobby.getPlayers().remove(pname);
			main.inlobby.remove(pname);
			sender.sendMessage("§4[§c§lMLMC§4] §7Successfully left lobby!");
			lobby.broadcast("&e" + sender.getName() + " &7has left the lobby!");
		}
	}

	public void kickPlayer(Player sender, String name) {
		Lobby lobby = main.inlobby.get(sender.getName());
		if (lobby.getHost().equals(sender.getName())) {
			lobby.getPlayers().remove(name);
			if (Bukkit.getPlayer(name) != null) {
				Bukkit.getPlayer(name).sendMessage("§7You were kicked from the lobby!");
				lobby.broadcast("&e" + Bukkit.getPlayer(name).getName() + "&7 has been kicked by the host!");
			}
			else {
				lobby.broadcast("&e" + name + "&7 has been kicked by the host!");
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cOnly hosts can kick from lobby!");
		}
	}

	public void invitePlayer(Player sender, String name) {
		String sname = sender.getName();
		Lobby lobby = main.inlobby.get(sname);
		Player invited = Bukkit.getPlayer(name);
		if (invited == null) {
			return;
		}
		if (lobby.getHost().equals(sname)) {
			lobby.getInvited().add(invited.getName());
			lobby.broadcast("&7Successfully invited &e" + invited.getName() + "&7!");
			invited.sendMessage("§4[§c§lMLMC§4] §7You were invited to uno lobby §e" + lobby.getName() + "§7! Join with §c/uno join " + lobby.getName() + "§7.");
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cOnly hosts can invite to lobby!");
		}
	}

	public void setPointsToWin(Player sender, String amt) {
		String sname = sender.getName();
		Lobby lobby = main.inlobby.get(sname);
		int amount = 0;
		try {
			amount = Integer.parseInt(amt);
		} catch (NumberFormatException e) {
			sender.sendMessage("§4[§c§lMLMC§4] §cInvalid number format!");
			return;
		}
		
		if (amount < 100 || amount > 100000) {
			sender.sendMessage("§4[§c§lMLMC§4] §cAmount must be between 100 and 100000!");
			return;
		}

		if (lobby.getHost().equals(sname)) {
			lobby.setPointsToWin(amount);
			lobby.broadcast("&7Successfully set points to win to &e" + amt + "!");
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cOnly hosts can change starting money!");
		}
	}

	public void startGame(Player sender) {
		String sname = sender.getName();
		Lobby lobby = main.inlobby.get(sname);
		if (lobby.getPlayers().size() >= 2) {
			if (lobby.getHost().equals(sname)) {
				try {
					Game game = new Game(main, lobby.getName(), lobby.getPlayers(), lobby.getPointsToWin());
					main.games.put(lobby.getName(), game);
					for (String name : lobby.getPlayers()) {
						main.inlobby.remove(name);
						main.ingame.put(name, game);
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
		String sname = sender.getName();
		if (!main.ingame.containsKey(sname)) {
			if (main.games.containsKey(name)) {
				Game game = main.games.get(name);
				GamePlayer gp = new GamePlayer(game, sname);
				main.ingame.put(sname, game);
				game.spectators.add(gp);
				gp.message("&7You're now spectating! Leave any time with &c/uno quit&7.");
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
