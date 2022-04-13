package me.Neoblade298.NeoProfessions.Commands;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Inventories.GardenSelectInventory;
import me.Neoblade298.NeoProfessions.Managers.ProfessionManager;
import me.Neoblade298.NeoProfessions.PlayerProfessions.Profession;
import me.Neoblade298.NeoProfessions.PlayerProfessions.ProfessionType;


public class GardensCommand implements CommandExecutor {
	Professions main;
	
	public GardensCommand(Professions main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0 && sender instanceof Player) {
			new GardenSelectInventory(p);
			return true;
		}
		return false;
	}
}