package me.neoblade298.neomythicextension.mechanics;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.adapters.bukkit.BukkitAdapter;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neobossinstances.Main;

public class InstanceTpMechanic extends SkillMechanic implements ITargetedEntitySkill {
	protected final String boss;
	protected final int max;
	private int count;
	private String instance;
	Main nbi;

	public InstanceTpMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        this.nbi = (Main) MythicMobs.inst().getServer().getPluginManager().getPlugin("NeoBossInstances");
        
        this.boss = config.getString("boss", "Ratface");
        this.max = config.getInteger("max", 4);
        this.count = 0;
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
    	LivingEntity bukkitTarget = (LivingEntity) BukkitAdapter.adapt(target);

    	// Check if target is player
    	if (bukkitTarget instanceof Player) {
    		Player p = (Player) bukkitTarget;
    		Main pl = (Main) Bukkit.getPluginManager().getPlugin("NeoBossInstances");
    		
    		// If player is in hashmap, check their cooldown.
    		if (pl.cooldowns.get(this.boss).containsKey(p.getUniqueId())) {
	    		long lastUse = pl.cooldowns.get(this.boss).get(p.getUniqueId());
	    		long currTime = System.currentTimeMillis();
	    		long cooldown = pl.bossInfo.get(this.boss).getCooldown() * 1000;
	    		if (currTime > lastUse + cooldown) {
	    			sendPlayer(p);
	    		}
	    		else {
					double temp = (lastUse + cooldown - currTime) / 6000;
					temp /= 10;
		    		p.sendMessage("§4[§c§lBosses§4] §7You are on cooldown for §e" + temp + " §7more minutes!");
	    		}
    		}
    		// If player is not in hashmap, they don't have a cooldown
    		else {
    			sendPlayer(p);
    		}
    	}
    	return true;
    }
	
	public void sendPlayer(Player p) {
		if (count < max) {
			if (count == 0) {
				this.instance = this.nbi.findInstance(this.boss);
			}
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "boss tp " + p.getName() + " " + this.boss + " " + this.instance);
			count++;
    		BukkitRunnable resetCount = new BukkitRunnable() {
    			public void run() {
    				count = 0;
    			}
    		};
    		resetCount.runTaskLater(MythicMobs.inst(), 60L);
		}
	}

}
