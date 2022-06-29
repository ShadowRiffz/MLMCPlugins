package me.neoblade298.neocore.commands.builtin;

import java.io.File;
import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.NeoCore;
import me.neoblade298.neocore.commands.CommandArgument;
import me.neoblade298.neocore.commands.CommandArguments;
import me.neoblade298.neocore.commands.Subcommand;
import me.neoblade298.neocore.commands.SubcommandRunner;
import me.neoblade298.neocore.messaging.MessagingManager;
import me.neoblade298.neocore.util.Util;
import net.md_5.bungee.api.chat.BaseComponent;

public class CmdCoreMessage implements Subcommand {
	private static final CommandArguments args = new CommandArguments(Arrays.asList(new CommandArgument("page", false)));
	private BaseComponent[][] msgs;
	
	public CmdCoreMessage(String section) {
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(NeoCore.inst().getDataFolder(), "messages.yml"));
		msgs = new BaseComponent[1][];
		msgs[0] = MessagingManager.parseMessage(yml.getConfigurationSection(section));
	}
	
	public CmdCoreMessage(String section, int pages) {
		YamlConfiguration yml = YamlConfiguration.loadConfiguration(new File(NeoCore.inst().getDataFolder(), "messages.yml"));
		msgs = new BaseComponent[pages][];
		for (int i = 0; i < pages; i++) {
			msgs[i] = MessagingManager.parseMessage(yml.getConfigurationSection(section + i));
		}
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
		return "Plays a message";
	}

	@Override
	public void run(CommandSender s, String[] args) {
		if (args.length == 0) {
			((Player) s).spigot().sendMessage(msgs[0]);
		}
		else if (args.length == 1) {
			if (!StringUtils.isNumeric(args[0])) {
				Util.msg(s, "&cArgument is not a number!");
				return;
			}
			
			int page = Integer.parseInt(args[0]) - 1;
			if (page < 0 || page > msgs.length - 1) {
				Util.msg(s, "&cPage is out of bounds!");
				return;
			}

			((Player) s).spigot().sendMessage(msgs[page]);
		}
	}

	@Override
	public CommandArguments getArgs() {
		return args;
	}
}
