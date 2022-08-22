package me.neoblade298.neomonopoly.Commands;

import org.bukkit.entity.Player;

import me.neoblade298.neomonopoly.Monopoly;
import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class TradeCommands {
	Monopoly main;

	public TradeCommands(Monopoly main) {
		this.main = main;
	}
	
	public void startTrade(Player sender, Player receiver) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!main.ingame.containsKey(receiver)) {
				gp.message("&cThat player isn't in a game!");
			}
			GamePlayer gpView = main.ingame.get(receiver).players.get(receiver);
			if (!game.gameplayers.contains(gpView)) {
				gp.message("&cThat player isn't in your game!");
			}
			
			if (!isPlayerTurn(game, gp) || !isBusy(game, gp) || !noTradeExists(game, gp)) {
				return;
			}

			game.startTrade(gp, gpView);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void viewTrade(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);

			game.trade.privateDisplay(gp);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void setConfirmTrade(Player sender, boolean confirm) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!tradeExists(game, gp) || !inTrade(game, gp)) {
				return;
			}

			game.trade.setConfirm(gp, confirm);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void cancelTrade(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!tradeExists(game, gp) || !inTrade(game, gp)) {
				return;
			}

			game.trade.cancel(gp);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void requestProperty(Player sender, String prefix) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!tradeExists(game, gp) || !inTrade(game, gp)) {
				return;
			}
			
			game.trade.requestProperty(gp, prefix);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void offerProperty(Player sender, String prefix) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!tradeExists(game, gp) || !inTrade(game, gp)) {
				return;
			}
			
			game.trade.offerProperty(gp, prefix);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void requestMoney(Player sender, int amount) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!tradeExists(game, gp) || !inTrade(game, gp)) {
				return;
			}

			if (amount < 0) {
				gp.message("&cYou can't request negative amounts!");
				return;
			}
			
			game.trade.requestMoney(gp, amount);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void offerMoney(Player sender, int amount) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!tradeExists(game, gp) || !inTrade(game, gp)) {
				return;
			}

			if (amount < 0) {
				gp.message("&cYou can't offer negative amounts!");
				return;
			}
			
			game.trade.offerMoney(gp, amount);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void requestJailFree(Player sender, int amount) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!tradeExists(game, gp) || !inTrade(game, gp)) {
				return;
			}

			if (amount < 0) {
				gp.message("&cYou can't request negative amounts!");
				return;
			}
			
			game.trade.requestJailFree(gp, amount);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void offerJailFree(Player sender, int amount) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!tradeExists(game, gp) || !inTrade(game, gp)) {
				return;
			}

			if (amount < 0) {
				gp.message("&cYou can't request negative amounts!");
				return;
			}
			
			game.trade.offerJailFree(gp, amount);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	private boolean noTradeExists(Game game, GamePlayer gp) {
		if (game.trade != null) {
			gp.message("&cA trade already exists!");
			return false;
		}
		return true;
	}

	private boolean tradeExists(Game game, GamePlayer gp) {
		if (game.trade == null) {
			gp.message("&cA trade does not currently exist!");
			return false;
		}
		return true;
	}
	
	private boolean inTrade(Game game, GamePlayer gp) {
		if (!game.trade.traderA.equals(gp) && !game.trade.traderB.equals(gp)) {
			gp.message("&cYou're not part of this trade!");
			return false;
		}
		return true;
	}

	private boolean isPlayerTurn(Game game, GamePlayer gp) {
		if (!game.currentTurn.get(0).equals(gp)) {
			gp.message("&cIt's not your turn!");
			return false;
		}
		return true;
	}

	private boolean isBusy(Game game, GamePlayer gp) {
		if (game.isBusy) {
			gp.message("&cYou can't do that right now!");
			return false;
		}
		return true;
	}
}
