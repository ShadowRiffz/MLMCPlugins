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
import org.bukkit.scheduler.BukkitTask;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;
import com.sucy.skill.api.util.FlagManager;
import com.sucy.skill.api.util.StatusFlag;

import me.Neoblade298.NeoConsumables.Consumables;
import me.Neoblade298.NeoConsumables.SkullCreator;
import me.Neoblade298.NeoConsumables.runnables.AttributeRunnable;
import me.Neoblade298.NeoConsumables.runnables.AttributeTask;
import me.Neoblade298.NeoConsumables.runnables.HealthRunnable;
import me.Neoblade298.NeoConsumables.runnables.ManaRunnable;

public class FoodConsumable extends Consumable {
	static HashMap<UUID, PlayerCooldowns> cds;
	static HashMap<UUID, DurationEffects> effects;
	
	Material mat;
	ArrayList<PotionEffect> potions;
	StoredAttributes attributes;
	int attributeTime;
	ArrayList<String> commands, worlds, lore;
	double saturation;
	int hunger;
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

		// Check gcd
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
		// Sounds, commands
		p.sendMessage("§4[§c§lMLMC§4] §7You ate " + item.getItemMeta().getDisplayName() + "§7!");
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}
		executeCommands(p);
		
		// Food event only happens if hunger is changing
		UUID uuid = p.getUniqueId();
		int foodLevel = Math.min(20, p.getFoodLevel() + hunger);
		if (hunger > 0) {
			FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, foodLevel);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				p.setFoodLevel(foodLevel);
				p.setSaturation((float) Math.min(p.getFoodLevel(), this.saturation + p.getSaturation()));
			}
		}
		
		// Calculate multipliers and remove flags
		double garnish = 1, preserve = 1, spice = 1;
		for (String line : item.getItemMeta().getLore()) {
			if (line.contains("Garnished")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				garnish = Double.parseDouble(toParse);
			}
			if (line.contains("Preserved")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				preserve = Double.parseDouble(toParse);
			}
			if (line.contains("Spiced")) {
				String toParse = line.substring(line.indexOf('(') + 1, line.indexOf('x'));
				spice = Double.parseDouble(toParse);
			}
			if (line.contains("Remedies")) {
				if (line.contains("Remedies stun")) {
					FlagManager.removeFlag(p, StatusFlag.STUN);
				}
				else if (line.contains("Remedies curse")) {
					FlagManager.removeFlag(p, "curse");
				}
				else if (line.contains("Remedies root")) {
					FlagManager.removeFlag(p, StatusFlag.ROOT);
				}
				else if (line.contains("Remedies silence")) {
					FlagManager.removeFlag(p, StatusFlag.SILENCE);
				}
			}
		}

		// Set cooldowns
		long nextEat = System.currentTimeMillis() + (long) (this.cooldown * 50L * preserve);
		long ticks = (long) (this.cooldown * preserve);
		if (!ignoreGcd) {
			main.globalCooldowns.put(uuid, System.currentTimeMillis() + 20000L);
		}
		if (this.cooldown > 0) {
			main.foodCooldowns.get(uuid).put(this, nextEat);
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				public void run() {
					String message = "§4[§c§lMLMC§4] " + displayname + "&7 can be eaten again.";
					message = message.replaceAll("&", "§");
					p.sendMessage(message);
				}
			}, ticks);
		}

		// Potion effects
		for (PotionEffect effect : this.getEffect()) {
			p.addPotionEffect(effect);
		}

		// SkillAPI Attributes
		if (attributes != null) {
			// If food already consumed before, remove the attributes, cancel the remove task, then add new
			if (main.attributes.get(uuid).containsKey(this)) {
				AttributeTask at = main.attributes.get(uuid).get(this);
				at.getAttr().removeAttributes(p);
				at.getTask().cancel();
			}
			Attributes newAttr = attributes.clone();
			newAttr.multiply(garnish);
			newAttr.applyAttributes(p);
			BukkitTask newTask = new AttributeRunnable(main, p, newAttr, this).runTaskLater(main, this.attributeTime);
			main.attributes.get(uuid).put(this, new AttributeTask(newTask, newAttr));
		}

		// Health and mana regen
		PlayerData data = SkillAPI.getPlayerData(p);

		if (getHealthTime() == 0 && !p.isDead()) {
				p.setHealth(Math.min(p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue(), p.getHealth() + (health * spice)));
		}
		else if (getHealthTime() > 0 && !p.isDead()) {
			new HealthRunnable(p, health * spice, getHealthTime()).runTaskTimer(main, 0, getHealthDelay());
		}
		
		if (data.getMainClass().getData().getManaName().contains("MP")) {
			if (getManaTime() == 0 && !p.isDead()) {
				data.setMana(Math.min(data.getMaxMana(), data.getMana() + (mana * spice)));
			}
			else if (getManaTime() > 0 && !p.isDead()) {
				new ManaRunnable(p, mana * spice, getHealthTime()).runTaskTimer(main, 0, getManaDelay());
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
		return item;
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

	public void setSaturation(double saturation) {
		this.saturation = saturation;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}
	
	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
	}
	
	public void setIsDuration(boolean isDuration) {
		this.isDuration = isDuration;
	}
}
