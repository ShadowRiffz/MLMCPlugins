package me.fopzl.skillprofiles;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.player.PlayerSkill;
import com.sucy.skill.api.player.PlayerSkillBar;
import com.sucy.skill.api.skills.Skill;

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
	
	// TODO: verify stuff (e.g. exists) before loading (IMPORTANT: check if on same class/whatever)
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
		profiles.put(profileName, new Profile(data));
	}
	
	void load(PlayerData data, String profileName) {
		if(!profiles.containsKey(profileName)) {
			// TODO
			return;
		}
		
		profiles.get(profileName).Load(data);
	}
}

class Profile {
	private HashMap<String, Integer> attributes;
	private HashMap<String, Integer> skillLevels;
	private HashMap<String, Material> skillBinds;
	private HashMap<Integer, String> skillBar;
	private boolean skillBarEnabled;
	
	public Profile(PlayerData data) {
		/* attributes */
		attributes = data.getInvestedAttributes();
		
		/* skills */
		Collection<PlayerSkill> skills = data.getSkills();
		skillLevels = new HashMap<String, Integer>();
		skillBinds = new HashMap<String, Material>();
		for(PlayerSkill ps : skills) {
			String name = ps.getData().getName();
			skillLevels.put(name, ps.getLevel());
			skillBinds.put(name, ps.getBind());
		}
		
		/* skill bar */
		skillBar = new HashMap<Integer, String>();
		skillBar.putAll(data.getSkillBar().getData());
		skillBarEnabled = data.getSkillBar().isEnabled();
	}
	
	public void Load(PlayerData data) {
		/* attributes */
		data.refundAttributes();
		for (Map.Entry<String, Integer> kvp : attributes.entrySet()) {
			for(int i = 0; i < kvp.getValue().intValue(); i++) {
				data.upAttribute(kvp.getKey());
			}
		}
		
		/* skills */
		Collection<PlayerSkill> skills = data.getSkills();
		for(PlayerSkill ps : skills) {
			int level = ps.getLevel();
			for(int i = 0; i < level; i++) {
				data.downgradeSkill(ps.getData());
			}
		}
		
		// need to level essence first
		PlayerSkill essence = null;
		for(PlayerSkill ps : skills) {
			Skill s = ps.getData();
			if(s.getName().contains("'s Essence")) {
				essence = ps;
				
				for(int i = 0; i < skillLevels.getOrDefault(s.getName(), 0); i++) {
					data.upgradeSkill(s, true);
				}
				ps.setBind(skillBinds.get(s.getName()));
			}
		}
		if(essence != null) {
			skills.remove(essence);
		}
		
		for(PlayerSkill ps : skills) {
			Skill s = ps.getData();
			for(int i = 0; i < skillLevels.getOrDefault(s.getName(), 0); i++) {
				data.upgradeSkill(s, true);
			}
			ps.setBind(skillBinds.get(s.getName()));
		}
		
		/* skill bar */
		PlayerSkillBar bar = data.getSkillBar();
		
		bar.clear(data.getPlayer());
		bar.getData().clear();
		bar.getData().putAll(skillBar);
		bar.setup(data.getPlayer());
		
		if(bar.isEnabled() != skillBarEnabled) {
			bar.toggleEnabled();
		}
	}
}