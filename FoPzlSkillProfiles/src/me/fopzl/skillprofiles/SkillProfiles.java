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
	
	public boolean save(Player player, String profileName) {
		if(!profileName.matches("[A-z0-9_]{1,16}")) {
			player.sendMessage("§4[§c§lMLMC§4] §cError: §7Invalid profile name");
		}
		
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
		
		if(!profs.save(data, accId, profileName)) {
			//player.sendMessage("§4[§c§lMLMC§4] §cError: §7Profile not saved");
		} else {
			player.sendMessage("§4[§c§lMLMC§4] §6Saved profile: §e" + profileName);
		}
		return true;
	}
	
	public boolean load(Player player, String profileName) {
		PlayerData data = SkillAPI.getPlayerData(player);
		int accId = SkillAPI.getPlayerAccountData(player).getActiveId();
		UUID uuid = player.getUniqueId();
		
		if(!playerProfiles.containsKey(uuid) || !playerProfiles.get(uuid).load(data, accId, profileName)) {
			player.sendMessage("§4[§c§lMLMC§4] §cError: §7Profile §e" + profileName + " §7doesn't exist");
		} else {
			player.sendMessage("§4[§c§lMLMC§4] §6Loaded profile: §e" + profileName);
		}
		return true;
	}
	
	public boolean list(Player player) {
		int accId = SkillAPI.getPlayerAccountData(player).getActiveId();
		UUID uuid = player.getUniqueId();
		
		if(!playerProfiles.containsKey(uuid) || !playerProfiles.get(uuid).list(player, accId)) {
			player.sendMessage("§4[§c§lMLMC§4] §cError: §7No profiles to list");
		}
		return true;
	}
	
	public boolean delete(Player player, String profileName) {
		PlayerData data = SkillAPI.getPlayerData(player);
		int accId = SkillAPI.getPlayerAccountData(player).getActiveId();
		UUID uuid = player.getUniqueId();
		
		if(!playerProfiles.containsKey(uuid) || !playerProfiles.get(uuid).delete(data, accId, profileName)) {
			player.sendMessage("§4[§c§lMLMC§4] §cError: §7Profile §e" + profileName + " §7doesn't exist");
		} else {
			player.sendMessage("§4[§c§lMLMC§4] §6Profile deleted: §e" + profileName);
		}
		return true;
	}
}

class PlayerProfiles {
	HashMap<Integer, AccountProfiles> profiles = new HashMap<Integer, AccountProfiles>();
	
	boolean save(PlayerData data, int accId, String profileName) {
		AccountProfiles profs;
		if(!profiles.containsKey(accId)) {
			profs = new AccountProfiles();
			profiles.put(accId, profs);
		} else {
			profs = profiles.get(accId);
		}
		
		return profs.save(data, profileName);
	}
	
	boolean load(PlayerData data, int accId, String profileName) {
		if(!profiles.containsKey(accId)) {
			return false;
		}
		
		return profiles.get(accId).load(data, profileName);
	}
	
	boolean list(Player player, int accId) {
		if(!profiles.containsKey(accId)) {
			return false;
		}
		
		return profiles.get(accId).list(player);
	}
	
	boolean delete(PlayerData data, int accId, String profileName) {
		if(!profiles.containsKey(accId)) {
			return false;
		}
		
		return profiles.get(accId).delete(profileName);
	}
}

class AccountProfiles {
	HashMap<String, Profile> profiles = new HashMap<String, Profile>();
	
	boolean save(PlayerData data, String profileName) {
		if(profiles.size() >= 3 &&
		   !profiles.containsKey(profileName) &&
		   !data.getPlayer().hasPermission("fopzlskillprofiles.tbdpermission")) {
			data.getPlayer().sendMessage("§4[§c§lMLMC§4] §cError: §7Can't save more than 3 profiles");
			return false;
		}
		
		profiles.put(profileName, new Profile(data));
		return true;
	}
	
	boolean load(PlayerData data, String profileName) {
		if(!profiles.containsKey(profileName)) {
			return false;
		}
		
		profiles.get(profileName).Load(data);
		return true;
	}
	
	boolean list(Player player) {
		if(profiles.size() == 0) {
			return false;
		}
		
		String msg = "";
		for(String s : profiles.keySet()) {
			msg += s + ", ";
		}
		msg = msg.substring(0, msg.length() - 2);
		
		player.sendMessage("§4[§c§lMLMC§4] §6Profiles: §e" + msg);
		return true;
	}
	
	boolean delete(String profileName) {
		if(!profiles.containsKey(profileName)) {
			return false;
		}
		
		profiles.remove(profileName);
		return true;
	}
}

class Profile {
	private HashMap<String, Integer> attributes;
	private HashMap<String, Integer> skillLevels;
	private HashMap<String, Material> skillBinds;
	private HashMap<Integer, String> skillBar;
	
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
	}
}