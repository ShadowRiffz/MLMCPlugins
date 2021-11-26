package me.neoblade298.neosapiaddons;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageModifier;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import com.google.common.collect.ImmutableList;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.SkillPlugin;
import com.sucy.skill.api.event.SkillHealEvent;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.dynamic.custom.CustomEffectComponent;
import com.sucy.skill.dynamic.trigger.Trigger;

import me.neoblade298.neosapiaddons.conditions.AbsorptionCondition;
import me.neoblade298.neosapiaddons.conditions.AttackChargeCondition;
import me.neoblade298.neosapiaddons.conditions.BlockingCondition;
import me.neoblade298.neosapiaddons.conditions.InfinityCondition;
import me.neoblade298.neosapiaddons.conditions.ManaNameCondition;
import me.neoblade298.neosapiaddons.mechanics.AddAbsorptionMechanic;
import me.neoblade298.neosapiaddons.mechanics.IncreasePotionMechanic;
import me.neoblade298.neosapiaddons.mechanics.SpawnMythicmobMechanic;
import me.neoblade298.neosapiaddons.mechanics.ValueAttackSpeedMechanic;
import me.neoblade298.neosapiaddons.mechanics.ValueMaxMechanic;
import me.neoblade298.neosapiaddons.mechanics.ValueSkillLevelMechanic;

@SuppressWarnings("deprecation")
public class Main extends JavaPlugin implements Listener, SkillPlugin {
	private HungerController hc;
	public void onEnable() {
		super.onEnable();
		hc = new HungerController(this);
		Bukkit.getServer().getLogger().info("NeoSAPIAddons Enabled");
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(hc, this);

		getCommand("nsapi").setExecutor(new Commands(this));
	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoSAPIAddons Disabled");
		super.onDisable();
	}

	// Implement curse status properly
	@EventHandler
	public void onHeal(SkillHealEvent e) {
		LivingEntity target = e.getTarget();
		if (FlagManager.hasFlag(target, "curse")) {
			e.setCancelled(true);
			target.damage(e.getAmount());
		}
	}
	
	// Stop players from blocking mythicmobs
	@EventHandler(ignoreCancelled=false)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player) {
			Player p = (Player) e.getEntity();
			
			// Make damage go through shields
			if (p.isBlocking()) {
				String world = p.getWorld().getName();
				if (world.equals("Argyll") || world.equals("ClassPVP") || world.equals("Dev")) {
					e.setDamage(DamageModifier.BLOCKING, e.getDamage(DamageModifier.BLOCKING) * 0.2);
				}
				else {
					double blocked = e.getDamage(DamageModifier.BLOCKING);
					if (blocked < -15) {
						e.setDamage(DamageModifier.BLOCKING, -15 + ((blocked + 15) * 0.5));
					}
				}
			}
		}
	}
	
	@EventHandler
	public void onEat(PlayerItemConsumeEvent e) {
		if (e.getPlayer().getWorld().getName().equalsIgnoreCase("ClassPVP") ||
				e.getPlayer().getWorld().getName().equalsIgnoreCase("Argyll")) {
			if (e.getItem().getType().equals(Material.GOLDEN_APPLE) ||
					e.getItem().getType().equals(Material.ENCHANTED_GOLDEN_APPLE)) {
				e.setCancelled(true);
				e.getPlayer().sendMessage("§4[§c§lMLMC§4] §cGolden apples are restricted in the quest world.");
				return;
			}
		}
	}

	@Override
	public void registerClasses(SkillAPI api) {
		
	}

	@Override
	public void registerSkills(SkillAPI arg0) {

	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public List<Trigger> getTriggers() {
		return ImmutableList.of(
		);
	}
    
    @Override
    public List<CustomEffectComponent> getComponents() {
        return ImmutableList.of(
            new ValueMaxMechanic(),
            new SpawnMythicmobMechanic(),
            new AddAbsorptionMechanic(),
            new IncreasePotionMechanic(),
            new ValueAttackSpeedMechanic(),
            new ValueSkillLevelMechanic(),
            new AbsorptionCondition(),
            new AttackChargeCondition(),
            new ManaNameCondition(),
            new BlockingCondition(),
            new InfinityCondition()
        );
    }
    
    public boolean isQuestWorld(World w) {
    	String name = w.getName();
    	return name.equalsIgnoreCase("Argyll") || name.equalsIgnoreCase("Dev") || name.equalsIgnoreCase("ClassPVP");
    }
}