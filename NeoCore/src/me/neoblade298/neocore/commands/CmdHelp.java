package me.neoblade298.neocore.commands;

import java.io.File;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.messaging.MessagingManager;
import net.md_5.bungee.api.chat.BaseComponent;

public class CmdHelp implements Subcommand {
	private static final CommandArguments args = new CommandArguments();
	private static final BaseComponent[] mainHelp;
	
	static {
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(NeoCore.inst().getDataFolder(), "help.yml"));
		mainHelp = MessagingManager.parseMessage(yml.getConfigurationSection("main"));
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
	public String getKey() {
		return "";
	}

	@Override
	public String getDescription() {
		return "Opens the help menu";
	}

	@Override
	public void run(CommandSender s, String[] args) {
		((Player) s).spigot().sendMessage(mainHelp);
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
