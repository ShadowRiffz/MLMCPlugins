package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

public interface Space {
	public void onLand(GamePlayer lander, int dice);
	public void onStart(GamePlayer starter);
	public ChatColor getColor();
	public void setColor(ChatColor color);
	public Game getGame();
	public void setGame(Game game);
	public void displayProperty(GamePlayer gp);
	public String getShorthand(GamePlayer gp);
}
