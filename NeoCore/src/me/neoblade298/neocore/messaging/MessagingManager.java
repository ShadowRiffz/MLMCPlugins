package me.neoblade298.neocore.messaging;

import org.bukkit.configuration.ConfigurationSection;

import me.neoblade298.neocore.util.Util;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;

public class MessagingManager {
	public static BaseComponent[] parseMessage(ConfigurationSection cfg) {
		boolean firstKey = true;
		ComponentBuilder builder = null;
		for (String key : cfg.getKeys(false)) {
			ConfigurationSection sec = cfg.getConfigurationSection(key);
			if (firstKey) {
				firstKey = false;
				builder = new ComponentBuilder(Util.translateColors(sec.getString("text")));
			}
			else {
				builder.append(Util.translateColors(sec.getString("text")), FormatRetention.NONE);
			}
			
			if (sec.getString("suggest") != null) {
				builder.event(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, sec.getString("suggest")));
			}
			
			if (sec.getString("run") != null) {
				builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, sec.getString("run")));
			}
			
			if (sec.getString("hover") != null) {
				builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(Util.translateColors(sec.getString("hover")))));
			}
		}
		return builder.create();
	}
}
