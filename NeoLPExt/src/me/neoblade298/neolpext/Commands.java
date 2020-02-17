package me.neoblade298.neolpext;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;


public class Commands implements CommandExecutor{
	
	Main main;
	UserManager mngr;
	ArrayList<String> validAttrs;
	
	public Commands(Main main, LuckPerms api) {
		this.main = main;
		this.mngr = api.getUserManager();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!sender.hasPermission("lpext.admin")) {
			return true;
		}
		
		// lpext removecontains 
		if (args.length == 0) {
			sender.sendMessage("§cNeoLPExt (lpext.admin)");
			sender.sendMessage("§c/lpext removeall [player] [perm prefix] - Removes all perms that start with perm prefix from player");
			sender.sendMessage("§c/lpext removelength [player] [perm prefix] [perm length] - Same as above but perm must be specified length");
			return true;
		}
		
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("removeall")) {
				User user = mngr.getUser(args[1]);
				ArrayList<Node> removables = (ArrayList<Node>) user.getNodes().stream()
				.filter(e -> e.getKey().startsWith(args[2]))
				.collect(Collectors.toList());
				
				for (Node perm : removables) {
					user.data().remove(perm);
				}
				mngr.saveUser(user);
			}
		}
		
		if (args.length == 4) {
			if (args[0].equalsIgnoreCase("removelength")) {
				User user = mngr.getUser(args[1]);
				ArrayList<Node> removables = (ArrayList<Node>) user.getNodes().stream()
				.filter(e -> e.getKey().startsWith(args[2]))
				.filter(e -> e.getKey().length() == Integer.parseInt(args[3]))
				.collect(Collectors.toList());
				
				for (Node perm : removables) {
					user.data().remove(perm);
				}
				mngr.saveUser(user);
			}
		}
		return true;
	}
}