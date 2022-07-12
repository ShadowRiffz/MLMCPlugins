package me.neoblade298.neocore.util;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import com.google.common.collect.SortedMultiset;
import com.google.common.collect.TreeMultiset;

import net.md_5.bungee.api.ChatColor;

public class Util {
	public static final Pattern HEX_PATTERN = Pattern.compile("&(#[A-Fa-f0-9]{6})");
	
	public static void msg(CommandSender s, String msg) {
		msg(s, msg, true);
	}
	
	public static void msg(CommandSender s, String msg, boolean hasPrefix) {
		if (hasPrefix) {
			msg = "&4[&c&lMLMC&4] &7" + msg;
		}
		s.sendMessage(translateColors(msg));
	}

	public static String translateColors(String textToTranslate) {

		Matcher matcher = HEX_PATTERN.matcher(textToTranslate);
		StringBuffer buffer = new StringBuffer();

		while (matcher.find()) {
			matcher.appendReplacement(buffer, ChatColor.of(matcher.group(1)).toString());
		}

		return ChatColor.translateAlternateColorCodes('&', matcher.appendTail(buffer).toString());
	}
	
	public static <T extends Comparable<T>> SortedMultiset<T> getTop(Collection<T> list, int num, boolean descending) {
		TreeMultiset<T> sorted = TreeMultiset.create();
		for (T item : list) {
			sorted.add(item);
			if (sorted.size() > num) {
				if (descending) {
					sorted.pollFirstEntry();
				}
				else {
					sorted.pollLastEntry();
				}
			}
		}
		return descending ? sorted.descendingMultiset() : sorted;
	}
	
	public Location stringToLoc(String loc) {
		String args[] = loc.split(" ");
		World w = Bukkit.getWorld(args[0]);
		double x = Double.parseDouble(args[1]);
		double y = Double.parseDouble(args[2]);
		double z = Double.parseDouble(args[3]);
		return new Location(w, x, y, z);
	}
	
	public String locToString(Location loc) {
		return loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ();
	}
}
