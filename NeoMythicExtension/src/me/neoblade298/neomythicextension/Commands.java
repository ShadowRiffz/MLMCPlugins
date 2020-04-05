package me.neoblade298.neomythicextension;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;


public class Commands implements CommandExecutor{
	Main main;
	
	public Commands (Main main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
	    if(args.length == 2 && args[0].equalsIgnoreCase("debug") && (sender.isOp() || sender.hasPermission("mycommand.staff"))) {
	    	sender.sendMessage("Global score of " + args[1] + " is " + main.globalscores.get(args[1]));
	    	return true;
	    }
	    if (args.length > 1 && args[0].equalsIgnoreCase("nbc") && (sender.isOp() || sender.hasPermission("mycommand.staff"))) {
	    	UUID uuid = UUID.fromString(args[1]);
	        Optional<ActiveMob> am = MythicMobs.inst().getMobManager().getActiveMob(uuid);
    		ArrayList<Entity> near = (ArrayList<Entity>) am.get().getEntity().getBukkitEntity().getNearbyEntities(40, 40, 40);
    		String msg = "";
    		for (int i = 2; i < args.length; i++) {
    			msg += args[i];
    			if (i != args.length - 1) {
    				msg += " ";
    			}
    		}
    		for(Entity e : near) {
    			if (e instanceof Player) {
    				Player p = (Player) e;
    				p.sendMessage(msg);
    			}
    		}
	    }
    	return false;
	}
}
