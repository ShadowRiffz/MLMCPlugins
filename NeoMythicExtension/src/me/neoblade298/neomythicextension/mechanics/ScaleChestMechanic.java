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
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class ScaleChestMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final ItemStack item;
	protected final double basechance;
	protected final double basicmult;
	protected final double advancedmult;
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
        this.basicmult = 1.2;
        this.advancedmult = 1.5;
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

			chance += Math.min(0.25, 0 + (0.0025 * (data.getCaster().getLevel() - 1)));;
			double moddedChance = chance;
			
			// Check if player is holding a drop charm
			Player p = (Player) target.getBukkitEntity();
			ItemStack[] items = new ItemStack[] { p.getInventory().getItemInMainHand()};
			int dropType = 0;

			if (p.hasPermission("tokens.active.boss")) {
				dropType = 3;
				chance = 1;
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + p.getName() + " permission unset tokens.active.boss");
			}
			else {
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
					if (lore.size() <= 1) {
						continue;
					}
					
					for (int i = lore.size() - 2; i >= 0; i--) {
						String line = lore.get(i);
						if (line.contains("Advanced Drop Charm")) {
							dropType = 2;
							moddedChance = chance * this.basicmult;
							break;
						}
						else if (line.contains("Drop Charm")) {
							dropType = 1;
							moddedChance = chance * this.advancedmult;
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
			if (rand <= moddedChance) {
				HashMap<Integer, ItemStack> failed = p.getInventory().addItem(this.item);
				if (!failed.isEmpty()) p.getWorld().dropItem(p.getLocation(), this.item);
				
				// Message
				String localMsg = msg;
				if (dropType == 1 && rand >= chance) localMsg += " via Basic Drop Charm";
				if (dropType == 2 && rand >= chance) localMsg += " via Advanced Drop Charm";
				if (dropType == 3) localMsg += " via Boss Chest Token";
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
