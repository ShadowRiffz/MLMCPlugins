package me.neoblade298.neomonopoly.SpaceCards;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
import net.md_5.bungee.api.chat.ComponentBuilder;

public interface Space {
	public void onLand(GamePlayer lander, int dice);
	public void onStart(GamePlayer starter);
	public ChatColor getColor();
	public void setColor(ChatColor color);
	public Game getGame();
	public void setGame(Game game);
	public void displayProperty(GamePlayer gp);
	public String getShorthand(GamePlayer gp);
	public char getMapChar();
	public void addComponent(ComponentBuilder builder, ArrayList<GamePlayer> players);
}
