package me.neoblade298.neomonopoly.SpaceCards;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Railroad implements Property {
	private String name;
	private GamePlayer owner;
	private Game game;
	private boolean isMortgaged;
	private int[] rent;
	private int price;

	private ChatColor color;
	
	public Railroad (String name, int[] rent, ChatColor color, Game game, int price) {
		this.owner = null;
		this.isMortgaged = false;
		this.name = name;
		this.rent = rent;
		this.setColor(color);
		this.game = game;
		this.price = price;
	}

	@Override
	public void onLand(GamePlayer lander, int dice) {
		// Auction or purchase
		if (owner == null) {
			game.requiredActions.get(lander).add(0, "UNOWNED_SPACE");
			game.broadcast("&7This space is unowned! You may buy it with &c/mono buy &7or auction it with &c/mono auction&7.");
		}
		else {
			if (!owner.equals(lander)) {
				if (isMortgaged) {
					game.broadcast("&7This space is mortgaged! No rent needed.");
				}
				else {
					game.billPlayer(lander, calculateRent(dice), owner);
				}
			}
		}
		game.isBusy = false;
	}
	
	public void onRNGLand(GamePlayer lander) {
		if (owner == null) {
			game.requiredActions.get(lander).add(0, "UNOWNED_SPACE");
			game.broadcast("&7This space is unowned! You may buy it with &c/mono buy &7or auction it with &c/mono auction&7.");
		}
		else {
			if (!owner.equals(lander)) {
				if (isMortgaged) {
					game.broadcast("&7This space is mortgaged! No rent needed.");
				}
				else {
					game.billPlayer(lander, calculateRent(0), owner);
				}
			}
		}
		game.isBusy = false;
	}
	
	public char getMapChar() {
		return 'x';
	}

	@Override
	public void onStart(GamePlayer starter) {
		game.requiredActions.get(starter).add("ROLL_MOVE");
	}
	
	public GamePlayer getOwner() {
		return owner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner(GamePlayer owner) {
		this.owner = owner;
	}
	
	public boolean canMortgage() {
		return true;
	}

	public boolean isMortgaged() {
		return isMortgaged;
	}

	public void setMortgaged(boolean isMortgaged) {
		this.isMortgaged = isMortgaged;
	}

	public int[] getRent() {
		return rent;
	}

	public void setRent(int[] rent) {
		this.rent = rent;
	}

	@Override
	public ChatColor getColor() {
		return color;
	}

	@Override
	public void setColor(ChatColor color) {
		this.color = color;
	}

	@Override
	public void onOwned(GamePlayer owner) {
		owner.setNumRailroads(owner.getNumRailroads() + 1);
		game.broadcast("&e" + owner + " &7now owns &e" + owner.getNumRailroads() + " &7railroads.");
	}

	@Override
	public void onUnowned(GamePlayer formerOwner) {
		owner.setNumRailroads(owner.getNumRailroads() - 1);
		game.broadcast("&e" + formerOwner + " &7now owns &e" + owner.getNumRailroads() + " &7railroads.");
	}

	@Override
	public void onBankrupt(GamePlayer formerOwner) {
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
		String ownerName = owner == null ? "Unowned" : owner.toString();
		gp.message("&7[" + color + name + "&7 (" + ownerName + "&7)]");
		gp.message("&7Value: &e" + price);
		if (!isMortgaged) {
			String msg = new String();
			for (int i = 1; i <= 4; i++) {
				if (owner != null && owner.getNumRailroads() == i) {
					msg += "&e";
				}
				else {
					msg += "&7";
				}
				if (i == 0) {
					msg += rent[i - 1];
				}
				else {
					msg += " " + rent[i - 1];
				}
			}
			gp.message("&7Rent&7: " + msg);
		}
		else {
			gp.message("&cCurrently mortgaged. Price to unmortgage: &e" + price);
		}
	}
	
	@Override
	public String getShorthand(GamePlayer gp) {
		String ownerName = owner == null ? "&a$" + price : owner.toString();
		if (isMortgaged) {
			return "&7&m[" + color + "&m" + name + "&7&m (" + ownerName + "&7&m)]&7";
		}
		else {
			return "&7[" + color + name + "&7 (" + ownerName + "&7)]";
		}
	}
	
	@Override
	public String getColoredName() {
		if (isMortgaged) {
			return "&7&m[" + color + "&m" + name + "&7&m)]&7";
		}
		else {
			return "&7[" + color + name + "&7)]";
		}
	}
	
	@Override
	public String listComponent() {
		if (isMortgaged) {
			return "&7&m[" + color + "&m" + name + "&7&m] Rent: &e&m" + calculateRent(0);
		}
		else {
			return "&7[" + color + name + "&7] Rent: &e" + calculateRent(0);
		}
	}

	@Override
	public int calculateRent(int dice) {
		return rent[owner.getNumRailroads() - 1];
	}

	@Override
	public int getPrice() {
		return price;
	}

	@Override
	public void setPrice(int price) {
		this.price = price;
	}
	
	@Override
	public void addComponent(ComponentBuilder builder, ArrayList<GamePlayer> players) {
		if (players.size() > 1) {
			builder.append(new TextComponent("@"));
		}
		else if (players.size() == 1) {
			builder.append(new TextComponent(Character.toString(players.get(0).mapChar)));
		}
		else if (owner == null) {
			builder.append(new TextComponent("x"));
		}
		else {
			builder.append(new TextComponent(Character.toString(owner.mapChar).toUpperCase()));
		}
		builder.color(game.main.spigotToBungee.get(color));

		ComponentBuilder hoverBuild = new ComponentBuilder("[").color(net.md_5.bungee.api.ChatColor.GRAY)
				.append(new TextComponent(name)).color(game.main.spigotToBungee.get(color));
		
			if (owner != null) {
				hoverBuild.append(new TextComponent(" (")).color(net.md_5.bungee.api.ChatColor.GRAY)
				.append(new TextComponent(owner.toString())).color(net.md_5.bungee.api.ChatColor.GRAY)
				.append(new TextComponent(")")).color(net.md_5.bungee.api.ChatColor.GRAY);
			}
			hoverBuild.append(new TextComponent("]")).color(net.md_5.bungee.api.ChatColor.GRAY)
			.append(new TextComponent("\nValue: ")).color(net.md_5.bungee.api.ChatColor.GRAY)
			.append(new TextComponent("" + price)).color(net.md_5.bungee.api.ChatColor.YELLOW);
		if (isMortgaged) {
			builder.strikethrough(true);
			hoverBuild.append(new TextComponent("Currently mortgaged")).color(net.md_5.bungee.api.ChatColor.RED);
		}
		else if (owner != null) {
			hoverBuild.append(new TextComponent("\nRent: ")).color(net.md_5.bungee.api.ChatColor.GRAY)
			.append(new TextComponent("" + calculateRent(0))).color(net.md_5.bungee.api.ChatColor.YELLOW);
		}
		
		if (players.size() > 0) {
			builder.underlined(true).italic(true);
			hoverBuild.append(new TextComponent("\nPlayers on Space:")).color(net.md_5.bungee.api.ChatColor.GRAY);
			for (GamePlayer gp : players) {
				hoverBuild.append(new TextComponent("\n- ")).color(net.md_5.bungee.api.ChatColor.GRAY)
				.append(new TextComponent(gp.toString())).color(net.md_5.bungee.api.ChatColor.GRAY);
			}
		}
		builder.event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverBuild.create()));
		builder.event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/mono view " + name));
		builder.append(new TextComponent(" ")).reset();
	}
}
