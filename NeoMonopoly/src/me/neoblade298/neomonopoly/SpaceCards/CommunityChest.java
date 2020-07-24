package me.neoblade298.neomonopoly.SpaceCards;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class CommunityChest implements Space {
	private Game game;
	
	public CommunityChest(Game game) {
		this.game = game;
	}

	@Override
	public void onLand(GamePlayer lander, int dice) {
		game.drawChest(lander);
		game.isBusy = false;
	}
	
	public char getMapChar() {
		return 'o';
	}
	
	@Override
	public void addComponent(ComponentBuilder builder, ArrayList<GamePlayer> players) {
		if (players.size() > 1) {
			builder.append(new TextComponent("@"));
		}
		else if (players.size() == 1) {
			builder.append(new TextComponent(Character.toString(players.get(0).mapChar)));
		}
		else {
			builder.append(new TextComponent(Character.toString(getMapChar()).toUpperCase()));
		}
		builder.color(game.main.spigotToBungee.get(getColor()));

		ComponentBuilder hoverBuild = new ComponentBuilder("[").color(net.md_5.bungee.api.ChatColor.GRAY)
			.append(new TextComponent("Community Chest")).color(game.main.spigotToBungee.get(getColor()))
			.append(new TextComponent("]")).color(net.md_5.bungee.api.ChatColor.GRAY);
		
		if (players.size() > 0) {
			builder.underlined(true).italic(true);
			hoverBuild.append(new TextComponent("\nPlayers on Space:")).color(net.md_5.bungee.api.ChatColor.GRAY);
			for (GamePlayer gp : players) {
				hoverBuild.append(new TextComponent("\n- ")).color(net.md_5.bungee.api.ChatColor.GRAY)
				.append(new TextComponent(gp.toString())).color(net.md_5.bungee.api.ChatColor.GRAY);
			}
		}
		builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBuild.create()));
		builder.append(new TextComponent(" ")).reset();
	}

	@Override
	public void onStart(GamePlayer starter) {
		game.requiredActions.get(starter).add("ROLL_MOVE");
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.WHITE;
	}

	@Override
	public void setColor(ChatColor color) {
		return;
	}

	@Override
	public Game getGame() {
		return this.game;
	}

	@Override
	public void setGame(Game game) {
		this.game = game;
	}
	
	@Override
	public void displayProperty(GamePlayer gp) {
		gp.message("&7[&fCommunity Chest&7]");
	}
	
	@Override
	public String getShorthand(GamePlayer gp) {
		return "&7[&fCommunity Chest&7]";
	}

}
