package me.Neoblade298.NeoCooldowns;

import java.util.Iterator;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor{
	
	Main main;
	
	public Commands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		
		if(sender.hasPermission("neocooldowns.view")) {
			if(args.length == 1) {
				// cooldown list
				if(args[0].equalsIgnoreCase("list") || args[0].equalsIgnoreCase("help")) {
					String[] list = main.getFM().getList();
					for(String s : list) {
						s = s.replaceAll("&", "§");
						sender.sendMessage(s);
					}
					return true;
				}
			}
			
			if(args.length >= 1) {
				
				String arg = args[0];
				for(int i = 1; i < args.length; i++) {
					arg += (" " + args[i]);
				}
				int temp = main.getFM().getCooldownKey(arg);
				
				// Getting cooldowns
				if(temp != 0) {
					
					// Got a cooldown
					if(temp == 1) {
						String name = main.getFM().getKey(arg);
						String filename = main.getFM().cooldowns.getString(name);
						sendCooldown(sender, name, filename);
					}

					else if(temp == 2) {
						List<String> names = main.getFM().getKeys(arg);
						Iterator<String> nameIter = names.iterator();
						
						while(nameIter.hasNext()) {
							String name = nameIter.next();
							String filename = main.getFM().cooldowns.getString(name);
							sendCooldown(sender, name, filename);
						}
					}
					return true;
				}
				
				// Cooldown does not exist
				else {
					String message = "&4[&c&lMLMC&4] &cThis cooldown does not exist. Try /cooldown list";
					message = message.replaceAll("&", "§");
					sender.sendMessage(message);
					return true;
				}
			}
		}
		String[] list = main.getFM().getList();
		for(String s : list) {
			s = s.replaceAll("&", "§");
			sender.sendMessage(s);
		}
		return true;
	}
	
	public void sendCooldown(CommandSender p, String name, String filename) {
		double timeLeft = main.getFM().getCooldown(filename);
		String message = null;
		if(timeLeft > 0) {
			message = "&4[&c&lMLMC&4] &4&l" + name + " &7has &c" + timeLeft + " &7minutes remaining!";
		}
		else {
			message = "&4[&c&lMLMC&4] &4&l" + name + " &7is off cooldown!";
		}
		message = message.replaceAll("&", "§");
		p.sendMessage(message);
	}
}