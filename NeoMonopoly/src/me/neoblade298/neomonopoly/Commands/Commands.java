package me.neoblade298.neomonopoly.Commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neomonopoly.Monopoly;
import me.neoblade298.neomonopoly.Objects.Lobby;


public class Commands implements CommandExecutor{
	
	private Monopoly main;
	private LobbyCommands lobbyCommands;
	private GameCommands gameCommands;
	private TradeCommands tradeCommands;
	private AdminCommands adminCommands;
	
	
	public Commands(Monopoly main) {
		this.main = main;
		lobbyCommands = new LobbyCommands(main);
		gameCommands = new GameCommands(main);
		tradeCommands = new TradeCommands(main);
		adminCommands = new AdminCommands(main);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!(sender instanceof Player) || !sender.hasPermission("neomonopoly.player")) {
			return true;
		}
		Player p = (Player) sender;
		
		if (!main.inlobby.containsKey(p) && !main.ingame.containsKey(p)) {
			if (args.length == 0) {
				p.sendMessage("§4[§c§lMonopoly§4]");
				p.sendMessage("§c/mono create [name] §7- Create a lobby");
				p.sendMessage("§c/mono join [name] §7- Join a lobby");
				p.sendMessage("§c/mono spectate [name] §7- Spectate a game");
				if (p.hasPermission("neomonopoly.admin")) {
					p.sendMessage("§4/mono endgame [name] §7- End a game");
					p.sendMessage("§4/mono forcequit [player] §7- Forces a player in the game to quit");
					p.sendMessage("§4/mono games §7- Lists all games and player");
				}
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
				lobbyCommands.createLobby(args[1], p);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
				lobbyCommands.joinLobby(args[1], p);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("spectate")) {
				lobbyCommands.spectateGame(p, args[1]);
				return true;
			}
		}
		else if (main.inlobby.containsKey(p)) {
			Lobby lobby = main.inlobby.get(p);
			if (args.length == 0) {
				p.sendMessage("§4[§c§lMonopoly§4]");
				p.sendMessage("§7Lobby name: §e" + lobby.getName());
				p.sendMessage("§7Players: §e" + lobby.getPlayerList());
				p.sendMessage("§c/mono leave §7- Leave the lobby you're in");
				p.sendMessage("§c/mono start §7- Start the game (host only)");
				p.sendMessage("§c/mono invite [player] §7- Invite a player to the lobby (host only)");
				p.sendMessage("§c/mono kick [player] §7- Kick player from lobby (host only)");
				p.sendMessage("§c/mono set money [amt] §7- Set game starting money (host only)");
				if (p.hasPermission("neomonopoly.admin")) {
					p.sendMessage("§4/mono endgame [name] §7- End a game");
					p.sendMessage("§4/mono forcequit [player] §7- Forces a player in the game to quit");
					p.sendMessage("§4/mono games §7- Lists all games and player");
				}
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
				lobbyCommands.leaveLobby(p);
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
				lobbyCommands.startGame(p);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {
				lobbyCommands.invitePlayer(p, args[1]);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("kick")) {
				lobbyCommands.kickPlayer(p, args[1]);
				return true;
			}
			else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
				if (args[1].equalsIgnoreCase("money")) {
					lobbyCommands.setStartingMoney(p, args[2]);
					return true;
				}
			}
		}
		
		else if (main.ingame.containsKey(p)) {
			
			// Help menus
			if (args.length == 0) {
				p.sendMessage("§4[§c§lMonopoly §7(1/2)§4]");
				p.sendMessage("§7[] = Required, {} = Optional");
				p.sendMessage("§c/mono roll §7- Rolls the dice");
				p.sendMessage("§c/mono summary {player} §7- Summarize a player's properties");
				p.sendMessage("§c/mono color [color] {player} §7- Shows monopoly requirements for color");
				p.sendMessage("§c/mono pos §7- Shows where every player is");
				p.sendMessage("§c/mono list {player} §7- Shows a list of owned properties");
				p.sendMessage("§c/mono view [name] §7- View a property's info card");
				p.sendMessage("§c/mono build/destroy [name]§7- Builds/destroys a house/hotel on a property");
				p.sendMessage("§c/mono end {player} §7- Ends your turn");
				p.sendMessage("§c/mono map {player} §7- Shows a map of the board");
				if (p.hasPermission("neomonopoly.admin")) {
					p.sendMessage("§4/mono endgame [name] §7- End a game");
					p.sendMessage("§4/mono forcequit [player] §7- Forces a player in the game to quit");
					p.sendMessage("§4/mono games §7- Lists all games and player");
				}
				return true;
			}
			
			else if (args.length == 1 && args[0].equals("1")) {
				p.sendMessage("§4[§c§lMonopoly §7(1/2)§4]");
				p.sendMessage("§7[] = Required, {} = Optional");
				p.sendMessage("§c/mono roll §7- Rolls the dice");
				p.sendMessage("§c/mono summary {player} §7- Summarize a player's properties");
				p.sendMessage("§c/mono color [color] {player} §7- Shows monopoly requirements for color");
				p.sendMessage("§c/mono pos §7- Shows where every player is");
				p.sendMessage("§c/mono list {player} §7- Shows a list of owned properties");
				p.sendMessage("§c/mono view [name] §7- View a property's info card");
				p.sendMessage("§c/mono build/destroy [name]§7- Builds/destroys a house/hotel on a property");
				p.sendMessage("§c/mono map {player} §7- Shows a map of the board");
				p.sendMessage("§c/mono end {player} §7- Ends your turn");
				return true;
			}
			
			else if (args.length == 1 && args[0].equals("2")) {
				p.sendMessage("§4[§c§lMonopoly §7(1/2)§4]");
				p.sendMessage("§7[] = Required, {} = Optional");
				p.sendMessage("§c/mono mortgage/unmortgage [name] §7- Mortgages/unmortgages a property");
				p.sendMessage("§c/mono buy/auction §7- Buy/auction the unowned property you're on");
				p.sendMessage("§c/mono bid [money] §7- Bid on an active auction");
				p.sendMessage("§c/mono auction view/leave§7 - View or leave the auction");
				p.sendMessage("§c/mono trade §7- Shows all commands related to trading");
				p.sendMessage("§c/mono bankrupt §7- Give up everything, continue to spectate");
				p.sendMessage("§c/mono quit §7- Exit the game completely");
				return true;
			}
			
			else if (args.length == 1 && args[0].equalsIgnoreCase("trade")) {
				p.sendMessage("§4[§c§lMonopoly §7(1/2)§4]");
				p.sendMessage("§7[] = Required, {} = Optional");
				p.sendMessage("§c/mono trade [player] §7- Starts a trade with a player");
				p.sendMessage("§c/mono trade view §7- View your current trade");
				p.sendMessage("§c/mono offer/request money [amt]");
				p.sendMessage("§c/mono offer/request property [name]");
				p.sendMessage("§c/mono offer/request jailfree [amt]");
				p.sendMessage("§c/mono confirm/unconfirm §7- Confirm the trade");
				p.sendMessage("§c/mono cancel §7- Cancel the trade");
				return true;
			}
			
			// Game commands
			// mono roll
			else if (args.length == 1 && args[0].equalsIgnoreCase("roll")) {
				gameCommands.rollDice(p);
				return true;
			}
			// mono summary [player]
			else if (args[0].equalsIgnoreCase("summary")) {
				if (args.length == 2 && Bukkit.getPlayer(args[1]) != null) {
					Player view = Bukkit.getPlayer(args[1]);
					gameCommands.summarize(p, view);
					return true;
				}
				else if (args.length == 1) {
					gameCommands.summarize(p, p);
					return true;
				}
			}
			// mono color [color] {player}
			else if (args[0].equalsIgnoreCase("color")) {
				if (args.length == 3 && Bukkit.getPlayer(args[2]) != null) {
					Player view = Bukkit.getPlayer(args[2]);
					gameCommands.viewColor(p, view, args[1]);
					return true;
				}
				else if (args.length == 2) {
					gameCommands.viewColor(p, p, args[1]);
					return true;
				}
			}
			// mono pos
			else if (args.length == 1 && args[0].equalsIgnoreCase("pos")) {
				gameCommands.showPositions(p);
				return true;
			}
			// mono list {player}
			else if (args[0].equalsIgnoreCase("list")) {
				if (args.length == 2 && Bukkit.getPlayer(args[1]) != null) {
					Player view = Bukkit.getPlayer(args[1]);
					gameCommands.listProperties(p, view);
					return true;
				}
				else if (args.length == 1) {
					gameCommands.listProperties(p, p);
					return true;
				}
			}
			// mono view [name]
			else if (args.length > 1 && args[0].equalsIgnoreCase("view")) {
				String name = args[1];
				for (int i = 2; i < args.length; i++) {
					name += " " + args[i];
				}
				gameCommands.viewProperty(p, name.toLowerCase());
				return true;
			}
			// mono end {player}
			else if (args.length == 1 && args[0].equalsIgnoreCase("end")) {
				gameCommands.endTurn(p);
				return true;
			}
			// mono jailfree {player}
			else if (args.length == 1 && args[0].equalsIgnoreCase("jailfree")) {
				gameCommands.useJailFree(p);
				return true;
			}
			// mono payjail {player}
			else if (args.length == 1 && args[0].equalsIgnoreCase("payjail")) {
				gameCommands.payJail(p);
				return true;
			}
			// mono paybills
			else if (args.length == 1 && args[0].equalsIgnoreCase("paybills")) {
				gameCommands.payBills(p);
				return true;
			}
			// mono map
			else if (args.length == 1 && args[0].equalsIgnoreCase("map")) {
				gameCommands.displayMap(p);
				return true;
			}
			// mono mortgage/unmortgage [name]
			else if (args.length >= 2 && args[0].equalsIgnoreCase("mortgage")) {
				String name = args[1];
				for (int i = 2; i < args.length; i++) {
					name += " " + args[i];
				}
				gameCommands.mortgageProperty(p, name.toLowerCase());
				return true;
			}
			else if (args.length >= 2 && args[0].equalsIgnoreCase("unmortgage")) {
				String name = args[1];
				for (int i = 2; i < args.length; i++) {
					name += " " + args[i];
				}
				gameCommands.unmortgageProperty(p, name.toLowerCase());
				return true;
			}
			// mono build/destroy/destroyhotel
			else if (args.length >= 2 && args[0].equalsIgnoreCase("build")) {
				String name = args[1];
				for (int i = 2; i < args.length; i++) {
					name += " " + args[i];
				}
				gameCommands.buildOnProperty(p, name.toLowerCase());
				return true;
			}
			else if (args.length >= 2 && args[0].equalsIgnoreCase("destroy")) {
				String name = args[1];
				for (int i = 2; i < args.length; i++) {
					name += " " + args[i];
				}
				gameCommands.destroyOnProperty(p, name.toLowerCase());
				return true;
			}
			else if (args.length >= 2 && args[0].equalsIgnoreCase("destroyhotel")) {
				String name = args[1];
				for (int i = 2; i < args.length; i++) {
					name += " " + args[i];
				}
				gameCommands.destroyHotel(p, name.toLowerCase());
				return true;
			}
			// mono buy/auction
			else if (args.length == 1 && args[0].equalsIgnoreCase("buy")) {
				gameCommands.buyProperty(p);
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("auction")) {
				gameCommands.auctionProperty(p);
				return true;
			}
			// mono bid [amount]
			else if (args.length == 2 && args[0].equalsIgnoreCase("bid")) {
				if (StringUtils.isNumeric(args[1])) {
					gameCommands.bidAuction(p, Integer.parseInt(args[1]));
					return true;
				}
			}
			// mono auction view/leave
			else if (args.length == 2 && args[0].equalsIgnoreCase("auction")) {
				if (args[1].equalsIgnoreCase("view")) {
					gameCommands.viewAuction(p);
					return true;
				}
				else if (args[1].equalsIgnoreCase("leave")) {
					gameCommands.leaveAuction(p);
					return true;
				}
			}
			// mono bankrupt
			else if (args.length == 1 && args[0].equalsIgnoreCase("bankrupt")) {
				gameCommands.goBankrupt(p);
				return true;
			}
			// mono quit
			else if (args.length == 1 && args[0].equalsIgnoreCase("quit")) {
				gameCommands.quit(p);
				return true;
			}
			
			// Trade commands
			else if (args.length == 2 && args[0].equalsIgnoreCase("trade")) {
				// mono trade view
				if (args[1].equalsIgnoreCase("view")) {
					tradeCommands.viewTrade(p);
					return true;
				}
				// mono trade [player]
				else if (Bukkit.getPlayer(args[1]) != null) {
					tradeCommands.startTrade(p, Bukkit.getPlayer(args[1]));
					return true;
				}
			}
			// mono confirm
			else if (args[0].equalsIgnoreCase("confirm")) {
				tradeCommands.setConfirmTrade(p, true);
				return true;
			}
			// mono unconfirm
			else if (args[0].equalsIgnoreCase("unconfirm")) {
				tradeCommands.setConfirmTrade(p, false);
				return true;
			}
			// mono cancel
			else if (args[0].equalsIgnoreCase("cancel")) {
				tradeCommands.cancelTrade(p);
				return true;
			}
			// mono offer money/property/jailfree
			else if (args[0].equalsIgnoreCase("offer") && args.length >= 3) {
				if (args[1].equalsIgnoreCase("money")) {
					if (StringUtils.isNumeric(args[2])) {
						tradeCommands.offerMoney(p, Integer.parseInt(args[2]));
						return true;
					}
				}
				else if (args[1].equalsIgnoreCase("property")) {
					String name = args[2];
					for (int i = 3; i < args.length; i++) {
						name += " " + args[i];
					}
					tradeCommands.offerProperty(p, name.toLowerCase());
					return true;
				}
				else if (args[1].equalsIgnoreCase("jailfree")) {
					if (StringUtils.isNumeric(args[2])) {
						tradeCommands.offerJailFree(p, Integer.parseInt(args[2]));
						return true;
					}
				}
			}
			// mono request money/property/jailfree
			else if (args[0].equalsIgnoreCase("request") && args.length >= 3) {
				if (args[1].equalsIgnoreCase("money")) {
					if (StringUtils.isNumeric(args[2])) {
						tradeCommands.requestMoney(p, Integer.parseInt(args[2]));
						return true;
					}
				}
				else if (args[1].equalsIgnoreCase("property")) {
					String name = args[2];
					for (int i = 3; i < args.length; i++) {
						name += " " + args[i];
					}
					tradeCommands.requestProperty(p, name.toLowerCase());
					return true;
				}
				else if (args[1].equalsIgnoreCase("jailfree")) {
					if (StringUtils.isNumeric(args[2])) {
						tradeCommands.requestJailFree(p, Integer.parseInt(args[2]));
						return true;
					}
				}
			}
		}
		
		if (p.hasPermission("neomonopoly.admin")) {
			if (args.length == 2 && args[0].equalsIgnoreCase("endgame")) {
				adminCommands.endGame(p, args[1]);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("forcequit")) {
				adminCommands.kickPlayer(p, Bukkit.getPlayer(args[1]));
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("games")) {
				adminCommands.checkGames(p, main);
				return true;
			}
		}
		p.sendMessage("§4[§c§lMLMC§4] §7Unknown command!");
		return true;
	}
}