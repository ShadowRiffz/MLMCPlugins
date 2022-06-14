package me.neoblade298.neoquestaddons;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;


public class CmdQuitQuest implements CommandExecutor{
	
	QuestAddons main;
	ArrayList<String> validAttrs;
	
	public CmdQuitQuest(QuestAddons main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				Quester quester = main.getQuests().getQuester(p.getUniqueId());
				ConcurrentHashMap<Quest, Integer> map = quester.getCurrentQuests();
				for (Quest q : map.keySet()) {
					quester.quitQuest(q, new String("&4[&c&lMLMC&4] &7Successfully quit &6" + q.getName() + "&7!").replaceAll("&", "§"));
				}
				return true;
			}
		}
		return false;
	}
}