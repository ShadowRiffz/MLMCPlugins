package me.neoblade298.neouno.Commands;

import org.bukkit.entity.Player;

import me.neoblade298.neouno.Uno;
import me.neoblade298.neouno.Objects.Game;
import me.neoblade298.neouno.Objects.GamePlayer;

public class AdminCommands {
	Uno main;
	public AdminCommands(Uno main) {
		this.main = main;
	}
	
	public void endGame(Player sender, String gameName) {
		if (main.games.containsKey(gameName)) {
			Game game = main.games.get(gameName);
			game.forceEndGame();
			sender.sendMessage("§4[§c§lMLMC§4] §7Successfully ended game!");
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cThat game doesn't exist!");
		}
	}
	
	public void kickPlayer(Player sender, String toKick) {
		if (main.ingame.containsKey(toKick)) {
			Game game = main.ingame.get(toKick);
			GamePlayer gp = game.players.get(toKick);
			if (!isBusy(game, gp)) {
				return;
			}
			
			game.playerLeave(true, gp);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cThat player isn't in a game!");
		}
	}
	
	public void checkGames(Player sender, Uno main) {
		if (main.games.size() == 0) {
			sender.sendMessage("§cNo games being played.");
		}
		for (String name : main.games.keySet()) {
			Game game = main.games.get(name);
			String msg = "§e" + game.getName() + "§7: §e";
			for (GamePlayer gp : game.gameplayers) {
				msg += gp + " ";
			}
			sender.sendMessage(msg);
		}
	}

	private boolean isBusy(Game game, GamePlayer gp) {
		if (game.isBusy) {
			gp.message("&cYou can't do that right now!");
			return false;
		}
		return true;
	}
}
