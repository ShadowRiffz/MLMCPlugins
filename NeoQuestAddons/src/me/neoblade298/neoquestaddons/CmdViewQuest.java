package me.neoblade298.neoquestaddons;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;


public class CmdViewQuest implements CommandExecutor{
	
	QuestAddons main;
	ArrayList<String> validAttrs;
	
	public CmdViewQuest(QuestAddons main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		// /viewquest [player]
		if (args.length == 0) {
			sender.sendMessage("§c/viewquest [player]");
		}
		if (args.length == 1) {
			Player viewed = Bukkit.getPlayer(args[0]);
			if (viewed instanceof Player) {
				Quester quester = main.getQuests().getQuester(viewed.getUniqueId());
				ConcurrentHashMap<Quest, Integer> map = quester.getCurrentQuests();
				for (Quest q : map.keySet()) {
					sender.sendMessage("§6---(" + q.getName() + ")---");
					for (String line : quester.getCurrentObjectives(q, false)) {
						sender.sendMessage(line);
					}
				}
				return true;
			}
		}
		return false;
	}
}