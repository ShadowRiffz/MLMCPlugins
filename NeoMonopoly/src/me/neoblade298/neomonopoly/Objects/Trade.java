package me.neoblade298.neomonopoly.Objects;

import java.util.ArrayList;

import org.bukkit.ChatColor;

import me.neoblade298.neomonopoly.SpaceCards.BuildableProperty;
import me.neoblade298.neomonopoly.SpaceCards.Property;

public class Trade {
	public Game game;
	public GamePlayer traderA, traderB;
	public ArrayList<Property> propertiesA, propertiesB;
	public int moneyA, moneyB, jailFreeA, jailFreeB;
	public boolean confirmA, confirmB;
	
	public Trade(Game game, GamePlayer trader, GamePlayer tradee) {
		this.game = game;
		this.traderA = trader;
		this.traderB = tradee;
		this.moneyA = 0;
		this.moneyB = 0;
		this.jailFreeA = 0;
		this.jailFreeB = 0;
		this.propertiesA = new ArrayList<Property>();
		this.propertiesB = new ArrayList<Property>();
		this.confirmA = false;
		this.confirmB = false;
	}
	
	public void offerProperty(GamePlayer offerer, String prefix) {
		if (traderA.equals(offerer)) {
			Property prop = null;
			for (Property search : traderA.getProperties()) {
				if (search.getName().toLowerCase().startsWith(prefix)) {
					prop = search;
					if (prop.isMortgaged()) {
						offerer.message("&cCannot trade mortgaged properties!");
						return;
					}
					break;
				}
			}
			if (prop == null) {
				offerer.message("&cCould not find the specified property!");
				return;
			}
			if (!canTradeProperty(prop)) {
				offerer.message("&cAll houses on the same color properties must be sold before you may trade this!");
				return;
			}
			
			if (propertiesA.contains(prop)) {
				game.broadcast("&e" + offerer + " &7stopped offering " + prop.getColoredName() + "&7.");
				propertiesA.remove(prop);
			}
			else {
				game.broadcast("&e" + offerer + " &7has offered " + prop.getColoredName() + "&7.");
				propertiesA.add(prop);
			}
		}
		else {
			Property prop = null;
			for (Property search : traderB.getProperties()) {
				if (search.getName().toLowerCase().startsWith(prefix)) {
					prop = search;
					if (prop.isMortgaged()) {
						offerer.message("&cCannot trade mortgaged properties!");
						return;
					}
					break;
				}
			}
			if (prop == null) {
				offerer.message("&cCould not find the specified property!");
				return;
			}
			if (!canTradeProperty(prop)) {
				offerer.message("&cAll houses on the same color properties must be sold before you may trade this!");
				return;
			}
			
			if (propertiesB.contains(prop)) {
				game.broadcast("&e" + offerer + " &7stopped offering " + prop.getColoredName() + "&7.");
				propertiesB.remove(prop);
			}
			else {
				game.broadcast("&e" + offerer + " &7has offered " + prop.getColoredName() + "&7.");
				propertiesB.add(prop);
			}
		}
		this.confirmA = false;
		this.confirmB = false;
	}
	
	public void offerMoney(GamePlayer offerer, int money) {
		if (traderA.equals(offerer)) {
			if (traderA.getMoney() >= money) {
				moneyA = money;
			}
			else {
				offerer.message("&cYou don't have enough money for that!");
			}
		}
		else {
			if (traderB.getMoney() >= money) {
				moneyB = money;
			}
			else {
				offerer.message("&cYou don't have enough money for that!");
			}
		}
		game.broadcast("&e" + offerer + " &7has offered &a$" + money + "&7.");
		this.confirmA = false;
		this.confirmB = false;
	}
	
	public void offerJailFree(GamePlayer offerer, int jailFree) {
		if (traderA.equals(offerer)) {
			if (traderA.getNumJailFree() >= jailFree) {
				jailFreeA = jailFree;
			}
			else {
				offerer.message("&cYou don't have enough get out of jail free cards for that!");
			}
		}
		else {
			if (traderB.getNumJailFree() >= jailFree) {
				jailFreeB = jailFree;
			}
			else {
				offerer.message("&cYou don't have enough get out of jail free cards for that!");
			}
		}
		game.broadcast("&e" + offerer + " &7has offered &e" + jailFree + "&7 get out of jail free card(s).");
		this.confirmA = false;
		this.confirmB = false;
	}
	
	public void requestProperty(GamePlayer requester, String prefix) {
		if (traderB.equals(requester)) {
			Property prop = null;
			for (Property search : traderA.getProperties()) {
				if (search.getName().startsWith(prefix)) {
					prop = search;
					if (prop.isMortgaged()) {
						requester.message("&cCannot trade mortgaged properties!");
						return;
					}
					break;
				}
			}
			if (prop == null) {
				requester.message("&cCould not find the specified property!");
				return;
			}
			if (!canTradeProperty(prop)) {
				requester.message("&cAll houses on the same color properties must be sold before they may trade this!");
				return;
			}
			
			if (propertiesA.contains(prop)) {
				game.broadcast("&e" + requester + " &7has stopped requesting " + prop.getColoredName() + "&7.");
				propertiesA.remove(prop);
			}
			else {
				game.broadcast("&e" + requester + " &7has requested " + prop.getColoredName() + "&7.");
				propertiesA.add(prop);
			}
		}
		else {
			Property prop = null;
			for (Property search : traderB.getProperties()) {
				if (search.getName().toLowerCase().startsWith(prefix)) {
					prop = search;
					if (prop.isMortgaged()) {
						requester.message("&cCannot trade mortgaged properties!");
						return;
					}
					break;
				}
			}
			if (prop == null) {
				requester.message("&cCould not find the specified property!");
				return;
			}
			if (!canTradeProperty(prop)) {
				requester.message("&cAll houses on the same color properties must be sold before they may trade this!");
				return;
			}
			
			if (propertiesB.contains(prop)) {
				game.broadcast("&e" + requester + " &7has stopped requesting " + prop.getColoredName() + "&7.");
				propertiesB.remove(prop);
			}
			else {
				game.broadcast("&e" + requester + " &7has requested " + prop.getColoredName() + "&7.");
				propertiesB.add(prop);
			}
		}
		this.confirmA = false;
		this.confirmB = false;
	}
	
	public void requestMoney(GamePlayer requester, int money) {
		if (traderB.equals(requester)) {
			if (traderA.getMoney() >= money) {
				moneyA = money;
			}
			else {
				requester.message("&cThe player does not have enough money for that!");
				return;
			}
		}
		else {
			if (traderB.getMoney() >= money) {
				moneyB = money;
			}
			else {
				requester.message("&cThe player does not have enough money for that!");
				return;
			}
		}
		game.broadcast("&e" + requester + " &7has requested &a$" + money + "&7.");
		this.confirmA = false;
		this.confirmB = false;
	}
	
	public void requestJailFree(GamePlayer requester, int jailFree) {
		if (traderB.equals(requester)) {
			if (traderA.getNumJailFree() >= jailFree) {
				jailFreeA = jailFree;
			}
			else {
				requester.message("&cThe player does not have enough get out of jail free cards for that!");
				return;
			}
		}
		else {
			if (traderB.getNumJailFree() >= jailFree) {
				jailFreeB = jailFree;
			}
			else {
				requester.message("&cThe player does not have enough get out of jail free cards for that!");
				return;
			}
		}
		game.broadcast("&e" + requester + " &7has requested &e" + jailFree + "&7 get out of jail free card(s).");
		this.confirmA = false;
		this.confirmB = false;
	}
	
	public void setConfirm(GamePlayer gp, boolean confirm) {
		if (traderA.equals(gp)) {
			confirmA = confirm;
		}
		else {
			confirmB = confirm;
		}
		if (confirm) game.broadcast("&e" + gp + " &7has accepted the trade!");
		else game.broadcast("&e" + gp + " &7has stopped accepting the trade!");
		
		if (confirmA && confirmB) {
			game.broadcast("&7Trade successful!");
			display();
			
			for (Property prop : propertiesA) {
				traderA.getProperties().remove(prop);
				traderB.getProperties().add(prop);
				prop.onUnowned(traderA);
				prop.setOwner(traderB);
				prop.onOwned(traderB);
			}
			
			for (Property prop : propertiesB) {
				traderB.getProperties().remove(prop);
				traderA.getProperties().add(prop);
				prop.onUnowned(traderB);
				prop.setOwner(traderA);
				prop.onOwned(traderA);
			}
			
			traderA.setMoney(traderA.getMoney() + moneyB - moneyA);
			traderB.setMoney(traderB.getMoney() + moneyA - moneyB);
			traderA.setNumJailFree(traderA.getNumJailFree() + jailFreeB - jailFreeA);
			traderB.setNumJailFree(traderB.getNumJailFree() + jailFreeA - jailFreeB);
			game.trade = null;
		}
	}
	
	public void cancel(GamePlayer gp) {
		game.broadcast("&e" + gp + " &7has cancelled the trade!");
		game.trade = null;
	}
	
	public void display() {
		// Trader A gets
		String line = new String();
		line += "&e" + traderA + " &7gets: ";
		int count = 0;
		int value = 0;
		for (Property prop : propertiesB) {
			if (count != 0) {
				line += "&7, ";
			}
			line += prop.getColoredName();
			value += prop.getPrice();
			count++;
		}
		if (moneyB != 0) {
			if (count != 0) {
				line += "&7, ";
			}
			line += "&a$" + moneyB;
			value += moneyB;
			count++;
		}
		if (jailFreeB != 0) {
			if (count != 0) {
				line += "&7, ";
			}
			line += "&e" + jailFreeB + " &7get out of jail free cards";
			value += 50;
			count++;
		}
		if (count == 0) {
			line += "&7Nothing";
		}
		game.broadcast(line + "&7.");
		game.broadcast("&7Total Value: &a$" + value + "&7.");

		// Trader B gets
		line = new String();
		line += "&e" + traderB + " &7gets: ";
		count = 0;
		value = 0;
		for (Property prop : propertiesA) {
			if (count != 0) {
				line += "&7, ";
			}
			line += prop.getColoredName();
			value += prop.getPrice();
			count++;
		}
		if (moneyA != 0) {
			if (count != 0) {
				line += "&7, ";
			}
			line += "&a$" + moneyA;
			value += moneyA;
			count++;
		}
		if (jailFreeA != 0) {
			if (count != 0) {
				line += "&7, ";
			}
			line += "&e" + jailFreeA + " &7get out of jail free cards";
			value += 50;
			count++;
		}
		if (count == 0) {
			line += "&7Nothing";
		}
		game.broadcast(line + "&7.");
		game.broadcast("&7Total Value: &a$" + value + "&7.");
	}
	
	public void privateDisplay(GamePlayer gp) {
		// Trader A gets
		String line = new String();
		line += "&e" + traderA + " &7gets: ";
		int count = 0;
		int value = 0;
		for (Property prop : propertiesB) {
			if (count != 0) {
				line += "&7, ";
			}
			line += prop.getColoredName();
			value += prop.getPrice();
			count++;
		}
		if (moneyB != 0) {
			if (count != 0) {
				line += "&7, ";
			}
			line += "&a$" + moneyB;
			value += moneyB;
			count++;
		}
		if (jailFreeB != 0) {
			if (count != 0) {
				line += "&7, ";
			}
			line += "&e" + jailFreeB + " &7get out of jail free cards";
			value += 50;
			count++;
		}
		if (count == 0) {
			line += "&7Nothing";
		}
		gp.message(line + "&7.");
		gp.message("&7Total Value: &a$" + value + "&7.");

		// Trader B gets
		line = new String();
		line += "&e" + traderB + " &7gets: ";
		count = 0;
		value = 0;
		for (Property prop : propertiesA) {
			if (count != 0) {
				line += "&7, ";
			}
			line += prop.getColoredName();
			value += prop.getPrice();
			count++;
		}
		if (moneyA != 0) {
			if (count != 0) {
				line += "&7, ";
			}
			line += "&a$" + moneyA;
			value += moneyA;
			count++;
		}
		if (jailFreeA != 0) {
			if (count != 0) {
				line += "&7, ";
			}
			line += "&e" + jailFreeA + " &7get out of jail free cards";
			value += 50;
			count++;
		}
		if (count == 0) {
			line += "&7Nothing";
		}
		gp.message(line + "&7.");
		gp.message("&7Total Value: &a$" + value + "&7.");
	}
	
	private boolean canTradeProperty(Property prop) {
		if (prop instanceof BuildableProperty) {
			BuildableProperty bprop = (BuildableProperty) prop;
			if (bprop.isMonopoly()) {
				ChatColor color = prop.getColor();
				boolean noHouses = true;
				for (BuildableProperty bp : game.colors.get(color)) {
					if (bp.getNumHotels() > 0 || bp.getNumHotels() > 0) noHouses = false;
				}
				return noHouses;
			}
			else {
				return true;
			}
		}
		else {
			return true;
		}
		
	}
}
