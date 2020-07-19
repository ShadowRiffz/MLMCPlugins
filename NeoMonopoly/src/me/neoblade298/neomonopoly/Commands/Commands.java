package me.neoblade298.neomonopoly.Commands;

import java.util.ArrayList;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neomonopoly.Monopoly;


public class Commands implements CommandExecutor{
	
	private Monopoly main;
	private LobbyCommands lobbyCommands;
	
	
	public Commands(Monopoly main) {
		this.main = main;
		lobbyCommands = new LobbyCommands(main);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!(sender instanceof Player) || !sender.hasPermission("neomonopoly.player")) {
			return true;
		}
		Player p = (Player) sender;
		
		if (!main.inlobby.containsKey(p) && !main.ingame.containsKey(p)) {
			if (args.length == 0) {
				p.sendMessage("�4[�c�lMonopoly�4]");
				p.sendMessage("�c/mono create [name] �7- Create a lobby");
				p.sendMessage("�c/mono join [name] �7- Join a lobby");
			}
		}
		else if (main.inlobby.containsKey(p)) {
			if (args.length == 0) {
				p.sendMessage("�4[�c�lMonopoly�4]");
				p.sendMessage("�c/mono leave �7- Leave the lobby you're in");
				p.sendMessage("�c/mono start �7- Start the game (host only)");
				p.sendMessage("�c/mono invite [player] �7- Invite a player to the lobby (host only)");
				p.sendMessage("�c/mono kick [player] �7- Kick player from lobby (host only)");
				p.sendMessage("�c/mono set money [amt] �7- Set game starting money (host only)");
			}
			
			else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
				lobbyCommands.createLobby(args[1], p);
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
				lobbyCommands.joinLobby(args[1], p);
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("leave")) {
				lobbyCommands.leaveLobby(p);
			}
			else if (args.length == 1 && args[0].equalsIgnoreCase("start")) {
				lobbyCommands.startGame(p);
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("invite")) {
				lobbyCommands.invitePlayer(p, args[1]);
			}
			else if (args.length == 2 && args[0].equalsIgnoreCase("kick")) {
				lobbyCommands.kickPlayer(p, args[1]);
			}
			else if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
				if (args[1].equalsIgnoreCase("money")) {
					lobbyCommands.setStartingMoney(p, args[2]);
				}
			}
		}
		
		else if (main.ingame.containsKey(p)) {
			if (args.length == 0) {
				p.sendMessage("�4[�c�lMonopoly�4]");
				p.sendMessage("�c/mono roll �7- Rolls the dice");
				p.sendMessage("�c/mono properties {player} �7- Views owned properties");
				p.sendMessage("�c/mono mortgage # �7- Mortgages a property");
				p.sendMessage("�c/mono view {player}�7- Views the space you're on");
				p.sendMessage("�c/mono paybills�7- Pays any bills you owe");
				p.sendMessage("�c/mono build #�7- Builds a house/hotel on a property");
				p.sendMessage("�c/mono destroy # �7- Destroys a house/hotel on a property");
				p.sendMessage("�c/mono trade �7- Shows all commands related to trading");
				p.sendMessage("�c/mono bankrupt �7- Give up everything, continue to spectate");
				p.sendMessage("�c/mono quit �7- Bankrupt and exit the game");
			}
			
			if (args.length == 1 && args[0].equalsIgnoreCase("trade")) {
				p.sendMessage("�c/mono trade [player] �7- Starts a trade with a player");
				p.sendMessage("�c/mono trade view �7- View your current trade");
				p.sendMessage("�c/mono trade offer/request money [amt]");
				p.sendMessage("�c/mono trade offer/request property #");
				p.sendMessage("�c/mono trade offer/request jailfree [amt]");
				p.sendMessage("�c/mono trade accept �7- Accept the trade");
				p.sendMessage("�c/mono trade cancel �7- Cancel the trade");
			}
		}
		
		if (p.hasPermission("neomonopoly.admin")) {
			p.sendMessage("�4/mono end [name] �7- End a game");
			p.sendMessage("�4/mono forcequit [player] �7- Forces a player in the game to quit");
			p.sendMessage("�4/mono check [name] �7- Checks what game a player is in");
		}
		return true;
	}
}