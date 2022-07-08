package me.ShanaChans.SellAll.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.ShanaChans.SellAll.SellAllManager;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;

public class SellAllCommand implements Subcommand
{

	@Override
	public String getDescription() 
	{
		return "Sell all your riches!";
	}

	@Override
	public String getKey() 
	{
		return "";
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
		
		SellAllManager.getPlayers().get(player.getUniqueId()).sellAll(player.getInventory(), player);
	}

}
