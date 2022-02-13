package me.neoblade298.neomythicextension.mechanics;

import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neomythicextension.events.ChestDropEvent;

public class ScaleChestMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final ItemStack item;
	protected final double basechance;
	protected final String msg;
	protected final String boss;
	protected final Random rand;

	public ScaleChestMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        String itemString = config.getString("i", "mi_sewerzombie");
        this.item = MythicMobs.inst().getItemManager().getItemStack(itemString);
        this.basechance = config.getDouble(new String[] {"basechance", "bc"}, 0.25);
        this.msg = new String("&4[&c&lMLMC&4] &7" + config.getString("msg", "&7You found a Boss Chest")).replaceAll("&", "§");
        this.boss = config.getString("boss", "Ratface");
        this.rand = new Random();
        
        if (this.item == null) {
        	Bukkit.getLogger().log(Level.WARNING, "[NeoMythicExtension] Item doesn't exist: " + itemString);
        }
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player) {
			double rand = this.rand.nextDouble();
			double chance = this.basechance;

			// Increases chance by boss level
			chance += Math.min(0.25, 0 + (0.0025 * (data.getCaster().getLevel() - 1)));;
			double moddedChance = chance;
			
			// Check if player is holding a drop charm
			Player p = (Player) target.getBukkitEntity();
			int dropType = 0;

			if (p.hasPermission("tokens.active.boss")) {
				dropType = 2;
				chance = 1;
				moddedChance = 1;
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset tokens.active.boss");
			}
			
			ChestDropEvent e = new ChestDropEvent(p, chance, dropType);
			Bukkit.getPluginManager().callEvent(e);
			moddedChance = e.getChance();
			dropType = e.getDropType();
			
			// Check for successful drop
			if (rand <= moddedChance) {
				HashMap<Integer, ItemStack> failed = p.getInventory().addItem(this.item);
				if (!failed.isEmpty()) p.getWorld().dropItem(p.getLocation(), this.item);
				
				// Message
				String localMsg = msg;
				if (dropType == 1 && rand >= chance) localMsg += " via Chest Augment";
				if (dropType == 2) localMsg += " via Boss Chest Token";
				localMsg += "!";
				p.sendMessage(localMsg);
				String name = this.item.getItemMeta().getDisplayName().replaceAll("§", "&");
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sync console all neoshinies " + p.getName() + " has found " + name);
			}
			return true;
		}
		return false;
    }
}
