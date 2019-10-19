package me.neoblade298.neoshinies;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Commands
  implements CommandExecutor
{
  public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args)
  {
    if (sender.isOp())
    {
      String message = "&4[&c&lMLMC&4] &e" + args[0] + "&7";
      for (int i = 1; i < args.length; i++)
      {
        message = message + " ";
        message = message + args[i];
      }
      message = message + "&7!";
      message = message.replaceAll("&", "§");
      Bukkit.broadcastMessage(message);
      return true;
    }
    return false;
  }
}
