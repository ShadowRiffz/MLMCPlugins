package me.neoblade298.neouno.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.neoblade298.neouno.Objects.Game;
import me.neoblade298.neouno.Objects.GamePlayer;
import me.neoblade298.neouno.Uno;
import me.neoblade298.neouno.Cards.Card;

public class GameCommands {
	Uno main;
	public GameCommands(Uno main) {
		this.main = main;
	}
	
	public void showHand(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!isNotBusy(game, gp)) {
				return;
			}
			
			gp.showHand();
			
		}
		else {
			sender.sendMessage("�4[�c�lMLMC�4] �cYou're not in a game!");
		}
	}
	
	public void quitGame(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!isNotBusy(game, gp)) {
				return;
			}

			game.playerLeave(false, gp);
			
		}
		else {
			sender.sendMessage("�4[�c�lMLMC�4] �cYou're not in a game!");
		}
	}
	
	public void playCard(Player sender, String name) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!isPlayerTurn(game, gp) || !isNotBusy(game, gp)) {
				return;
			}

			// Number card
			if (StringUtils.isNumeric(name.substring(0, 1)) && name.length() == 2) {
				ChatColor color = main.stringToColor.get(name.substring(1, 2));
				int num = Integer.parseInt(name.substring(0, 1));
				for (Card card : gp.getCards()) {
					if (card.getColor().equals(color) && card.getNumber() == num) {
						game.playCard(gp, card);
						return;
					}
				}
			}
			// Named color card
			else if (main.stringToColor.containsKey(name.substring(name.length() - 1, name.length()))) {
				ChatColor color = main.stringToColor.get(name.substring(name.length() - 1, name.length()));
				for (Card card : gp.getCards()) {
					if (ChatColor.stripColor(card.getDisplay()).equalsIgnoreCase(name.substring(0, name.length() - 1)) &&
							color.equals(card.getColor())) {
						game.playCard(gp, card);
						return;
					}
				}
			}
			// Wildcard
			else {
				for (Card card : gp.getCards()) {
					if (ChatColor.stripColor(card.getDisplay()).equalsIgnoreCase(name)) {
						game.playCard(gp, card);
						return;
					}
				}
			}
		}
		else {
			sender.sendMessage("�4[�c�lMLMC�4] �cYou're not in a game!");
		}
	}
	
	public void changeColor(Player sender, String color) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!isPlayerTurn(game, gp) || !isNotBusy(game, gp)) {
				return;
			}
			if (game.requiredAction == null || !game.requiredAction.equalsIgnoreCase("PICK_COLOR")) {
				gp.message("&cYou can't pick a color right now!");
				return;
			}
			if (!game.main.stringToColor.containsKey(color)) {
				gp.message("&cThat isn't a valid color");
				return;
			}

			game.requiredAction = null;
			ChatColor clr = game.main.stringToColor.get(color);
			game.topCard.setColor(clr);
			String display = game.main.colorToString.get(clr);
			game.broadcast("&f" + gp + " &7has changed the color to " + clr + display + "&7!");
			game.turns.add(game.curr);
			game.curr = game.turns.remove(0);
			game.nextTurn();
		}
		else {
			sender.sendMessage("�4[�c�lMLMC�4] �cYou're not in a game!");
		}
	}
	
	public void callUno(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!isNotBusy(game, gp)) {
				return;
			}
			
			if (gp.calledUno()) {
				gp.message("&cYou already called uno!");
			}	
			
			if (game.curr.equals(gp) && gp.getCards().size() == 2) {
				game.broadcast("&f" + gp + " &7calls &lUno&7!");
				gp.setCalledUno(true);
				return;
			}
			else if (!game.curr.equals(gp) && gp.getCards().size() == 1) {
				game.broadcast("&f" + gp + " &7calls &lUno&7!");
				gp.setCalledUno(true);
				return;
			}
			gp.message("&cYou can't call uno when you have this many cards!");
		}
		else {
			sender.sendMessage("�4[�c�lMLMC�4] �cYou're not in a game!");
		}
	}
	
	public void challengeUno(Player sender, Player toChallenge) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (main.ingame.containsKey(toChallenge)) {
				GamePlayer challenged = game.players.get(toChallenge);

				if (!isNotBusy(game, gp)) {
					return;
				}
				if (challenged.calledUno()) {
					gp.message("&cThat player called uno!");
					return;
				}
				if (game.curr.equals(challenged)) {
					gp.message("&cIt's not the end of that player's turn yet!");
					return;
				}

				if (challenged.getCards().size() > 1) {
					gp.message("&cThat player has more than 1 card in hand!");
					return;
				}
				
				game.broadcast("&f" + gp + " &7called out &f" + challenged + " &7for not calling uno!");
				game.drawCard(challenged, 2);
			}
		}
		else {
			sender.sendMessage("�4[�c�lMLMC�4] �cYou're not in a game!");
		}
	}
	
	public void drawCard(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);

			if (!isPlayerTurn(game, gp) || !isNotBusy(game, gp)) {
				return;
			}
			
			game.drawCard(gp, 1);
		}
		else {
			sender.sendMessage("�4[�c�lMLMC�4] �cYou're not in a game!");
		}
	}
	
	public void displayPlayers(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);

			if (!isNotBusy(game, gp)) {
				return;
			}
			
			game.broadcast("&7Current turn: &f" + game.curr);
			int count = 2;
			for (GamePlayer p : game.gameplayers) {
				game.broadcast("&7" + count + ". &f" + p);
				count++;
			}
		}
		else {
			sender.sendMessage("�4[�c�lMLMC�4] �cYou're not in a game!");
		}
	}
	
	private boolean isNotBusy(Game game, GamePlayer gp) {
		if (game.isBusy) {
			gp.message("&cYou can't do that right now!");
			return false;
		}
		return true;
	}
	
	private boolean isPlayerTurn(Game game, GamePlayer gp) {
		if (!game.curr.equals(gp)) {
			gp.message("&cIt isn't your turn!");
			return false;
		}
		return true;
	}
}
