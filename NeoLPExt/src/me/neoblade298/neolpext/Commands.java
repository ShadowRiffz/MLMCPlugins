package me.neoblade298.neolpext;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;


public class Commands implements CommandExecutor{
	
	Main main;
	UserManager mngr;
	ArrayList<String> validAttrs;
	boolean debug;
	
	public Commands(Main main, LuckPerms api) {
		this.main = main;
		this.mngr = api.getUserManager();
		debug = false;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (!sender.hasPermission("lpext.admin")) {
			return true;
		}
		
		// lpext removecontains 
		if (args.length == 0) {
			sender.sendMessage("§cNeoLPExt (lpext.admin)");
			sender.sendMessage("§c/lpext debug - Sends debug to system");
			sender.sendMessage("§c/lpext removeall [player] [perm prefix] - Removes all perms that start with perm prefix from player");
			sender.sendMessage("§c/lpext switch [player] [perm prefix] [new perm] - Removes all other prefixes, then adds new perm");
			sender.sendMessage("§c/lpext removelength [player] [perm prefix] [minlen:maxlen] - Same as removeall but perm must be specified length. Min-max of length separated by :");
			sender.sendMessage("§c/lpext switchlength [player] [perm prefix] [minlen:maxlen] [new perm] - Same as switch but for specified length");
			return true;
		}
		
		if (args.length == 1 && args[0].equalsIgnoreCase("debug")) {
			debug = !debug;
			sender.sendMessage("§cDebug: " + debug);
		}
		
		if (args.length == 3) {
			if (args[0].equalsIgnoreCase("removeall")) {
				BukkitRunnable update = new BukkitRunnable() { public void run() {
					User user = mngr.getUser(args[1]);
					ArrayList<Node> removables = (ArrayList<Node>) user.getNodes().stream()
					.filter(e -> e.getKey().startsWith(args[2]))
					.collect(Collectors.toList());
					
					for (Node perm : removables) {
						user.data().remove(perm);
						if (debug) sender.sendMessage("§7Removed perm: §c" + perm.getKey());
					}
					mngr.saveUser(user);
				}};
				update.runTaskAsynchronously(main);
				
			}
		}
		
		else if (args.length == 4) {
			if (args[0].equalsIgnoreCase("removelength")) {
				BukkitRunnable update = new BukkitRunnable() { public void run() {
					User user = mngr.getUser(args[1]);
					String[] lengths = args[3].split(":");
					int minlen = Integer.parseInt(lengths[0]);
					int maxlen = Integer.parseInt(lengths[1]);
					ArrayList<Node> removables = (ArrayList<Node>) user.getNodes().stream()
					.filter(e -> e.getKey().startsWith(args[2]))
					.filter(e -> e.getKey().length() >= minlen)
					.filter(e -> e.getKey().length() <= maxlen)
					.collect(Collectors.toList());
					
					for (Node perm : removables) {
						user.data().remove(perm);
						if (debug) sender.sendMessage("§7Removed perm: §c" + perm.getKey());
					}
					mngr.saveUser(user);
				}};
				update.runTaskAsynchronously(main);
			}
			// lpext switch [player] [perm prefix] [new perm]
			else if (args[0].equalsIgnoreCase("switch")) {
				BukkitRunnable update = new BukkitRunnable() { public void run() {
					User user = mngr.getUser(args[1]);
					ArrayList<Node> removables = (ArrayList<Node>) user.getNodes().stream()
					.filter(e -> e.getKey().startsWith(args[2]))
					.collect(Collectors.toList());
					
					for (Node perm : removables) {
						user.data().remove(perm);
						if (debug) sender.sendMessage("§7Removed perm: §c" + perm.getKey());
					}
					
					user.data().add(Node.builder(args[3]).build());
					mngr.saveUser(user);
				}};
				update.runTaskAsynchronously(main);
			}
		}
		
		else if (args.length == 5) {
			// lpext switchlength [player] [perm prefix] [minlen:maxlen] [new perm]
			if (args[0].equalsIgnoreCase("switchlength")) {
				BukkitRunnable update = new BukkitRunnable() { public void run() {
					User user = mngr.getUser(args[1]);
					String[] lengths = args[3].split(":");
					int minlen = Integer.parseInt(lengths[0]);
					int maxlen = Integer.parseInt(lengths[1]);
					ArrayList<Node> removables = (ArrayList<Node>) user.getNodes().stream()
					.filter(e -> e.getKey().startsWith(args[2]))
					.filter(e -> e.getKey().length() >= minlen)
					.filter(e -> e.getKey().length() <= maxlen)
					.collect(Collectors.toList());
					
					for (Node perm : removables) {
						user.data().remove(perm);
						if (debug) sender.sendMessage("§7Removed perm: §c" + perm.getKey());
					}
					user.data().add(Node.builder(args[4]).build());
					mngr.saveUser(user);
				}};
				update.runTaskAsynchronously(main);
			}
		}
		return true;
	}
}