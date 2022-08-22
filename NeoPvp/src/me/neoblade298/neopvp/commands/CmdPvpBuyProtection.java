package me.neoblade298.neopvp.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neopvp.PvpManager;

public class CmdPvpBuyProtection implements Subcommand {
	private static final int PRICE = 5000;
	private static final int MINUTES = 30;
	private static final CommandArguments args = new CommandArguments();

	@Override
	public String getDescription() {
		return "Purchases " + MINUTES + " minutes of protection for " + PRICE + "g";
	}

	@Override
	public String getKey() {
		return "buyprotection";
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.PLAYER_ONLY;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		Player p = (Player) s;
		NeoCore.getEconomy().withdrawPlayer(p, PRICE);
		PvpManager.getAccount(p).addProtection(1000 * 60 * MINUTES);
	}
	
	@Override
	public CommandArguments getArgs() {
		return args;
	}

}
