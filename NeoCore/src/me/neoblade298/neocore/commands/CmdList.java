package me.neoblade298.neocore.commands;

import java.util.Iterator;
import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import me.neoblade298.neocore.util.Util;

public class CmdList implements Subcommand {
	private String base, key;
	private ChatColor color;
	private TreeMap<String, Subcommand> cmds;
	private static final int CMDS_PER_PAGE = 10;
	
	public CmdList(String key, String base, TreeMap<String, Subcommand> cmds) {
		this(key, base, cmds, ChatColor.RED);
	}
	
	public CmdList(String key, String base, TreeMap<String, Subcommand> cmds, ChatColor color) {
		this.key = key;
		this.base = base;
		this.cmds = cmds;
		this.color = color;
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
		s.sendMessage("§7List of commands: [] = Required, {} = Optional");
		int offset = key.length() == 0 ? 0 : 1;
		if (args.length == offset) {
			showPage(s, 1);
		}
		else {
			showPage(s, Integer.parseInt(args[offset]));
		}
	}
	
	@Override
	public String getDescription() {
		return "List all commands";
	}

	@Override
	public ChatColor getColor() {
		return color;
	}

	@Override
	public String getArgs() {
		return "{page}";
	}
	
	private void showPage(CommandSender s, int page) {
		int totalPages = ((cmds.size() - 1) / CMDS_PER_PAGE) + 1;
		if (page > totalPages || page < 1) {
			Util.msg(s, "&cPage is out of bounds!");
			return;
		}
		
		Iterator<Subcommand> iter = cmds.values().iterator();
		for (int i = 1; i < (page - 1) * CMDS_PER_PAGE; i++) {
			iter.next();
		}

		for (int i = 0; i < CMDS_PER_PAGE && iter.hasNext(); i++) {
			Subcommand sc = iter.next();
			if (sc.isHidden()) {
				continue;
			}
			
			String line = sc.getColor() + "/" + base;
			// Add subcommand name
			if (sc.getKey().length() != 0) {
				line += " " + sc.getKey();
			}
			
			// Add args
			if (sc.getArgs() != null) {
				line += " " + sc.getArgs();
			}
			
			// Add description
			if (sc.getDescription() != null) {
				line += "§7 - " + sc.getDescription();
				s.sendMessage(line);
			}
		}
		Util.msg(s, "&7Page &f" + page + " &7/ &f" + totalPages, false);
	}
}
