package me.neoblade298.neomonopoly.Commands;

import org.bukkit.entity.Player;

import me.neoblade298.neomonopoly.Monopoly;
import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
import me.neoblade298.neomonopoly.SpaceCards.Property;

public class AdminCommands {
	Monopoly main;
	public AdminCommands(Monopoly main) {
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
	
	public void kickPlayer(Player sender, Player toKick) {
		if (main.ingame.containsKey(toKick)) {
			Game game = main.ingame.get(toKick);
			GamePlayer gp = game.players.get(toKick);
			if (!isBusy(game, gp)) {
				return;
			}

			if (!gp.isBankrupt()) {
				for (Property prop : gp.getProperties()) {
					prop.setOwner(null);
					prop.onBankrupt(gp);
				}
				game.currentTurn.remove(gp);
				game.requiredActions.remove(gp);
				game.onBankrupt();
			}
			game.broadcast("&4&l" + gp + " has been kicked by force by an admin! Any properties they've owned are now unowned!");
			game.gameplayers.remove(gp);
			game.players.remove(toKick);
			game.main.ingame.remove(toKick);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cThat player isn't in a game!");
		}
	}
	
	public void checkGames(Player sender, Monopoly main) {
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
	
	public void checkPlayer(Player sender, Player toView) {
		if (main.ingame.containsKey(toView)) {
			Game game = main.ingame.get(toView);
			GamePlayer gp = game.players.get(toView);
			sender.sendMessage("§4[§c§lMLMC§4] §7" + game.requiredActions.get(gp));
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cThat player isn't in a game!");
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
