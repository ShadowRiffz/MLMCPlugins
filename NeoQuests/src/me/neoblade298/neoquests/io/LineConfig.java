package me.neoblade298.neoquests.io;

import java.util.HashMap;

public class LineConfig {
	private String key, line, fullLine;
	private HashMap<String, String> args = new HashMap<String, String>();
	public LineConfig(String line) {
		fullLine = line;
		int keyIndex = line.indexOf(' ');
		int lineIndex = line.indexOf('>');
		String argLine;
		key = line.substring(0, keyIndex).toLowerCase();
		if (lineIndex != -1) {
			this.line = line.substring(lineIndex + 1).trim().replaceAll("&", "§");
			argLine = line.substring(keyIndex, lineIndex).trim().toLowerCase();
		}
		else {
			this.line = "";
			argLine = line.substring(keyIndex).trim().toLowerCase();
		}
		String[] argEntries = argLine.split(" ");
		for (String entry : argEntries) {
			String[] arg = entry.split(":");
			if (arg.length == 2) {
				args.put(arg[0], arg[1]);
			}
		}
	}
	
	public String getKey() {
		return key;
	}
	
	public String getLine() {
		return line;
	}
	
	public String getFullLine() {
		return fullLine;
	}
	
	public String getString(String key, String def) {
		return args.getOrDefault(key, def);
	}
	
	public boolean getBool(String key, boolean def) {
		if (args.containsKey(key)) {
			return args.get(key).equalsIgnoreCase("true");
		}
		return def;
	}
	
	public int getInt(String key, int def) {
		if (args.containsKey(key)) {
			return Integer.parseInt(args.get(key));
		}
		return def;
	}
}
