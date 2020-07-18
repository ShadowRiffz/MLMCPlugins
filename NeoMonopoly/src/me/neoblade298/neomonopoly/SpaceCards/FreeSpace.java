package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.GamePlayer;

public class FreeSpace implements Space {
	public void onLand(GamePlayer lander, int dice) {
		return;
	}
	
	public void onStart() {
		return;
	}

	@Override
	public ChatColor getColor() {
		// TODO Auto-generated method stub
		return ChatColor.WHITE;
	}

	@Override
	public void setColor(ChatColor color) {
		return;
	}

}
