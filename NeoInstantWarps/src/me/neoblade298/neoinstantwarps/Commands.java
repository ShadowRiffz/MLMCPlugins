package me.neoblade298.neoinstantwarps;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.commands.WarpNotFoundException;

import net.ess3.api.InvalidWorldException;


public class Commands implements CommandExecutor{
	private Essentials ess = null;

	public Commands (Main plugin, Essentials essentials) {
		this.ess = essentials;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
	    if(args.length == 2 && (sender.isOp() || sender.hasPermission("mycommand.staff"))) {
	    	try {
				Location loc = ess.getWarps().getWarp(args[0]);
				Player p = Bukkit.getPlayer(args[1]);
				if (p != null) {
					p.teleport(loc);
				}
				else {
					sender.sendMessage("§4[§c§lMLMC§4] §cError: Player not found");
				}
			} catch (WarpNotFoundException e) {
				sender.sendMessage("§4[§c§lMLMC§4] §cError: Warp not found");
				e.printStackTrace();
			} catch (InvalidWorldException e) {
				sender.sendMessage("§4[§c§lMLMC§4] §cError: Invalid world");
				e.printStackTrace();
			}
	    }
    	return false;
	}
}
