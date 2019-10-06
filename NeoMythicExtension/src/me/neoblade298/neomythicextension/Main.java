package me.neoblade298.neomythicextension;

import java.util.HashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.gmail.berndivader.mythicmobsext.conditions.MobsInRadiusCondition;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import me.neoblade298.neomythicextension.conditions.GlobalScoreCondition;
import me.neoblade298.neomythicextension.conditions.ScoreCondition;
import me.neoblade298.neomythicextension.conditions.SkillAPIFlagCondition;
import me.neoblade298.neomythicextension.mechanics.InstanceTpMechanic;
import me.neoblade298.neomythicextension.mechanics.ModGlobalScore;
import me.neoblade298.neomythicextension.mechanics.ModScore;

public class Main extends JavaPlugin implements Listener {

	private Logger log;
	public HashMap<String, Integer> globalscores;
	// Hashmap of objectives which leads to hashmap of uuids to integers
	public HashMap<String, HashMap<String, Integer>> scores;
	 
	@Override
	public void onEnable() {
		log = this.getLogger();
		Bukkit.getPluginManager().registerEvents(this, this);
		globalscores = new HashMap<String, Integer>();
		scores = new HashMap<String, HashMap<String, Integer>>();
		
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
		}

		if(event.getConditionName().equalsIgnoreCase("mobsinradius"))	{
			MobsInRadiusCondition condition = new MobsInRadiusCondition(event.getConfig());
			event.register(condition);
		}

		if(event.getConditionName().equalsIgnoreCase("nglobalscore"))	{
			GlobalScoreCondition condition = new GlobalScoreCondition(event.getConfig());
			event.register(condition);
		}

		if(event.getConditionName().equalsIgnoreCase("nscore"))	{
			ScoreCondition condition = new ScoreCondition(event.getConfig());
			event.register(condition);
		}
	}
	
	@EventHandler
	public void onMythicMechanicLoad(MythicMechanicLoadEvent event) {
		log.info("MythicMechanicLoadEvent called for mechanic " + event.getMechanicName());

		if(event.getMechanicName().equalsIgnoreCase("instancetp"))	{
			InstanceTpMechanic mechanic = new InstanceTpMechanic(event.getConfig());
			event.register(mechanic);
		}

		if(event.getMechanicName().equalsIgnoreCase("nscore"))	{
			ModScore mechanic = new ModScore(event.getConfig());
			event.register(mechanic);
		}

		if(event.getMechanicName().equalsIgnoreCase("nglobalscore"))	{
			ModGlobalScore mechanic = new ModGlobalScore(event.getConfig());
			event.register(mechanic);
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