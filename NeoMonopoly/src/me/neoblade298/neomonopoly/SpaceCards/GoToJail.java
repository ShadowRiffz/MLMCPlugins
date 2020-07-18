package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class GoToJail implements Space {

	@Override
	public void onLand(GamePlayer lander, int dice) {
		
	}

	@Override
	public void onStart() {
		
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.WHITE;
	}

	@Override
	public void setColor(ChatColor color) {
		return;
	}
}
