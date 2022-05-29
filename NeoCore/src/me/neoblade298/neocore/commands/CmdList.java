package me.neoblade298.neocore.commands;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class CmdList implements Subcommand {
	private String base, key;
	private HashMap<String, Subcommand> cmds;
	
	public CmdList(String key, String base, HashMap<String, Subcommand> cmds) {
		this.base = base;
		this.cmds = cmds;
	}

	@Override
	public String getPermission() {
		return null;
	}

	@Override
	public SubcommandRunner getRunner() {
		return SubcommandRunner.BOTH;
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void run(CommandSender s, String[] args) {
		for (String cmd : cmds.keySet()) {
			Subcommand sc = cmds.get(cmd);
			if (sc.isHidden()) {
				continue;
			}
			
			String line = sc.getColor() + base;
			if (cmd.length() != 0) {
				line += " " + cmd;
			}
			if (sc.getDescription() != null) {
				line += "�7 - " + sc.getDescription();
				s.sendMessage(line);
			}
		}
	}
	
	@Override
	public String getDescription() {
		return "List all commands";
	}

	@Override
	public ChatColor getColor() {
		return ChatColor.GRAY;
	}
}
