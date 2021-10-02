package me.Neoblade298.NeoDurability;

import com.sucy.skill.api.event.PlayerCastSkillEvent;
import com.sucy.skill.api.event.SkillDamageEvent;
import com.sucy.skill.api.util.FlagManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class Main extends JavaPlugin implements Listener {
	public static final String DURABILITYSTRING = ChatColor.GRAY + "Durability ";
	public Random gen = new Random();

	public void onEnable() {
		Bukkit.getServer().getLogger().info("NeoDurability Enabled");
		getServer().getPluginManager().registerEvents(this, this);

	}

	public void onDisable() {
		Bukkit.getServer().getLogger().info("NeoDurability Disabled");
	}

	@EventHandler
	public void onDamageMelee(EntityDamageByEntityEvent e) {
		Entity cause = e.getDamager();
		Entity target = e.getEntity();
		if (target.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
			return;
		}

		// Lowers durability of damager
		if (((cause instanceof Player)) && (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
				&& (!e.isCancelled()) && (!FlagManager.hasFlag((LivingEntity) cause, "WeaponDur"))) {
			Player player = (Player) cause;

			ItemStack main = player.getEquipment().getItemInMainHand();
			ItemStack off = player.getEquipment().getItemInOffHand();
			double random = gen.nextDouble();
			if (random <= 0.5) {
				if (main != null && main.hasItemMeta() && main.getItemMeta().hasLore()) {
					FlagManager.addFlag(player, "WeaponDur", 20);
					reduceDurability(main, player, 0);
				}
				else if (off != null && off.hasItemMeta() && off.getItemMeta().hasLore()) {
					FlagManager.addFlag(player, "WeaponDur", 20);
					reduceDurability(off, player, 5);
				}
			}
			else {
				if (off != null && off.hasItemMeta() && off.getItemMeta().hasLore()) {
					FlagManager.addFlag(player, "WeaponDur", 20);
					reduceDurability(off, player, 5);
				}
				else if (main != null && main.hasItemMeta() && main.getItemMeta().hasLore()) {
					FlagManager.addFlag(player, "WeaponDur", 20);
					reduceDurability(main, player, 0);
				}
			}
		}

		// Lowers durability of damagee
		if (((target instanceof Player)) && (!FlagManager.hasFlag((LivingEntity) target, "ArmorDur"))
				&& (e.getDamage() > 1 || e.getCause() != DamageCause.CUSTOM)) {
			Player player = (Player) target;
			if (player.getInventory().getBoots() != null) {
				reduceDurability(player.getInventory().getBoots(), player, 1);
			}
			if (player.getInventory().getLeggings() != null) {
				reduceDurability(player.getInventory().getLeggings(), player, 2);
			}
			if (player.getInventory().getChestplate() != null) {
				reduceDurability(player.getInventory().getChestplate(), player, 3);
			}
			if (player.getInventory().getHelmet() != null) {
				reduceDurability(player.getInventory().getHelmet(), player, 4);
			}
			if (player.getEquipment().getItemInMainHand().getType() == Material.SHIELD) {
				if (player.isBlocking()) {
					reduceDurability(player.getInventory().getItemInMainHand(), player, 0);
				}
			}
			if (player.getEquipment().getItemInOffHand().getType() == Material.SHIELD) {
				if (player.isBlocking()) {
					reduceDurability(player.getInventory().getItemInOffHand(), player, 5);
				}
			}
			FlagManager.addFlag(player, "ArmorDur", 40);
		}
	}

	@EventHandler
	public void onShoot(EntityShootBowEvent e) {
		if ((e.getEntity() instanceof Player) && (!FlagManager.hasFlag(e.getEntity(), "WeaponDur"))) {
			Player player = (Player) e.getEntity();
			if (player.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
				return;
			}

			ItemStack main = player.getEquipment().getItemInMainHand();
			ItemStack off = player.getEquipment().getItemInOffHand();
			double random = gen.nextDouble();
			if (random <= 0.5) {
				if (main != null && main.hasItemMeta() && main.getItemMeta().hasLore()) {
					FlagManager.addFlag(player, "WeaponDur", 20);
					reduceDurability(main, player, 0);
				}
				else if (off != null && off.hasItemMeta() && off.getItemMeta().hasLore()) {
					FlagManager.addFlag(player, "WeaponDur", 20);
					reduceDurability(off, player, 5);
				}
			}
			else {
				if (off != null && off.hasItemMeta() && off.getItemMeta().hasLore()) {
					FlagManager.addFlag(player, "WeaponDur", 20);
					reduceDurability(off, player, 5);
				}
				else if (main != null && main.hasItemMeta() && main.getItemMeta().hasLore()) {
					FlagManager.addFlag(player, "WeaponDur", 20);
					reduceDurability(main, player, 0);
				}
			}
		}
	}

	@EventHandler
	public void onSkillCast(PlayerCastSkillEvent e) {
		Player player = e.getPlayer();
		if (e.isCancelled()) { return; }
		if (player.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
			return;
		}

		ItemStack main = player.getEquipment().getItemInMainHand();
		ItemStack off = player.getEquipment().getItemInOffHand();
		double random = gen.nextDouble();
		if (random <= 0.5) {
			if (main != null && main.hasItemMeta() && main.getItemMeta().hasLore()) {
				FlagManager.addFlag(player, "WeaponDur", 20);
				reduceDurability(main, player, 0);
			}
			else if (off != null && off.hasItemMeta() && off.getItemMeta().hasLore()) {
				FlagManager.addFlag(player, "WeaponDur", 20);
				reduceDurability(off, player, 5);
			}
		}
		else {
			if (off != null && off.hasItemMeta() && off.getItemMeta().hasLore()) {
				FlagManager.addFlag(player, "WeaponDur", 20);
				reduceDurability(off, player, 5);
			}
			else if (main != null && main.hasItemMeta() && main.getItemMeta().hasLore()) {
				FlagManager.addFlag(player, "WeaponDur", 20);
				reduceDurability(main, player, 0);
			}
		}
	}

	@EventHandler
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

	@EventHandler
	public void onSkillDamaged(SkillDamageEvent e) {
		if ((e.getTarget() instanceof Player) && (!FlagManager.hasFlag((LivingEntity) e.getTarget(), "ArmorDur"))) {
			Player player = (Player) e.getTarget();
			if (player.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
				return;
			}
			if (player.getInventory().getBoots() != null) {
				reduceDurability(player.getInventory().getBoots(), player, 1);
			}
			if (player.getInventory().getLeggings() != null) {
				reduceDurability(player.getInventory().getLeggings(), player, 2);
			}
			if (player.getInventory().getChestplate() != null) {
				reduceDurability(player.getInventory().getChestplate(), player, 3);
			}
			if (player.getInventory().getHelmet() != null) {
				reduceDurability(player.getInventory().getHelmet(), player, 4);
			}
			if (player.getEquipment().getItemInMainHand().getType() == Material.SHIELD) {
				if (player.isBlocking()) {
					reduceDurability(player.getInventory().getItemInMainHand(), player, 0);
				}
			}
			if (player.getEquipment().getItemInOffHand().getType() == Material.SHIELD) {
				if (player.isBlocking()) {
					reduceDurability(player.getInventory().getItemInOffHand(), player, 5);
				}
			}
			FlagManager.addFlag(player, "ArmorDur", 20);
		}
	}
	
	public boolean isQuestItem(ItemStack item) {
		if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
			for (String line : item.getItemMeta().getLore()) {
				if (line.contains("Tier")) {
					return true;
				}
			}
		}
		return false;
	}

	public void reduceDurability(ItemStack item, Player player, int i) {
		if (!isQuestItem(item)) return;
		BukkitRunnable removeCrossbowMain = null;
		BukkitRunnable removeCrossbowOff = null;
		if ((item != null) && (item.hasItemMeta()) && (item.getItemMeta().hasEnchant(Enchantment.DURABILITY))) {
			Random rand = new Random();
			double ench = item.getItemMeta().getEnchantLevel(Enchantment.DURABILITY);
			if (ench >= 6) ench = 6;
			double chance = rand.nextDouble();
			if (chance - (ench * 0.05) <= 0) {
				return;
			}
		}
		if (item.getType().equals(Material.CROSSBOW)) {
			removeCrossbowMain = new BukkitRunnable() {
				public void run() {
					Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(player, item));
					player.getInventory().setItemInMainHand(null);
					player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
				}
			};
			removeCrossbowOff = new BukkitRunnable() {
				public void run() {
					Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(player, item));
					player.getInventory().setItemInOffHand(null);
					player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
				}
			};
		}
		if ((item != null) && (item.hasItemMeta()) && (item.getItemMeta().hasLore())) {
			ItemMeta im = item.getItemMeta();
			List<String> newLore = new ArrayList<String>();
			for (String lore : item.getItemMeta().getLore()) {
				if ((lore.contains(DURABILITYSTRING)) && (lore.contains("/"))) {
					String end = lore.substring(lore.indexOf(" ") + 1);
					String[] numbers = end.split("/");
					double d = Integer.parseInt(numbers[0].trim());
					double dM = Integer.parseInt(numbers[1].trim());
					
					if (d == 25 && player.hasPermission("donator.warndurability")) {
						player.sendMessage("§4[§c§lMLMC§4] §4WARNING: Your item, " + item.getItemMeta().getDisplayName() + "§4, is at 25 durability!");
					}

					d -= 1;
					if (d <= 0) {
						if (i == 1) {
							Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(player, item));
							player.getInventory().setBoots(null);
							player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
							return;
						}
						if (i == 2) {
							Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(player, item));
							player.getInventory().setLeggings(null);
							player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
							return;
						}
						if (i == 3) {
							Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(player, item));
							player.getInventory().setChestplate(null);
							player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
							return;
						}
						if (i == 4) {
							Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(player, item));
							player.getInventory().setHelmet(null);
							player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
							return;
						}
						if (i == 0) {
							if (item.getType().equals(Material.CROSSBOW)) {
								removeCrossbowMain.runTaskLater(this, 1L);
							}
							else {
								Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(player, item));
								player.getInventory().setItemInMainHand(null);
								player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
							}
							return;
						}
						if (i == 5) {
							if (item.getType().equals(Material.CROSSBOW)) {
								removeCrossbowOff.runTaskLater(this, 1L);
							}
							else {
								Bukkit.getServer().getPluginManager().callEvent(new PlayerItemBreakEvent(player, item));
								player.getInventory().setItemInOffHand(null);
								player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
							}
							return;
						}
					}
					lore = DURABILITYSTRING + (int) d + " / " + (int) dM;
					if ((!im.isUnbreakable()) && (item.getType().getMaxDurability() > 0)) {
						double p = 1.0D - d / dM;

						double dN = p * (item.getType().getMaxDurability() - 1);
						((Damageable) im).setDamage((int) dN);
					}
				}
				newLore.add(lore);
			}
			im.setLore(newLore);
			item.setItemMeta(im);
		}
	}
}
