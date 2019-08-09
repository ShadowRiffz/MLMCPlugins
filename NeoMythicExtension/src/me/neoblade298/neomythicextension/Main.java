package me.neoblade298.neomythicextension;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.berndivader.mythicmobsext.conditions.MobsInRadiusCondition;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import me.neoblade298.neomythicextension.conditions.SkillAPIFlagCondition;

public class Main extends JavaPlugin implements Listener {

	private Logger log;
	 
	@Override
	public void onEnable() {
		log = this.getLogger();
		Bukkit.getPluginManager().registerEvents(this, this);
		
		log.info("NeoMythicExtensions Enabled!");
	}
	
	public void onDisable(){
		log.info("NeoMythicExtensions Disabled!");
	}

	/*
	 * Registers all of the custom mechanics when MythicMechanicLoadEvent is called
	 */
	/*@EventHandler
	public void onMythicMechanicLoad(MythicMechanicLoadEvent event)	{
		// Empty for now
	}*/
	
	/*
	 * Registers all of the custom conditions when MythicConditionLoadEvent is called
	 */
	@EventHandler
	public void onMythicConditionLoad(MythicConditionLoadEvent event)	{
		log.info("MythicConditionLoadEvent called for condition " + event.getConditionName());

		if(event.getConditionName().equalsIgnoreCase("hasflag"))	{
			SkillCondition condition = new SkillAPIFlagCondition(event.getConfig());
			event.register(condition);
			log.info("-- Registered SkillAPIFlagCondition!");
		}

		if(event.getConditionName().equalsIgnoreCase("mobsinradius"))	{
			MobsInRadiusCondition condition = new MobsInRadiusCondition(event.getConfig());
			event.register(condition);
			log.info("-- Registered MobsInRadiusCondition!");
		}
	}
	
	/*
	 * Registers all of the custom drops when MythicDropLoadEvent is called
	 */
	/*@EventHandler
	public void onMythicDropLoad(MythicDropLoadEvent event)	{
		log.info("MythicDropLoadEvent called for drop " + event.getDropName());

		if(event.getDropName().equalsIgnoreCase("SPECIAL"))	{
			Drop drop = new SpecialItem(event.getConfig());
			event.register(drop);
			log.info("-- Registered SPECIAL drop!");
		}
	}*/
}