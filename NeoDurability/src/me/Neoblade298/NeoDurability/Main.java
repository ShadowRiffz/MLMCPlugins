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
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.player.PlayerItemDamageEvent;

public class Main
  extends JavaPlugin
  implements Listener
{
  public static final String DURABILITYSTRING = ChatColor.GRAY + "Durability ";
  public Random gen = new Random();
  
  public void onEnable()
  {
    Bukkit.getServer().getLogger().info("NeoDurability Enabled");
    getServer().getPluginManager().registerEvents(this, this);
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoDurability Disabled");
  }
  
  @SuppressWarnings("deprecation")
	@EventHandler
  public void onDamageMelee(EntityDamageByEntityEvent e)
  {
    Entity cause = e.getDamager();
    Entity target = e.getEntity();
    if(target.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
    	return;
    }
    
    // Damage simulator to spare armor durability
    if ((e.getDamage() > 20.0D) &&
    		(e.getCause() != EntityDamageEvent.DamageCause.MAGIC) &&
    		(e.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) &&
    		(e.getEntity() instanceof Player) &&
    		(!(e.getDamager() instanceof Player)) &&
    		(!e.isCancelled()))
    {
      Player player = (Player)e.getEntity();
      

      
      double oldDamage = e.getDamage();
      e.setCancelled(true);
      player.damage(oldDamage);
      EntityDamageEvent newDamage = new EntityDamageByEntityEvent(e.getDamager(), e.getEntity(), EntityDamageEvent.DamageCause.MAGIC, oldDamage);
      Bukkit.getPluginManager().callEvent(newDamage);
    }
    
    // Lowers durability of damager
    if (((cause instanceof Player)) && (e.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) && 
        (!e.isCancelled()) &&
        (!FlagManager.hasFlag((LivingEntity)cause, "WeaponDur")))
    {
      Player player = (Player)cause;
      ItemStack main = player.getEquipment().getItemInMainHand();
      ItemStack off = player.getEquipment().getItemInOffHand();
      double random = gen.nextDouble();
      if(random <= 0.5) {
	      if (!main.equals(new ItemStack(Material.AIR)) && main.hasItemMeta() && main.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(main, player, 0);
	      } else if (!off.equals(new ItemStack(Material.AIR)) && off.hasItemMeta() && off.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(off, player, 5);
	      }
      }
      else {
	      if (!off.equals(new ItemStack(Material.AIR)) && off.hasItemMeta() && off.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(off, player, 0);
	      } else if (!main.equals(new ItemStack(Material.AIR)) && main.hasItemMeta() && main.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(main, player, 5);
	      }
      }
    }
    
    // Lowers durability of damagee
    if (((target instanceof Player)) && (!FlagManager.hasFlag((LivingEntity)target, "ArmorDur")) &&
    	(e.getDamage() > 1 || e.getCause() != DamageCause.CUSTOM))
    {
      Player player = (Player)target;
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
      	if(player.isBlocking()) {
      		reduceDurability(player.getInventory().getItemInMainHand(), player, 0);
      	}
      }
      if (player.getEquipment().getItemInOffHand().getType() == Material.SHIELD) {
      	if(player.isBlocking()) {
      		reduceDurability(player.getInventory().getItemInOffHand(), player, 5);
      	}
      }
      FlagManager.addFlag(player, "ArmorDur", 40);
    }
  }
  
  @EventHandler
  public void onShoot(EntityShootBowEvent e)
  {
    if ((e.getEntity() instanceof Player) &&
      (!FlagManager.hasFlag(e.getEntity(), "WeaponDur")))
    {
      Player player = (Player)e.getEntity();
      if(player.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
      	return;
      }
      
      ItemStack main = player.getEquipment().getItemInMainHand();
      ItemStack off = player.getEquipment().getItemInOffHand();
      double random = gen.nextDouble();
      if(random <= 0.5) {
	      if (!main.equals(new ItemStack(Material.AIR)) && main.hasItemMeta() && main.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(main, player, 0);
	      } else if (!off.equals(new ItemStack(Material.AIR)) && off.hasItemMeta() && off.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(off, player, 5);
	      }
      }
      else {
	      if (!off.equals(new ItemStack(Material.AIR)) && off.hasItemMeta() && off.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(off, player, 0);
	      } else if (!main.equals(new ItemStack(Material.AIR)) && main.hasItemMeta() && main.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(main, player, 5);
	      }
      }
    }
  }
  
  @EventHandler
  public void onSkillCast(PlayerCastSkillEvent e)
  {
    Player player = e.getPlayer();
    if(player.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
    	return;
    }
    
    ItemStack main = player.getEquipment().getItemInMainHand();
    ItemStack off = player.getEquipment().getItemInOffHand();
    double random = gen.nextDouble();
    if(random <= 0.5) {
	      if (!main.equals(new ItemStack(Material.AIR)) && main.hasItemMeta() && main.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(main, player, 0);
	      } else if (!off.equals(new ItemStack(Material.AIR)) && off.hasItemMeta() && off.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(off, player, 5);
	      }
    }
    else {
	      if (!off.equals(new ItemStack(Material.AIR)) && off.hasItemMeta() && off.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(off, player, 0);
	      } else if (!main.equals(new ItemStack(Material.AIR)) && main.hasItemMeta() && main.getItemMeta().getLore().get(0).contains("Tier")) {
	        FlagManager.addFlag(player, "WeaponDur", 20);
	        reduceDurability(main, player, 5);
	      }
    }
  }
  
  @EventHandler
  public void onDurabilityLoss(PlayerItemDamageEvent e) {
  	if(e.getPlayer().getWorld().getName().equalsIgnoreCase("Argyll") &&
  			e.getDamage() > 10) {
  		e.setCancelled(true);
  	}
  	else if(e.getPlayer().getWorld().getName().equalsIgnoreCase("ClassPvp")) {
  		e.setCancelled(true);
  	}
  }
  
  @EventHandler
  public void onSkillDamaged(SkillDamageEvent e)
  {
    if ((e.getTarget() instanceof Player) && (!FlagManager.hasFlag((LivingEntity)e.getTarget(), "ArmorDur")))
    {
      Player player = (Player)e.getTarget();
      if(player.getWorld().getName().equalsIgnoreCase("ClassPvP")) {
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
      	if(player.isBlocking()) {
      		reduceDurability(player.getInventory().getItemInMainHand(), player, 0);
      	}
      }
      if (player.getEquipment().getItemInOffHand().getType() == Material.SHIELD) {
      	if(player.isBlocking()) {
      		reduceDurability(player.getInventory().getItemInOffHand(), player, 5);
      	}
      }
      FlagManager.addFlag(player, "ArmorDur", 20);
    }
  }
  
  @SuppressWarnings("deprecation")
	public void reduceDurability(ItemStack item, Player player, int i)
  {
    if ((item != null) && 
        (item.hasItemMeta()) && 
        (item.getItemMeta().hasEnchant(Enchantment.DURABILITY)))
    {
      Random rand = new Random();
    	double ench = item.getItemMeta().getEnchantLevel(Enchantment.DURABILITY);
    	double chance = rand.nextDouble();
    	if(chance - (ench * 0.05) <= 0)
    	{
    		return;
    	}
    }
    if ((item != null) && 
      (item.hasItemMeta()) && 
      (item.getItemMeta().hasLore()))
    {
      ItemMeta im = item.getItemMeta();
      List<String> newLore = new ArrayList<String>();
      for (String lore : item.getItemMeta().getLore())
      {
        if ((lore.contains(DURABILITYSTRING)) && 
          (lore.contains("/")))
        {
          String end = lore.substring(lore.indexOf(" ") + 1);
          String[] numbers = end.split("/");
          double d = Integer.parseInt(numbers[0].trim());
          double dM = Integer.parseInt(numbers[1].trim());
          
          d -= 1.0D;
          if (d <= 0.0D)
          {
            if (i == 1)
            {
              player.getInventory().setBoots(null);
              player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
            }
            if (i == 2)
            {
              player.getInventory().setLeggings(null);
              player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
            }
            if (i == 3)
            {
              player.getInventory().setChestplate(null);
              player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
            }
            if (i == 4) {
            	player.getInventory().setHelmet(null);
            	player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
            }
            if (i == 0) {
            	player.getInventory().setItemInMainHand(null);
            	player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
            }
            if (i == 5) {
            	player.getInventory().setItemInOffHand(null);
            	player.getWorld().playSound(player.getEyeLocation(), "entity.item.break", 1.0F, 1.0F);
            }
          }
          lore = DURABILITYSTRING + (int)d + " / " + (int)dM;
          if ((!im.spigot().isUnbreakable()) && (item.getType().getMaxDurability() > 0))
          {
            double p = 1.0D - d / dM;
            
            double dN = p * (item.getType().getMaxDurability() - 1);
            item.setDurability((short)(int)dN);
          }
        }
        newLore.add(lore);
      }
      im.setLore(newLore);
      item.setItemMeta(im);
    }
  }
}
