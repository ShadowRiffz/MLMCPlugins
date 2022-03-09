package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.SkillBuffEvent;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.util.Buff;
import com.sucy.skill.api.util.BuffManager;
import com.sucy.skill.api.util.BuffType;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.listener.AttributeListener;
import com.sucy.skill.listener.MechanicListener;

import me.Neoblade298.NeoConsumables.Consumables;
import me.Neoblade298.NeoConsumables.runnables.HealthRunnable;
import me.Neoblade298.NeoConsumables.runnables.ManaRunnable;

public class DurationEffects {
	private Consumables main;
	private long startTime;
	private FoodConsumable cons;
	private ArrayList<BukkitTask> tasks;
	private Player p;
	
	public DurationEffects(Consumables main, FoodConsumable cons, long startTime, Player p, ArrayList<BukkitTask> tasks) {
		this.main = main;
		this.cons = cons;
		this.startTime = startTime;
		this.tasks = tasks;
		this.p = p;
	}
	
	public FoodConsumable getCons() {
		return cons;
	}
	
	public ArrayList<BukkitTask> getTasks() {
		return tasks;
	}
	
	public void startEffects() {
		int secondsElapsed = (int) ((System.currentTimeMillis() - startTime));
		int ticksElapsed = secondsElapsed * 20;
		boolean isActive = false; 
		
		// Attributes
		if (cons.getAttributeTime() - secondsElapsed > 0) {
			isActive = true;
			cons.getAttributes().applyAttributes(p);
		}
		
		// Flags
		for (FlagAction flag : cons.getFlags()) {
			if (flag.isAdd()) {
				int remaining = flag.getDuration() - secondsElapsed;
				if (remaining > 0) {
					isActive = true;
					FlagManager.addFlag(p, p, flag.getFlag(), remaining * 20);
				}
			}
		}
		
		// Buffs
		for (BuffAction buff : cons.getBuffs()) {
	        BuffType buffType = buff.getType();
	        int remaining = buff.getDuration() - secondsElapsed;
	        String category = buff.getCategory();
	        int remainingTicks = remaining * 20;
	        if (remainingTicks > 0) {
	            SkillBuffEvent event = new SkillBuffEvent(p, p, buff.getValue(), remainingTicks, buffType, buff.isPercent());
	            Bukkit.getPluginManager().callEvent(event);
	            
	            if (!event.isCancelled()) {
	    			isActive = true;
	                BuffManager.getBuffData(p).addBuff(
	                        buffType,
	                        category,
	                        new Buff(cons.getKey() + "-" + p, event.getAmount(), buff.isPercent()),
	                        event.getTicks());
	            }
	        }
		}
		
		// Speed
		int speedTime = cons.getSpeedTime();
		if (speedTime - ticksElapsed > 0) {
			isActive = true;
            AttributeListener.refreshSpeed(p);
            FlagManager.addFlag(p, p, MechanicListener.SPEED_KEY, speedTime * 20);
            p.setWalkSpeed((float) (speedTime * p.getWalkSpeed()));
		}

		// Health and mana regen
		PlayerData data = SkillAPI.getPlayerData(p);
		int healthReps = cons.getHealthReps();
		int manaReps = cons.getManaReps();
		int remainingHealthReps = healthReps - (secondsElapsed / cons.getHealthPeriod());
		int remainingManaReps = manaReps - (secondsElapsed / cons.getManaPeriod());
		
		if (remainingHealthReps > 0 && !p.isDead()) {
			isActive = true;
			tasks.add(new HealthRunnable(p, cons.getHealth(), remainingHealthReps).runTaskTimer(main, 0, cons.getHealthPeriod() * 20));
		}
		
		if (remainingManaReps > 0 && !p.isDead()) {
			if (data.getMainClass().getData().getManaName().contains("MP")) {
				isActive = true;
				tasks.add(new ManaRunnable(p, cons.getMana(), remainingManaReps).runTaskTimer(main, 0, cons.getManaPeriod() * 20));
			}
		}
		
		// If no effects are happening anymore, remove it
		if (!isActive) {
			FoodConsumable.effects.remove(p.getUniqueId());
		}
	}
	
	public void endEffects() {
		int secondsElapsed = (int) ((System.currentTimeMillis() - startTime));
		
		for (BukkitTask task : tasks) {
			if (task.isCancelled()) {
				task.cancel();
			}
		}
		
		// Potion effects
		for (PotionEffect effect : cons.getPotions()) {
			if (p.hasPotionEffect(effect.getType())) {
				p.removePotionEffect(effect.getType());
			}
		}
		
		if (cons.getAttributeTime() - secondsElapsed > 0) {
			cons.getAttributes().removeAttributes(p);
		}
	}
}
