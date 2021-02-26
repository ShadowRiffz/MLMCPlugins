package me.neoblade298.neomythicextension;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import me.neoblade298.neomythicextension.conditions.AboveBlockCondition;
import me.neoblade298.neomythicextension.conditions.GlobalScoreCondition;
import me.neoblade298.neomythicextension.conditions.PlayersInBossCondition;
import me.neoblade298.neomythicextension.conditions.ScoreCondition;
import me.neoblade298.neomythicextension.conditions.SkillAPIFlagCondition;
import me.neoblade298.neomythicextension.mechanics.FlagMechanic;
import me.neoblade298.neomythicextension.mechanics.DropChanceMechanic;
import me.neoblade298.neomythicextension.mechanics.InstanceTpMechanic;
import me.neoblade298.neomythicextension.mechanics.ModGlobalScore;
import me.neoblade298.neomythicextension.mechanics.ModScore;
import me.neoblade298.neomythicextension.mechanics.RandomizeGoldMechanic;
import me.neoblade298.neomythicextension.mechanics.RemoveFlagMechanic;
import me.neoblade298.neomythicextension.mechanics.ScaleHealthMechanic;
import me.neoblade298.neomythicextension.mechanics.TauntMechanic;
import me.neoblade298.neomythicextension.mechanics.WarnMechanic;
import me.neoblade298.neomythicextension.objects.SpawnerMaker;
import me.neoblade298.neomythicextension.targeters.OffsetTargeter;
import me.neoblade298.neomythicextension.targeters.PlayersInBossTargeter;

public class Main extends JavaPlugin implements Listener {

	private Logger log;
	public ConcurrentHashMap<String, Integer> globalscores;
	// ConcurrentHashMap of objectives which leads to ConcurrentHashMap of uuids to
	// integers
	public ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>> scores;
	public HashMap<UUID, SpawnerMaker> spawnermakers;

	@Override
	public void onEnable() {
		log = this.getLogger();
		Bukkit.getPluginManager().registerEvents(this, this);
		globalscores = new ConcurrentHashMap<String, Integer>();
		scores = new ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>();
		spawnermakers = new HashMap<UUID, SpawnerMaker>();

		// Get command listener
		this.getCommand("nme").setExecutor(new Commands(this));
		log.info("NeoMythicExtensions Enabled!");
	}

	public void onDisable() {
		log.info("NeoMythicExtensions Disabled!");
	}

	@EventHandler
	public void onMythicConditionLoad(MythicConditionLoadEvent event) {

		if (event.getConditionName().equalsIgnoreCase("hasflag")) {
			SkillCondition condition = new SkillAPIFlagCondition(event.getConfig(), this);
			event.register(condition);
		}

		if (event.getConditionName().equalsIgnoreCase("nglobalscore")) {
			GlobalScoreCondition condition = new GlobalScoreCondition(event.getConfig());
			event.register(condition);
		}

		if (event.getConditionName().equalsIgnoreCase("nscore")) {
			ScoreCondition condition = new ScoreCondition(event.getConfig());
			event.register(condition);
		}

		if (event.getConditionName().equalsIgnoreCase("playersinboss")) {
			PlayersInBossCondition condition = new PlayersInBossCondition(event.getConfig());
			event.register(condition);
		}

		if (event.getConditionName().equalsIgnoreCase("aboveblock")) {
			AboveBlockCondition condition = new AboveBlockCondition(event.getConfig());
			event.register(condition);
		}
	}

	@EventHandler
	public void onMythicTargeterLoad(MythicTargeterLoadEvent event) {

		if (event.getTargeterName().equalsIgnoreCase("playersinboss")
				|| event.getTargeterName().equalsIgnoreCase("pib")) {
			PlayersInBossTargeter targeter = new PlayersInBossTargeter(event.getConfig());
			event.register(targeter);
		}

		if (event.getTargeterName().equalsIgnoreCase("offset")) {
			OffsetTargeter targeter = new OffsetTargeter(event.getConfig());
			event.register(targeter);
		}
	}

	@EventHandler
	public void onMythicMechanicLoad(MythicMechanicLoadEvent event) {

		if (event.getMechanicName().equalsIgnoreCase("instancetp")) {
			InstanceTpMechanic mechanic = new InstanceTpMechanic(event.getConfig());
			event.register(mechanic);
		}

		if (event.getMechanicName().equalsIgnoreCase("nscore")) {
			ModScore mechanic = new ModScore(event.getConfig());
			event.register(mechanic);
		}

		if (event.getMechanicName().equalsIgnoreCase("nglobalscore")) {
			ModGlobalScore mechanic = new ModGlobalScore(event.getConfig());
			event.register(mechanic);
		}

		if (event.getMechanicName().equalsIgnoreCase("warn")) {
			WarnMechanic mechanic = new WarnMechanic(event.getConfig());
			event.register(mechanic);
		}

		if (event.getMechanicName().equalsIgnoreCase("taunt")) {
			TauntMechanic mechanic = new TauntMechanic(event.getConfig());
			event.register(mechanic);
		}

		if (event.getMechanicName().equalsIgnoreCase("flag")) {
			FlagMechanic mechanic = new FlagMechanic(event.getConfig());
			event.register(mechanic);
		}

		if (event.getMechanicName().equalsIgnoreCase("removeflag")) {
			RemoveFlagMechanic mechanic = new RemoveFlagMechanic(event.getConfig());
			event.register(mechanic);
		}

		if (event.getMechanicName().equalsIgnoreCase("dropchance")) {
			DropChanceMechanic mechanic = new DropChanceMechanic(event.getConfig());
			event.register(mechanic);
		}

		if (event.getMechanicName().equalsIgnoreCase("scalehealth")) {
			ScaleHealthMechanic mechanic = new ScaleHealthMechanic(event.getConfig());
			event.register(mechanic);
		}

		if (event.getMechanicName().equalsIgnoreCase("randomizegold")) {
			RandomizeGoldMechanic mechanic = new RandomizeGoldMechanic(event.getConfig());
			event.register(mechanic);
		}
	}

}