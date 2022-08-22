package me.neoblade298.neochatmod;

import org.bukkit.Bukkit;
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
		if (args.length == 0 && sender.hasPermission("mycommand.staff")) {
			if (this.main.getMute()) {
				Bukkit.broadcastMessage("§4[§c§lMLMC§4] §7The server chat has been unmuted!");
			}
			else {
				for (int i = 0; i < 15; i++) {
					Bukkit.broadcastMessage("");
				}
				Bukkit.broadcastMessage("§4[§c§lMLMC§4] §7The server chat has been muted!");
			}
			this.main.toggleMute();
			return true;
		}
		return false;
	}
}