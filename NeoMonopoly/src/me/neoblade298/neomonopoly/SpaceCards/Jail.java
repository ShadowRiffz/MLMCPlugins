package me.neoblade298.neomonopoly.SpaceCards;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Jail implements Space {
	private Game game;
	
	public Jail(Game game) {
		this.game = game;
	}

	@Override
	public void onLand(GamePlayer lander, int dice) {
		game.isBusy = false;
	}

	@Override
	public void onStart(GamePlayer starter) {
		if (starter.isJailed()) {
			game.requiredActions.get(starter).add("JAIL_ACTION");
		}
		else {
			game.requiredActions.get(starter).add("ROLL_MOVE");
		}
	}
	
	public char getMapChar() {
		return 'J';
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
		String msg = gp.isJailed() ? "&cIn Jail" : "&6Just Visiting Jail";
		gp.message("&7[" + msg + "&7]");
	}

	@Override
	public String getShorthand(GamePlayer gp) {
		String msg = gp.isJailed() ? "&cIn Jail" : "&6Just Visiting Jail";
		return "&7[" + msg + "&7]";
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
			.append(new TextComponent("Jail")).color(game.main.spigotToBungee.get(getColor()))
			.append(new TextComponent("]")).color(net.md_5.bungee.api.ChatColor.GRAY);
		
		if (players.size() > 0) {
			builder.underlined(true).italic(true);
			ArrayList<GamePlayer> visiting = new ArrayList<GamePlayer>();
			ArrayList<GamePlayer> jailed = new ArrayList<GamePlayer>();
			for (GamePlayer gp : players) {
				if (!gp.isJailed()) {
					visiting.add(gp);
				}
				else {
					jailed.add(gp);
				}
			}
			
			if (visiting.size() > 0) {
				hoverBuild.append(new TextComponent("\nPlayers visiting:")).color(net.md_5.bungee.api.ChatColor.GRAY);
				for (GamePlayer gp : visiting) {
					hoverBuild.append(new TextComponent("\n- ")).color(net.md_5.bungee.api.ChatColor.GRAY)
					.append(new TextComponent(gp.toString())).color(net.md_5.bungee.api.ChatColor.GRAY);
				}
			}
			if (jailed.size() > 0) {
				hoverBuild.append(new TextComponent("\nPlayers in jail:")).color(net.md_5.bungee.api.ChatColor.GRAY);
				for (GamePlayer gp : jailed) {
					hoverBuild.append(new TextComponent("\n- ")).color(net.md_5.bungee.api.ChatColor.GRAY)
					.append(new TextComponent(gp.toString())).color(net.md_5.bungee.api.ChatColor.GRAY);
				}
			}
		}
		builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBuild.create()));
		builder.append(new TextComponent(" ")).reset();
	}

}
