package me.Neoblade298.NeoConsumables;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

public class Consumable {
	NeoConsumables main;
	ArrayList<PotionEffect> potions = new ArrayList<PotionEffect>();
	Attributes attributes = null;
	int attributeTime;
	ArrayList<Sound> sounds = new ArrayList<Sound>();
	ArrayList<String> commands = new ArrayList<String>();
	ArrayList<String> worlds = new ArrayList<String>();
	String name;
	ArrayList<String> lore;
	double saturation;
	int hunger;
	int health, healthTime, healthDelay;
	int mana, manaTime, manaDelay;
	long cooldown = 0;
	boolean ignoreGcd = false;
	ConsumableType type = ConsumableType.FOOD;

	public void setCommands(List<String> commands) {
		this.commands = new ArrayList<String>(commands);
	}

	public void executeCommands(Player player) {
		for (String cmd : this.commands) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd.replaceAll("%p", player.getName()));
		}
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public void setCooldown(long cooldown) {
		this.cooldown = cooldown;
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
		this.worlds = new ArrayList<String>(worlds);
	}

	public int getHealth() {
		return this.health;
	}

	public int getMana() {
		return this.mana;
	}

	public int getHealthTime() {
		return this.healthTime;
	}

	public int getManaTime() {
		return this.manaTime;
	}

	public int getHealthDelay() {
		return this.healthDelay;
	}

	public int getManaDelay() {
		return this.manaDelay;
	}

	public List<String> getWorlds() {
		return this.worlds;
	}

	public Consumable(NeoConsumables main, String name) {
		this.main = main;
		this.name = name;
	}

	public void addEffect(PotionEffect effect) {
		this.potions.add(effect);
	}

	public ArrayList<String> getLore() {
		return this.lore;
	}

	public String getName() {
		return this.name;
	}

	public ArrayList<PotionEffect> getEffect() {
		return this.potions;
	}

	public Attributes getAttributes() {
		return this.attributes;
	}
	
	public void setAttributes(Attributes attributes) {
		this.attributes = attributes;
	}

	public int getHunger() {
		return this.hunger;
	}

	public double getSaturation() {
		return this.saturation;
	}

	public ArrayList<Sound> getSounds() {
		return this.sounds;
	}
	
	public void setType(ConsumableType type) {
		this.type = type;
	}
	
	public ConsumableType getType() {
		return this.type;
	}

	public boolean isSimilar(ItemMeta meta) {
		if ((!meta.hasLore()) && (!getLore().isEmpty())) {
			return false;
		}
		if (!getLore().isEmpty()) {
			if (!meta.hasLore()) {
				return false;
			}

			ArrayList<String> flore = getLore();
			ArrayList<String> mlore = (ArrayList<String>) meta.getLore();

			for (int i = 0; i < flore.size(); i++) {
				String fLine = getLore().get(i);
				String mLine = mlore.get(i);
				if (!mLine.contains(fLine)) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean canEat(Player p) {
		// Check gcd
		UUID uuid = p.getUniqueId();
		if (!main.isOffGcd(p) && !ignoreGcd) {
			long remainingCooldown = main.globalCooldowns.get(p.getUniqueId()) - System.currentTimeMillis();
			remainingCooldown /= 1000L;
			String message = "&cYou cannot use any consumables for another " + remainingCooldown + " seconds";
			message = message.replaceAll("&", "§");
			p.sendMessage(message);
			return false;
		}

		// Check per-consumable cooldown
		if (main.foodCooldowns.containsKey(uuid) && main.foodCooldowns.get(uuid).containsKey(this)) {
			long nextEat = main.foodCooldowns.get(uuid).get(this);
			if (System.currentTimeMillis() < nextEat) {
				long remainingCooldown = nextEat - System.currentTimeMillis();
				remainingCooldown /= 1000L;
				String message = "&cYou cannot use this consumables for another " + remainingCooldown + " seconds";
				message = message.replaceAll("&", "§");
				p.sendMessage(message);
				return false;
			}
		}

		// Check world compatible
		if (!getWorlds().contains(p.getWorld().getName())) {
			return false;
		}

		// Check if it's a chest
		if (getName().contains("Chest") && main.isInstance) {
			String message = "&cYou cannot open chests in a boss fight!";
			message = message.replaceAll("&", "§");
			p.sendMessage(message);
			return false;
		}
		return true;
	}

	public void eat(final Player p, double garnish, double spice, double preserve) {
		// Food event only happens if hunger is changing
		UUID uuid = p.getUniqueId();
		int foodLevel = Math.min(20, p.getFoodLevel() + getHunger());
		if (getHunger() > 0) {
			FoodLevelChangeEvent event = new FoodLevelChangeEvent(p, foodLevel);
			Bukkit.getPluginManager().callEvent(event);
			if (!event.isCancelled()) {
				p.setFoodLevel(foodLevel);
				p.setSaturation((float) Math.min(p.getFoodLevel(), this.getSaturation() + p.getSaturation()));
			}
		}


		// Set cooldowns
		long nextEat = System.currentTimeMillis() + (long) (this.cooldown * 50L * (1 - preserve));
		long ticks = (long) (this.cooldown * preserve);
		if (!ignoreGcd) {
			main.globalCooldowns.put(uuid, System.currentTimeMillis() + 20000L);
		}
		if (this.cooldown > 0) {
			main.foodCooldowns.get(uuid).put(this, nextEat);
			Bukkit.getScheduler().scheduleSyncDelayedTask(main, new Runnable() {
				public void run() {
					String message = Consumable.this.name + "&7 can be eaten again.";
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
			// If the food's attributes still exist, just delay the remove attribute task
			if (main.attributes.get(uuid).containsKey(this)) {
				main.attributes.get(uuid).get(this).cancel();
				main.attributes.get(uuid).put(this, new AttributeRunnable(p, this.getAttributes()).runTaskLater(main, this.attributeTime));
			}
			else {
				attributes.applyAttributes(p);
				main.attributes.get(uuid).put(this, new AttributeRunnable(p, this.getAttributes()).runTaskLater(main, this.attributeTime));
			}
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
		
		// Sounds, commands
		for (Sound sound : getSounds()) {
			p.getWorld().playSound(p.getEyeLocation(), sound, 1.0F, 1.0F);
		}
		executeCommands(p);
	}

	public NeoConsumables getMain() {
		return main;
	}

	public void setMain(NeoConsumables main) {
		this.main = main;
	}

	public ArrayList<PotionEffect> getPotions() {
		return potions;
	}

	public void setPotions(ArrayList<PotionEffect> effects) {
		this.potions = effects;
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

	public void setCommands(ArrayList<String> commands) {
		this.commands = commands;
	}

	public boolean isIgnoreGcd() {
		return ignoreGcd;
	}

	public void setIgnoreGcd(boolean ignoreGcd) {
		this.ignoreGcd = ignoreGcd;
	}

	public long getCooldown() {
		return cooldown;
	}

	public void setSounds(ArrayList<Sound> sounds) {
		this.sounds = sounds;
	}

	public void setWorlds(ArrayList<String> worlds) {
		this.worlds = worlds;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLore(ArrayList<String> lore) {
		this.lore = lore;
	}

	public void setSaturation(double saturation) {
		this.saturation = saturation;
	}

	public void setHunger(int hunger) {
		this.hunger = hunger;
	}
}
