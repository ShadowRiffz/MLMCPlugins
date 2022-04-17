package me.neoblade298.neomythicextension;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicConditionLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicMechanicLoadEvent;
import io.lumine.xikage.mythicmobs.api.bukkit.events.MythicTargeterLoadEvent;
import io.lumine.xikage.mythicmobs.skills.SkillCondition;
import me.neoblade298.neomythicextension.conditions.*;
import me.neoblade298.neomythicextension.mechanics.*;
import me.neoblade298.neomythicextension.objects.SpawnerMaker;
import me.neoblade298.neomythicextension.targeters.*;
import me.neoblade298.neomythicextension.triggers.*;
import me.neoblade298.neomythicextension.triggers.StatusTrigger;

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
		Bukkit.getPluginManager().registerEvents(new StatusTrigger(this), this);
		globalscores = new ConcurrentHashMap<String, Integer>();
		scores = new ConcurrentHashMap<String, ConcurrentHashMap<String, Integer>>();
		spawnermakers = new HashMap<UUID, SpawnerMaker>();
		
		// Create Settings
		// settings = (NeoSettings) Bukkit.getPluginManager().getPlugin("NeoSettings");
		
		// Get command listener
		this.getCommand("nme").setExecutor(new Commands(this));
		log.info("NeoMythicExtensions Enabled!");
	}

	public void onDisable() {
		log.info("NeoMythicExtensions Disabled!");
	}

	@EventHandler
	public void onMythicConditionLoad(MythicConditionLoadEvent event) {
		String name = event.getConditionName();
		if (name.equalsIgnoreCase("hasflag")) {
			SkillCondition condition = new SkillAPIFlagCondition(event.getConfig(), this);
			event.register(condition);
		}

		else if (name.equalsIgnoreCase("nglobalscore")) {
			GlobalScoreCondition condition = new GlobalScoreCondition(event.getConfig());
			event.register(condition);
		}

		else if (name.equalsIgnoreCase("nscore")) {
			ScoreCondition condition = new ScoreCondition(event.getConfig());
			event.register(condition);
		}

		else if (name.equalsIgnoreCase("playersinboss")) {
			PlayersInBossCondition condition = new PlayersInBossCondition(event.getConfig());
			event.register(condition);
		}

		else if (name.equalsIgnoreCase("aboveblock")) {
			AboveBlockCondition condition = new AboveBlockCondition(event.getConfig());
			event.register(condition);
		}

		else if (name.equalsIgnoreCase("strongplayerwithin")) {
			StrongPlayerWithin condition = new StrongPlayerWithin(event.getConfig());
			event.register(condition);
		}

		else if (event.getConditionName().equalsIgnoreCase("fightingboss")) {
			FightingBossCondition condition = new FightingBossCondition(event.getConfig());
			event.register(condition);
		}
	}

	@EventHandler
	public void onMythicTargeterLoad(MythicTargeterLoadEvent event) {
		String name = event.getTargeterName();
		if (name.equalsIgnoreCase("playersinboss")
				|| event.getTargeterName().equalsIgnoreCase("pib")) {
			PlayersInBossTargeter targeter = new PlayersInBossTargeter(event.getConfig());
			event.register(targeter);
		}

		else if (name.equalsIgnoreCase("offset")) {
			OffsetTargeter targeter = new OffsetTargeter(event.getConfig());
			event.register(targeter);
		}
	}

	@EventHandler
	public void onMythicMechanicLoad(MythicMechanicLoadEvent event) {

		if (event.getMechanicName().equalsIgnoreCase("nscore")) {
			ModScore mechanic = new ModScore(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("nglobalscore")) {
			ModGlobalScore mechanic = new ModGlobalScore(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("warn")) {
			WarnMechanic mechanic = new WarnMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("taunt")) {
			TauntMechanic mechanic = new TauntMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("reducethreat")) {
			ReduceThreatMechanic mechanic = new ReduceThreatMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("flag")) {
			FlagMechanic mechanic = new FlagMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("removeflag")) {
			RemoveFlagMechanic mechanic = new RemoveFlagMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("scalechest")) {
			ScaleChestMechanic mechanic = new ScaleChestMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("scaletolevel")) {
			ScaleToLevelMechanic mechanic = new ScaleToLevelMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("scalegold")) {
			ScaleGoldMechanic mechanic = new ScaleGoldMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("researchpoints")) {
			ResearchPointsMechanic mechanic = new ResearchPointsMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("researchpointschance")) {
			ResearchPointsChanceMechanic mechanic = new ResearchPointsChanceMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("researchkills")) {
			ResearchKillsMechanic mechanic = new ResearchKillsMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("modmana")) {
			ModManaMechanic mechanic = new ModManaMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("scaleheal")) {
			ScaleHealMechanic mechanic = new ScaleHealMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("scaleexp")) {
			ScaleExpMechanic mechanic = new ScaleExpMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("givestoreditem")) {
			GiveStoredItemMechanic mechanic = new GiveStoredItemMechanic(event.getConfig());
			event.register(mechanic);
		}

		else if (name.equalsIgnoreCase("startminigame")) {
			StartMinigameMechanic mechanic = new StartMinigameMechanic(event.getConfig());
			event.register(mechanic);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onDummyDamage(EntityDamageByEntityEvent e) {
		String name = e.getEntity().getCustomName();
		if (name != null && name.contains("(Damage)")) {
			if (e.getDamager() instanceof Player) {
				Player p = (Player) e.getDamager();
				for (Entity ent : e.getEntity().getNearbyEntities(5, 5, 5)) {
					if (ent instanceof Player) {
						ent.sendMessage("§e" + p.getName() + " §7did §c" + (Math.round(e.getFinalDamage() * 100) / 100) + " §7damage");
					}
				}
			}
		}
	}

}