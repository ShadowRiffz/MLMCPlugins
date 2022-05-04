package me.neoblade298.neomythicextension.mechanics;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import io.lumine.mythic.api.adapters.AbstractEntity;
import io.lumine.mythic.api.config.MythicLineConfig;
import io.lumine.mythic.api.skills.ITargetedEntitySkill;
import io.lumine.mythic.api.skills.SkillMetadata;
import io.lumine.mythic.api.skills.SkillResult;
import io.lumine.mythic.bukkit.MythicBukkit;
import me.neoblade298.neomythicextension.MythicExt;
import me.neoblade298.neomythicextension.events.ChestDropEvent;

public class ScaleChestMechanic implements ITargetedEntitySkill {

	protected final ItemStack item;
	protected final double basechance;
	protected final String msg;
	protected final String boss;
	protected final Random rand;

	public ScaleChestMechanic(MythicLineConfig config) {
        String itemString = config.getString("i", "mi_sewerzombie");
        this.item = MythicBukkit.inst().getItemManager().getItemStack(itemString);
        this.basechance = config.getDouble(new String[] {"basechance", "bc"}, 0.25);
        this.msg = new String("&4[&c&lMLMC&4] &7" + config.getString("msg", "&7You found a Boss Chest")).replaceAll("&", "�");
        this.boss = config.getString("boss", "Ratface");
        this.rand = new Random();
        
        if (this.item == null) {
        	Bukkit.getLogger().log(Level.WARNING, "[NeoMythicExtension] Item doesn't exist: " + itemString);
        }
	}
	
	@Override
    public SkillResult castAtEntity(SkillMetadata data, AbstractEntity target) {
		try {
			if (target.getBukkitEntity() instanceof Player) {
				if (data.getCaster().getLevel() < 1) {
					return SkillResult.CONDITION_FAILED;
				}
				
				double rand = this.rand.nextDouble();
	
				// Increases chance by boss level
				double chance = Math.min(0.5, this.basechance + (0.005 * (data.getCaster().getLevel() - 1)));
				
				// Check if player is holding a drop charm
				Player p = (Player) target.getBukkitEntity();
				int dropType = 0;
	
				if (p.hasPermission("tokens.active.boss")) {
					dropType = 2;
					chance = 1;
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset tokens.active.boss");
				}
	
				final ItemStack fitem = this.item;
				final double fchance = chance;
				final int fdrop = dropType;
				new BukkitRunnable() {
					public void run() {
						ChestDropEvent e = new ChestDropEvent(p, fchance, fdrop);
						Bukkit.getPluginManager().callEvent(e);
						double moddedChance = e.getChance();
						double dropType = e.getDropType();
						
						// Check for successful drop
						if (rand <= moddedChance) {
							HashMap<Integer, ItemStack> failed = p.getInventory().addItem(fitem);
							if (!failed.isEmpty()) p.getWorld().dropItem(p.getLocation(), fitem);
							
							// Message
							String localMsg = msg;
							if (dropType == 1 && rand >= fchance) localMsg += " via Chest Augment";
							if (dropType == 2) localMsg += " via Boss Chest Token";
							localMsg += "!";
							p.sendMessage(localMsg);
							String name = fitem.getItemMeta().getDisplayName().replaceAll("�", "&");
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sync console all neoshinies " + p.getName() + " has found " + name);
						}
					}
				}.runTask(MythicExt.inst);
				return SkillResult.SUCCESS;
			}
			return SkillResult.INVALID_TARGET;
		} catch (Exception e) {
			e.printStackTrace();
			return SkillResult.ERROR;
		}
    }
}
