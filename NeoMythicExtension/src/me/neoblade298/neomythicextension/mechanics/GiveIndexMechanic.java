package me.neoblade298.neomythicextension.mechanics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.lumine.xikage.mythicmobs.MythicMobs;
import io.lumine.xikage.mythicmobs.adapters.AbstractEntity;
import io.lumine.xikage.mythicmobs.io.MythicLineConfig;
import io.lumine.xikage.mythicmobs.skills.ITargetedEntitySkill;
import io.lumine.xikage.mythicmobs.skills.SkillMechanic;
import io.lumine.xikage.mythicmobs.skills.SkillMetadata;

public class GiveIndexMechanic extends SkillMechanic implements ITargetedEntitySkill {

	protected final ItemStack item;
	protected final double basechance;
	protected final double basicmult;
	protected final double advancedmult;
	protected final double basicchance;
	protected final double advancedchance;
	protected final boolean announce;
	protected final String type;
	protected final Random rand;

	public GiveIndexMechanic(MythicLineConfig config) {
		super(config.getLine(), config);
        this.setAsyncSafe(false);
        this.setTargetsCreativePlayers(false);
        
        String itemString = config.getString("i", "mi_sewerzombie");
        this.item = MythicMobs.inst().getItemManager().getItemStack(itemString);
        this.basechance = config.getDouble(new String[] {"basechance", "bc"}, 1);
        this.basicmult = config.getDouble(new String[] {"basicmult", "bm"}, 1.2);
        this.advancedmult = config.getDouble(new String[] {"advancedmult", "am"}, 1.5);
        this.announce = config.getString("announce", "false").equalsIgnoreCase("true");
        this.type = config.getString("type", "other");
        this.basicchance = basechance * basicmult;
        this.advancedchance = basechance * advancedmult;
        this.rand = new Random();
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
			for (ItemStack item : items) {
				if (!item.hasItemMeta()) {
					continue;
				}
				if (!item.getItemMeta().hasLore()) {
					continue;
				}
				
				// Check for advanced or basic drop charm
				ArrayList<String> lore = (ArrayList<String>) item.getItemMeta().getLore();
				int count = 0;
				if (lore.size() > 1) {
					for (int i = lore.size() - 2; i >= 0; i--) {
						String line = lore.get(i);
						System.out.println("line: " + line);
						if (line.contains("Advanced Drop Charm")) {
							System.out.println("advanced");
							dropType = 2;
							chance = this.advancedchance;
							break;
						}
						else if (line.contains("Drop Charm")) {
							System.out.println("basic");
							dropType = 1;
							chance = this.basicchance;
							break;
						}
						else if (count >= 3) {
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
				if ((this.type.equals("shiny") && p.hasPermission("filters.indexes.shiny")) || (this.type.equals("index") && p.hasPermission("filters.indexes.regular"))
						|| (!this.type.equals("shiny") && !this.type.equals("index"))) {
					HashMap<Integer, ItemStack> failed = p.getInventory().addItem(this.item);
					if (!failed.isEmpty()) p.getWorld().dropItem(p.getLocation(), this.item);
					
					// Message
					String localMsg = "§4[§c§lMLMC§4] §7You found a Monster Index";
					if (dropType == 1 && rand >= this.basechance) localMsg += " via Basic Drop Charm";
					if (dropType == 2 && rand >= this.basechance) localMsg += " via Advanced Drop Charm";
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
