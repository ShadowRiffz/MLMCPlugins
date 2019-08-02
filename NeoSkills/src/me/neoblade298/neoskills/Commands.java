package me.neoblade298.neoskills;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands
  implements CommandExecutor
{
  public Commands(Main plugin) {}
  
  public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
  {
    if ((sender.hasPermission("neoskills.use")) && 
      (args.length == 3))
    {
      Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "class forcecast " + args[2] + " " + args[0] + " " + args[1].substring(0, args[1].length() - 2));
      return true;
    }
    System.out.println("Neoskills something went wrong.");
    return true;
  }
}
