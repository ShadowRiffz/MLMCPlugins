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

public class DropChanceMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final ItemStack item;
	protected final double basechance;
	protected final double basicmult;
	protected final double advancedmult;
	protected final double basicchance;
	protected final double advancedchance;
	protected final boolean announce;
	protected final String msg;
	protected final String type;
	protected final Random rand;

	public DropChanceMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        String itemString = config.getString("i", "mi_sewerzombie");
        this.item = MythicMobs.inst().getItemManager().getItemStack(itemString);
        this.basechance = config.getDouble(new String[] {"basechance", "bc"}, 1);
        this.basicmult = config.getDouble(new String[] {"basicmult", "bm"}, 1.2);
        this.msg = new String("&4[&c&lMLMC&4] &7" + config.getString("msg", "&7You found a Monster Index")).replaceAll("&", "§");
        this.advancedmult = config.getDouble(new String[] {"advancedmult", "am"}, 1.5);
        this.announce = config.getString("announce", "false").equalsIgnoreCase("true");
        this.type = config.getString("type", "other");
        this.basicchance = basechance * basicmult;
        this.advancedchance = basechance * advancedmult;
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
			
			// Check if player is holding a drop charm
			Player p = (Player) target.getBukkitEntity();
			ItemStack[] items = new ItemStack[] { p.getInventory().getItemInMainHand(), p.getInventory().getItemInOffHand()};
			int dropType = 0;

			if (this.type.equals("chest") && p.hasPermission("tokens.active.boss")) {
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
			}
			
			// Check for successful drop
			if (rand <= chance) {
				if ((this.type.equals("shiny") && p.hasPermission("filters.indexes.shiny")) || (this.type.equals("index") && p.hasPermission("filters.indexes.regular"))
						|| (!this.type.equals("shiny") && !this.type.equals("index"))) {
					HashMap<Integer, ItemStack> failed = p.getInventory().addItem(this.item);
					if (!failed.isEmpty()) p.getWorld().dropItem(p.getLocation(), this.item);
					
					// Message
					String localMsg = msg;
					if (dropType == 1 && rand >= this.basechance) localMsg += " via Basic Drop Charm";
					if (dropType == 2 && rand >= this.basechance) localMsg += " via Advanced Drop Charm";
					if (dropType == 3) localMsg += " via Boss Chest Token";
					localMsg += "!";
					p.sendMessage(localMsg);
					if (this.announce) {
						String name = this.item.getItemMeta().getDisplayName().replaceAll("§", "&");
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "sync console all neoshinies " + p.getName() + " has found " + name);
					}
				}
			}
			return true;
		}
		return false;
    }
}
