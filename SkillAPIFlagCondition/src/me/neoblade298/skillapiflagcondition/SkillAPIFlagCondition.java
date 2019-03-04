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
    private String msg;
    
    public SkillAPIFlagCondition(String line, MythicLineConfig mlc) {
        super(line,mlc);
        this.flags = mlc.getString("flag").trim().split(",");
        castinstead = mlc.getString("castinstead").equals("true");
        msg = mlc.getString("msg");
    }

    public boolean check(AbstractEntity t) {
        Entity target = t.getBukkitEntity();
        ActiveMob am = Utils.mobmanager.getMythicMobInstance(target);
        boolean result = false;
        if (am != null) {
        	LivingEntity ent = am.getLivingEntity();
        	for (String flag : flags) {
        		if (FlagManager.hasFlag(ent, flag)) {
        	    	if (castinstead) {
            	    	if(msg != null) {
            	    		ArrayList<Entity> near = (ArrayList<Entity>) am.getLivingEntity().getNearbyEntities(40, 40, 40);
            	    		msg = msg.replace("<mob.name>", am.getEntity().getName());
            	    		msg = msg.replace("&", "§");
            	    		msg = msg.replace("_", " ");
    	    				if(!am.getEntity().hasScoreboardTag("StunTag")) {
    	        	    		am.getEntity().addScoreboardTag("StunTag");
    	        	    		for(Entity e : near) {
    	        	    			if (e instanceof Player) {
    	        	    				Player p = (Player) e;
    	        	    				p.sendMessage(msg);
    	        	    			}
    	        	    		}
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
