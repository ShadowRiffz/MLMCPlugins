package me.ShanaChans.SellAll.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.SellAll.SellAllManager;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class SellAllReload implements Subcommand
{
	@Override
	public String getDescription() 
	{
		return "Reload the config";
	}

	@Override
	public String getKey() 
	{
		return "reload";
	}

	@Override
	public String getPermission() 
	{
		return "sellall.reload";
	}

	@Override
	public SubcommandRunner getRunner() 
	{
		return SubcommandRunner.PLAYER_ONLY;
	}
	
	@Override
	public void run(CommandSender sender, String[] args) 
	{	
		SellAllManager.reloadConfig(null);
	}

}
