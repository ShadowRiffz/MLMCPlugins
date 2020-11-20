package me.neoblade298.neoautotag;

import java.io.File;
import java.io.IOException;

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
		if (args.length > 2) {
			String tag = args[1] + " ";
			for (int i = 2; i < args.length; i++) {
				tag += args[i] + " ";
			}
			String internalTag = tag.replaceAll("&", "§").replaceAll(" ", "").toLowerCase();
			internalTag = ChatColor.stripColor(internalTag);
			File file = new File("/home/MLMC/ServerTowny/plugins/DeluxeTags");
			YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
			
			// First check for next order number
			ConfigurationSection tags = cfg.getConfigurationSection("deluxetags");
			int order = cfg.getInt("order");
			
			int version = 2;
			if (tags.contains(internalTag)) {
				// If internal tag is used, and actual tag is the same
				ConfigurationSection tagSec = tags.getConfigurationSection(internalTag);
				if (tagSec.getString("tag").equals(tag)) {
					String lpcmd = "lp user " + args[0] + " permission set deluxetags.tag." + internalTag;
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), lpcmd);
					return true;
				}
				
				// If internal tag is used, but actual tag is different (color codes)
				else {
					while (tags.contains(internalTag + version)) {
						tagSec = tags.getConfigurationSection(internalTag + version);
						if (tagSec.getString("tag").equals(tag)) {
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

			// Found a non-used version
			ConfigurationSection newTag = null;
			if (version == 1) {
				String lpcmd = "lp user " + args[0] + " permission set deluxetags.tag." + internalTag;
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), lpcmd);
				newTag = tags.createSection(internalTag);
			}
			else {
				String lpcmd = "lp user " + args[0] + " permission set deluxetags.tag." + internalTag + version;
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), lpcmd);
				newTag = tags.createSection(internalTag + version);
			}
			newTag.set("order", order);
			newTag.set("tag", tag);
			newTag.set("desc", "&7Donated for a custom tag!");
			cfg.set("order", order + 1);
			try {
				cfg.save(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tags reload");
			
		}
		return true;
	}
}