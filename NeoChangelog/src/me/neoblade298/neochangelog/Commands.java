package me.neoblade298.neochangelog;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public class Commands implements CommandExecutor{
	private Main main = null;

	public Commands (Main plugin) {
		main = plugin;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		List<List> days = main.getFM().getDays();
    if(args.length < 1) {
    	if(sender.hasPermission("changelog.view")) {
	    	if(days == null) {
	    		sender.sendMessage("Changelog is empty!");
	    		return true;
	    	}
	    	for(int i = 0; i < days.size(); i++) {
	    		sender.sendMessage("" + ChatColor.RED + "Date: " + days.get(i).get(0) + ChatColor.GRAY + " (" + i + ")");
	    		sender.sendMessage("" + ChatColor.RED + ChatColor.STRIKETHROUGH + "---------------");
	    		for(int j = 1; j < days.get(i).size(); j++) {
	    			sender.sendMessage(ChatColor.GOLD + "" + j + ": " + ChatColor.GRAY + ((String)days.get(i).get(j)).replaceAll("&", "§"));
	    		}
	    		if(i == 2) {
	    			return true;
	    		}
    	  }
	      return true;
    	}
  		else {
  			sender.sendMessage(ChatColor.RED + "You do not have permission for this!");
        return true;
  		}
    }
    if(args.length == 1 &&
    	 StringUtils.isNumeric(args[0])) {
    	if(sender.hasPermission("changelog.view")) {
    		int arg1 = Integer.parseInt(args[0]);
    		if(arg1 >= days.size()) {
    			sender.sendMessage(ChatColor.RED + "There are not that many changelog pages!");
    			return true;
    		}
    		sender.sendMessage(ChatColor.RED + "Date: " + days.get(arg1).get(0) + ChatColor.GRAY + " (" + arg1  + ")");
    		for(int j = 1; j < days.get(arg1).size(); j++) {
    			sender.sendMessage(ChatColor.GOLD + "" + j + ": " + ChatColor.GRAY + ((String)days.get(arg1).get(j)).replaceAll("&", "§"));
    		}
    		return true;
    	}
  		else {
  			sender.sendMessage(ChatColor.RED + "You do not have permission for this!");
        return true;
  		}
    }
    if(args.length >= 1) {
    	if(args[0].equalsIgnoreCase("help")) {
    		if(sender.hasPermission("changelog.edit")) {
	        sender.sendMessage(ChatColor.YELLOW + "/changelog add [message]");
	        sender.sendMessage(ChatColor.YELLOW + "/changelog addto [day index] [message]");
	        sender.sendMessage(ChatColor.YELLOW + "/changelog remove [day index] [index]");
	        sender.sendMessage(ChatColor.YELLOW + "/changelog removeday [day index]");
	        sender.sendMessage(ChatColor.YELLOW + "/changelog removedays [day index]");
	        return true;
    		}
    		else {
    			sender.sendMessage(ChatColor.RED + "You do not have permission for this!");
          return true;
    		}
    	}
    	if(args[0].equalsIgnoreCase("add")) {
    		if(sender.hasPermission("changelog.edit")) {
		  		if(args.length > 1) {
		  			String log = args[1];
		  			for(int i = 2; i < args.length; i++) {
		  				log += " " + args[i];
		  			}
						if(main.getFM().addLog(log)) {
							sender.sendMessage("Successfully added log!");
							return true;
						}
		    		return false;
		  		}
    		}
    		else {
    			sender.sendMessage(ChatColor.RED + "You do not have permission for this!");
    			return true;
    		}
    	}
    	if(args[0].equalsIgnoreCase("addto")) {
    		if(sender.hasPermission("changelog.edit")) {
	    		if(args.length > 2 &&
	    			 StringUtils.isNumeric(args[1])) {
    				int arg1 = Integer.parseInt(args[1]);
    				String log = args[2];
    				for(int i = 3; i < args.length; i++) {
    					log += " " + args[i];
    				}
	    			if(main.getFM().addLog(log, arg1)) {
	    				sender.sendMessage("Successfully added log!");
	    				return true;
	    			}
	    			return false;
	    		}
    		}
    		else {
    			sender.sendMessage(ChatColor.RED + "You do not have permission for this!");
          return true;
    		}
    	}
    	if(args[0].equalsIgnoreCase("remove")) {
    		if(sender.hasPermission("changelog.edit")) {
	    		if(args.length == 3) {
	    			if(StringUtils.isNumeric(args[1]) &&
	    				 StringUtils.isNumeric(args[2])) {
	    				int arg1 = Integer.parseInt(args[1]);
	    				int arg2 = Integer.parseInt(args[2]);
	    				if(main.getFM().removeLog(arg1, arg2)) {
	    					sender.sendMessage("Successfully removed log!");
	    					return true;
	    				}
	    				return false;
	    			}
	    		}
    		}
    		else {
    			sender.sendMessage(ChatColor.RED + "You do not have permission for this!");
    			return true;
    		}
    	}
    	if(args[0].equalsIgnoreCase("removeday")) {
    		if(sender.hasPermission("changelog.edit")) {
    			if(args.length == 2) {
    				if(StringUtils.isNumeric(args[1])) {
    					int arg1 = Integer.parseInt(args[1]);
    					if(main.getFM().removeDay(arg1)) {
	    					sender.sendMessage("Successfully removed day!");
	    					return true;
    					}
    					return false;
    				}
    			}
    		}
    		else {
    			sender.sendMessage(ChatColor.RED + "You do not have permission for this!");
    			return true;
    		}
    	}
    	if(args[0].equalsIgnoreCase("removedays")) {
    		if(sender.hasPermission("changelog.edit")) {
	    		if(args.length == 2) {
	    			if(StringUtils.isNumeric(args[1])) {
	    				int arg1 = Integer.parseInt(args[1]);
	    		    if(main.getFM().removeDays(arg1)) {
	    					sender.sendMessage("Successfully removed days!");
	    		    	return true;
	    		    }
	    		    return false;
	    			}
	    		}
    		}
    		else {
    			sender.sendMessage(ChatColor.RED + "You do not have permission for this!");
    			return true;
    		}
    	}
    }
    return false;
	}
}
