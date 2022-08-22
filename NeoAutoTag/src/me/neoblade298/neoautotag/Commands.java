package me.neoblade298.neoautotag;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import net.md_5.bungee.api.ChatColor;


public class Commands implements CommandExecutor{
	
	Main main;
	
	public Commands(Main main) {
		this.main = main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
		// /autotag [player] [tag]
		if (args.length >= 2 && sender.isOp()) {
			String tag = args[1] + " ";
			for (int i = 2; i < args.length; i++) {
				tag += args[i] + " ";
			}
			String internalTag = tag.replaceAll("&", "§").replaceAll(" ", "").toLowerCase();
			internalTag = ChatColor.stripColor(internalTag);
			File file = new File("/home/MLMC/ServerTowny/plugins/DeluxeTags/config.yml");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			// First check for next order number
			ConfigurationSection tags = cfg.getConfigurationSection("deluxetags");
			int order = cfg.getInt("order");
			
			int version = 2;
			if (tags.contains(internalTag)) {
				// If internal tag is used, and actual tag is the same
				ConfigurationSection tagSec = tags.getConfigurationSection(internalTag);
				if (tagSec.getString("tag").equals(tag)) {
					Bukkit.getLogger().log(Level.INFO, "[AutoTag] Gave player " + args[0] + " existing tag " + internalTag);
					String lpcmd = "lp user " + args[0] + " permission set deluxetags.tag." + internalTag;
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), lpcmd);
					return true;
				}
				
				// If internal tag is used, but actual tag is different (color codes)
				else {
					while (tags.contains(internalTag + version)) {
						// If internal tag is versioned but used, and actual tag is the same
						tagSec = tags.getConfigurationSection(internalTag + version);
						if (tagSec.getString("tag").equals(tag)) {
							Bukkit.getLogger().log(Level.INFO, "[AutoTag] Gave player " + args[0] + " existing tag " + internalTag + version);
							String lpcmd = "lp user " + args[0] + " permission set deluxetags.tag." + internalTag + version;
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), lpcmd);
							return true;
						}
						else {
							version++;
						}
					}
					
				}
			}

			// Need a brand new tag, found a non-used version
			String lpcmd = "lp user " + args[0] + " permission set deluxetags.tag." + internalTag + version;
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), lpcmd);
			ConfigurationSection newTag = tags.createSection(internalTag + version);
			Bukkit.getLogger().log(Level.INFO, "[AutoTag] Gave player " + args[0] + " new tag " + internalTag + version);
			newTag.set("order", order);
			newTag.set("tag", tag.replaceAll("§", "&"));
			newTag.set("description", "&7Donated for a custom tag!");
			cfg.set("order", order + 1);
			try {
				cfg.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tags reload");
			
			if (Bukkit.getPlayer(args[0]) != null) {
				Bukkit.getPlayer(args[0]).sendMessage("§4[§c§lMLMC§4] §7Successfully gave " + tag.replaceAll("&", "§") + "§7tag! Do §c/tags§7!");
			}
		}
		return true;
	}
}