package me.ShanaChans.SellAll.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.SellAll.Items.Items;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class SellAllGive implements Subcommand
{
	@Override
	public String getDescription() 
	{
		return "Give yourself a sell wand";
	}

	@Override
	public String getKey() 
	{
		return "give";
	}

	@Override
	public String getPermission() 
	{
		return null;
	}

	@Override
	public SubcommandRunner getRunner() 
	{
		return SubcommandRunner.PLAYER_ONLY;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) 
	{
		Player player = (Player) sender;
		
		if(args.length > 0)
		{
			if(Bukkit.getPlayer(args[0]) != null && Bukkit.getPlayer(args[0]).isOnline())
			{
				Bukkit.getPlayer(args[0]).getInventory().addItem(Items.getChestSellStick());
			}
		}
		else
		{
			player.getInventory().addItem(Items.getChestSellStick());
		}
	}
}
