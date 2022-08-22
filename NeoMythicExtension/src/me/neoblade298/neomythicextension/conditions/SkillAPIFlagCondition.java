package me.neoblade298.neomythicextension.conditions;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import com.sucy.skill.api.util.FlagManager;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.conditions.IEntityCondition;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import me.neoblade298.neomythicextension.MythicExt;

public class SkillAPIFlagCondition implements IEntityCondition {
	private String[] flags;
	private boolean castinstead = false;
	private boolean stunchildren = false;
	private boolean action = true;
	private int setgcd = -1;
	private String msg;
	private ArrayList<Entity> near;
	private MythicExt main;

	public SkillAPIFlagCondition(MythicLineConfig mlc, MythicExt main) {
		this.flags = mlc.getString(new String[] { "flag", "f" }).trim().split(",");
		if (mlc.getString("action") != null) {
			castinstead = mlc.getString("action").equals("castinstead");

			// Make it so action is true by default
			action = !mlc.getString("action").equals("false");
		}
		if (mlc.getString("stunchildren") != null) {
			stunchildren = mlc.getString("stunchildren").equals("true");
		}
		if (mlc.getInteger("setgcd") != 0) {
			setgcd = mlc.getInteger("setgcd");
		}
		msg = mlc.getString("msg");
		near = null;
		this.main = main;
	}

	public boolean check(AbstractEntity t) {
    	try {
			ActiveMob am = MythicBukkit.inst().getMobManager().getMythicMobInstance(t);
			boolean result = false;
			
			// Checking a player for a flag
			if (t.getBukkitEntity() instanceof Player) {
				Player p = (Player) t.getBukkitEntity();
				result = checkPlayer(p);
			}
			else if (am != null) {
				result = checkMob((LivingEntity) t.getBukkitEntity(), am);
			}
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean checkPlayer(Player p) {
		boolean result = false;
		if (action) {
			for (String flag : flags) {
				if (FlagManager.hasFlag(p, flag)) {
					result = true;
				}
				else {
					result = false;
					break;
				}
			}
		}
		else {
			for (String flag : flags) {
				if (!FlagManager.hasFlag(p, flag)) {
					result = true;
				}
				else {
					result = false;
					break;
				}
			}
		}
		return result;
	}
	
	private boolean checkMob(LivingEntity ent, ActiveMob am) {
		boolean result = true;
		if (action) {
			for (String flag : flags) {
				if (FlagManager.hasFlag(ent, flag)) {
					result = true;
	
					// Very specific behavior for stun rework
					if (castinstead) {
						// Set result to false if castinstead
						result = false;
						// Give the entity a stun tag
						if (!am.getEntity().hasScoreboardTag("StunTag")) {
							am.getEntity().addScoreboardTag("StunTag");
	
							// If a message was specified, show players in radius the message
							if (msg != null) {
								if (am.getEntity().getName() != null) {
									msg = msg.replace("<mob.name>", am.getEntity().getName());
								}
								msg = msg.replace("&", "§");
								msg = msg.replace("_", " ");
								Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
									public void run() {
										near = (ArrayList<Entity>) am.getEntity().getBukkitEntity()
												.getNearbyEntities(40, 40, 40);
										displayMessage();
									}
								});
							}
						}
						if (setgcd != -1) {
							am.setGlobalCooldown(setgcd);
						}
	
						// If stun children, iterate through each child and also give them a stun tag
						if (stunchildren) {
							for (AbstractEntity e : am.getChildren()) {
								e.getBukkitEntity().addScoreboardTag("StunTag");
							}
						}
	
					}
				}
				else {
					// Doesn't have flag, which with castinstead means cast (true). Otherwise don't.
					result = castinstead;
					break;
				}
			}
		}
		else {
			for (String flag : flags) {
				if (FlagManager.hasFlag(ent, flag)) {
					// If any flag exists, result is false.
					result = false;
					break;
				}
			}
		}
		return result;
	}

	public void displayMessage() {
		for (Entity e : near) {
			if (e instanceof Player) {
				Player p = (Player) e;
				p.sendMessage(msg);
			}
		}
	}
}
