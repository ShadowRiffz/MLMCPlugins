package me.neoblade298.neoquestaddons;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentSkipListSet;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.blackvein.quests.Quest;
import me.blackvein.quests.Quester;


public class CmdRefundQuests implements CommandExecutor{
	
	QuestAddons main;
	ArrayList<String> validAttrs;
	
	public CmdRefundQuests(QuestAddons main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		if (args.length == 0) {
			getRefund(sender, (Player) sender);
			return true;
		}
		if (args.length == 1) {
			getRefund(sender, Bukkit.getPlayer(args[0]));
			return true;
		}
		return false;
	}
	
	private void getRefund(CommandSender viewer, Player viewed) {
		int amount = 0;
		if (viewed instanceof Player) {
			Quester quester = main.getQuests().getQuester(viewed.getUniqueId());
			ConcurrentSkipListSet<Quest> list = quester.getCompletedQuests();
			for (Quest q : list) {
				amount += q.getRewards().getMoney();
			}
		}
		viewer.sendMessage("§4[§c§lMLMC§4] §6" + viewed.getName() + " §7requires §e" + amount + "g §7to refund their quests.");
	}
}