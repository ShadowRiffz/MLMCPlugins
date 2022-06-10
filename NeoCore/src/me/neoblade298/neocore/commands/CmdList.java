package me.neoblade298.neocore.commands;

import java.util.TreeMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.neoblade298.neocore.util.PaginatedList;
import me.neoblade298.neocore.util.Util;

public class CmdList implements Subcommand {
	private String base, key;
	private ChatColor color;
	private TreeMap<String, Subcommand> cmds;
	private PaginatedList<Subcommand> pages = null;
	
	private static CommandArguments args = new CommandArguments(new CommandArgument[] {
			new CommandArgument("page", false) });
	
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
		int offset = key.length() == 0 ? 0 : 1;
		if (pages == null) {
			pages = new PaginatedList<Subcommand>(cmds.values());
		}
		
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
	public CommandArguments getArgs() {
		return args;
	}
	
	private void showPage(CommandSender s, int page) {
		page = page - 1;
		if (page >= pages.size() || page < 0) {
			Util.msg(s, "&cPage is out of bounds!");
			return;
		}

		s.sendMessage("§7List of commands: [] = Required, {} = Optional");
		for (Subcommand sc : pages.get(page)) {
			if (sc.isHidden()) {
				continue;
			}
			
			String line = sc.getColor() + "/" + base;
			// Add subcommand name
			if (sc.getKey().length() != 0) {
				line += " " + sc.getKey();
			}
			
			// Add args
			if (sc.getArgOverride() != null) {
				line += " " + sc.getArgOverride();
			}
			else if (sc.getArgs() != null) {
				line += " " + sc.getArgs().getDisplay();
			}
			
			// Add description
			if (sc.getDescription() != null) {
				line += "§7 - " + sc.getDescription();
				s.sendMessage(line);
			}
		}
		if (s instanceof Player) {
			String nextCmd = "/" + this.base + " " + (this.key.length() == 0 ? "" : this.key + " ") + (page + 2);
			String prevCmd = "/" + this.base + " " + (this.key.length() == 0 ? "" : this.key + " ") + page;
			pages.displayFooter((Player) s, page, nextCmd, prevCmd);
		}
		else {
			Util.msg(s, "&7Page &f" + (page + 1) + " &7/ &f" + pages.size(), false);
		}
	}
}
