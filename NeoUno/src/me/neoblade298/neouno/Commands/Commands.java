package me.neoblade298.neouno.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neouno.Uno;
import me.neoblade298.neouno.Objects.Lobby;


public class Commands implements CommandExecutor{
	
	Uno main;
	LobbyCommands lc;
	GameCommands gc;
	AdminCommands ac;
	
	public Commands(Uno main) {
		this.main = main;
		lc = new LobbyCommands(main);
		gc = new GameCommands(main);
		ac = new AdminCommands(main);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!(sender instanceof Player) || !sender.hasPermission("neouno.player")) {
			return true;
		}
		Player p = (Player) sender;
		
		if (!main.inlobby.containsKey(sender) && !main.ingame.containsKey(sender)) {
			if (args.length == 0) {
				p.sendMessage("§4[§c§lUno§4]");
				p.sendMessage("§c/uno create [name] §7- Create a lobby");
				p.sendMessage("§c/uno join [name] §7- Join a lobby");
				p.sendMessage("§c/uno spectate [name] §7- Spectate a game");
				if (p.hasPermission("neouno.admin")) {
					p.sendMessage("§4/uno endgame [name] §7- End a game");
					p.sendMessage("§4/uno forcequit [player] §7- Forces a player in the game to quit");
					p.sendMessage("§4/uno games §7- Lists all games and player");
				}
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
				lc.createLobby(args[1], p);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
				lc.joinLobby(args[1], p);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("spectate")) {
				lc.spectateGame(p, args[1]);
				return true;
			}
		}
		else if (main.inlobby.containsKey(sender)) {
			Lobby lobby = main.inlobby.get(sender);
			if (args.length == 0) {
				p.sendMessage("§4[§c§lUno§4]");
				p.sendMessage("§7Lobby name: §e" + lobby.getName());
				p.sendMessage("§7Players: §e" + lobby.getPlayerList());
				p.sendMessage("§c/uno leave §7- Leave the lobby you're in");
				p.sendMessage("§c/uno start §7- Start the game (host only)");
				p.sendMessage("§c/uno invite [player] §7- Invite a player to the lobby (host only)");
				p.sendMessage("§c/uno kick [player] §7- Kick player from lobby (host only)");
				p.sendMessage("§c/uno set points [amt] §7- Set game points to win (host only)");
				if (p.hasPermission("neouno.admin")) {
					p.sendMessage("§4/uno endgame [name] §7- End a game");
					p.sendMessage("§4/uno forcequit [player] §7- Forces a player in the game to quit");
					p.sendMessage("§4/uno games §7- Lists all games and player");
				}
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
				lc.leaveLobby(p);
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
				lc.startGame(p);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {
				lc.invitePlayer(p, args[1]);
				return true;
			}
			else if (args.length == 3 && args[0].equalsIgnoreCase("set") && args[1].equalsIgnoreCase("points")) {
				lc.setPointsToWin(p, args[2]);
				return true;
			}
		}
		else if (main.ingame.containsKey(sender)) {
			if (args.length == 0) {
				p.sendMessage("§4[§c§lUno §7(1/2)§4]");
				p.sendMessage("§7[] = Required, {} = Optional");
				p.sendMessage("§c/uno hand §7- Shows you your hand");
				p.sendMessage("§c/uno play #[r/g/b/y] §7- Plays card. Example, play blue 3: /uno play 3b");
				p.sendMessage("§c/uno draw §7- Draw a card instead of playing one.");
				p.sendMessage("§c/uno call §7- Call uno when you have 1 card left!");
				p.sendMessage("§c/uno color [r/g/b/y] §7- Set the color when you use a wildcard!");
				p.sendMessage("§c/uno challenge [player] §7- If someone fails to call uno, use this!");
				p.sendMessage("§c/uno players §7- View players and order of players");
				p.sendMessage("§c/uno quit §7- Quit the game you're in");
				if (p.hasPermission("neouno.admin")) {
					p.sendMessage("§4/uno endgame [name] §7- End a game");
					p.sendMessage("§4/uno forcequit [player] §7- Forces a player in the game to quit");
					p.sendMessage("§4/uno games §7- Lists all games and player");
				}
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("hand")) {
				gc.showHand(p);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("play")) {
				gc.playCard(p, args[1]);
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("draw")) {
				gc.drawCard(p);
				return true;
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("call")) {
				gc.callUno(p);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("color")) {
				gc.changeColor(p, args[1]);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("challenge")) {
				Player toChallenge = Bukkit.getPlayer(args[1]);
				if (toChallenge != null) {
					gc.challengeUno(p, toChallenge);
					return true;
				}
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("players")) {
				gc.displayPlayers(p);
				return true;
			}
		}
		
		if (p.hasPermission("neouno.admin")) {
			if (args.length == 2 && args[0].equalsIgnoreCase("forcequit")) {
				Player toKick = Bukkit.getPlayer(args[1]);
				if (toKick != null) {
					ac.kickPlayer(p, toKick);
					return true;
				}
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("endgame")) {
				ac.endGame(p, args[1]);
				return true;
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("games")) {
				ac.checkGames(p, main);
				return true;
			}
		}
		
		
		p.sendMessage("§4[§c§lMLMC§4] §7Unknown command!");
		return true;
	}
}