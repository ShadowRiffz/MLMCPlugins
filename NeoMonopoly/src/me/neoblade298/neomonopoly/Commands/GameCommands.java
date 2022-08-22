package me.neoblade298.neomonopoly.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import me.neoblade298.neomonopoly.Monopoly;
import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
import me.neoblade298.neomonopoly.SpaceCards.BuildableProperty;
import me.neoblade298.neomonopoly.SpaceCards.Property;
import me.neoblade298.neomonopoly.SpaceCards.Space;
import net.md_5.bungee.api.chat.ComponentBuilder;

public class GameCommands {
	Monopoly main;
	Random gen;

	public GameCommands(Monopoly main) {
		this.main = main;
		this.gen = new Random();
	}
	
	public void rollDice(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isPlayerTurn(game, gp) || !hasRequiredActions(game, gp)
					|| (!isRequiredAction(game, gp, "ROLL_") && (!isRequiredAction(game, gp, "JAIL_")))
					|| !isBusy(game, gp)) {
				return;
			}

			game.isBusy = true;
			int dice1 = gen.nextInt(6) + 1;
			int dice2 = gen.nextInt(6) + 1;
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
						game.broadcast("&a" + dice2 + "! &lDOUBLES! " + (dice1 + dice2) + " total.");
					}
					else {
						game.broadcast("&a" + dice2 + "! " + (dice1 + dice2) + " total.");
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

	public void listProperties(Player sender, Player toView) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!main.ingame.containsKey(toView)) {
				gp.message("&cThat player isn't in a game!");
			}
			GamePlayer gpView = main.ingame.get(toView).players.get(toView);
			if (!game.gameplayers.contains(gpView)) {
				gp.message("&cThat player isn't in your game!");
			}
			
			int count = 0;
			if (gpView.getProperties().size() == 0) {
				gp.message("&7No properties to show!");
			}
			for (Property prop : gpView.getProperties()) {
				gp.message("&f" + count + ". " + prop.listComponent());
				count++;
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void viewProperty(Player sender, String prefix) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			for (Space space : game.board) {
				if (space instanceof Property) {
					Property prop = (Property) space;
					if (prop.getName().toLowerCase().startsWith(prefix)) {
						prop.displayProperty(gp);
						return;
					}
				}
			}
			gp.message("&cCould not find that property!");
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}

	public void endTurn(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isPlayerTurn(game, gp) || !hasNoRequiredActions(game, gp) ||
				!isBusy(game, gp) || !hasNoTrades(game, gp)) {
				return;
			}
			
			game.endTurn(gp);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void mortgageProperty(Player sender, String prefix) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!owesBills(game, gp)) {
				if (!isPlayerTurn(game, gp) || !isBusy(game, gp)) {
					return;
				}
			}

			Property prop = null;
			for (Property search : gp.getProperties()) {
				if (search.getName().toLowerCase().startsWith(prefix)) {
					prop = search;
					break;
				}
			}
			if (prop == null) {
				gp.message("&cCould not find the specified property!");
				return;
			}
			for (BuildableProperty bp : game.colors.get(prop.getColor())) {
				if (!bp.canMortgage()) {
					gp.message("&cAll constructions must be destroyed on all same color properties before mortgaging this property! /mono destroy [name]!");
					return;
				}
			}
			if (prop.isMortgaged()) {
				gp.message("&cThis property is already mortgaged!");
			}
			
			prop.setMortgaged(true);
			game.giveMoney(prop.getPrice() / 2, gp, "&e" + gp + "&7 mortgaged " + prop.getShorthand(gp) + "&7 for &a+$" + (prop.getPrice() / 2) + "&7.", true);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
		
	public void unmortgageProperty(Player sender, String prefix) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isPlayerTurn(game, gp) || !isBusy(game, gp)) {
				return;
			}
			
			Property prop = null;
			for (Property search : gp.getProperties()) {
				if (search.getName().toLowerCase().startsWith(prefix)) {
					prop = search;
					break;
				}
			}
			if (prop == null) {
				gp.message("&cCould not find the specified property!");
				return;
			}
			if (!prop.canMortgage()) {
				gp.message("&cAll constructions must be destroyed before mortgaging this property! /mono destroy [name]!");
				return;
			}
			if (prop.isMortgaged()) {
				gp.message("&cThis property is already mortgaged!");
				return;
			}
			if (!canAfford(game, gp, prop.getPrice())) {
				return;
			}

			prop.setMortgaged(false);
			game.takeMoney(prop.getPrice(), gp, "&e" + gp + "&7 unmortgaged " + prop.getShorthand(gp) + "&7 for &c-$" + prop.getPrice() + "&7.", true);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void viewPlayer(Player sender, Player toView) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!main.ingame.containsKey(toView)) {
				gp.message("&cThat player isn't in a game!");
				return;
			}
			GamePlayer gpView = main.ingame.get(toView).players.get(toView);
			if (!game.gameplayers.contains(gpView)) {
				gp.message("&cThat player isn't in your game!");
				return;
			}
			
			gp.message("&e" + gpView + " &7is at: " + game.board.get(gpView.getPosition()).getShorthand(gpView));
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void checkBuild(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			gp.message("&7There are currently &e" + game.getHouses() + " &7 and &e" + game.getHotels() + " &7left.");
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void summarize(Player sender, Player toView) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!main.ingame.containsKey(toView)) {
				gp.message("&cThat player isn't in a game!");
			}
			GamePlayer gpView = main.ingame.get(toView).players.get(toView);
			if (!game.gameplayers.contains(gpView)) {
				gp.message("&cThat player isn't in your game!");
			}
			
			
			HashMap<ChatColor, Integer> numOwned = new HashMap<ChatColor, Integer>();
			for (ChatColor color : game.colors.keySet()) {
				numOwned.put(color, 0);
			}
			for (Property prop : gpView.getProperties()) {
				if (prop instanceof BuildableProperty) {
					BuildableProperty bprop = (BuildableProperty) prop;
					numOwned.put(bprop.getColor(), numOwned.get(bprop.getColor()) + 1);
				}
			}
			String line = new String("&dPink: &f" + numOwned.get(ChatColor.LIGHT_PURPLE) + "/" + game.colors.get(ChatColor.LIGHT_PURPLE).size());
			line += "&7, &3Cyan: &f" + numOwned.get(ChatColor.DARK_AQUA) + "/" + game.colors.get(ChatColor.DARK_AQUA).size();
			line += "&7, &5Purple: &f" + numOwned.get(ChatColor.DARK_PURPLE) + "/" + game.colors.get(ChatColor.DARK_PURPLE).size();
			gp.message(line);
			line = new String("&6Orange: &f" + numOwned.get(ChatColor.GOLD) + "/" + game.colors.get(ChatColor.GOLD).size());
			line += "&7, &4Red: &f" + numOwned.get(ChatColor.DARK_RED) + "/" + game.colors.get(ChatColor.DARK_RED).size();
			line += "&7, &eYellow: &f" + numOwned.get(ChatColor.YELLOW) + "/" + game.colors.get(ChatColor.YELLOW).size();
			gp.message(line);
			line = new String("&2Green: &f" + numOwned.get(ChatColor.DARK_GREEN) + "/" + game.colors.get(ChatColor.DARK_GREEN).size());
			line += "&7, &9Blue: &f" + numOwned.get(ChatColor.BLUE) + "/" + game.colors.get(ChatColor.BLUE).size();
			gp.message(line);
			line = new String("&7Railroads: &f" + gpView.getNumRailroads() + "/4&7, &8Utilities: &f" + gpView.getNumUtilities() + "/2");
			gp.message(line);
			gp.message("&7Money: &a$" + gpView.getMoney() + "&7, Get out of jail free cards: &e" + gpView.getNumJailFree());
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void viewColor(Player sender, Player toView, String color) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			if (!main.ingame.containsKey(toView)) {
				gp.message("&cThat player isn't in a game!");
			}
			GamePlayer gpView = main.ingame.get(toView).players.get(toView);
			if (!game.gameplayers.contains(gpView)) {
				gp.message("&cThat player isn't in your game!");
			}

			color = color.toLowerCase();
			ChatColor ccolor = ChatColor.LIGHT_PURPLE;
			ArrayList<BuildableProperty> missing = new ArrayList<BuildableProperty>();
			// Parse the color
			switch (color) {
			case "pink":	ccolor = ChatColor.LIGHT_PURPLE; break;
			case "cyan":	ccolor = ChatColor.DARK_AQUA; break;
			case "purple":	ccolor = ChatColor.DARK_PURPLE; break;
			case "orange":	ccolor = ChatColor.GOLD; break;
			case "red":		ccolor = ChatColor.DARK_RED; break;
			case "yellow":	ccolor = ChatColor.YELLOW; break;
			case "green":	ccolor = ChatColor.DARK_GREEN; break;
			case "blue":	ccolor = ChatColor.BLUE; break;
			}
			
			for (BuildableProperty prop : game.colors.get(ccolor)) {
				if (prop.getOwner() == null || !prop.getOwner().equals(gpView)) {
					missing.add(prop);
				}
			}
			if (missing.size() == 0) {
				gp.message("&e" + gpView + " &7has a monopoly for " + ccolor + color + "&7!");
			}
			else {
				String missingLine = new String();
				for (int i = 0; i < missing.size(); i++) {
					missingLine += missing.get(i).getShorthand(gpView);
					if (i != missing.size() - 1) {
						missingLine += "&7, ";
					}
				}
				gp.message("&e" + gpView + "&7 is missing: " + missingLine);
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void showPositions(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			int count = 1;
			for (GamePlayer p : game.currentTurn) {
				gp.message("&f" + count + ". &e" + p + " &7: "+ game.board.get(p.getPosition()).getShorthand(p));
				count++;
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void buildOnProperty(Player sender, String prefix) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isPlayerTurn(game, gp) || !isBusy(game, gp)) {
				return;
			}

			Property prop = null;
			for (Property search : gp.getProperties()) {
				if (search.getName().toLowerCase().startsWith(prefix)) {
					prop = search;
					break;
				}
			}
			if (prop == null) {
				gp.message("&cCould not find the specified property!");
				return;
			}
			if (!(prop instanceof BuildableProperty)) {
				gp.message("&cYou can't build on this type of property!");
				return;
			}
			
			BuildableProperty bprop = (BuildableProperty) prop;
			if (!canAfford(game, gp, bprop.getHouseprice())) {
				return;
			}
			if (!bprop.isMonopoly()) {
				gp.message("&cYou can't build on a property without owning all of the properties of the same color!");
				return;
			}
			
			// Check if all of same color have the same or more houses
			int currUpgrades = bprop.getNumHotels() * 5 + bprop.getNumHouses();
			for (BuildableProperty bp : game.colors.get(bprop.getColor())) {
				int diff = currUpgrades - (bp.getNumHotels() * 5 + bp.getNumHouses());
				if (diff > 0) {
					gp.message("&cYou can't build more on this property without first building an equal number of "
							+ "houses on other properties of this color!");
					return;
				}
			}
			
			if (bprop.getNumHouses() == 4) {
				if (game.getHotels() > 0) {
					bprop.setNumHotels(1);
					bprop.setNumHouses(0);
					game.addHouses(4);
					game.addHotels(-1);
					game.takeMoney(bprop.getHouseprice(), gp,
							"&e" + gp + " &7built a hotel on " + bprop.getShorthand(gp) + " &7for &c-$" +
					bprop.getHouseprice() + "&7. The rent there is now &e" + bprop.calculateRent(0) + "&7." +
									" &7There are now &e" + game.getHouses() + " &7free houses and &e" +
					game.getHotels() + "&7 free hotels.", true);
					return;
				}
				else {
					gp.message("&cThere are no hotels remaining!");
					return;
				}
			}
			else if (bprop.getNumHotels() == 1) {
				gp.message("&cThis property is already fully upgraded!");
				return;
			}
			else {
				if (game.getHouses() > 0) {
					bprop.setNumHouses(bprop.getNumHouses() + 1);
					game.addHouses(-1);
					game.takeMoney(bprop.getHouseprice(), gp,
							"&e" + gp + " &7built house &e" + bprop.getNumHouses() + " &7on " + bprop.getShorthand(gp) + " &7for &c-$" +
					bprop.getHouseprice() + "&7. The rent there is now &e" + bprop.calculateRent(0) + "&7." +
									" &7There are now &e" + game.getHouses() + " &7free houses.", true);
					return;
				}
				else {
					gp.message("&cThere are no houses remaining!");
					return;
				}
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void destroyOnProperty(Player sender, String prefix) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!owesBills(game, gp)) {
				if (!isPlayerTurn(game, gp) || !isBusy(game, gp)) {
					return;
				}
			}

			Property prop = null;
			for (Property search : gp.getProperties()) {
				if (search.getName().toLowerCase().startsWith(prefix)) {
					prop = search;
					break;
				}
			}
			if (prop == null) {
				gp.message("&cCould not find the specified property!");
				return;
			}
			if (!(prop instanceof BuildableProperty)) {
				gp.message("&cYou can't build on this type of property!");
				return;
			}
			
			BuildableProperty bprop = (BuildableProperty) prop;
			// Check if all of same color have the same or more houses
			int currUpgrades = bprop.getNumHotels() * 5 + bprop.getNumHouses();
			for (BuildableProperty bp : game.colors.get(bprop.getColor())) {
				int diff = currUpgrades - (bp.getNumHotels() * 5 + bp.getNumHouses());
				if (diff < 0) {
					gp.message("&cYou can't destroy more on this property without first destroying an equal number of "
							+ "houses on other properties of this color!");
					return;
				}
			}
			
			if (bprop.getNumHotels() == 1) {
				if (game.getHouses() >= 4) {
					bprop.setNumHotels(0);
					bprop.setNumHouses(4);
					game.addHouses(-4);
					game.addHotels(1);
					game.takeMoney(bprop.getHouseprice(), gp,
							"&e" + gp + " &7downgraded to &e4 &7houses on " + bprop.getShorthand(gp) + " &7for &c+$" +
					bprop.getHouseprice() + "&7. The rent there is now &e" + bprop.calculateRent(0) + "&7." +
									" &7There are now &e" + game.getHouses() + " &7free houses and &e" +
					game.getHotels() + "&7 free hotels.", true);
					return;
				}
				else {
					gp.message("&cThere are no houses remaining! To sell the hotel completely for only cash, do /mono destroyhotel [name]");
					return;
				}
			}
			else if (bprop.getNumHotels() == 0 && bprop.getNumHouses() == 0) {
				gp.message("&cThis property has nothing to destroy!");
				return;
			}
			else if (bprop.getNumHotels() == 0 && bprop.getNumHouses() > 0) {
				bprop.setNumHouses(bprop.getNumHouses() - 1);
				game.addHouses(1);
				game.giveMoney(bprop.getHouseprice() / 2, gp,
						"&e" + gp + " &7destroyed a house, leaving &e" + bprop.getNumHouses() + " &7on " + bprop.getShorthand(gp) + " &7for &c-$" +
				(bprop.getHouseprice() / 2) + "&7. The rent there is now &e" + bprop.calculateRent(0) + "&7." +
								" &7There are now &e" + game.getHouses() + " &7free houses.", true);
				return;
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void destroyHotel(Player sender, String prefix) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!owesBills(game, gp)) {
				if (!isPlayerTurn(game, gp) || !isBusy(game, gp)) {
					return;
				}
			}

			Property prop = null;
			for (Property search : gp.getProperties()) {
				if (search.getName().toLowerCase().startsWith(prefix)) {
					prop = search;
					break;
				}
			}
			if (prop == null) {
				gp.message("&cCould not find the specified property!");
				return;
			}
			if (!(prop instanceof BuildableProperty)) {
				gp.message("&cYou can't build on this type of property!");
				return;
			}
			
			BuildableProperty bprop = (BuildableProperty) prop;
			
			if (bprop.getNumHotels() == 1) {
				bprop.setNumHotels(0);
				game.addHotels(1);
				game.takeMoney(bprop.getHouseprice() * 5, gp,
						"&e" + gp + " &7downgraded to &e4 &7houses on " + bprop.getShorthand(gp) + " &7for &c+$" +
				(bprop.getHouseprice() * 5) + "&7. The rent there is now &e" + bprop.calculateRent(0) + "&7." +
								" &7There are now &e" + game.getHouses() + " &7free houses and &e" +
				game.getHotels() + "&7 free hotels.", true);
				return;
			}
			else if (bprop.getNumHotels() == 0 && bprop.getNumHouses() == 0) {
				gp.message("&cThis property has nothing to destroy!");
				return;
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void displayMap(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			
			// First find positions;
			HashMap<Integer, ArrayList<GamePlayer>> positions = new HashMap<Integer, ArrayList<GamePlayer>>();
			for (GamePlayer p : game.gameplayers) {
				int pos = p.getPosition();
				if (positions.containsKey(pos)) {
					positions.get(pos).add(p);
				}
				else {
					positions.put(pos, new ArrayList<GamePlayer>());
					positions.get(pos).add(p);
				}
			}
			
			// Start mapping
			for (int i = 0; i <= 10; i++) {
				ComponentBuilder builder = new ComponentBuilder("");
				int pos = -1;
				for (int j = 0; j <= 10; j++) {
					pos = -1;
					if (j == 0) {
						pos = 10 - i;
					}
					else if (j == 10) {
						pos = i + 20;
					}
					else if (i == 0) {
						pos = j + 10;
					}
					else if (i == 10) {
						pos = 40 - j;
					}
					else {
						builder.append("- ").color(net.md_5.bungee.api.ChatColor.DARK_GRAY);
						continue;
					}
					Space space = game.board.get(pos);
					
					if (positions.containsKey(pos)) {
						space.addComponent(builder, positions.get(pos));
					}
					else {
						space.addComponent(builder, new ArrayList<GamePlayer>());
					}
				}
				if (i < game.gameplayers.size()) {
					GamePlayer p = game.gameplayers.get(i);
					builder.append(" " + p.mapChar + (", " + p.mapChar).toUpperCase() + ": " + p).color(net.md_5.bungee.api.ChatColor.WHITE);
				}
				if (i == 8) {
					builder.append(" x: Unowned, o: Chest/Chance").color(net.md_5.bungee.api.ChatColor.WHITE);
				}
				if (i == 9) {
					builder.append(" J: Jail, /: Go to jail").color(net.md_5.bungee.api.ChatColor.WHITE);
				}
				if (i == 10) {
					builder.append(" t: Tax, @: Multiple players").color(net.md_5.bungee.api.ChatColor.WHITE);
				}
				gp.getPlayer().spigot().sendMessage(builder.create());
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void auctionProperty(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isPlayerTurn(game, gp) || !isBusy(game, gp) ||
					!hasRequiredActions(game, gp) || !isRequiredAction(game, gp, "UNOWNED_")) {
				return;
			}
			if (game.auction != null) {
				gp.message("&cAn auction is already happening!");
				return;
			}
			
			Property prop = (Property) game.board.get(gp.getPosition());

			game.startAuction(gp, prop);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void bidAuction(Player sender, int amount) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (game.auction == null) {
				gp.message("&cNo auction is happening right now!");
				return;
			}
			
			game.auction.bid(gp, amount);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void leaveAuction(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (game.auction == null) {
				gp.message("&cNo auction is happening right now!");
				return;
			}

			game.auction.dropPlayer(gp);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void viewAuction(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (game.auction == null) {
				gp.message("&cNo auction is happening right now!");
				return;
			}
			
			game.auction.display(gp);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void buyProperty(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isPlayerTurn(game, gp) || !isBusy(game, gp) ||
					!hasRequiredActions(game, gp) || !isRequiredAction(game, gp, "UNOWNED_")) {
				return;
			}
			
			Property prop = (Property) game.board.get(gp.getPosition());
			if (!canAfford(game, gp, prop.getPrice())) {
				return;
			}
			
			game.requiredActions.get(gp).remove(0);
			gp.getProperties().add(prop);
			game.buyProperty(gp, prop);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void goBankrupt(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!owesBills(game, gp)) {
				if (!isPlayerTurn(game, gp) || !isBusy(game, gp)) {
					return;
				}
			}

			for (Property prop : gp.getProperties()) {
				prop.setOwner(null);
				prop.onBankrupt(gp);
			}
			
			if (game.currentTurn.get(0).equals(gp)) {
				game.endTurn(gp);
			}
			gp.setPosition(-1);
			game.currentTurn.remove(gp);
			game.requiredActions.remove(gp);
			gp.message("&7You may leave the game any time with &c/mono quit&7.");
			game.broadcast("&4&l" + gp + " has filed for bankruptcy. All of their properties are now unowned.");
			game.onBankrupt();

			// Check for edge case where everyone owes the current player money due to the chance card
			ArrayList<String> currentActions = game.requiredActions.get(game.currentTurn.get(0));
			boolean allPaid = true;
			if (currentActions.size() > 0 && currentActions.get(0).equals("WAIT_PLAYERBILLS")) {
				for (GamePlayer p : game.requiredActions.keySet()) {
					if (!p.equals(game.currentTurn.get(0))) {
						if (game.requiredActions.get(p).size() != 0) allPaid = false;
					}
				}
				if (allPaid) currentActions.remove("WAIT_PLAYERBILLS");
			}
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void payBills(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isPlayerTurn(game, gp) || !isBusy(game, gp)
					|| !hasRequiredActions(game, gp) || !isRequiredAction(game, gp, "PAY_")) {
				return;
			}

			game.payBill(gp);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void useJailFree(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isPlayerTurn(game, gp) || !isBusy(game, gp)
					|| !hasRequiredActions(game, gp) || !isRequiredAction(game, gp, "JAIL_")) {
				return;
			}
			
			if (gp.getNumJailFree() == 0) {
				gp.message("&cYou do not have any jail free cards!");
				return;
			}
			
			gp.setNumJailFree(gp.getNumJailFree() - 1);
			gp.setJailed(false);
			gp.resetJailTime();
			game.requiredActions.get(gp).remove(0);
			game.requiredActions.get(gp).add("ROLL_MOVE");
			game.broadcast("&e" + gp + " &7used a get out of jail free card! They may now roll the dice to move.");
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void payJail(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isPlayerTurn(game, gp) || !isBusy(game, gp)
					|| !hasRequiredActions(game, gp) || !isRequiredAction(game, gp, "JAIL_")) {
				return;
			}
			
			if (gp.getMoney() < 50) {
				gp.message("&cYou don't have enough money to pay to leave jail!");
				return;
			}

			gp.setMoney(gp.getMoney() - 50);
			game.broadcast("&e" + gp + " &7left jail by paying &a$50&7. They may now roll the dice to move.");
			gp.setJailed(false);
			gp.resetJailTime();
			game.requiredActions.get(gp).remove(0);
			game.requiredActions.get(gp).add("ROLL_MOVE");
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}
	
	public void quit(Player sender) {
		if (main.ingame.containsKey(sender)) {
			Game game = main.ingame.get(sender);
			GamePlayer gp = game.players.get(sender);
			if (!isBusy(game, gp)) {
				return;
			}

			if (!gp.isBankrupt()) {
				for (Property prop : gp.getProperties()) {
					prop.setOwner(null);
					prop.onBankrupt(gp);
				}
				game.broadcast("&4&l" + gp + " has filed for bankruptcy and left the game. All of their properties are now unowned.");
				gp.setPosition(-1);
				game.currentTurn.remove(gp);
				game.requiredActions.remove(gp);
				game.onBankrupt();
				if (game.currentTurn.get(0).equals(gp)) {
					game.endTurn(gp);
				}

				// Check for edge case where everyone owes the current player money due to the chance card
				ArrayList<String> currentActions = game.requiredActions.get(game.currentTurn.get(0));
				boolean allPaid = true;
				if (currentActions.size() > 0 && currentActions.get(0).equals("WAIT_PLAYERBILLS")) {
					for (GamePlayer p : game.requiredActions.keySet()) {
						if (!p.equals(game.currentTurn.get(0))) {
							if (game.requiredActions.get(p).size() != 0) allPaid = false;
						}
					}
					if (allPaid) currentActions.remove("WAIT_PLAYERBILLS");
				}
			}
			else {
				game.broadcast("&4&l" + gp + " has left the game.");
			}
			game.gameplayers.remove(gp);
			game.players.remove(sender);
			game.main.ingame.remove(sender);
		}
		else {
			sender.sendMessage("§4[§c§lMLMC§4] §cYou're not in a game!");
		}
	}

	private boolean hasRequiredActions(Game game, GamePlayer gp) {
		if (game.requiredActions.get(gp).size() == 0) {
			gp.message("&cNo actions remaining! Manage properties or end your turn with /mono end!");
			return false;
		}
		return true;
	}

	private boolean hasNoRequiredActions(Game game, GamePlayer gp) {
		if (game.requiredActions.get(gp).size() != 0) {
			game.messageRequiredAction(gp);
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

	private boolean isRequiredAction(Game game, GamePlayer gp, String prefix) {
		if (!game.requiredActions.get(gp).get(0).startsWith(prefix)) {
			game.messageRequiredAction(gp);
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
	
	private boolean canAfford(Game game, GamePlayer gp, int amount) {
		if (gp.getMoney() < amount) {
			gp.message("&cYou can't afford to do that!");
			return false;
		}
		return true;
	}
	
	private boolean hasNoTrades(Game game, GamePlayer gp) {
		if (game.trade != null) {
			gp.message("&cYou must first cancel your trade! /mono trade cancel!");
			return false;
		}
		return true;
	}
	
	private boolean owesBills(Game game, GamePlayer gp) {
		return gp.getBills() > 0;
	}
}
