package me.Neoblade298.NeoProfessions.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.Neoblade298.NeoProfessions.Professions;
import me.Neoblade298.NeoProfessions.Inventories.RecipeSelectInventory;


public class CraftCommand implements CommandExecutor {
	Professions main;
	
	public CraftCommand(Professions main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0 && sender instanceof Player) {
			new RecipeSelectInventory((Player) sender);
			return true;
		}
		return false;
	}
}