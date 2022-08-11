package me.fopzl.skillprofiles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerAccounts;
import com.sucy.skill.api.player.PlayerData;

public class SkillProfiles extends JavaPlugin {
	HashMap<UUID, PlayerProfiles> playerProfiles = new HashMap<UUID, PlayerProfiles>(); 
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("FoPzlSkillProfiles Enabled");
		this.getCommand("skillprofile").setExecutor(new Commands(this));
	}
	
	public void onDisable() {
		Bukkit.getServer().getLogger().info("FoPzlSkillProfiles Disabled");
		super.onDisable();
	}
	
	// TODO: check if requirements met for saving (# of profiles, clean name, etc.)
	public void save(Player player, String profileName) {
		PlayerData data = SkillAPI.getPlayerData(player);
		int accId = SkillAPI.getPlayerAccountData(player).getActiveId();
		UUID uuid = player.getUniqueId();
		
		PlayerProfiles profs;
		if(!playerProfiles.containsKey(uuid)) {
			profs = new PlayerProfiles();
			playerProfiles.put(uuid, profs);
		} else {
			profs = playerProfiles.get(uuid);
		}
		
		profs.save(data, accId, profileName);
	}
	
	// TODO: verify stuff (e.g. exists) before loading
	public void load(Player player, String profileName) {
		PlayerData data = SkillAPI.getPlayerData(player);
		int accId = SkillAPI.getPlayerAccountData(player).getActiveId();
		UUID uuid = player.getUniqueId();
		
		if(!playerProfiles.containsKey(uuid)) {
			// TODO
			return;
		}
		
		playerProfiles.get(uuid).load(data, accId, profileName);
	}
}

class PlayerProfiles {
	HashMap<Integer, AccountProfiles> profiles = new HashMap<Integer, AccountProfiles>();
	
	void save(PlayerData data, int accId, String profileName) {
		AccountProfiles profs;
		if(!profiles.containsKey(accId)) {
			profs = new AccountProfiles();
			profiles.put(accId, profs);
		} else {
			profs = profiles.get(accId);
		}
		
		profs.save(data, profileName);
	}
	
	void load(PlayerData data, int accId, String profileName) {
		if(!profiles.containsKey(accId)) {
			// TODO
			return;
		}
		
		profiles.get(accId).load(data, profileName);
	}
}

class AccountProfiles {
	HashMap<String, Profile> profiles = new HashMap<String, Profile>();
	
	void save(PlayerData data, String profileName) {
		Profile profile = new Profile();
		
		profile.attributes = data.getInvestedAttributes();
		
		profiles.put(profileName, profile);
	}
	
	void load(PlayerData data, String profileName) {
		if(!profiles.containsKey(profileName)) {
			// TODO
			return;
		}
		
		Profile profile = profiles.get(profileName);
		
		data.refundAttributes();
		
		for (Map.Entry<String, Integer> kvp : profile.attributes.entrySet()) {
			for(int i = 0; i < kvp.getValue().intValue(); i++) {
				data.upAttribute(kvp.getKey());
			}
		}
	}
}

class Profile {
	HashMap<String, Integer> attributes;
}