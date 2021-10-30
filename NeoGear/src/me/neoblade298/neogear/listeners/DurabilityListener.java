package me.neoblade298.neogear.listeners;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import com.sucy.skill.api.event.PlayerCastSkillEvent;
import com.sucy.skill.api.util.FlagManager;

import me.neoblade298.neogear.Gear;

public class DurabilityListener implements Listener {
	Gear main;
	Random gen;
	private final String DURABILITYSTRING = "§7Durability ";
	private final String WEAPONCD = "WeaponDurability";
	private final String ARMORCD = "ArmorDurability";
	private final int CDTIME = 20;
	
	public DurabilityListener(Gear main) {
		this.main = main;
		gen = new Random();
	}

	@EventHandler(ignoreCancelled = true)
	public void onDamageMelee(EntityDamageByEntityEvent e) {
		Entity cause = e.getDamager();
		Entity target = e.getEntity();

		// Lowers durability of damager
		if (cause instanceof Player) {
			reduceWeaponDurability((Player) cause);
		}

		// Lowers durability of damaged
		if (target instanceof Player) {
			reduceArmorDurability((Player) target);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onShoot(EntityShootBowEvent e) {
		if (e.getEntity() instanceof Player) {
			reduceWeaponDurability((Player) e.getEntity());
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onSkillCast(PlayerCastSkillEvent e) {
		reduceWeaponDurability(e.getPlayer());
	}

	@EventHandler(ignoreCancelled = true)
	public void onDurabilityLoss(PlayerItemDamageEvent e) {
		if (e.getItem().hasItemMeta() && e.getItem().getItemMeta().hasLore()) {
			if (!isQuestItem(e.getItem())) return;
			
			String world = e.getPlayer().getWorld().getName();
			if (world.equalsIgnoreCase("Argyll") || world.equalsIgnoreCase("Dev")) {
				e.setCancelled(true);
			}
		}
		if (e.getPlayer().getWorld().getName().equalsIgnoreCase("ClassPvp")) {
			e.setCancelled(true);
		}
	}
	
	private boolean isQuestItem(ItemStack item) {
		if (item != null && item.hasItemMeta() && item.getItemMeta().hasLore()) {
			for (String line : item.getItemMeta().getLore()) {
				if (line.contains("Tier")) {
					return true;
				}
			}
		}
		return false;
	}
	
	private void reduceArmorDurability(Player p) {
		if (FlagManager.hasFlag(p, ARMORCD)) return;
		if (p.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
			return;
		}
		
		FlagManager.addFlag(p, ARMORCD, CDTIME);
		reduceDurability(p, p.getInventory().getHelmet(), 1);
		reduceDurability(p, p.getInventory().getChestplate(), 2);
		reduceDurability(p, p.getInventory().getLeggings(), 3);
		reduceDurability(p, p.getInventory().getBoots(), 4);
		if (p.isBlocking()) {
			if (p.getEquipment().getItemInMainHand().getType() == Material.SHIELD) {
				reduceDurability(p, p.getInventory().getItemInMainHand(), 0);
			}
			if (p.getEquipment().getItemInOffHand().getType() == Material.SHIELD) {
				reduceDurability(p, p.getInventory().getItemInOffHand(), 5);
			}
		}
	}
	
	private void reduceWeaponDurability(Player p) {
		if (FlagManager.hasFlag(p, WEAPONCD)) return;
		if (p.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
			return;
		}
		
		FlagManager.addFlag(p, WEAPONCD, CDTIME);
		if (gen.nextDouble() > 0.5) {
			if (isQuestItem(p.getInventory().getItemInMainHand())) {
				reduceDurability(p, p.getInventory().getItemInMainHand(), 0);
			}
			else {
				reduceDurability(p, p.getInventory().getItemInOffHand(), 5);
			}
		}
		else {
			if (isQuestItem(p.getInventory().getItemInOffHand())) {
				reduceDurability(p, p.getInventory().getItemInOffHand(), 5);
			}
			else {
				reduceDurability(p, p.getInventory().getItemInMainHand(), 0);
			}
		}
	}
	
	private void reduceDurability(Player p, ItemStack item, int slot) {
		if (!isQuestItem(item)) return;

		// Check unbreaking
		ItemMeta im = item.getItemMeta();
		if (im.hasEnchant(Enchantment.DURABILITY)) {
			Random rand = new Random();
			double ench = item.getItemMeta().getEnchantLevel(Enchantment.DURABILITY);
			if (ench >= 6) ench = 6;
			double chance = rand.nextDouble();
			if (chance - (ench * 0.05) <= 0) {
				return;
			}
		}
		
		ArrayList<String> lore = (ArrayList<String>) im.getLore();
		String line = lore.get(lore.size() - 1);
		
		if (line.contains(DURABILITYSTRING)) {
			String end = line.substring(line.indexOf(" ") + 1);
			String[] numbers = end.split("/");
			double d = Integer.parseInt(numbers[0].trim());
			double dM = Integer.parseInt(numbers[1].trim());
			
			if (d == 25 && p.hasPermission("donator.warndurability")) {
				p.sendMessage("§4[§c§lMLMC§4] §4WARNING: Your item, " + item.getItemMeta().getDisplayName() + "§4, is at 25 durability!");
			}

			d -= 1;
			if (d <= 0) {
				breakItem(item, p, slot);
				return;
			}
			
			line = DURABILITYSTRING + (int) d + " / " + (int) dM;
			if ((!im.isUnbreakable()) && (item.getType().getMaxDurability() > 0)) {
				double pct = 1.0D - d / dM;

				double dN = pct * (item.getType().getMaxDurability() - 1);
				((Damageable) im).setDamage((int) dN);
			}
		}
		lore.set(lore.size() - 1, line);
		im.setLore(lore);
		item.setItemMeta(im);
	}
	
	private void breakItem(ItemStack item, Player p, int slot) {
		Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(p, item));
		item.setAmount(0);
		p.getWorld().playSound(p.getLocation(), "entity.item.break", 1.0F, 1.0F);
	}
}
