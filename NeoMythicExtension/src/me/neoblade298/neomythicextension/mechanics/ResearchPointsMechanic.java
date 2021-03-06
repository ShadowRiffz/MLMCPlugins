package me.neoblade298.neomythicextension.mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.mobs.ActiveMob;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;
import me.neoblade298.neoresearch.Research;

public class ResearchPointsMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final int amount;
	protected final double basechance;
	protected final double basicmult;
	protected final double advancedmult;
	protected final double basicchance;
	protected final double advancedchance;
	protected final Random rand;
	protected final Research nr;

	public ResearchPointsMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        this.amount = config.getInteger("a");
        this.basechance = config.getDouble(new String[] {"basechance", "bc"}, 1);
        this.basicmult = config.getDouble(new String[] {"basicmult", "bm"}, 1.2);
        this.advancedmult = config.getDouble(new String[] {"advancedmult", "am"}, 1.5);
        this.basicchance = basechance * basicmult;
        this.advancedchance = basechance * advancedmult;
        this.rand = new Random();
        
        nr = (Research) Bukkit.getPluginManager().getPlugin("NeoResearch");
	}
	
	@Override
    public boolean castAtEntity(SkillMetadata data, AbstractEntity target) {
		if (target.getBukkitEntity() instanceof Player && data.getCaster().getEntity() instanceof ActiveMob) {
			double rand = this.rand.nextDouble();
			double chance = this.basechance;
			ActiveMob amob = (ActiveMob) data.getCaster().getEntity();
			String mob = amob.getType().getInternalName();
			
			// Check if player is holding a drop charm
			Player p = (Player) target.getBukkitEntity();
			ItemStack[] items = new ItemStack[] { p.getInventory().getItemInMainHand(), p.getInventory().getItemInOffHand()};
			
			int dropType = 0;
			for (ItemStack item : items) {
				if (!item.hasItemMeta()) {
					continue;
				}
				if (!item.getItemMeta().hasLore()) {
					continue;
				}
				if (item.getType().equals(Material.PRISMARINE_CRYSTALS)) {
					continue;
				}
				
				// Check for advanced or basic drop charm
				ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
				int count = 0;
				if (lore.size() > 1) {
					for (int i = lore.size() - 2; i >= 0; i--) {
						String line = lore.get(i);
						if (line.contains("Advanced Drop Charm")) {
							dropType = 2;
							chance = this.advancedchance;
							break;
						}
						else if (line.contains("Drop Charm")) {
							dropType = 1;
							chance = this.basicchance;
							break;
						}
						else if (count >= 4) {
							break;
						}
					}
					
					// If found, break
					if (dropType > 0) {
						break;
					}
				}
			}
			
			// Check for successful drop
			if (rand <= chance) {
				nr.giveResearchPoints(p, this.amount, mob);
			}
			return true;
		}
		return false;
    }
}
