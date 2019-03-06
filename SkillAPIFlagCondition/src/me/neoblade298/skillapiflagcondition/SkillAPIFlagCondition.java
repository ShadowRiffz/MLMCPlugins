package me.neoblade298.skillapiflagcondition;

import java.util.ArrayList;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.gmail.berndivader.mythicmobsext.conditions.AbstractCustomCondition;
import com.gmail.berndivader.mythicmobsext.externals.ConditionAnnotation;
import com.gmail.berndivader.mythicmobsext.utils.Utils;
import com.sucy.skill.api.util.FlagManager;

import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.conditions.IEntityCondition;

@ConditionAnnotation(name="hasflag", author="Neoblade298")
public class SkillAPIFlagCondition extends AbstractCustomCondition implements IEntityCondition {
    private String[] flags;
    private boolean castinstead = false;
    private boolean stunchildren = false;
    private String msg;
    
    public SkillAPIFlagCondition(String line, MythicLineConfig mlc) {
        super(line,mlc);
        this.flags = mlc.getString("flag").trim().split(",");
        if(mlc.getString("castinstead") != null) {
        	castinstead = mlc.getString("castinstead").equals("true");
        }
        if(mlc.getString("stunchildren") != null) {
        	stunchildren = mlc.getString("stunchildren").equals("true");
        }
        msg = mlc.getString("msg");
    }

    public boolean check(AbstractEntity t) {
        Entity target = t.getBukkitEntity();
        ActiveMob am = Utils.mobmanager.getMythicMobInstance(target);
        if(msg != null) {
        	if(am.getEntity().getName() != null) {
        		msg = msg.replace("<mob.name>", am.getEntity().getName());
        	}
			msg = msg.replace("&", "§");
			msg = msg.replace("_", " ");
        }
        boolean result = false;
        if (am != null) {
        	LivingEntity ent = am.getLivingEntity();
        	for (String flag : flags) {
        		if (FlagManager.hasFlag(ent, flag)) {
        			
        			// Very specific behavior for stun rework, only use this when castinstead is enabled
        	    	if (castinstead) {
        	    		// Give the entity a stun tag
	    				if(!am.getEntity().hasScoreboardTag("StunTag")) {
	        	    		am.getEntity().addScoreboardTag("StunTag");
	        	    		
	        	    		// If a message was specified, show players in radius the message
	            	    	if(msg != null) {
	            	    		ArrayList<Entity> near = (ArrayList<Entity>) am.getLivingEntity().getNearbyEntities(40, 40, 40);
    	        	    		for(Entity e : near) {
    	        	    			if (e instanceof Player) {
    	        	    				Player p = (Player) e;
    	        	    				p.sendMessage(msg);
    	        	    			}
    	        	    		}
            	    		}
            	    	}
	    				
	    				// If stun children, iterate through each child and also give them a stun tag
	    				if(stunchildren) {
	    					for (AbstractEntity e : am.getChildren()) {
	    						e.getBukkitEntity().addScoreboardTag("StunTag");
	    					}
	    				}
	    				
        	    	}
        			result = true;
        		}
        		else {
        			result = false;
        			break;
        		}
        	}
        }
        if(castinstead) {
        	result = !result;
        }
        return result;
    }
}
