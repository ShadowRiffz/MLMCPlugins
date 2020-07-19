package me.neoblade298.neomonopoly.SpaceCards;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.Objects.Game;
import me.neoblade298.neomonopoly.Objects.GamePlayer;

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
			game.requiredActions.get(lander).add("UNOWNED_SPACE");
		}
		else {
			if (!owner.equals(lander)) {
				game.billPlayer(lander, calculateRent(dice), owner);
			}
		}
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
		game.broadcast("&e" + owner + " now owns &e" + owner.getNumRailroads() + " &7railroads.");
	}

	@Override
	public void onUnowned(GamePlayer formerOwner) {
		owner.setNumRailroads(owner.getNumRailroads() - 1);
		game.broadcast("&e" + owner + " now owns &e" + owner.getNumRailroads() + " &7railroads.");
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
			gp.message("&eRent&7: " + msg);
		}
		gp.message("&cCurrently mortgaged. Price to unmortgage: &e" + price);
	}
	
	@Override
	public String getShorthand(GamePlayer gp) {
		String ownerName = owner == null ? "Unowned" : owner.toString();
		return "&7[" + color + name + "&7 (" + ownerName + "&7)]";
	}

	@Override
	public int calculateRent(int dice) {
		return rent[owner.getNumRailroads() - 1];
	}
}
