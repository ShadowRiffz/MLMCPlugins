package me.neoblade298.neocore.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
}
