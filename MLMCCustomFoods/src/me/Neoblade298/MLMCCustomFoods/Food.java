package me.Neoblade298.MLMCCustomFoods;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Food
{
  ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
  ArrayList<AttributeEffect> attributes = new ArrayList<AttributeEffect>();
  ArrayList<Sound> sounds = new ArrayList<Sound>();
  ArrayList<String> commands = new ArrayList<String>();
  ArrayList<String> worlds = new ArrayList<String>();
  HashMap<UUID, Long> lastEaten = new HashMap<UUID, Long>();
  String name;
  ArrayList<String> lore;
  double saturation;
  int hunger;
  int health;
  int mana;
  int healthTime;
  int manaTime;
  int healthDelay;
  int manaDelay;
  int cooldown = 0;
  boolean quaffable = false;
  
  public void setCommands(List<String> commands)
  {
    this.commands = new ArrayList<String>(commands);
  }
  
  public void executeCommands(Player player)
  {
    for (String cmd : this.commands) {
      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%p", player.getName()));
    }
  }
  
  public void setHealth(int health)
  {
    this.health = health;
  }
  
  public void setMana(int mana)
  {
    this.mana = mana;
  }
  
  public void setCooldown(int cooldown)
  {
    this.cooldown = cooldown;
  }
  
  public void setEatable(boolean eatable)
  {
    this.quaffable = eatable;
  }
  
  public boolean quaffable()
  {
    return this.quaffable;
  }
  
  public void setHealthTime(int healthTime)
  {
    this.healthTime = healthTime;
  }
  
  public void setManaTime(int manaTime)
  {
    this.manaTime = manaTime;
  }
  
  public void setHealthDelay(int healthDelay)
  {
    this.healthDelay = healthDelay;
  }
  
  public void setManaDelay(int manaDelay)
  {
    this.manaDelay = manaDelay;
  }
  
  public void setWorlds(List<String> worlds)
  {
    this.worlds = new ArrayList<String>(worlds);
  }
  
  public int getHealth()
  {
    return this.health;
  }
  
  public int getMana()
  {
    return this.mana;
  }
  
  public int getHealthTime()
  {
    return this.healthTime;
  }
  
  public int getManaTime()
  {
    return this.manaTime;
  }
  
  public int getHealthDelay()
  {
    return this.healthDelay;
  }
  
  public int getManaDelay()
  {
    return this.manaDelay;
  }
  
  public List<String> getWorlds()
  {
    return this.worlds;
  }
  
  public Food(String name, int hunger, double saturation, String... lore)
  {
    this.name = name;
    this.hunger = hunger;
    this.saturation = saturation;
    this.lore = new ArrayList<String>(Arrays.asList(lore));
  }
  
  public Food(String name, int hunger, double saturation, ArrayList<String> lore, ArrayList<PotionEffect> effects, ArrayList<AttributeEffect> attributes, ArrayList<Sound> sounds)
  {
    this.name = name;
    this.lore = lore;
    this.hunger = hunger;
    this.saturation = saturation;
    this.effects = effects;
    this.attributes = attributes;
    this.sounds = sounds;
  }
  
  public void addEffect(PotionEffect effect)
  {
    this.effects.add(effect);
  }
  
  public void addAttribute(AttributeEffect effect)
  {
    this.attributes.add(effect);
  }
  
  public ArrayList<String> getLore()
  {
    return this.lore;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public ArrayList<PotionEffect> getEffect()
  {
    return this.effects;
  }
  
  public ArrayList<AttributeEffect> getAttributes()
  {
    return this.attributes;
  }
  
  public int getHunger()
  {
    return this.hunger;
  }
  
  public double getSaturation()
  {
    return this.saturation;
  }
  
  public ArrayList<Sound> getSounds()
  {
    return this.sounds;
  }
  
  public boolean canEat(Player player)
  {
    if (this.lastEaten.containsKey(player.getUniqueId())) {
      return System.currentTimeMillis() - ((Long)this.lastEaten.get(player.getUniqueId())).longValue() > this.cooldown;
    }
    return true;
  }
  
  public void eat(final Player player)
  {
    this.lastEaten.put(player.getUniqueId(), Long.valueOf(System.currentTimeMillis()));
    if (this.cooldown > 0) {
      Bukkit.getScheduler().scheduleSyncDelayedTask(MLMCCustomFoodsMain.getMain(), new Runnable()
      {
        public void run()
        {
          String message = Food.this.name + "&7 can be eaten again.";
          message = message.replaceAll("&", "§");
          player.sendMessage(message);
        }
      }, this.cooldown / 50);
    }
  }
}
