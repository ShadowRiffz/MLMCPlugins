package me.neoblade298.neomonopoly.Commands;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neomonopoly.Monopoly;
import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class GameCommands {
	Monopoly main;
	Random gen;

	public GameCommands(Monopoly main) {
		this.main = main;
		this.gen = new Random();
	}
	
	public void rollDice(Player sender) {
		int dice1 = gen.nextInt(6) + 1;
		int dice2 = gen.nextInt(6) + 1;
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);

			if (game.requiredActions.get(gp).size() == 0) {
				gp.message("&cYou cannot roll the dice right now!");
				return;
			}
			if (!game.currentTurn.get(0).equals(gp)) {
				sender.sendMessage("§4[§c§lMLMC§4] §cIt's not your turn!");
				return;
			}	
			
			if (!game.isBusy) {
				sender.sendMessage("§4[§c§lMLMC§4] §cYou can't do that right now!");
				return;
			}

			if (!game.requiredActions.get(gp).get(0).startsWith("ROLL_")) {
				game.messageRequiredAction(gp);
			}
			
			game.isBusy = true;
			game.broadcast("&e" + sender.getName() + " &7rolls a...");
			new BukkitRunnable() {
				public void run() {
					game.broadcast("&a" + dice1 + "!");
				}
			}.runTaskLater(main, 20L);
			new BukkitRunnable() {
				public void run() {
					game.broadcast("&7and a...");
				}
			}.runTaskLater(main, 30L);
			new BukkitRunnable() {
				public void run() {
					if (dice1 == dice2) {
						game.broadcast("&a" + dice1 + "! &lDOUBLES!");
					}
					else {
						game.broadcast("&a" + dice1 + "!");
					}
				}
			}.runTaskLater(main, 50L);
			new BukkitRunnable() {
				public void run() {
					game.handleDiceRoll(game.players.get(sender), dice1 + dice2, dice1 == dice2);
				}
			}.runTaskLater(main, 80L);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
}
