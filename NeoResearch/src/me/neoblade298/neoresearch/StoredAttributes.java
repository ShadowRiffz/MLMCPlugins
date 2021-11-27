package me.neoblade298.neoresearch;

import java.util.HashMap;

import org.bukkit.entity.Player;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

public class StoredAttributes {
	private HashMap<String, Integer> stored;
	private HashMap<String, Integer> active;
	
	public StoredAttributes() {
		this.stored = new HashMap<String, Integer>();
		this.active = new HashMap<String, Integer>();
	}


	public StoredAttributes(HashMap<String, Integer> attrs) {
		this.stored = attrs;
		this.active = new HashMap<String, Integer>();
	}

	public void applyAttributes(Player p) {
		PlayerData data = SkillAPI.getPlayerData(p);
		active.clear();
		for (String attr : stored.keySet()) {
			if (attr.equals("unused")) {
				continue;
			}
			// Must be deep copy
			active.put(attr, Integer.valueOf(stored.get(attr)));
			data.addBonusAttributes(attr, stored.get(attr));
		}
	}

	public void removeAttributes(Player p) {
		PlayerData data = SkillAPI.getPlayerData(p);
		for (String attr : active.keySet()) {
			data.addBonusAttributes(attr, -active.get(attr));
		}
		active.clear();
	}
	
	public void resetAttributes() {
		active.clear();
	}

	public HashMap<String, Integer> getStoredAttrs() {
		return stored;
	}

	public HashMap<String, Integer> getActiveAttrs() {
		return active;
	}
	
	public int getAttribute(String attr) {
		return stored.getOrDefault(attr, 0);
	}
	
	public void setAttribute(String attr, int num) {
		stored.put(attr, num);
	}
	
	public void addAttribute(String attr, int num) {
		stored.put(attr, stored.get(attr) + num);
	}
	
	public void investAttribute(String attr, int num) {
		if (stored.get("unused") >= num) {
			stored.put("unused", stored.get("unused") - num);
			stored.put(attr, stored.getOrDefault(attr, 0) + num);
		}
	}
	
	public void unvestAttribute(String attr, int num) {
		if (stored.get(attr) >= num) {
			stored.put("unused", stored.getOrDefault("unused", 0) + num);
			stored.put(attr, stored.get(attr) - num);
		}
	}

	public String toString() {
		return stored.toString();
	}

}
