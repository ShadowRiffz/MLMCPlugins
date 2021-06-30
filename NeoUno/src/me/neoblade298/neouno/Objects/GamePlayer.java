package me.neoblade298.neouno.Objects;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import me.neoblade298.neouno.Cards.Card;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GamePlayer {

	private Game game;
	private ArrayList<Card> cards;
	private int points;
	private UUID player;
	private boolean calledUno;

	public GamePlayer(Game game, UUID uuid) {
		this.game = game;
		this.cards = new ArrayList<Card>();
		this.player = uuid;
		this.points = 0;
		this.calledUno = false;
	}

	public void message(String msg) {
		String message = new String("&4[&c&lMLMC&4] &7" + msg).replaceAll("&", "§");
		Player p = Bukkit.getPlayer(player);
		p.sendMessage(message);
	}
	
	public void showHandOld() {
		if (game.curr.equals(this)) {
			message("&lYour turn! &7Top card is: " + game.topCard.getDisplay());
		}
		else {
			message("&f" + game.curr + "'s &7turn! Top card is: " + game.topCard.getDisplay());
		}
		String msg = new String("&7Hand: ");
		for (Card card : this.cards) {
			msg += card.getColor() + "" + card.getDisplay() + " ";
		}
		message(msg);
	}
	
	public void showHand() {
		if (game.curr.equals(this)) {
			message("&lYour turn! Top card is: " + game.topCard.getDisplay());
		}
		else {
			message("&f" + game.curr + "'s &7turn! Top card is: " + game.topCard.getDisplay());
		}
		
		ComponentBuilder builder = new ComponentBuilder("Hand: ").color(net.md_5.bungee.api.ChatColor.GRAY);
		for (Card card : this.cards) {
			builder.append(new TextComponent(card.getDisplay()));
			switch (card.getColor()) {
			case DARK_GREEN:	builder.color(net.md_5.bungee.api.ChatColor.DARK_GREEN);
			break;
			case RED:			builder.color(net.md_5.bungee.api.ChatColor.RED);
			break;
			case YELLOW:		builder.color(net.md_5.bungee.api.ChatColor.YELLOW);
			break;
			case BLUE:			builder.color(net.md_5.bungee.api.ChatColor.BLUE);
			break;
			default:			break;
			}
			builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to play!").create()));
			builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/uno play " + card.getAlias()));
			builder.append(new TextComponent(" "));
		}
		builder.append(new TextComponent("|| (Draw Card)")).color(net.md_5.bungee.api.ChatColor.WHITE)
		.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to draw card!").create()))
		.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/uno draw"));
		Bukkit.getPlayer(player).spigot().sendMessage(builder.create());
	}

	public boolean equals(GamePlayer gp) {
		return this.player.equals(gp.getPlayer());
	}

	public ArrayList<Card> getCards() {
		return cards;
	}

	public void setCards(ArrayList<Card> cards) {
		this.cards = cards;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public UUID getPlayer() {
		return player;
	}

	public void setPlayer(UUID uuid) {
		this.player = uuid;
	}
	
	public String toString() {
		return Bukkit.getPlayer(player).getName();
	}

	public boolean calledUno() {
		return calledUno;
	}

	public void setCalledUno(boolean calledUno) {
		this.calledUno = calledUno;
	}
}
