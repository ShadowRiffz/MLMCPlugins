package me.fopzl.skillprofiles;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerAccounts;
import com.sucy.skill.api.player.PlayerData;

public class SkillProfiles extends JavaPlugin {
	ArrayList<HashMap<String, Integer>> tempfopzl = new ArrayList<HashMap<String, Integer>>();
	
	public void onEnable() {
		Bukkit.getServer().getLogger().info("FoPzlSkillProfiles Enabled");
		this.getCommand("skillprofile").setExecutor(new Commands(this));
		
		tempfopzl.add(new HashMap<String, Integer>());
		tempfopzl.add(new HashMap<String, Integer>());
		tempfopzl.add(new HashMap<String, Integer>());
	}
	
	public void onDisable() {
		Bukkit.getServer().getLogger().info("FoPzlSkillProfiles Disabled");
		super.onDisable();
	}
	
	public void Save(Player player, int slot) {
		PlayerData pd = SkillAPI.getPlayerData(player);
		
		PlayerAccounts pad = SkillAPI.getPlayerAccountData(player);
		int classAccId = pad.getActiveId();
		
		HashMap<String, Integer> attributes = pd.getAttributes();
		tempfopzl.set(classAccId - 1, attributes);
	}
	
	public void Load(Player player, int slot) {
		PlayerData pd = SkillAPI.getPlayerData(player);
		
		PlayerAccounts pad = SkillAPI.getPlayerAccountData(player);
		int classAccId = pad.getActiveId();
		
		HashMap<String, Integer> attributes = pd.getAttributeData();
		attributes.putAll(tempfopzl.get(classAccId - 1));
	}
}