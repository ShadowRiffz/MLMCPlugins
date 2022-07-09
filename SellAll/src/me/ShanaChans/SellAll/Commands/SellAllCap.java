package me.ShanaChans.SellAll.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.SellAll.SellAllManager;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class SellAllCap implements Subcommand
{

	@Override
	public String getDescription() 
	{
		return "Check the sell limits for your materials!";
	}

	@Override
	public String getKey() 
	{
		return "limit";
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
				SellAllManager.getPlayers().get(Bukkit.getPlayer(args[0]).getUniqueId()).getSellCap(player, Bukkit.getPlayer(args[0]));
			}
		}
		else
		{
			SellAllManager.getPlayers().get(player.getUniqueId()).getSellCap(player, player);
		}
	}

}
