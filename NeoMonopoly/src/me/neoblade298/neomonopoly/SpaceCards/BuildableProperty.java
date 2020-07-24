package me.neoblade298.neomonopoly.SpaceCards;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class BuildableProperty implements Property {
	private GamePlayer owner;
	private int numHouses;
	private int numHotels;
	private boolean isMonopoly;
	private boolean isMortgaged;
	private int[] rent;
	private Game game;

	private int price;
	private int houseprice;
	private String name;
	private ChatColor color;
	
	public BuildableProperty(String name, int[] rent, int price, int houseprice, ChatColor color, Game game) {
		numHouses = 0;
		numHotels = 0;
		isMonopoly = false;
		isMortgaged = false;
		owner = null;
		this.name = name;
		this.rent = rent;
		this.price = price;
		this.setHouseprice(houseprice);
		this.color = color;
		this.game = game;
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

	@Override
	public void onStart(GamePlayer starter) {
		game.requiredActions.get(starter).add("ROLL_MOVE");
	}

	public GamePlayer getOwner() {
		return owner;
	}

	public void setOwner(GamePlayer owner) {
		this.owner = owner;
	}
	
	public boolean canMortgage() {
		return numHouses == 0 && numHotels == 0;
	}

	public boolean isMortgaged() {
		return isMortgaged;
	}
	
	public char getMapChar() {
		return 'x';
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

	public int getNumHouses() {
		return numHouses;
	}

	public void setNumHouses(int numHouses) {
		this.numHouses = numHouses;
	}

	public int getNumHotels() {
		return numHotels;
	}

	public void setNumHotels(int numHotels) {
		this.numHotels = numHotels;
	}

	public boolean isMonopoly() {
		return isMonopoly;
	}

	public void setMonopoly(boolean isMonopoly) {
		this.isMonopoly = isMonopoly;
	}

	public int getHouseprice() {
		return houseprice;
	}

	public void setHouseprice(int houseprice) {
		this.houseprice = houseprice;
	}
	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChatColor getColor() {
		return color;
	}

	public void setColor(ChatColor color) {
		this.color = color;
	}

	@Override
	public void onOwned(GamePlayer owner) {
		ArrayList<BuildableProperty> colorProps = game.colors.get(color);
		int count = 0;
		for (BuildableProperty prop : colorProps) {
			if (prop.getOwner() != null && prop.getOwner().equals(owner)) {
				count++;
			}
		}
		String msg = "&e" + owner + " &7now owns &e" + count + "/" + colorProps.size() + " " + color + game.main.colorToString.get(color) +
				" &7properties!";
		if (count == colorProps.size()) {
			for (BuildableProperty prop : colorProps) {
				prop.setMonopoly(true);
			}
			msg += " They have a monopoly and may now start buying houses there.";
		}
		game.broadcast(msg);
	}

	@Override
	public void onUnowned(GamePlayer formerOwner) {
		ArrayList<BuildableProperty> colorProps = game.colors.get(color);
		int count = 0;
		for (BuildableProperty prop : colorProps) {
			if (prop.getOwner() != null && prop.getOwner().equals(formerOwner)) {
				count++;
			}
			prop.setMonopoly(false);
		}
		String msg = "&e" + formerOwner + " &7now owns &e" + count + "/" + colorProps.size() + " " + color + game.main.colorToString.get(color) +
				" &7properties!";
		game.broadcast(msg);
	}

	@Override
	public void onBankrupt(GamePlayer formerOwner) {
		ArrayList<BuildableProperty> colorProps = game.colors.get(color);
		for (BuildableProperty prop : colorProps) {
			prop.setMonopoly(false);
		}
		game.addHouses(numHouses);
		game.addHotels(numHotels);
		this.numHouses = 0;
		this.numHotels = 0;
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
			.append(new TextComponent("" + price)).color(net.md_5.bungee.api.ChatColor.YELLOW)
			.append(new TextComponent(", Construction price: ")).color(net.md_5.bungee.api.ChatColor.GRAY)
			.append(new TextComponent("" + houseprice)).color(net.md_5.bungee.api.ChatColor.YELLOW);
		if (isMortgaged) {
			builder.strikethrough(true);
			hoverBuild.append(new TextComponent("Currently mortgaged")).color(net.md_5.bungee.api.ChatColor.RED);
		}
		else if (owner != null) {
			hoverBuild.append(new TextComponent("\nRent: ")).color(net.md_5.bungee.api.ChatColor.GRAY)
			.append(new TextComponent("" + calculateRent(0))).color(net.md_5.bungee.api.ChatColor.YELLOW)
			.append(new TextComponent("\nHouses: ")).color(net.md_5.bungee.api.ChatColor.GRAY)
			.append(new TextComponent("" + numHouses)).color(net.md_5.bungee.api.ChatColor.YELLOW)
			.append(new TextComponent(", Hotels: ")).color(net.md_5.bungee.api.ChatColor.GRAY)
			.append(new TextComponent("" + numHotels)).color(net.md_5.bungee.api.ChatColor.YELLOW);
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
		gp.message("&7Value: &e" + price + "&7, Construction price: &e" + houseprice);
		if (!isMortgaged) {
			String msg = new String();
			for (int i = 0; i <= 5; i++) {
				if ((i == numHouses || (i == 5 && numHotels == 1)) && owner != null) {
					msg += "&e";
				}
				else {
					msg += "&7";
				}
				if (i == 0) {
					if (isMonopoly) {
						msg += rent[i] * 2;
					}
					else {
						msg += rent[i];
					}
				}
				else {
					msg += " " + rent[i];
				}

			}
			gp.message("&7Rent&7: " + msg);
			gp.message("&7Number of Houses: &e" + numHouses + "&7, Number of Hotels: &e" + numHotels);
		}
		else {
			gp.message("&cCurrently mortgaged. Price to unmortgage: &e" + price);
		}
	}
	
	@Override
	public String getShorthand(GamePlayer gp) {
		String ownerName = owner == null ? "&a$" + price : owner.toString();
		if (isMortgaged) {
			return "&7[&m" + color + "&m" + name + "&7&m (" + ownerName + "&7&m)]&7";
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
			return "&7&m[" + color + "&m" + name + "&7&m] Houses: &e&m" + numHouses + "&7, Hotels: &e&m" + numHotels + "&7&m, Rent: &e&m" + calculateRent(0);
		}
		else {
			return "&7[" + color + name + "&7] Houses: &e" + numHouses + "&7, Hotels: &e" + numHotels + "&7, Rent: &e" + calculateRent(0);
		}
	}

	@Override
	public int calculateRent(int dice) {
		if (numHouses + numHotels == 0 && isMonopoly) {
			return rent[0] * 2;
		}
		return rent[numHouses + (numHotels * 5)];
	}

}
