package me.neoblade298.neouno.Cards;

import org.bukkit.ChatColor;

public interface Card {
	public ChatColor getColor();
	public void setColor(ChatColor color);
	public int getNumber();
	public void setNumber(int number);
	public String getDisplay();
	public void onPlay();
	public void onDraw(boolean normalDraw);
}
