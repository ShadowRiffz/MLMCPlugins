package me.neoblade298.neoinstruments;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public class Main
  extends JavaPlugin
  implements Listener
{
  List<Player> playing = new ArrayList<Player>();
  List<Player> upperRegister = new ArrayList<Player>();
  
  public void onEnable()
  {
    super.onEnable();
    Bukkit.getServer().getLogger().info("NeoInstruments Enabled");
    getServer().getPluginManager().registerEvents(this, this);
  }
  
  public void onDisable()
  {
    Bukkit.getServer().getLogger().info("NeoInstruments Disabled");
    super.onDisable();
  }
  
  
  // All the magic happens here
  @EventHandler
  public void rightClickEvent(PlayerInteractEvent e)
  {
    if(e.getHand() == EquipmentSlot.OFF_HAND) {
	  return;
    }
    if ((e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.RIGHT_CLICK_BLOCK))
    {
      if ((e.getItem() != null) && (e.getItem().hasItemMeta()) && (e.getItem().getItemMeta().hasLore()) && (!this.playing.contains(e.getPlayer())))
      {
        List<String> lore = e.getItem().getItemMeta().getLore();
        if (((String)lore.get(0)).contains("Instrument"))
        {
          lore.set(0, ChatColor.stripColor((String)lore.get(0)));
          String[] temp = ((String)lore.get(0)).split(" ");
          e.getPlayer().addScoreboardTag(temp[0]);
          this.playing.add(e.getPlayer());
          e.getPlayer().getInventory().setHeldItemSlot(8);
          e.getPlayer().sendMessage("§4[§c§lMLMC§4] §7You begin playing your instrument! Right click again to stop!");
        }
      }
      else if (this.playing.contains(e.getPlayer()))
      {
        this.playing.remove(e.getPlayer());
        if (this.upperRegister.contains(e.getPlayer())) {
          this.upperRegister.remove(e.getPlayer());
        }
        e.getPlayer().removeScoreboardTag("Ocarina");
        e.getPlayer().removeScoreboardTag("Bell");
        e.getPlayer().removeScoreboardTag("Chime");
        e.getPlayer().removeScoreboardTag("Base");
        e.getPlayer().removeScoreboardTag("Guitar");
        e.getPlayer().removeScoreboardTag("Xylophone");
        e.getPlayer().removeScoreboardTag("Clicks");
        e.getPlayer().removeScoreboardTag("Piano");
        e.getPlayer().removeScoreboardTag("Snare");
        e.getPlayer().removeScoreboardTag("Double");
        e.getPlayer().removeScoreboardTag("Harp");
        e.getPlayer().sendMessage("§4[§c§lMLMC§4] §7You stopped playing your instrument!");
      }
    }
    else if (((e.getAction() == Action.LEFT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK)) && 
      (this.playing.contains(e.getPlayer()))) {
      if (this.upperRegister.contains(e.getPlayer())) {
        this.upperRegister.remove(e.getPlayer());
      } else {
        this.upperRegister.add(e.getPlayer());
      }
    }
  }
  
  @EventHandler
  public void changeSlotEvent(PlayerItemHeldEvent e)
  {
    if (e.getNewSlot() == 8) {
      return;
    }
    if (this.playing.contains(e.getPlayer()))
    {
      e.setCancelled(true);
      e.getPlayer().getWorld().spawnParticle(Particle.NOTE, e.getPlayer().getLocation().add(0, 2, 0), 1, null);
      Set<String> tags = e.getPlayer().getScoreboardTags();
      Sound sound = null;
      if (tags.contains("Piano")) {
        sound = Sound.BLOCK_NOTE_HARP;
      } else if (tags.contains("Ocarina")) {
        sound = Sound.BLOCK_NOTE_FLUTE;
      } else if (tags.contains("Bell")) {
        sound = Sound.BLOCK_NOTE_BELL;
      } else if (tags.contains("Chime")) {
        sound = Sound.BLOCK_NOTE_CHIME;
      } else if (tags.contains("Base")) {
        sound = Sound.BLOCK_NOTE_BASEDRUM;
      } else if (tags.contains("Guitar")) {
        sound = Sound.BLOCK_NOTE_GUITAR;
      } else if (tags.contains("Xylophone")) {
        sound = Sound.BLOCK_NOTE_XYLOPHONE;
      } else if (tags.contains("Clicks")) {
        sound = Sound.BLOCK_NOTE_HAT;
      } else if (tags.contains("Harp")) {
        sound = Sound.BLOCK_NOTE_PLING;
      } else if (tags.contains("Snare")) {
        sound = Sound.BLOCK_NOTE_SNARE;
      } else if (tags.contains("Double")) {
        sound = Sound.BLOCK_NOTE_BASS;
      }
      if (!this.upperRegister.contains(e.getPlayer()))
      {
        if (!e.getPlayer().isSneaking()) {
          switch (e.getNewSlot())
          {
          case 0: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.5F);
            break;
          case 1: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.5612F);
            break;
          case 2: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.6299F);
            break;
          case 3: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.6674F);
            break;
          case 4: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.7492F);
            break;
          case 5: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.8409F);
            break;
          case 6: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.9439F);
            break;
          case 7: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.0F);
          }
        } else {
          switch (e.getNewSlot())
          {
          case 0: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.5297F);
            break;
          case 1: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.5946F);
            break;
          case 2: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.6674F);
            break;
          case 3: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.7071F);
            break;
          case 4: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.7937F);
            break;
          case 5: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 0.8909F);
            break;
          case 6: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.0F);
            break;
          case 7: 
            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.06F);
          }
        }
      }
      else if (!e.getPlayer().isSneaking()) {
        switch (e.getNewSlot())
        {
        case 0: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.0F);
          break;
        case 1: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.1225F);
          break;
        case 2: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.2599F);
          break;
        case 3: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.3348F);
          break;
        case 4: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.4983F);
          break;
        case 5: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.6818F);
          break;
        case 6: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.8877F);
          break;
        case 7: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 2.0F);
        }
      } else {
        switch (e.getNewSlot())
        {
        case 0: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.0595F);
          break;
        case 1: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.1892F);
          break;
        case 2: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.3348F);
          break;
        case 3: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.4142F);
          break;
        case 4: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.5874F);
          break;
        case 5: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 1.7818F);
          break;
        case 6: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 2.0F);
          break;
        case 7: 
          e.getPlayer().getWorld().playSound(e.getPlayer().getLocation(), sound, 3.0F, 2.0F);
        }
      }
    }
  }
}