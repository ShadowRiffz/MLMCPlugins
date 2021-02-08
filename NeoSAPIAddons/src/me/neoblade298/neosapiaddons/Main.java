package me.neoblade298.neosapiaddons;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.collect.ImmutableList;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.SkillPlugin;
import com.sucy.skill.api.event.SkillHealEvent;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.dynamic.custom.CustomEffectComponent;
import com.sucy.skill.dynamic.trigger.Trigger;

public class Main extends JavaPlugin implements Listener, SkillPlugin {
	HashMap<Player, Player> ironbond;
	public void onEnable() {
		super.onEnable();
		Bukkit.getServer().getLogger().info("NeoSAPIAddons Enabled");
		getServer().getPluginManager().registerEvents(this, this);

		getCommand("neosapiaddons").setExecutor(new Commands());
		
		ironbond = new HashMap<Player, Player>();
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
	
	@EventHandler
	public void onDamage(EntityDamageByEntityEvent e) {
		if (e.getEntity() instanceof Player && !(e.getDamager() instanceof Player)) {
			Player p = (Player) e.getEntity();
			
			// Guardian angel
			if (p.getAbsorptionAmount() > 0) {
				e.setCancelled(true);
				p.setAbsorptionAmount(p.getAbsorptionAmount() - 1 >= 0 ? p.getAbsorptionAmount() - 1 : 0);
			}
			
			// Iron bond collection activate
			if (p.getHealth() <= e.getFinalDamage()) {
				List<Entity> nearby = p.getNearbyEntities(20, 20, 20);
				for (Entity ent : nearby) {
					if (ent instanceof Player && ent.hasPermission("collections.sh.use.36") && !ent.hasPermission("*")) {
						Player bond = (Player) ent;
						if (!FlagManager.hasFlag(bond, "cd_ironBond") &&
								bond.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.3 <= bond.getHealth() && bond.isValid() &&
								!p.getName().equals(bond.getName()) && bond.getHealth() > e.getFinalDamage()) {
							FlagManager.addFlag(bond, "cd_ironBond", 60 * 20);
							p.sendMessage("§cIron Bond§7 was activated by §e"  + bond.getName());
							bond.sendMessage("§cIron Bond§7 was activated");
							ironbond.put(p, bond);
							FlagManager.addFlag(p, "fl_ironBond", 5 * 20);
							if (bond.getAbsorptionAmount() >= 1) {
								bond.setHealth(bond.getHealth() - e.getFinalDamage());
								bond.damage(1, e.getDamager());
							}
							else {
								bond.setAbsorptionAmount(bond.getAbsorptionAmount() - 1 > 0 ? bond.getAbsorptionAmount() - 1 : 0);
							}
				    		BukkitRunnable ironBondCooldown = new BukkitRunnable() {
				    			public void run() {
		    						if (bond.isOnline()) {
		    							bond.sendMessage("§cIron Bond§7 is off cooldown");
		    						}
				    			}
				    		};
				    		ironBondCooldown.runTaskLater(this, 20L * 60L);
							e.setCancelled(true);
							break;
						}
					}
				}
			}
			// Iron bond collection continued
			if (FlagManager.hasFlag(p, "fl_ironBond")) {
				Player bond = ironbond.get(p);
				if (bond.isValid() && bond.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() * 0.3 <= bond.getHealth() &&
						bond.getHealth() > e.getFinalDamage()) {
					if (bond.getAbsorptionAmount() < 1) {
						bond.setHealth(bond.getHealth() - e.getFinalDamage());
						bond.damage(1, e.getDamager());
					}
					else {
						bond.setAbsorptionAmount(bond.getAbsorptionAmount() - 1 > 0 ? bond.getAbsorptionAmount() - 1 : 0);
					}
					e.setCancelled(true);
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
            new AbsorptionCondition(),
            new AttackChargeCondition(),
            new ManaNameCondition()
        );
    }
}