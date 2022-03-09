package me.Neoblade298.NeoConsumables.objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.event.SkillBuffEvent;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.util.Buff;
import com.sucy.skill.api.util.BuffManager;
import com.sucy.skill.api.util.BuffType;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.listener.AttributeListener;
import com.sucy.skill.listener.MechanicListener;

import de.tr7zw.nbtapi.NBTItem;
import me.Neoblade298.NeoConsumables.Consumables;
import me.Neoblade298.NeoConsumables.SkullCreator;
import me.Neoblade298.NeoConsumables.runnables.HealthRunnable;
import me.Neoblade298.NeoConsumables.runnables.ManaRunnable;

public class FoodConsumable extends Consumable {
	static HashMap<UUID, PlayerCooldowns> cds;
	static HashMap<UUID, DurationEffects> effects;
	
	Material mat;
	ArrayList<PotionEffect> potions;
	ArrayList<FlagAction> flags;
	ArrayList<BuffAction> buffs;
	StoredAttributes attributes;
	int attributeTime;
	ArrayList<String> commands, worlds, lore;
	double saturation;
	int hunger;
	double speed;
	int speedTime;
	int health, healthTime, healthDelay;
	int mana, manaTime, manaDelay;
	long cooldown;
	String display, base64;
	boolean isDuration;
	
	public FoodConsumable(Consumables main, String key) {
		super(main, key);
		
		potions = new ArrayList<PotionEffect>();
		attributes = new StoredAttributes();
		commands = new ArrayList<String>();
		worlds = new ArrayList<String>();
		lore = new ArrayList<String>();
		flags = new ArrayList<FlagAction>();
		buffs = new ArrayList<BuffAction>();
		display = null;
		base64 = null;
		
		cooldown = 15000; // Default 15 seconds
		isDuration = false; // Default instant
	}

	public boolean canUse(Player p, ItemStack item) {
		// Check world compatible
		if (!worlds.contains(p.getWorld().getName())) {
			String message = "§4[§c§lMLMC§4] §cYou cannot use this consumable in this world";
			p.sendMessage(message);
			return false;
		}

		// Check cds
		UUID uuid = p.getUniqueId();
		if (!cds.containsKey(uuid)) {
			cds.put(uuid, new PlayerCooldowns());
			return true;
		}
		
		if (!isDuration) {
			long remaining = cds.get(uuid).getInstantCooldown() - System.currentTimeMillis();
			if (remaining <= 0) {
				return true;
			}
			remaining /= 1000L;
			String message = "§4[§c§lMLMC§4] &cInstant consumables cooldown: §e" + remaining + "s§7.";
			p.sendMessage(message);
			return false;
		}
		else {
			long remaining = cds.get(uuid).getDurationCooldown() - System.currentTimeMillis();
			if (remaining <= 0) {
				return true;
			}
			remaining /= 1000L;
			String message = "§4[§c§lMLMC§4] &cDuration consumables cooldown: §e" + remaining + "s§7.";
			p.sendMessage(message);
			return false;
		}
	}

	public void use(final Player p, ItemStack item) {
		// First get rid of any existing effects
		UUID uuid = p.getUniqueId();
		if (effects.containsKey(uuid) && effects.get(uuid).getEndTime() > System.currentTimeMillis()) {
			effects.get(uuid).endEffects();
		}
		
		// Sounds, commands
		p.sendMessage("§4[§c§lMLMC§4] §7You ate " + item.getItemMeta().getDisplayName() + "§7!");
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}
		executeCommands(p);
		
		// Food event only happens if hunger is changing
		int foodLevel = Math.min(20, p.getFoodLevel() + hunger);
		if (hunger > 0) {
			FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, foodLevel);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				p.setFoodLevel(foodLevel);
				p.setSaturation((float) Math.min(p.getFoodLevel(), this.saturation + p.getSaturation()));
			}
		}

		// Set cooldown
		long nextEat = System.currentTimeMillis() + this.cooldown;
		long ticks = this.cooldown / 50; // Milliseconds to ticks
		if (!isDuration) {
			cds.get(uuid).setInstantCooldown(nextEat);
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				public void run() {
					if (p != null) {
						String message = "§4[§c§lMLMC§4] Instant consumables can be eaten again.";
						p.sendMessage(message);
					}
				}
			}, ticks);
		}
		else {
			cds.get(uuid).setDurationCooldown(nextEat);
		}

		// Potion effects
		for (PotionEffect effect : this.getEffect()) {
			p.addPotionEffect(effect);
		}

		// SkillAPI Attributes
		if (!attributes.isEmpty()) {
			// If food already consumed before, remove the attributes, cancel the remove task, then add new
			attributes.applyAttributes(p);
		}
		
		// Flags
		for (FlagAction flag : flags) {
			if (flag.isAdd()) {
				FlagManager.addFlag(p, p, flag.getFlag(), flag.getDuration());
			}
			else {
				FlagManager.removeFlag(p, flag.getFlag());
			}
		}
		
		// Buffs
		for (BuffAction buff : buffs) {
	        BuffType buffType = buff.getType();
	        double seconds = buff.getDuration();
	        String category = buff.getCategory();
	        int buffticks = (int) (seconds * 20);
            SkillBuffEvent event = new SkillBuffEvent(p, p, buff.getValue(), buffticks, buffType, buff.isPercent());
            Bukkit.getPluginManager().callEvent(event);
            
            if (!event.isCancelled()) {
                BuffManager.getBuffData(p).addBuff(
                        buffType,
                        category,
                        new Buff(key + "-" + p, event.getAmount(), buff.isPercent()),
                        event.getTicks());
            }
		}
		
		// Speed
		if (speedTime > 0) {
            AttributeListener.refreshSpeed(p);
            FlagManager.addFlag(p, p, MechanicListener.SPEED_KEY, speedTime);
            p.setWalkSpeed((float) (speed * p.getWalkSpeed()));
		}

		// Health and mana regen
		PlayerData data = SkillAPI.getPlayerData(p);

		if (healthTime == 0 && !p.isDead()) {
				p.setHealth(Math.min(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), p.getHealth() + health));
		}
		else if (healthTime > 0 && !p.isDead()) {
			new HealthRunnable(p, health, healthTime).runTaskTimer(main, 0, healthDelay);
		}
		
		if (data.getMainClass().getData().getManaName().contains("MP")) {
			if (manaTime == 0 && !p.isDead()) {
				data.setMana(Math.min(data.getMaxMana(), data.getMana() + mana));
			}
			else if (manaTime > 0 && !p.isDead()) {
				new ManaRunnable(p, mana, healthTime).runTaskTimer(main, 0, manaDelay);
			}
		}
		
		item.setAmount(item.getAmount() - 1);
	}

	public void executeCommands(Player player) {
		for (String cmd : this.commands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%p", player.getName()));
		}
	}
	
	public ItemStack getItem(int amount) {
		ItemStack item;
		if (base64 != null) {
			item = SkullCreator.itemFromBase64(base64);
		}
		else {
			item = new ItemStack(mat);
		}
		item.setAmount(amount);
		
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		meta.setDisplayName(display);
		item.setItemMeta(meta);
		NBTItem nbti = new NBTItem(item);
		nbti.setString("consumable", key);
		return nbti.getItem();
	}
	
	public void setDisplay(String display) {
		this.display = display;
	}
	
	public void setMaterial(String mat) {
		this.mat = Material.getMaterial(mat.toUpperCase());
	}
	
	public void setBase64(String base64) {
		this.base64 = base64;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public void setHealthTime(int healthTime) {
		this.healthTime = healthTime;
	}

	public void setManaTime(int manaTime) {
		this.manaTime = manaTime;
	}

	public void setHealthDelay(int healthDelay) {
		this.healthDelay = healthDelay;
	}

	public void setManaDelay(int manaDelay) {
		this.manaDelay = manaDelay;
	}
	
	public void setWorlds(List<String> worlds) {
		this.worlds = (ArrayList<String>) worlds;
	}
	
	public void setCommands(List<String> commands) {
		this.commands = (ArrayList<String>) commands;
	}

	public ArrayList<PotionEffect> getEffect() {
		return this.potions;
	}

	public StoredAttributes getAttributes() {
		return this.attributes;
	}

	public ArrayList<PotionEffect> getPotions() {
		return potions;
	}

	public int getAttributeTime() {
		return attributeTime;
	}

	public void setAttributeTime(int attributeTime) {
		this.attributeTime = attributeTime;
	}

	public ArrayList<String> getCommands() {
		return commands;
	}
	
	public ArrayList<FlagAction> getFlags() {
		return flags;
	}
	
	public ArrayList<BuffAction> getBuffs() {
		return buffs;
	}

	public void setSaturation(double saturation) {
		this.saturation = saturation;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}
	
	public void setCooldown(long cooldown) {
		// Seconds to milliseconds
		this.cooldown = cooldown * 1000;
	}
	
	public void setIsDuration(boolean isDuration) {
		this.isDuration = isDuration;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
	
	public void setSpeedTime(int time) {
		// Seconds to ticks
		this.speedTime = time * 20;
	}
}
