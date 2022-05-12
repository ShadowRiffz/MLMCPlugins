package me.neoblade298.neoquests.util;

import java.util.HashMap;

public class LineConfig {
	private String key, line;
	private HashMap<String, String> args;
	public LineConfig(String line) {
		int keyIndex = line.indexOf(' ');
		int lineIndex = line.indexOf('>');
		String argLine;
		key = line.substring(0, keyIndex).toLowerCase();
		if (lineIndex != -1) {
			this.line = line.substring(lineIndex).trim();
			argLine = line.substring(keyIndex, lineIndex).trim().toLowerCase();
		}
		else {
			this.line = "";
			argLine = line.substring(keyIndex).trim().toLowerCase();
		}
		String[] argEntries = argLine.split(" ");
		for (String entry : argEntries) {
			String[] arg = entry.split(":");
			args.put(arg[0], arg[1]);
		}
	}
	
	public String getKey() {
		return key;
	}
	
	public String getLine() {
		return line;
	}
	
	public String getString(String key) {
		return args.get(key);
	}
	
	public int getInt(String key) {
		return Integer.parseInt(args.get(key));
	}
}
