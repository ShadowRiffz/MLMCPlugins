package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.GamePlayer;

public interface Space {
	public void onLand(GamePlayer lander, int dice);
	public void onStart();
	public ChatColor getColor();
	public void setColor(ChatColor color);
}
