package me.neoblade298.neoconversations;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Commands implements CommandExecutor{
	
	Conversations main;
	
	public Commands(Conversations main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (sender.isOp() || sender.hasPermission("mycommand.staff")) {
			if (args.length == 0) {
				sender.sendMessage("§6/conv play [player] [convname] §7- Play the conversation to the player");
				sender.sendMessage("§6/conv reload §7- Reloads conversations");
			}
			else {
				// conv play [convname] [player]
				if (args[0].equalsIgnoreCase("play") && args.length == 3) {
					Player p = Bukkit.getPlayer(args[1]);
					String convName = args[2];
					
					if (this.main.conversations.containsKey(convName)) {
						this.main.playConversation(this.main.conversations.get(convName), p);
					}
					else {
						sender.sendMessage("§cThe conversation " + convName + " doesn't exist!");
					}
					return true;
				}
				else if (args[0].equalsIgnoreCase("reload") && args.length == 1) {
					this.main.loadConfig();
					sender.sendMessage("§7Successfully reloaded config!");
					return true;
				}
			}
		}
		else {
			sender.sendMessage("§cYou do not have permission to do that!");
		}
		return true;
	}
}