package me.neoblade298.neoquests.actions;

import org.bukkit.entity.Player;

import me.neoblade298.neocore.io.LineConfigParser;

public interface Action extends LineConfigParser<Action> {
	public default void run(Player p) {};
	public String getKey();
}
